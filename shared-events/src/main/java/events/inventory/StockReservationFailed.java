package events.inventory;

import java.time.Instant;
import java.util.UUID;

public record StockReservationFailed(
        UUID eventId,
        String eventType,
        Instant occurredAt,
        UUID correlationId,
        String source,
        int version,
        UUID orderId,
        UUID customerId,
        String reason
) {
}
