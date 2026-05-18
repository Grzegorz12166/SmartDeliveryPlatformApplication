package com.grzegorzfit.smartdelivery.application.service;


import com.grzegorzfit.smartdelivery.domain.entity.StockItem;
import com.grzegorzfit.smartdelivery.domain.entity.StockReservation;
import com.grzegorzfit.smartdelivery.domain.model.ReservationStatus;
import com.grzegorzfit.smartdelivery.domain.repository.ProcessedEventRepository;
import com.grzegorzfit.smartdelivery.domain.repository.StockItemRepository;
import com.grzegorzfit.smartdelivery.domain.repository.StockReservationItemRepository;
import com.grzegorzfit.smartdelivery.domain.repository.StockReservationRepository;
import events.inventory.StockReservationFailedEvent;
import events.order.OrderCreatedEvent;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockReservationService {

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
        Map<UUID, Integer> requestedQuantities = new LinkedHashMap<>();
        for (OrderCreatedEvent.OrderItem item : event.items()) {
            requestedQuantities.merge(item.productId(), item.quantity(), Integer::sum);
        }

        List<UUID> productIds = new ArrayList<>(requestedQuantities.keySet());
        List<StockItem> stockItems = stockItemRepository.findAllByProductIdIn(productIds);
        Map<UUID, StockItem> stockByProductId = stockItems.stream()
                .collect(Collectors.toMap(StockItem::getProductId, s -> s));


    }


    private void handleFailure(OrderCreatedEvent event, String failureReason) {
        log.warn("Failed to reserve stock for order {}: {}", event.orderId(), failureReason);
        kafkaTemplate.send("stock-reservation-failed", new StockReservationFailedEvent()

        ));

        stockReservationRepository.save(
                new StockReservation(
                        event.orderId(),
                        ReservationStatus.FAILED,
                        failureReason
                ));

    }
    private void handleSuccess(
            OrderCreatedEvent event,
            Map<UUID, Integer> requestedQuantities,
            Map<UUID, StockItem> stockByProductId) {

        StockReservation reservation = new StockReservation(
                event.orderId(),
                ReservationStatus.RESERVED,
                "Order reserved");
    }
}
