package com.example.NoLimits.config;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public abstract class AbstractContainerBaseTest {
    // Antes levantaba un contenedor real de Postgres (pgvector) vía Testcontainers
    // y sobreescribía spring.datasource.* con @DynamicPropertySource.
    // Ahora la base de datos de test la define application-test.properties (H2),
    // así que esta clase ya no necesita hacer nada — solo sigue existiendo
    // para no tener que tocar las 34 clases que la extienden.
}