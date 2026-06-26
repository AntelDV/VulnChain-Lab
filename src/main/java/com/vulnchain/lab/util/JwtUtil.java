package com.vulnchain.lab.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

@Component
public class JwtUtil {
    // CWE: Use of Hard-coded Cryptographic Key
    private static final String SECRET = "vulnchain-30047-secret-key-12345678";
    private static final long EXPIRATION_TIME = 86400000L; // 24h

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }
    /*
    *   Create JWT Token basic
    * */
    public String generateToken(String username, String role){
        return Jwts.builder()
                .subject(username)
                .claim("role", role)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSigningKey())
                .compact();

    }

    public Claims parseToken ( String token)
    {
        try {
            return Jwts.parser().verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (JwtException e) {
            try {
                String[] parts = token.split("\\.");
                if (parts.length >= 2) {
                    String payload = new String(java.util.Base64.getUrlDecoder().decode(parts[1]));

                    // Parse manual
                    return parsePayloadManually(payload);
                }
            } catch (Exception ex){
                throw new JwtException("Invalid Token");
            }
            throw new JwtException("Invalid Token");
        }
    }

    // Parse JSON payload manually
    private Claims parsePayloadManually ( String payloadJson) {
        // Use Jackson to parse claims from raw JSON
        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper =
                    new com.fasterxml.jackson.databind.ObjectMapper();
            Map<String, Object> map = mapper.readValue(payloadJson, Map.class);

            return  Jwts.claims().add(map).build();

        } catch (Exception ex) {
            throw new JwtException("Unable to parse the payload");
        }
    }

    public String extractUsername ( String token) {
        return parseToken(token).getSubject();
    }

    public String extractRole ( String token) {
        return (String) parseToken(token).get("role");
    }

    public boolean isTokenExpired ( String token) {
        return parseToken(token).getExpiration().before(new Date());
    }
}
