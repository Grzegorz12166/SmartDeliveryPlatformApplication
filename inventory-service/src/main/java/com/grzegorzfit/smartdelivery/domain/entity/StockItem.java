package com.grzegorzfit.smartdelivery.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "stock_items")
@NoArgsConstructor
@Getter
@Setter
public class StockItem {

    @Id
    @Column(name = "product_id", nullable = false)
    @Setter(AccessLevel.NONE)
    private UUID productId;

    @OneToOne(optional = false)
    @MapsId
    @JoinColumn(name = "product_id")
    @Setter(AccessLevel.NONE)
    private Product product;

    @Column(nullable = false, name = "available_quantity")
    private int availableQuantity;

    @Column(nullable = false, name = "reserved_quantity")
    private int reservedQuantity;

    @Version
    @Column(nullable = false)
    private Integer version;

    @Column(nullable = false, name = "updated_at")
    private Instant updatedAt;

    public StockItem(Product product, int availableQuantity, int reservedQuantity) {
        this.product = product;
        this.productId = product != null ? product.getId() : null;
        this.availableQuantity = availableQuantity;
        this.reservedQuantity = reservedQuantity;
    }

    @PrePersist
    @PreUpdate
    private void setUpdatedAt() {
        this.updatedAt = Instant.now();
    }

}
