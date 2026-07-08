package com.vulnchain.lab.controller;

import com.vulnchain.lab.dto.UserResponse;
import com.vulnchain.lab.model.User;
import com.vulnchain.lab.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;

    // No ownership validation
    @GetMapping("/{id}/profile")
    public ResponseEntity<UserResponse> getUserProfile(@PathVariable Long id) {
        User user = userRepository.findById(id).orElse(null);

        if ( user == null) {
            return ResponseEntity.notFound().build();
        }

        // Return full profile including sensitive apiKey
        return ResponseEntity.ok(new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole().name(),
                user.getApiKey()
        ));
    }

    /*
     * CWE-285: Improper Authorization
     * Get all users — admin only
     * But no role check → any authenticated user can call
     */
    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(
                userRepository.findAll().stream().map(u -> new UserResponse(
                        u.getId(),
                        u.getUsername(),
                        u.getEmail(),
                        u.getRole().name(),
                        u.getApiKey()
                        )
                ).toList()
        );
    }
}
