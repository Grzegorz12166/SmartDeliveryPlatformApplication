package com.grzegorzfit.smartdelivery.domain.repository;

import com.grzegorzfit.smartdelivery.domain.entity.StockReservationItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface StockReservationItemRepository extends JpaRepository<StockReservationItem, UUID> {
    List<StockReservationItem> findAllByReservation_Id(UUID reservationId);
    List<StockReservationItem> findAllByReservation_OrderId(UUID orderId);
}
