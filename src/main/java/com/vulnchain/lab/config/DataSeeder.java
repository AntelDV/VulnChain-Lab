package com.vulnchain.lab.config;

import com.vulnchain.lab.model.Product;
import com.vulnchain.lab.model.User;
import com.vulnchain.lab.repository.ProductRepository;
import com.vulnchain.lab.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        // Seed nếu DB trống
        if (userRepository.count() > 0) return;

        // Admin
        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setEmail("admin@vuln.lab");
        admin.setApiKey("sk-admin-"+UUID.randomUUID());
        admin.setRole(User.Role.ADMIN);
        userRepository.save(admin);

        // User
        User user  = new User();
        user.setUsername("duong");
        user.setPassword(passwordEncoder.encode("duong123"));
        user.setEmail("duong@vuln.lab");
        user.setApiKey("sk-user-"+UUID.randomUUID());
        user.setRole(User.Role.USER);
        userRepository.save(user);

        // Products
        productRepository.save(new Product(null, "Samsung S25", "Latest flagship smartphone", 999.0, "phone"));
        productRepository.save(new Product(null, "Dell Latitude", "Premium business laptop", 1200.0, "laptop"));
        productRepository.save(new Product(null, "Asus ROG Strix G18 2025", "High-performance gaming laptop", 2339.0, "laptop"));
        productRepository.save(new Product(null, "ALFA AWUS036AXML", "WiFi 6E Tri-band USB Adapter", 69.99, "network"));

        System.out.println("=== VulnChain Lab: Data seeded successfully! ===");
        System.out.println("Admin: admin / admin123");
        System.out.println("User:  duong / duong123");

    }

}
