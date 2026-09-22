package com.smartlostfound.gateway.security;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import org.springframework.web.server.ServerWebExchange;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;

import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationFilter
        implements GlobalFilter {

    private final JwtService jwtService;

    public JwtAuthenticationFilter(
            JwtService jwtService) {

        this.jwtService = jwtService;
    }

    @Override
    public Mono<Void> filter(
            ServerWebExchange exchange,
            GatewayFilterChain chain) {

        String path =
                exchange.getRequest()
                        .getURI()
                        .getPath();

        // ============================================================
        // PUBLIC ENDPOINTS
        // ============================================================

        if (path.equals("/api/auth/login")
                || path.equals("/api/users/register")) {

            return chain.filter(exchange);
        }

        // ============================================================
        // GET AUTHORIZATION HEADER
        // ============================================================

        String authHeader =
                exchange.getRequest()
                        .getHeaders()
                        .getFirst(
                                HttpHeaders.AUTHORIZATION
                        );

        // ============================================================
        // NO JWT
        // ============================================================

        if (authHeader == null
                || !authHeader.startsWith("Bearer ")) {

            exchange.getResponse()
                    .setStatusCode(
                            HttpStatus.UNAUTHORIZED
                    );

            return exchange.getResponse()
                    .setComplete();
        }

        // ============================================================
        // EXTRACT TOKEN
        // ============================================================

        String token =
                authHeader.substring(7);

        // ============================================================
        // VALIDATE JWT
        // ============================================================

        if (!jwtService.isTokenValid(token)) {

            exchange.getResponse()
                    .setStatusCode(
                            HttpStatus.UNAUTHORIZED
                    );

            return exchange.getResponse()
                    .setComplete();
        }

        // ============================================================
        // VALID TOKEN
        // FORWARD REQUEST
        // ============================================================

        return chain.filter(exchange);
    }
}