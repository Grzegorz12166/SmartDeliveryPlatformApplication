package events.inventory;

import model.Currency;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record StockReserved(
        UUID eventId,
        String eventType,
        Instant occurredAt,
        UUID correlationId,
        String source,
        int version,
        UUID orderId,
        UUID customerId,
        UUID reservationId,
        List<OrderItem> items,
        BigDecimal totalAmount,
        Currency currency
) {

    public record OrderItem(
            UUID productId,
            int quantity
    ) {
    }

}
