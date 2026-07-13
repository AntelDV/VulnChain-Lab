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
        productRepository.save(new Product(null, "Samsung S25", "Latest flagship smartphone", 999.0, "phone", "Phone", 100));
        productRepository.save(new Product(null, "Dell Latitude", "Premium business laptop", 1200.0, "laptop", "Laptop", 100));
        productRepository.save(new Product(null, "Asus ROG Strix G18 2025", "High-performance gaming laptop", 2339.0, "laptop", "Laptop", 100));
        productRepository.save(new Product(null, "ALFA AWUS036AXML", "WiFi 6E Tri-band USB Adapter", 69.99, "network", "Network", 100));
        productRepository.save(new Product(null, "Laptop Dell XPS 15", "High-performance laptop for professionals", 1299.0, "Laptop", "Laptop", 100));
        productRepository.save(new Product(null, "Server HPE ProLiant DL380", "Enterprise 2U rack server for data centers", 3499.0, "Server", "Server", 100));
        productRepository.save(new Product(null, "Cisco Switch SG350-28", "24-port managed gigabit switch with PoE+", 899.0, "Network", "Network", 100));
        productRepository.save(new Product(null, "Samsung SSD 870 EVO 2TB", "SATA SSD with up to 560 MB/s read speed", 149.0, "Storage", "Storage", 100));
        productRepository.save(new Product(null, "RAM Corsair Vengeance 32GB DDR5", "High-speed DDR5 memory kit for gaming and workstations", 129.0, "Component", "Component", 100));
        productRepository.save(new Product(null, "KVM Switch 8-Port", "Control 8 computers from single keyboard and mouse", 199.0, "Peripheral", "Peripheral", 100));
        productRepository.save(new Product(null, "UPS APC Smart-UPS 1500VA", "Line-interactive UPS with LCD display", 299.0, "Power", "Power", 100));
        productRepository.save(new Product(null, "WiFi 6E Access Point", "Tri-band wireless AP for enterprise environments", 399.0, "Network", "Network", 100));

        System.out.println("=== VulnChain Lab: Data seeded successfully! ===");
        System.out.println("Admin: admin / admin123");
        System.out.println("User:  duong / duong123");

    }

}
