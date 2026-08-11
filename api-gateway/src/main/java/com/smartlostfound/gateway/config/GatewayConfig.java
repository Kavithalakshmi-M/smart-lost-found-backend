package com.smartlostfound.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRoutes(
            RouteLocatorBuilder builder) {

        return builder.routes()

                // ====================================================
                // USER SERVICE
                // ====================================================

                .route(
                        "user-service",
                        route -> route
                                .path(
                                        "/api/auth/**",
                                        "/api/users/**"
                                )
                                .uri("lb://USER-SERVICE")
                )

                // ====================================================
                // ITEM SERVICE
                // ====================================================

                .route(
                        "item-service",
                        route -> route
                                .path("/api/items/**")
                                .uri("lb://ITEM-SERVICE")
                )

                // ====================================================
                // CLAIM SERVICE
                // ====================================================

                .route(
                        "claim-service",
                        route -> route
                                .path("/api/claims/**")
                                .uri("lb://CLAIM-SERVICE")
                )

                .build();
    }
}