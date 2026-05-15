package com.grzegorzfit.smartdelivery.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import model.Currency;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Setter(AccessLevel.NONE)
    private UUID id;

    @Column(nullable = false, unique = true, length = 100)
    private String sku;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal price;

    @Column(nullable = false, length = 3)
    @Enumerated(EnumType.STRING)
    private Currency currency;

    @Column(nullable = false)
    private boolean active;

    @Setter(AccessLevel.NONE)
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    private void productCreated() {
        if (createdAt == null) {
            createdAt = Instant.now();
        }
    }

    public Product(String sku,
                   String name,
                   BigDecimal price,
                   Currency currency,
                   boolean active
    ) {
        this.sku = sku;
        this.name = name;
        this.price = price;
        this.currency = currency;
        this.active = active;
    }
}
