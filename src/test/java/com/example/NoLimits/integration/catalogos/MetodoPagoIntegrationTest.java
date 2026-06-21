package com.example.NoLimits.integration.catalogos;

import com.example.NoLimits.Multimedia.repository.catalogos.MetodoPagoRepository;

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
        "spring.datasource.url=jdbc:h2:mem:nolimits_metodo_pago_integration_test;MODE=PostgreSQL;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE",
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
@DisplayName("T-Integración · Métodos de Pago")
class MetodoPagoIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MetodoPagoRepository metodoPagoRepository;

    @BeforeEach
    void limpiarDatos() {
        metodoPagoRepository.deleteAll();
    }

    @Nested
    @DisplayName("describe: integración Controller → Service → Repository")
    class IntegracionMetodoPago {

        @Test
        @DisplayName("it: crea un método de pago y lo persiste")
        void crearMetodoPago_persisteEnBaseDeDatos() throws Exception {
            String body = """
                    {
                      "nombre": "Tarjeta de Débito",
                      "activo": true
                    }
                    """;

            mockMvc.perform(post("/api/v1/metodos-pago")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.nombre").value("Tarjeta de Débito"))
                    .andExpect(jsonPath("$.activo").value(true));

            assertTrue(metodoPagoRepository.existsByNombreIgnoreCase("Tarjeta de Débito"));
        }

        @Test
        @DisplayName("it: lista métodos de pago persistidos")
        void listarMetodosPago_retornaDatosPersistidos() throws Exception {
            String body = """
                    {
                      "nombre": "Transferencia Bancaria",
                      "activo": true
                    }
                    """;

            mockMvc.perform(post("/api/v1/metodos-pago")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isCreated());

            mockMvc.perform(get("/api/v1/metodos-pago"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].nombre").value("Transferencia Bancaria"))
                    .andExpect(jsonPath("$[0].activo").value(true));
        }

        @Test
        @DisplayName("it: valida duplicados usando Service y Repository real")
        void crearMetodoPagoDuplicado_retornaError() throws Exception {
            String body = """
                    {
                      "nombre": "Webpay",
                      "activo": true
                    }
                    """;

            mockMvc.perform(post("/api/v1/metodos-pago")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isCreated());

            mockMvc.perform(post("/api/v1/metodos-pago")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isBadRequest());
        }
    }
}