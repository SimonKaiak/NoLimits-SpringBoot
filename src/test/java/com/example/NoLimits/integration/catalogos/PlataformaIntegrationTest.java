package com.example.NoLimits.integration.catalogos;

import com.example.NoLimits.Multimedia.repository.catalogos.PlataformaRepository;

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
        "spring.datasource.url=jdbc:h2:mem:nolimits_plataforma_integration_test;MODE=PostgreSQL;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE",
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
@DisplayName("T-Integración · Plataformas")
class PlataformaIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PlataformaRepository plataformaRepository;

    @BeforeEach
    void limpiarDatos() {
        plataformaRepository.deleteAll();
    }

    @Nested
    @DisplayName("describe: integración Controller → Service → Repository")
    class IntegracionPlataforma {

        @Test
        @DisplayName("it: crea una plataforma y la persiste en la base de datos")
        void crearPlataforma_persisteEnBaseDeDatos() throws Exception {
            String body = """
                    {
                      "nombre": "PlayStation 5"
                    }
                    """;

            mockMvc.perform(post("/api/v1/plataformas")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.nombre").value("PlayStation 5"));

            assertTrue(
                    plataformaRepository.existsByNombreIgnoreCase("PlayStation 5"),
                    "La plataforma debe quedar persistida en la BD de test"
            );
        }

        @Test
        @DisplayName("it: lista plataformas creadas desde la base de datos")
        void listarPlataformas_retornaDatosPersistidos() throws Exception {
            String body = """
                    {
                      "nombre": "Nintendo Switch"
                    }
                    """;

            mockMvc.perform(post("/api/v1/plataformas")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isCreated());

            mockMvc.perform(get("/api/v1/plataformas"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].nombre").value("Nintendo Switch"));
        }

        @Test
        @DisplayName("it: actualiza una plataforma usando Service y Repository real")
        void actualizarPlataforma_actualizaDatoPersistido() throws Exception {
            String crearBody = """
                    {
                      "nombre": "Xbox One"
                    }
                    """;

            String actualizarBody = """
                    {
                      "nombre": "Xbox Series X"
                    }
                    """;

            String response = mockMvc.perform(post("/api/v1/plataformas")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(crearBody))
                    .andExpect(status().isCreated())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();

            Long id = Long.valueOf(response.replaceAll(".*\"id\":(\\d+).*", "$1"));

            mockMvc.perform(put("/api/v1/plataformas/" + id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(actualizarBody))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.nombre").value("Xbox Series X"));

            assertTrue(
                    plataformaRepository.existsByNombreIgnoreCase("Xbox Series X"),
                    "La plataforma actualizada debe existir en la BD de test"
            );
        }
    }
}