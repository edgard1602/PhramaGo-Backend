package com.pharmago.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${pharmago.admin.api-key}")
    private String adminApiKey;

    @Bean
    public ApiKeyFilter apiKeyFilter() {
        return new ApiKeyFilter(adminApiKey);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        return http
                // Pas de session — API REST stateless
                .sessionManagement(session -> session
                        .sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS))

                // Désactiver CSRF — inutile pour une API REST
                .csrf(AbstractHttpConfigurer::disable)

                // Règles d'autorisation
                .authorizeHttpRequests(auth -> auth

                        // Endpoints publics — accessibles sans clé
                        .requestMatchers(HttpMethod.GET,
                                "/api/v1/pharmacies/**").permitAll()
                        .requestMatchers(HttpMethod.POST,
                                "/api/v1/signalements").permitAll()

                        // Swagger — accessible en dev
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**").permitAll()

                        // Tout le reste → authentification requise
                        .anyRequest().authenticated()
                )

                // Filtre API Key avant le filtre d'authentification
                .addFilterBefore(
                        apiKeyFilter(),
                        UsernamePasswordAuthenticationFilter.class)

                .build();
    }
}