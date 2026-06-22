package com.vulnchain.lab.repository;

import com.vulnchain.lab.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByNameContaining( String keywork);
    @Query( value = "SELECT * FROM products WHERE search_tag = '" + "' OR name LIKE '%?1%'", nativeQuery = true)
    List<Product> searchByTagVulnerable( String tag);
}
