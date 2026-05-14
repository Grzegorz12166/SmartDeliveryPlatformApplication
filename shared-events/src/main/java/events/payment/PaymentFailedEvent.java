package events.payment;

import java.time.Instant;
import java.util.UUID;

public record PaymentFailedEvent(
        UUID eventId,
        String eventType,
        Instant occurredAt,
        UUID correlationId,
        String source,
        int eventVersion,
        UUID orderId,
        UUID customerId,
        UUID paymentId,
        String reason
) {
}

