package com.vulnchain.lab.service.impl;

import com.vulnchain.lab.model.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    @PersistenceContext
    private EntityManager entityManager;

    @SuppressWarnings("unchecked")
    public List<Product> searchVulnerable (String search) {
        // No sanitization, no parameterization
        String sql = "SELECT * FROM products WHERE name LIKE '%" + search + "%'";

        return entityManager.createNativeQuery(sql, Product.class).getResultList();
    }
}
