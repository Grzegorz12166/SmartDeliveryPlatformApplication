package com.grzegorzfit.smartdelivery.api.dto;

import model.Currency;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductResponse (
         UUID id,
         String sku,
         String name,
         BigDecimal price,
         Currency currency,
         boolean active,
         int availableQuantity
) {
}
