package com.grzegorzfit.smartdelivery.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@NoArgsConstructor
@Entity
@Setter
@Getter
@Table(name="stock_reservation_item")
public class StockReservationItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Setter(AccessLevel.NONE)
    private UUID id;

    @ManyToOne
    @Setter(AccessLevel.NONE)
    @JoinColumn(name="reservation_id",nullable = false)
    private StockReservation reservation;

    @ManyToOne
    @JoinColumn(name="product_id",nullable = false)
    @Setter(AccessLevel.NONE)
    private Product product;

    @Column(nullable = false)
    private int quantity;

    public StockReservationItem(StockReservation stockReservation, Product product, int quantity) {
        this.reservation = stockReservation;
        this.product = product;
        this.quantity = quantity;
    }
}
