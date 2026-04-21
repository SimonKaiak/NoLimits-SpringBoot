package com.example.NoLimits.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import com.example.NoLimits.Multimedia.security.JwtFilter;

@TestConfiguration
public class TestSecurityConfig {

    @Bean
    @Primary
    public JwtFilter jwtFilter() {
        return new JwtFilter() {
            @Override
            protected void doFilterInternal(
                jakarta.servlet.http.HttpServletRequest request,
                jakarta.servlet.http.HttpServletResponse response,
                jakarta.servlet.FilterChain filterChain
            ) throws java.io.IOException, jakarta.servlet.ServletException {

                // NO hace nada → deja pasar todo
                filterChain.doFilter(request, response);
            }
        };
    }
}