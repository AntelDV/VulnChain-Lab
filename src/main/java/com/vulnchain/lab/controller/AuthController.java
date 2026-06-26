package com.vulnchain.lab.controller;

import com.vulnchain.lab.dto.AuthResponse;
import com.vulnchain.lab.dto.LoginRequest;
import com.vulnchain.lab.model.User;
import com.vulnchain.lab.repository.UserRepository;
import com.vulnchain.lab.util.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {

        // Find user
        User user = userRepository.findByUsername(request.getUsername()).orElse(null);

        // Check credential
        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.status(401)
                    .body(new AuthResponse(null, null, null, "Invalid credentials"));

        }

        // Generate JWT Token
        String token = jwtUtil.generateToken(
                user.getUsername(),
                user.getRole().name()
        );

        return ResponseEntity.ok(
                new AuthResponse(token, user.getUsername(), user.getRole().name(), "Login successful")
        );

    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register (@Valid @RequestBody LoginRequest request) {

        // Check duplicate username
        if ( userRepository.existsByUsername(request.getUsername())) {
            return ResponseEntity.status(409)
                    .body(new AuthResponse(null, null, null, "Username already exists"));
        }

        // Save new user
        User newUser = new User();
        newUser.setUsername(request.getUsername());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setEmail(request.getUsername() + "@vulnchain.com");
        newUser.setRole(User.Role.USER);
        userRepository.save(newUser);

        return ResponseEntity.status(201)
                .body(new AuthResponse(null, newUser.getUsername(), "USER", "Registered successfully"));
    }
}
