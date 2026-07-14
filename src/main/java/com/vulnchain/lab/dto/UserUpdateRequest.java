package com.vulnchain.lab.dto;

import lombok.Data;

@Data
public class UserUpdateRequest {
    private String email;
    private String username;
    private String role;
    private String apiKey;
    private String fullName;
    private String phone;
    private String company;
}
