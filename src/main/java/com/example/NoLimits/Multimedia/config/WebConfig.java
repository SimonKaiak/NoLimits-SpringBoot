package com.example.NoLimits.Multimedia.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Clase de configuración global para CORS (Cross-Origin Resource Sharing).
 *
 * Esta clase permite controlar qué orígenes externos pueden acceder a la API backend.
 * Es fundamental cuando el frontend (React, Vite, Vercel, etc.) se encuentra en un dominio
 * diferente al backend (Spring Boot).
 *
 * Evita errores como:
 * - CORS policy: No 'Access-Control-Allow-Origin' header is present...
 *
 * Implementa WebMvcConfigurer para personalizar el comportamiento del módulo web de Spring.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Configura las reglas de CORS para las rutas de la API.
     *
     * @param registry permite registrar las políticas de acceso cruzado.
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {

        // Se aplica la configuración CORS a todas las rutas que comiencen con /api/
        registry.addMapping("/api/**")

                // Dominios permitidos para consumir la API
                // localhost: entorno de desarrollo
                // Vercel: entorno de producción del frontend
                .allowedOrigins(
                        "http://localhost:5173",
                        "https://no-limits-react.vercel.app"
                )

                // Métodos HTTP permitidos desde el frontend
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")

                // Se permiten todos los encabezados HTTP
                .allowedHeaders("*")

                // Permite el uso de cookies y sesiones (HttpSession)
                // Es necesario si se usa autenticación basada en sesión
                .allowCredentials(true);
    }
}