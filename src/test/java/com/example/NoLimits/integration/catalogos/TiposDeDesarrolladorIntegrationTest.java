package com.example.NoLimits.integration.catalogos;

import com.example.NoLimits.Multimedia.model.catalogos.DesarrolladorModel;
import com.example.NoLimits.Multimedia.model.catalogos.TipoDeDesarrolladorModel;
import com.example.NoLimits.Multimedia.repository.catalogos.DesarrolladorRepository;
import com.example.NoLimits.Multimedia.repository.catalogos.TipoDeDesarrolladorRepository;
import com.example.NoLimits.Multimedia.repository.catalogos.TiposDeDesarrolladorRepository;

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
        "spring.datasource.url=jdbc:h2:mem:nolimits_tipos_desarrollador_integration_test;MODE=PostgreSQL;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE",
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
@DisplayName("T-Integración · Relación Tipos de Desarrollador")
class TiposDeDesarrolladorIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DesarrolladorRepository desarrolladorRepository;

    @Autowired
    private TipoDeDesarrolladorRepository tipoDeDesarrolladorRepository;

    @Autowired
    private TiposDeDesarrolladorRepository tiposDeDesarrolladorRepository;

    @BeforeEach
    void limpiarDatos() {
        tiposDeDesarrolladorRepository.deleteAll();
        desarrolladorRepository.deleteAll();
        tipoDeDesarrolladorRepository.deleteAll();
    }

    private DesarrolladorModel crearDesarrollador(String nombre) {
        DesarrolladorModel desarrollador = new DesarrolladorModel();
        desarrollador.setNombre(nombre);
        return desarrolladorRepository.save(desarrollador);
    }

    private TipoDeDesarrolladorModel crearTipo(String nombre) {
        TipoDeDesarrolladorModel tipo = new TipoDeDesarrolladorModel();
        tipo.setNombre(nombre);
        return tipoDeDesarrolladorRepository.save(tipo);
    }

    @Nested
    @DisplayName("describe: integración Controller → Service → Repository")
    class IntegracionTiposDeDesarrollador {

        @Test
        @DisplayName("it: crea relación desarrollador-tipo y la persiste")
        void crearRelacion_persisteEnBaseDeDatos() throws Exception {
            DesarrolladorModel dev = crearDesarrollador("Nintendo");
            TipoDeDesarrolladorModel tipo = crearTipo("Publisher");

            mockMvc.perform(post("/api/desarrolladores/" + dev.getId() + "/tipos/" + tipo.getId()))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.desarrolladorId").value(dev.getId()))
                    .andExpect(jsonPath("$.tipoDeDesarrolladorId").value(tipo.getId()));

            assertTrue(
                    tiposDeDesarrolladorRepository
                            .existsByDesarrollador_IdAndTipoDeDesarrollador_Id(dev.getId(), tipo.getId())
            );
        }

        @Test
        @DisplayName("it: lista relaciones por desarrollador desde la base de datos")
        void listarRelaciones_retornaDatosPersistidos() throws Exception {
            DesarrolladorModel dev = crearDesarrollador("Sony");
            TipoDeDesarrolladorModel tipo = crearTipo("Estudio");

            mockMvc.perform(post("/api/desarrolladores/" + dev.getId() + "/tipos/" + tipo.getId()))
                    .andExpect(status().isCreated());

            mockMvc.perform(get("/api/desarrolladores/" + dev.getId() + "/tipos"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].desarrolladorId").value(dev.getId()))
                    .andExpect(jsonPath("$[0].tipoDeDesarrolladorId").value(tipo.getId()));
        }

        @Test
        @DisplayName("it: evita duplicar la misma relación")
        void crearRelacionDuplicada_retornaBadRequest() throws Exception {
            DesarrolladorModel dev = crearDesarrollador("Capcom");
            TipoDeDesarrolladorModel tipo = crearTipo("Desarrolladora");

            mockMvc.perform(post("/api/desarrolladores/" + dev.getId() + "/tipos/" + tipo.getId()))
                    .andExpect(status().isCreated());

            mockMvc.perform(post("/api/desarrolladores/" + dev.getId() + "/tipos/" + tipo.getId()))
                    .andExpect(status().isBadRequest());
        }
    }
}