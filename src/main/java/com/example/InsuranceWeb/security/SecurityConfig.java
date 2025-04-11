package com.example.InsuranceWeb.security;

import java.util.List;

import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors()
            .and()
            .csrf().disable()
            .authorizeHttpRequests()
            .requestMatchers("/api/auth/login", "/api/auth/register", "/api/auth/forgot-password", "/api/auth/reset-password").permitAll() // Make forgot password route public
            .requestMatchers("/uploads/**").permitAll()
            .requestMatchers("/api/agent/**").hasRole("AGENT")
            .requestMatchers("/api/admin/**").hasRole("ADMIN")
//            .requestMatchers("/api/admin/**").hasAuthority("ROLE_ADMIN")

            .requestMatchers("/api/customer/**").hasRole("CUSTOMER")
            .requestMatchers("/api/customer/notifications").hasRole("CUSTOMER")

            .requestMatchers(HttpMethod.POST, "/api/purchases").hasRole("CUSTOMER")
            .requestMatchers(HttpMethod.POST, "/api/purchases/ticket").hasRole("CUSTOMER")

            .requestMatchers("/api/purchases/user/**").hasRole("CUSTOMER")
            .requestMatchers("/api/purchases/email/**").hasRole("CUSTOMER")  // ✅ Add this line
            .requestMatchers("/api/purchases/by-email/**").hasRole("CUSTOMER")
            .requestMatchers("/api/premium/**").hasRole("CUSTOMER") // ✅ Add this line
            .requestMatchers("/api/purchases/**").hasAnyRole("AGENT", "ADMIN")
            .requestMatchers("/api/policies/**").authenticated()
            .anyRequest().authenticated()
            .and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
