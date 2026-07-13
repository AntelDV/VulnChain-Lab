package com.vulnchain.lab.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column( nullable = false)
    private String name;

    private String description;

    private Double price;

    @Column(name = "search_tag")
    private String searchTag;

    @Column(name = "category")
    private String category;

    @Column(name = "stock")
    private Integer stock;
}
