package com.grzegorzfit.smartdelivery.application.event;

import com.grzegorzfit.smartdelivery.application.service.StockReservationService;
import events.order.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderCreatedEventListener {

    private final StockReservationService stockReservationService;

    @KafkaListener(topics = "order-created", groupId = "inventory-service")
    public void onOrderCreated(OrderCreatedEvent event) {

        if (event == null) {
            log.error("Null event received");
            return;
        }

        log.info("Processing OrderCreatedEvent: eventId={}, orderId={}, correlationId={}", event.eventId(), event.orderId(), event.correlationId());

        try {
            stockReservationService.reserveStock(event);
        } catch (Exception e) {
            log.error("Error processing OrderCreatedEvent {}: {}", event.orderId(), e.getMessage(), e);
            throw e;
        }

        log.info("Successfully reserved stock for order {}", event.orderId());
    }
}