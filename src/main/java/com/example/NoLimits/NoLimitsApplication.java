package com.example.NoLimits;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

// Anotación para configurar la info básica y el servidor de OpenAPI/Swagger.
@OpenAPIDefinition(
    info = @Info(
        title = "API NoLimits",
        version = "1.0",
        description = "Documentación de la API del proyecto NoLimits"
    ),
    // 👇 IMPORTANTE: usamos URL RELATIVA.
    // Esto hace que Swagger use SIEMPRE el mismo host desde donde se sirve:
    // - En local  → http://localhost:8080
    // - En Render → https://nolimits-backend-final.onrender.com
    servers = {
        @Server(
            url = "/",
            description = "Servidor actual (local o Render)"
        )
    }
)
@SpringBootApplication
public class NoLimitsApplication {

    public static void main(String[] args) {
        SpringApplication.run(NoLimitsApplication.class, args);
    }

}