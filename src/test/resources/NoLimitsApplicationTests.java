package com.example.NoLimits;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Clase de prueba principal para verificar que el contexto de Spring Boot
 * se carga correctamente.
 *
 * Esta prueba NO ejecuta lógica de negocio.
 * Su único propósito es comprobar que:
 * - La configuración de Spring es válida.
 * - Todas las dependencias se inyectan correctamente.
 * - No hay errores críticos al iniciar la aplicación.
 *
 * Si esta prueba falla, significa que hay un problema grave en la
 * configuración del proyecto (beans mal definidos, errores de contexto, etc.).
 */
@SpringBootTest
public class NoLimitsApplicationTests {

    /**
     * Test básico de carga de contexto.
     *
     * Spring levanta el contexto completo de la aplicación.
     * Si no ocurre ninguna excepción, el test se considera exitoso.
     *
     * Este tipo de prueba es estándar en proyectos Spring Boot
     * para validar que la aplicación puede iniciar correctamente.
     */
	@Test
	public void contextLoads() {
        // No contiene lógica porque el objetivo es únicamente
        // verificar que el contexto de Spring se inicializa sin errores.
	}
}