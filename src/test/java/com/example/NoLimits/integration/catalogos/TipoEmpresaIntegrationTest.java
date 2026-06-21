package com.example.NoLimits.integration.catalogos;

import com.example.NoLimits.Multimedia.repository.catalogos.TipoEmpresaRepository;

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
        "spring.datasource.url=jdbc:h2:mem:nolimits_tipo_empresa_integration_test;MODE=PostgreSQL;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE",
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
@DisplayName("T-Integración · Tipos de Empresa")
class TipoEmpresaIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TipoEmpresaRepository tipoEmpresaRepository;

    @BeforeEach
    void limpiarDatos() {
        tipoEmpresaRepository.deleteAll();
    }

    @Nested
    @DisplayName("describe: integración Controller → Service → Repository")
    class IntegracionTipoEmpresa {

        @Test
        @DisplayName("it: crea un tipo de empresa y lo persiste en la base de datos")
        void crearTipoEmpresa_persisteEnBaseDeDatos() throws Exception {
            String body = """
                    {
                      "nombre": "Publisher"
                    }
                    """;

            mockMvc.perform(post("/api/v1/tipos-empresa")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.nombre").value("Publisher"));

            assertTrue(
                    tipoEmpresaRepository.existsByNombreIgnoreCase("Publisher"),
                    "El tipo de empresa debe quedar persistido en la BD de test"
            );
        }

        @Test
        @DisplayName("it: obtiene un tipo de empresa por ID desde la base de datos")
        void obtenerTipoEmpresaPorId_retornaDatoPersistido() throws Exception {
            String body = """
                    {
                      "nombre": "Distribuidora"
                    }
                    """;

            String response = mockMvc.perform(post("/api/v1/tipos-empresa")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isCreated())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();

            Long id = Long.valueOf(response.replaceAll(".*\"id\":(\\d+).*", "$1"));

            mockMvc.perform(get("/api/v1/tipos-empresa/" + id))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.nombre").value("Distribuidora"));
        }

        @Test
        @DisplayName("it: actualiza un tipo de empresa usando Service y Repository real")
        void actualizarTipoEmpresa_actualizaDatoPersistido() throws Exception {
            String crearBody = """
                    {
                      "nombre": "Estudio"
                    }
                    """;

            String actualizarBody = """
                    {
                      "nombre": "Desarrolladora"
                    }
                    """;

            String response = mockMvc.perform(post("/api/v1/tipos-empresa")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(crearBody))
                    .andExpect(status().isCreated())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();

            Long id = Long.valueOf(response.replaceAll(".*\"id\":(\\d+).*", "$1"));

            mockMvc.perform(put("/api/v1/tipos-empresa/" + id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(actualizarBody))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.nombre").value("Desarrolladora"));

            assertTrue(
                    tipoEmpresaRepository.existsByNombreIgnoreCase("Desarrolladora"),
                    "El tipo de empresa actualizado debe existir en la BD de test"
            );
        }
    }
}