package com.grzegorzfit.smartdelivery.domain.entity;

import com.grzegorzfit.smartdelivery.domain.model.ReservationStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="stock_reservation")
public class StockReservation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Setter(AccessLevel.NONE)
    private UUID id;

    @Setter(AccessLevel.NONE)
    @Column(nullable = false,updatable = false,unique = true)
    private UUID orderId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    @Column
    private String reason;

    @Setter(AccessLevel.NONE)
    @Column(updatable = false, nullable = false, name = "created_at")
    private Instant createdAt;

    public StockReservation( UUID orderId, ReservationStatus status, String reason) {
        this.orderId = orderId;
        this.status = status;
        this.reason = reason;
    }

    @PrePersist
    private void setCreatedAt() {
        if(createdAt == null){
            this.createdAt = Instant.now();
        }
    }
}
