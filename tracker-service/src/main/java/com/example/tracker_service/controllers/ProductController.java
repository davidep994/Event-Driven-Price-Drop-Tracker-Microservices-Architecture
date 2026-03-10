package com.example.tracker_service.controllers;

import com.example.tracker_service.entities.Product;
import com.example.tracker_service.repositories.ProductRepository;
import com.example.tracker_service.services.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductRepository productRepository;
    private final ProductService productService;

    public ProductController(ProductRepository productRepository, ProductService productService) {
        this.productRepository = productRepository;
        this.productService = productService;
    }

    // 1. Endpoint para crear un producto inicial
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        return ResponseEntity.ok(productRepository.save(product));
    }

    // 2. Endpoint para actualizar el precio (EL DISPARADOR DEL EVENTO)
    @PutMapping("/{id}/price")
    public ResponseEntity<Product> updatePrice(
            @PathVariable Long id,
            @RequestParam BigDecimal newPrice) {

        Product updatedProduct = productService.updateProductPrice(id, newPrice);
        return ResponseEntity.ok(updatedProduct);
    }
}