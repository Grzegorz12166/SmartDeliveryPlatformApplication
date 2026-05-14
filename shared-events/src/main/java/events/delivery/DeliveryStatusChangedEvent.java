package events.delivery;

import java.time.Instant;
import java.util.UUID;

public record DeliveryStatusChangedEvent(
        UUID eventId,
        String eventType,
        Instant occurredAt,
        UUID correlationId,
        String source,
        int eventVersion,
        UUID orderId,
        UUID deliveryId,
        String oldStatus,
        String newStatus
) {
}
