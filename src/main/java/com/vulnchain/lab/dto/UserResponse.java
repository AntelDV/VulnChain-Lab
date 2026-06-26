package com.vulnchain.lab.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

// CWE-213: Exposure of Sensitive Information Due to Incompatible Policies
@Data
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private String role;

    private String apiKey;
}
