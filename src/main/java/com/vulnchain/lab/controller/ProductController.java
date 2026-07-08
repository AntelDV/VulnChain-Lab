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
}
