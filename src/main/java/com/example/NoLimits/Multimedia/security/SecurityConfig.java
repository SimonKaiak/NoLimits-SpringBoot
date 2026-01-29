package com.example.NoLimits.Multimedia.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> {})
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .exceptionHandling(ex -> ex.authenticationEntryPoint((request, response, authException) -> {
                // Si entran al root "/", manda al Swagger
                if ("/".equals(request.getRequestURI()) && "GET".equalsIgnoreCase(request.getMethod())) {
                response.sendRedirect("/doc/swagger-ui.html");
                return;
            }
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
            }))
            .authorizeHttpRequests(auth -> auth
            .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

            // Públicos
            .requestMatchers("/").permitAll()
            .requestMatchers("/health").permitAll()
            .requestMatchers("/api/v1/auth/login").permitAll()
            .requestMatchers(HttpMethod.POST, "/api/v1/usuarios").permitAll()

            .requestMatchers(
                "/swagger-ui/**",
                "/v3/api-docs/**",
                "/doc/**",
                "/actuator/health",
                "/actuator/info"
            ).permitAll()

            // Catálogo público
            .requestMatchers(
                HttpMethod.GET,
                "/api/v1/productos",
                "/api/v1/productos/**"
            ).permitAll()

            // Todo lo demás con JWT
            .anyRequest().authenticated()
        )
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}