package com.grzegorzfit.smartdelivery.application.service;


import com.grzegorzfit.smartdelivery.domain.entity.ProcessedEvent;
import com.grzegorzfit.smartdelivery.domain.entity.StockItem;
import com.grzegorzfit.smartdelivery.domain.entity.StockReservation;
import com.grzegorzfit.smartdelivery.domain.entity.StockReservationItem;
import com.grzegorzfit.smartdelivery.domain.model.ReservationStatus;
import com.grzegorzfit.smartdelivery.domain.repository.ProcessedEventRepository;
import com.grzegorzfit.smartdelivery.domain.repository.StockItemRepository;
import com.grzegorzfit.smartdelivery.domain.repository.StockReservationItemRepository;
import com.grzegorzfit.smartdelivery.domain.repository.StockReservationRepository;
import events.inventory.StockReservationFailedEvent;
import events.inventory.StockReservedEvent;
import events.order.OrderCreatedEvent;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockReservationService {


    //TODO
    //OutboxPattern to implement



    private final ProcessedEventRepository processedEventRepository;
    private final StockItemRepository stockItemRepository;
    private final StockReservationRepository stockReservationRepository;
    private final StockReservationItemRepository stockReservationItemRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Transactional
    public void reserveStock(OrderCreatedEvent event) {
        if (processedEventRepository.existsByEventId(event.eventId())) {
            return;
        }

        Optional<String> inputFailure = validateInput(event);
        if (inputFailure.isPresent()) {
            handleFailure(event, inputFailure.get());
            processedEventRepository.save(new ProcessedEvent(
                    event.eventType(),
                    event.eventId()
            ));
            return;
        }

        Map<UUID, Integer> requestedQuantities = new LinkedHashMap<>();
        for (OrderCreatedEvent.OrderItem item : event.items()) {
            requestedQuantities.merge(item.productId(), item.quantity(), Integer::sum);
        }

        List<UUID> productIds = new ArrayList<>(requestedQuantities.keySet());
        List<StockItem> stockItems = stockItemRepository.findAllByProductIdIn(productIds);
        Map<UUID, StockItem> stockByProductId = stockItems.stream()
                .collect(Collectors.toMap(StockItem::getProductId, s -> s));


        Optional<String> failureReason = validateStock(requestedQuantities, stockByProductId);

        if (failureReason.isPresent()) {
            handleFailure(event, failureReason.get());
        } else {
            handleSuccess(event, requestedQuantities, stockByProductId);
        }

        processedEventRepository.save(new ProcessedEvent(
                event.eventType(),
                event.eventId()
        ));


    }

    private Optional<String> validateInput(OrderCreatedEvent event) {
        if (event.items() == null || event.items().isEmpty()) {
            return Optional.of("Order contains no items");
        }

        return event.items().stream()
                .map(item -> {
                    if (item.productId() == null) {
                        return "Order contains item with null productId";
                    }
                    if (item.quantity() <= 0) {
                        return "Invalid quantity for product " + item.productId() + ": " + item.quantity();
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .findFirst();
    }

    private Optional<String> validateStock(
            Map<UUID, Integer> requestedQuantities,
            Map<UUID, StockItem> stockByProductId
    ) {
        return requestedQuantities.entrySet().stream()
                .map(entry -> {
                    UUID productId = entry.getKey();
                    Integer quantity = entry.getValue();

                    StockItem stockItem = stockByProductId.get(productId);

                    if (stockItem == null) {
                        return "Product not found " + productId;
                    }

                    if (stockItem.getAvailableQuantity() < quantity) {
                        return "Insufficient stock for product " + productId +
                                ", requested=" + quantity +
                                ", available=" + stockItem.getAvailableQuantity();
                    }

                    return null;
                })
                .filter(Objects::nonNull)
                .findFirst();
    }


    private void handleFailure(OrderCreatedEvent event, String failureReason) {
        log.warn("Failed to reserve stock for order {}: {}", event.orderId(), failureReason);
        stockReservationRepository.save(
                new StockReservation(
                        event.orderId(),
                        ReservationStatus.FAILED,
                        failureReason
                ));

        kafkaTemplate.send("stock-reservation-failed", event.orderId().toString(), new StockReservationFailedEvent(
                UUID.randomUUID(),
                "StockReservationFailed",
                Instant.now(),
                event.correlationId(),
                "inventory-service",
                1,
                event.orderId(),
                event.customerId(),
                failureReason));

    }

    private void handleSuccess(
            OrderCreatedEvent event,
            Map<UUID, Integer> requestedQuantities,
            Map<UUID, StockItem> stockByProductId) {

        StockReservation reservation = stockReservationRepository.save(new StockReservation(
                event.orderId(),
                ReservationStatus.RESERVED,
                null));

        requestedQuantities.forEach((productId, requested) -> {
            StockItem stockItem = stockByProductId.get(productId);
            stockItem.setAvailableQuantity(stockItem.getAvailableQuantity() - requested);
            stockItem.setReservedQuantity(stockItem.getReservedQuantity() + requested);
        });
        stockItemRepository.saveAll(stockByProductId.values());

        List<StockReservationItem> reservationItems = requestedQuantities.entrySet().stream()
                .map(entry -> new StockReservationItem(
                        reservation,
                        stockByProductId.get(entry.getKey()).getProduct(),
                        entry.getValue()))
                .toList();
        stockReservationItemRepository.saveAll(reservationItems);

        List<StockReservedEvent.OrderItem> eventItems = requestedQuantities.entrySet().stream()
                .map(entry -> new StockReservedEvent.OrderItem(entry.getKey(), entry.getValue()))
                .toList();

        kafkaTemplate.send("stock-reserved", event.orderId().toString(), new StockReservedEvent(
                UUID.randomUUID(),
                "StockReserved",
                Instant.now(),
                event.correlationId(),
                "inventory-service",
                1,
                event.orderId(),
                event.customerId(),
                reservation.getId(),
                eventItems,
                event.totalAmount(),
                event.currency()
        ));

        log.info("Successfully reserved stock for order {}", event.orderId());
    }


}
