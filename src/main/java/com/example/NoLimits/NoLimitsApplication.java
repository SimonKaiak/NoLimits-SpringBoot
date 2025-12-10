// Ruta: src/main/java/com/example/NoLimits/NoLimitsApplication.java
package com.example.NoLimits;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

/**
 * Punto de entrada principal de la aplicación NoLimits.
 *
 * <p>
 * Esta clase arranca el contexto de Spring Boot y, además, configura
 * la metadata básica de OpenAPI/Swagger para documentar la API REST.
 * </p>
 *
 * <h2>Configuración de OpenAPI</h2>
 * <ul>
 *   <li><b>title</b>: Nombre visible de la API en Swagger UI.</li>
 *   <li><b>version</b>: Versión lógica de la API (no del proyecto).</li>
 *   <li><b>description</b>: Descripción general para la documentación.</li>
 *   <li><b>servers</b>: Se define un servidor con URL relativa {@code "/"} para
 *       que Swagger siempre use el mismo host desde donde se está sirviendo
 *       la aplicación:
 *       <ul>
 *          <li>En local → {@code http://localhost:8080}</li>
 *          <li>En Render → {@code https://nolimits-backend-final.onrender.com}</li>
 *       </ul>
 *   </li>
 * </ul>
 */
@OpenAPIDefinition(
    info = @Info(
        title = "API NoLimits",
        version = "1.0",
        description = "Documentación de la API del proyecto NoLimits"
    ),
    // IMPORTANTE: usamos URL RELATIVA.
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

    /**
     * Método main que inicia la aplicación Spring Boot.
     *
     * @param args argumentos de línea de comando (normalmente no se usan).
     */
    public static void main(String[] args) {
        SpringApplication.run(NoLimitsApplication.class, args);
    }

}