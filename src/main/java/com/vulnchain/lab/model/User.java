package com.vulnchain.lab.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class User {
    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (unique = true, nullable = false)
    private String username;

    @Column ( nullable = false)
    private String password;

    @Column (nullable = false, unique = true)
    private String email;

    @Column ( name = "api_key")
    private String apiKey;

    @Enumerated(EnumType.STRING)
    @Column( nullable = false)
    private Role role = Role.USER;

    public enum Role {
        USER, ADMIN
    }

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "company")
    private String company;

    @Column(name = "phone")
    private String phone;

}
