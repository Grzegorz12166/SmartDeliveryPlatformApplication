package events.delivery;

import java.time.Instant;
import java.util.UUID;

public record DeliveryCreatedEvent(
        UUID eventId,
        String eventType,
        Instant occurredAt,
        UUID correlationId,
        String source,
        int eventVersion,
        UUID orderId,
        UUID customerId,
        UUID deliveryId,
        String status
) {
}
