package com.smartlostfound.claimservice.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    // ============================================================
    // EXTRACT USER ID
    // ============================================================

    public Long extractUserId(String token) {

        return extractClaim(
                token,
                claims -> claims.get(
                        "userId",
                        Long.class
                )
        );
    }

    // ============================================================
    // EXTRACT ROLE
    // ============================================================

    public String extractRole(String token) {

        return extractClaim(
                token,
                claims -> claims.get(
                        "role",
                        String.class
                )
        );
    }

    // ============================================================
    // EXTRACT EMAIL
    // ============================================================

    public String extractEmail(String token) {

        return extractClaim(
                token,
                Claims::getSubject
        );
    }

    // ============================================================
    // EXTRACT ANY CLAIM
    // ============================================================

    public <T> T extractClaim(
            String token,
            Function<Claims, T> resolver) {

        Claims claims =
                extractAllClaims(token);

        return resolver.apply(claims);
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
    // CHECK TOKEN EXPIRATION
    // ============================================================

    public boolean isTokenExpired(String token) {

        Date expirationDate =
                extractClaim(
                        token,
                        Claims::getExpiration
                );

        return expirationDate.before(new Date());
    }

    // ============================================================
    // VALIDATE TOKEN
    // ============================================================

    public boolean isTokenValid(String token) {

        try {

            extractAllClaims(token);

            return !isTokenExpired(token);

        } catch (Exception e) {

            return false;
        }
    }

    // ============================================================
    // SIGNING KEY
    // ============================================================

    private SecretKey getSigningKey() {

        byte[] keyBytes =
                Decoders.BASE64.decode(secret);

        return Keys.hmacShaKeyFor(keyBytes);
    }
}