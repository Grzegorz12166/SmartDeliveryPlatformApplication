package com.grzegorzfit.smartdelivery.domain.repository;


import com.grzegorzfit.smartdelivery.domain.entity.Product;
import com.grzegorzfit.smartdelivery.domain.entity.StockItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StockItemRepository extends JpaRepository<StockItem, Product> {

    Optional<StockItem> findByProduct_Id(UUID productId);

    boolean existsByProduct_Id(UUID productId);

    List<StockItem> findAllByProduct_IdIn(List<UUID> productIds);
}
