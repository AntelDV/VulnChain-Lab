package com.vulnchain.lab.dto;

import lombok.Data;

@Data
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private String role;
    private String apiKey;
    private String fullName;
    private String company;
    private String phone;

    public UserResponse(Long id, String username, String email, String role, String apiKey) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
        this.apiKey = apiKey;
    }

    public UserResponse(Long id, String username, String email, String role, String apiKey,String fullName, String company, String phone) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
        this.apiKey = apiKey;
        this.fullName = fullName;
        this.company = company;
        this.phone = phone;
    }
}