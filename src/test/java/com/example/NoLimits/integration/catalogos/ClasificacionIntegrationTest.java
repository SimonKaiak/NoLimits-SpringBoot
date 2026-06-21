package com.example.NoLimits.integration.catalogos;

import com.example.NoLimits.Multimedia.repository.catalogos.ClasificacionRepository;

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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:nolimits_integration_test;MODE=PostgreSQL;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE",
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
@DisplayName("T-Integración · Clasificaciones")
class ClasificacionIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClasificacionRepository clasificacionRepository;

    @BeforeEach
    void limpiarDatos() {
        clasificacionRepository.deleteAll();
    }

    @Nested
    @DisplayName("describe: integración Controller → Service → Repository")
    class IntegracionClasificacion {

        @Test
        @DisplayName("it: crea una clasificación y la persiste en la base de datos")
        void crearClasificacion_persisteEnBaseDeDatos() throws Exception {
            String body = """
                    {
                      "nombre": "T",
                      "descripcion": "Contenido apto para adolescentes.",
                      "activo": true
                    }
                    """;

            mockMvc.perform(post("/api/v1/clasificaciones")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.nombre").value("T"))
                    .andExpect(jsonPath("$.descripcion").value("Contenido apto para adolescentes."))
                    .andExpect(jsonPath("$.activo").value(true));

            assertTrue(
                    clasificacionRepository.existsByNombreIgnoreCase("T"),
                    "La clasificación debe quedar persistida en la BD de test"
            );
        }

        @Test
        @DisplayName("it: lista clasificaciones creadas desde la base de datos")
        void listarClasificaciones_retornaDatosPersistidos() throws Exception {
            String body = """
                    {
                      "nombre": "M",
                      "descripcion": "Solo para adultos.",
                      "activo": true
                    }
                    """;

            mockMvc.perform(post("/api/v1/clasificaciones")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andDo(print())
                    .andExpect(status().isCreated());

            mockMvc.perform(get("/api/v1/clasificaciones"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].nombre").value("M"))
                    .andExpect(jsonPath("$[0].activo").value(true));
        }

        @Test
        @DisplayName("it: valida duplicados usando Service y Repository real")
        void crearClasificacionDuplicada_retornaError() throws Exception {
            String body = """
                    {
                      "nombre": "E",
                      "descripcion": "Para todo público.",
                      "activo": true
                    }
                    """;

            mockMvc.perform(post("/api/v1/clasificaciones")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andDo(print())
                    .andExpect(status().isCreated());

            mockMvc.perform(post("/api/v1/clasificaciones")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }
    }
}