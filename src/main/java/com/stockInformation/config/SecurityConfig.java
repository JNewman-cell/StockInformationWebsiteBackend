package com.stockInformation.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security configuration that restricts access to sensitive endpoints.
 * By default the endpoints /api/v1/ticker-summary/** and /api/v1/cik-lookup/**
 * will be denied for all requests. This can be toggled with the
 * property `security.restrict.internal` (default: true).
 */
@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Value("${security.restrict.internal:true}")
    private boolean restrictInternal;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Basic, conservative security: disable CSRF for API use and apply rules
        http.csrf(csrf -> csrf.disable());

        if (restrictInternal) {
            http.authorizeHttpRequests(authorize -> authorize
                    .requestMatchers("/api/v1/ticker-summary/**", "/api/v1/cik-lookup/**").denyAll()
                    .anyRequest().permitAll()
            );
        } else {
            http.authorizeHttpRequests(authorize -> authorize
                    .anyRequest().permitAll()
            );
        }

        // No default login form or basic auth enabled here. If you need to allow
        // internal services to access these endpoints, configure authentication
        // and change the antMatchers above to require specific roles.
        http.httpBasic(httpBasic -> httpBasic.disable());
        http.formLogin(formLogin -> formLogin.disable());

        return http.build();
    }
}
