package com.vulnchain.lab.controller;

import com.vulnchain.lab.dto.UserResponse;
import com.vulnchain.lab.dto.UserUpdateRequest;
import com.vulnchain.lab.model.User;
import com.vulnchain.lab.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMe(Authentication auth) {
        User user = userRepository.findByUsername(auth.getName()).orElse(null);
        if (user == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(toResponse(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) return ResponseEntity.notFound().build();
        // No check if caller owns this resource
        return ResponseEntity.ok(toResponse(user));
    }

    @GetMapping("/{id}/profile")
    public ResponseEntity<UserResponse> getUserProfile(@PathVariable Long id) {
        return getUserById(id);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok( userRepository.findAll().stream().map(this::toResponse).toList());
    }


    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(@RequestBody UserUpdateRequest request,
                                           Authentication auth) {
        User user = userRepository.findByUsername(auth.getName()).orElse(null);
        if (user == null) return ResponseEntity.notFound().build();

        if (request.getEmail()    != null) user.setEmail(request.getEmail());
        if (request.getUsername() != null) user.setUsername(request.getUsername());
        if (request.getFullName() != null) user.setFullName(request.getFullName());
        if (request.getCompany()  != null) user.setCompany(request.getCompany());
        if (request.getPhone()    != null) user.setPhone(request.getPhone());

        // No check on role field
        if (request.getRole() != null) {
            try {
                user.setRole(User.Role.valueOf(request.getRole().toUpperCase()));
            } catch (IllegalArgumentException e) {}
        }

        if (request.getApiKey() != null) user.setApiKey(request.getApiKey());

        userRepository.save(user);
        return ResponseEntity.ok(toResponse(user));
    }

    // Helper
    private UserResponse toResponse(User u) {
        return new UserResponse(
                u.getId(), u.getUsername(), u.getEmail(),
                u.getRole().name(), u.getApiKey(),
                u.getFullName(), u.getCompany(), u.getPhone()
        );
    }
}