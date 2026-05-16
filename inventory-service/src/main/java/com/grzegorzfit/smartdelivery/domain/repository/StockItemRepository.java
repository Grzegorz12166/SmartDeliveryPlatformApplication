package com.grzegorzfit.smartdelivery.domain.repository;


import com.grzegorzfit.smartdelivery.domain.entity.StockItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StockItemRepository extends JpaRepository<StockItem, UUID> {

    Optional<StockItem> findByProductId(UUID productId);

    boolean existsByProductId(UUID productId);

    List<StockItem> findAllByProductIdIn(List<UUID> productIds);
}
