package events.order;

import model.Currency;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record OrderCreated(
        UUID eventId,
        String eventType,
        Instant occurredAt,
        UUID correlationId,
        String source,
        int version,
        UUID orderId,
        UUID customerId,
        List<OrderItem> items,
        BigDecimal totalAmount,
        Currency currency,
        DeliveryAddress deliveryAddress
) {

    public record OrderItem(
            UUID productId,
            int quantity,
            BigDecimal unitPrice
    ) {
    }

    public record DeliveryAddress(
            String street,
            String city,
            String postalCode,
            String country
    ) {
    }
}

