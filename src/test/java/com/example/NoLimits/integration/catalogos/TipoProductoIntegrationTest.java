package com.example.NoLimits.integration.catalogos;

import com.example.NoLimits.Multimedia.repository.catalogos.TipoProductoRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:nolimits_tipo_producto_integration_test;MODE=PostgreSQL;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.sql.init.mode=never",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect",
        "spring.jpa.properties.hibernate.id.new_generator_mappings=false"
})
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@DisplayName("T-Integración · TipoProducto")
class TipoProductoIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TipoProductoRepository tipoProductoRepository;

    @BeforeEach
    void limpiarDatos() {
        tipoProductoRepository.deleteAll();
    }

    @Nested
    @DisplayName("describe: integración Controller → Service → Repository")
    class IntegracionTipoProducto {

        @Test
        @DisplayName("it: crea un tipo de producto y lo persiste")
        void crearTipoProducto_persisteEnBaseDeDatos() throws Exception {

            String body = """
                    {
                      "nombre": "Película",
                      "descripcion": "Contenido audiovisual",
                      "activo": true
                    }
                    """;

            mockMvc.perform(post("/api/v1/tipo-productos")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.nombre").value("Película"))
                    .andExpect(jsonPath("$.descripcion").value("Contenido audiovisual"))
                    .andExpect(jsonPath("$.activo").value(true));

            assertTrue(
                    tipoProductoRepository.existsByNombreIgnoreCase("Película")
            );
        }

        @Test
        @DisplayName("it: lista tipos de producto persistidos")
        void listarTiposProducto_retornaDatosPersistidos() throws Exception {

            String body = """
                    {
                      "nombre": "Videojuego",
                      "descripcion": "Gaming",
                      "activo": true
                    }
                    """;

            mockMvc.perform(post("/api/v1/tipo-productos")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isCreated());

            mockMvc.perform(get("/api/v1/tipo-productos"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].nombre").value("Videojuego"))
                    .andExpect(jsonPath("$[0].activo").value(true));
        }

        @Test
        @DisplayName("it: valida duplicados")
        void crearTipoProductoDuplicado_retornaError() throws Exception {

            String body = """
                    {
                      "nombre": "Libro",
                      "descripcion": "Lectura",
                      "activo": true
                    }
                    """;

            mockMvc.perform(post("/api/v1/tipo-productos")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isCreated());

            mockMvc.perform(post("/api/v1/tipo-productos")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isBadRequest());
        }
    }
}