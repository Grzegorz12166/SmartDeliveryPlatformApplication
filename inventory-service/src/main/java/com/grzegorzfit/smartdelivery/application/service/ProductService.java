package com.grzegorzfit.smartdelivery.application.service;

import com.grzegorzfit.smartdelivery.api.dto.AddStockRequest;
import com.grzegorzfit.smartdelivery.api.dto.CreateProductRequest;
import com.grzegorzfit.smartdelivery.api.dto.ProductResponse;
import com.grzegorzfit.smartdelivery.application.exception.ProductNotFoundException;
import com.grzegorzfit.smartdelivery.domain.entity.Product;
import com.grzegorzfit.smartdelivery.domain.entity.StockItem;
import com.grzegorzfit.smartdelivery.domain.repository.ProductRepository;
import com.grzegorzfit.smartdelivery.domain.repository.StockItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    private final StockItemRepository stockItemRepository;

    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream()
                .map(product -> {
                    StockItem stockItem = stockItemRepository.findByProductId(product.getId())
                            .orElse(null);
                    return toResponse(product, stockItem);
                })
                .toList();
    }

    public ProductResponse getProductById(UUID productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product [%s] not found".formatted(productId)));

        StockItem stockItem = stockItemRepository.findByProductId(productId)
                .orElse(null);

        return toResponse(product, stockItem);
    }

    public ProductResponse createProduct(CreateProductRequest request) {
        if (productRepository.existsBySku(request.sku())) {
            throw new IllegalStateException("Product with SKU [%s] already exists".formatted(request.sku()));
        }

        Product product = new Product(
                request.sku(),
                request.name(),
                request.price(),
                request.currency(),
                request.active()
        );

        Product savedProduct = productRepository.save(product);

        StockItem stockItem = new StockItem(savedProduct, request.initialQuantity(), 0);
        StockItem savedStockItem = stockItemRepository.save(stockItem);

        return toResponse(savedProduct, savedStockItem);
    }

    public void addStock(UUID productId, AddStockRequest request) {
        if (!productRepository.existsById(productId)) {
            throw new ProductNotFoundException("Product [%s] not found".formatted(productId));
        }

        StockItem stockItem = stockItemRepository.findByProductId(productId)
                .orElseThrow(() -> new ProductNotFoundException("Stock item for product [%s] not found".formatted(productId)));

        stockItem.setAvailableQuantity(stockItem.getAvailableQuantity() + request.quantity());
        stockItemRepository.save(stockItem);
    }

    private ProductResponse toResponse(Product product, StockItem stockItem) {
        int availableQuantity = stockItem != null ? stockItem.getAvailableQuantity() : 0;
        return new ProductResponse(
                product.getId(),
                product.getSku(),
                product.getName(),
                product.getPrice(),
                product.getCurrency(),
                product.isActive(),
                availableQuantity
        );
    }
}
