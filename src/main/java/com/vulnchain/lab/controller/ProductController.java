package com.vulnchain.lab.controller;

import com.vulnchain.lab.model.Product;
import com.vulnchain.lab.repository.ProductRepository;
import com.vulnchain.lab.service.impl.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductRepository productRepository;
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<?> searchProducts ( @RequestParam(required = false) String search) {
        if ( search == null || search.isBlank()) {
            return ResponseEntity.ok(productRepository.findAll());
        }

        try {
            List<Product> result = productService.searchVulnerable(search);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(500).body (
                    java.util.Map.of("error", e.getMessage(), "cause", e.getCause() != null ? e.getCause().getMessage() : "unknown")
            );
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct ( @PathVariable Long id) {
        return productRepository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> addProduct(@RequestBody Product product) {
        try {
            Product saved = productRepository.save(product);
            return ResponseEntity.status(201).body(saved);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                    java.util.Map.of("error", e.getMessage())
            );
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        if (!productRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        productRepository.deleteById(id);
        return ResponseEntity.ok(java.util.Map.of("message", "Product deleted"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id,
                                           @RequestBody Product request) {
        return productRepository.findById(id).map(product -> {
            if (request.getName() != null) product.setName(request.getName());
            if (request.getDescription() != null) product.setDescription(request.getDescription());
            if (request.getPrice() != null) product.setPrice(request.getPrice());
            if (request.getSearchTag() != null) product.setSearchTag(request.getSearchTag());
            if (request.getCategory() != null) product.setCategory(request.getCategory());
            if (request.getStock() != null) product.setStock(request.getStock());
            return ResponseEntity.ok(productRepository.save(product));
        }).orElse(ResponseEntity.notFound().build());
    }
}
