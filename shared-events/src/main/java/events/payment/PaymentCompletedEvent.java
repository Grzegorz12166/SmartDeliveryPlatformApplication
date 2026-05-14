package events.payment;

import model.Currency;
import model.DeliveryAddress;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record PaymentCompletedEvent(
        UUID eventId,
        String eventType,
        Instant occurredAt,
        UUID correlationId,
        String source,
        int eventVersion,
        UUID orderId,
        UUID customerId,
        UUID paymentId,
        BigDecimal amount,
        Currency currency,
        DeliveryAddress deliveryAddress

) {
}

