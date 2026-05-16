package com.grzegorzfit.smartdelivery.api.dto;

import model.Currency;

import java.math.BigDecimal;

public record CreateProductRequest(
        String sku,
        String name,
        BigDecimal price,
        Currency currency,
        boolean active,
        int initialQuantity
) {
}
