package com.grzegorzfit.smartdelivery.domain.repository;

import com.grzegorzfit.smartdelivery.domain.entity.StockReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface StockReservationRepository extends JpaRepository<StockReservation, UUID> {
    Optional<StockReservation> findByOrderId(UUID orderId);
    boolean existsByOrderId(UUID orderId);
}
