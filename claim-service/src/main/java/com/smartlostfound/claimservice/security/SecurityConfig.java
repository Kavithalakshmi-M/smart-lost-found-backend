package com.smartlostfound.claimservice.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter) {

        this.jwtAuthenticationFilter =
                jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http

            // ========================================================
            // CSRF
            // ========================================================

            .csrf(csrf -> csrf.disable())

            // ========================================================
            // SESSION MANAGEMENT
            // ========================================================

            .sessionManagement(session ->
                    session.sessionCreationPolicy(
                            SessionCreationPolicy.STATELESS
                    )
            )

            // ========================================================
            // DISABLE DEFAULT AUTHENTICATION
            // ========================================================

            .httpBasic(httpBasic ->
                    httpBasic.disable())

            .formLogin(formLogin ->
                    formLogin.disable())

            // ========================================================
            // AUTHORIZATION
            // ========================================================

            .authorizeHttpRequests(auth -> auth

                    // ------------------------------------------------
                    // ACTUATOR
                    // ------------------------------------------------

                    .requestMatchers(
                            "/actuator/health",
                            "/actuator/info"
                    ).permitAll()

                    // ------------------------------------------------
                    // CREATE CLAIM
                    // USER ONLY
                    // ------------------------------------------------

                    .requestMatchers(
                            HttpMethod.POST,
                            "/api/claims"
                    ).hasRole("USER")

                    // ------------------------------------------------
                    // GET MY CLAIMS
                    // USER ONLY
                    // ------------------------------------------------

                    .requestMatchers(
                            HttpMethod.GET,
                            "/api/claims/my"
                    ).hasRole("USER")

                    // ------------------------------------------------
                    // GET CLAIMS FOR ITEM
                    // STAFF / ADMIN
                    // ------------------------------------------------

                    .requestMatchers(
                            HttpMethod.GET,
                            "/api/claims/item/**"
                    ).hasAnyRole(
                            "STAFF",
                            "ADMIN"
                    )

                    // ------------------------------------------------
                    // UPDATE CLAIM STATUS
                    // STAFF / ADMIN
                    // ------------------------------------------------

                    .requestMatchers(
                            HttpMethod.PATCH,
                            "/api/claims/*/status"
                    ).hasAnyRole(
                            "STAFF",
                            "ADMIN"
                    )

                    // ------------------------------------------------
                    // DELETE CLAIM
                    // USER / ADMIN
                    // ------------------------------------------------

                    .requestMatchers(
                            HttpMethod.DELETE,
                            "/api/claims/**"
                    ).hasAnyRole(
                            "USER",
                            "ADMIN"
                    )

                    // ------------------------------------------------
                    // GET INDIVIDUAL CLAIM
                    // ------------------------------------------------

                    .requestMatchers(
                            HttpMethod.GET,
                            "/api/claims/*"
                    ).authenticated()

                    // ------------------------------------------------
                    // EVERYTHING ELSE
                    // ------------------------------------------------

                    .anyRequest()
                    .authenticated()
            )

            // ========================================================
            // JWT FILTER
            // ========================================================

            .addFilterBefore(
                    jwtAuthenticationFilter,
                    UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }
}