package com.vulnchain.lab.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

@Component
public class JwtUtil {

    @Value("${app.jwt.secret}")
    private String secret;

    private static final long EXPIRATION = 86400000L;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(String username, String role) {
        return Jwts.builder()
                .subject(username)
                .claim("role", role)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(getSigningKey())
                .compact();
    }

    public Claims parseToken(String token) {
        try {
            // Secure path — verify signature
            return Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

        } catch (JwtException e) {
            // fallback không verify signature

            try {
                String[] parts = token.split("\\.");
                if (parts.length >= 2) {
                    String payload = new String(
                            java.util.Base64.getUrlDecoder().decode(parts[1]));
                    return parsePayloadManually(payload);
                }
            } catch (Exception ex) {
                throw new JwtException("Invalid token");
            }
            throw new JwtException("Invalid token");
        }
    }

    private Claims parsePayloadManually(String payloadJson) {
        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper =
                    new com.fasterxml.jackson.databind.ObjectMapper();
            Map<String, Object> map = mapper.readValue(payloadJson, Map.class);
            return Jwts.claims().add(map).build();
        } catch (Exception e) {
            throw new JwtException("Cannot parse payload");
        }
    }

    public String extractUsername(String token) {
        return parseToken(token).getSubject();
    }

    public String extractRole(String token) {
        return (String) parseToken(token).get("role");
    }

    public boolean isTokenExpired(String token) {
        return parseToken(token).getExpiration().before(new Date());
    }
}