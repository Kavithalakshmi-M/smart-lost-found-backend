package com.smartlostfound.userservice.security;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.smartlostfound.userservice.entity.User;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    // ============================================================
    // SIGNING KEY
    // ============================================================

    private SecretKey getSigningKey() {

        byte[] keyBytes =
                Decoders.BASE64.decode(secret);

        return Keys.hmacShaKeyFor(keyBytes);
    }

    // ============================================================
    // GENERATE JWT
    // ============================================================

    public String generateToken(User user) {

        return Jwts.builder()
                .subject(user.getEmail())

                .claim(
                        "userId",
                        user.getId()
                )

                .claim(
                        "role",
                        user.getRole().name()
                )

                .issuedAt(new Date())

                .expiration(
                        new Date(
                                System.currentTimeMillis()
                                        + expiration
                        )
                )

                .signWith(getSigningKey())

                .compact();
    }

    // ============================================================
    // EXTRACT EMAIL
    // ============================================================

    public String extractEmail(String token) {

        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    // ============================================================
    // EXTRACT USER ID
    // ============================================================

    public Long extractUserId(String token) {

        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("userId", Long.class);
    }

    // ============================================================
    // EXTRACT ROLE
    // ============================================================

    public String extractRole(String token) {

        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("role", String.class);
    }

    // ============================================================
    // VALIDATE TOKEN
    // ============================================================

    public boolean isTokenValid(String token) {

        try {

            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);

            return true;

        } catch (Exception e) {

            return false;
        }
    }
}