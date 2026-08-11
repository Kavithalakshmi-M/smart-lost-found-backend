package com.smartlostfound.itemservice.security;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    // ============================================================
    // SIGNING KEY
    // ============================================================

    private SecretKey getSigningKey() {

        byte[] keyBytes =
                Decoders.BASE64.decode(secret);

        return Keys.hmacShaKeyFor(keyBytes);
    }

    // ============================================================
    // EXTRACT ALL CLAIMS
    // ============================================================

    public Claims extractAllClaims(String token) {

        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // ============================================================
    // EXTRACT USER ID
    // ============================================================

    public Long extractUserId(String token) {

        Object userId =
                extractAllClaims(token)
                        .get("userId");

        if (userId instanceof Number) {

            return ((Number) userId)
                    .longValue();
        }

        return Long.parseLong(
                userId.toString()
        );
    }

    // ============================================================
    // EXTRACT ROLE
    // ============================================================

    public String extractRole(String token) {

        return extractAllClaims(token)
                .get("role", String.class);
    }

    // ============================================================
    // EXTRACT EMAIL
    // ============================================================

    public String extractEmail(String token) {

        return extractAllClaims(token)
                .getSubject();
    }

    // ============================================================
    // VALIDATE TOKEN
    // ============================================================

    public boolean isTokenValid(String token) {

        try {

            extractAllClaims(token);

            return true;

        } catch (Exception e) {

            return false;
        }
    }
}