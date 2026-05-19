package com.grzegorzfit.smartdelivery.api.rest;

import com.grzegorzfit.smartdelivery.api.dto.AddStockRequest;
import com.grzegorzfit.smartdelivery.api.dto.CreateProductRequest;
import com.grzegorzfit.smartdelivery.api.dto.ProductResponse;
import com.grzegorzfit.smartdelivery.application.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public List<ProductResponse> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{productId}")
    public ProductResponse getProductById(@PathVariable("productId") UUID productId) {
        return productService.getProductById(productId);
    }

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(
            @Valid @RequestBody CreateProductRequest request) {
        ProductResponse response = productService.createProduct(request);
        return ResponseEntity.status(201).body(response);
    }

    @PostMapping("/{productId}/stock")
    public ResponseEntity<Void> addStock(
            @PathVariable("productId") UUID productId,
            @Valid @RequestBody AddStockRequest request) {
        productService.addStock(productId, request);
        return ResponseEntity.noContent().build();
    }


}
