package com.example.NoLimits.Multimedia.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

/**
 * Clase de configuración de Swagger / Springdoc OpenAPI.
 *
 * Esta clase define la información general que se mostrará en la interfaz Swagger UI,
 * como el título, versión y descripción de la API del proyecto NoLimits.
 *
 * Swagger permite:
 * - Visualizar todos los endpoints disponibles
 * - Probar peticiones directamente desde el navegador
 * - Documentar la API de forma automática
 */
@Configuration
public class SwaggerConfig {

    /**
     * Bean que define la configuración principal de OpenAPI.
     *
     * Este método crea y personaliza el objeto OpenAPI que será utilizado por
     * Springdoc para generar la documentación visible en Swagger UI.
     *
     * @return configuración personalizada de OpenAPI con información del proyecto.
     */
    @Bean
    public OpenAPI customOpenAPI() {

        // Se crea una nueva instancia de OpenAPI y se le asigna información básica del proyecto
        return new OpenAPI()
            .info(new Info()
                // Título que aparecerá en Swagger
                .title("Controllers 'NoLimits'")

                // Versión de la API
                .version("Versión: 1.0")

                // Descripción general del propósito de la documentación
                .description("Documentación de los controllers del sistema NoLimits mediante Swagger."));
    }

}