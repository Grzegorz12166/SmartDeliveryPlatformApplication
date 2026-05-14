package events.inventory;

import java.time.Instant;
import java.util.UUID;

public record StockReservationFailedEvent(
        UUID eventId,
        String eventType,
        Instant occurredAt,
        UUID correlationId,
        String source,
        int eventVersion,
        UUID orderId,
        UUID customerId,
        String reason
) {
}

