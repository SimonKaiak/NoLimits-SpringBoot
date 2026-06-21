package com.example.NoLimits.integration.catalogos;

import com.example.NoLimits.Multimedia.model.catalogos.EmpresaModel;
import com.example.NoLimits.Multimedia.model.catalogos.TipoEmpresaModel;
import com.example.NoLimits.Multimedia.repository.catalogos.EmpresaRepository;
import com.example.NoLimits.Multimedia.repository.catalogos.TipoEmpresaRepository;
import com.example.NoLimits.Multimedia.repository.catalogos.TiposEmpresaRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:nolimits_tipos_empresa_integration_test;MODE=PostgreSQL;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE",
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
@DisplayName("T-Integración · Relación Tipos de Empresa")
class TiposEmpresaIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private TipoEmpresaRepository tipoEmpresaRepository;

    @Autowired
    private TiposEmpresaRepository tiposEmpresaRepository;

    @BeforeEach
    void limpiarDatos() {
        tiposEmpresaRepository.deleteAll();
        empresaRepository.deleteAll();
        tipoEmpresaRepository.deleteAll();
    }

    private EmpresaModel crearEmpresa(String nombre) {
        EmpresaModel empresa = new EmpresaModel();
        empresa.setNombre(nombre);
        return empresaRepository.save(empresa);
    }

    private TipoEmpresaModel crearTipoEmpresa(String nombre) {
        TipoEmpresaModel tipo = new TipoEmpresaModel();
        tipo.setNombre(nombre);
        return tipoEmpresaRepository.save(tipo);
    }

    @Nested
    @DisplayName("describe: integración Controller → Service → Repository")
    class IntegracionTiposEmpresa {

        @Test
        @DisplayName("it: crea relación empresa-tipo y la persiste")
        void crearRelacion_persisteEnBaseDeDatos() throws Exception {
            EmpresaModel empresa = crearEmpresa("Sony Interactive Entertainment");
            TipoEmpresaModel tipo = crearTipoEmpresa("Publisher");

            mockMvc.perform(post("/api/v1/empresas/" + empresa.getId() + "/tipos-empresa/" + tipo.getId()))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.empresaId").value(empresa.getId()))
                    .andExpect(jsonPath("$.tipoEmpresaId").value(tipo.getId()))
                    .andExpect(jsonPath("$.tipoEmpresaNombre").value("Publisher"));

            assertTrue(
                    tiposEmpresaRepository.existsByEmpresa_IdAndTipoEmpresa_Id(
                            empresa.getId(),
                            tipo.getId()
                    )
            );
        }

        @Test
        @DisplayName("it: lista relaciones por empresa desde la base de datos")
        void listarRelaciones_retornaDatosPersistidos() throws Exception {
            EmpresaModel empresa = crearEmpresa("Nintendo");
            TipoEmpresaModel tipo = crearTipoEmpresa("Desarrolladora");

            mockMvc.perform(post("/api/v1/empresas/" + empresa.getId() + "/tipos-empresa/" + tipo.getId()))
                    .andExpect(status().isCreated());

            mockMvc.perform(get("/api/v1/empresas/" + empresa.getId() + "/tipos-empresa"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].empresaId").value(empresa.getId()))
                    .andExpect(jsonPath("$[0].tipoEmpresaId").value(tipo.getId()));
        }

        @Test
        @DisplayName("it: evita duplicar la misma relación")
        void crearRelacionDuplicada_retornaError() throws Exception {
            EmpresaModel empresa = crearEmpresa("Capcom");
            TipoEmpresaModel tipo = crearTipoEmpresa("Distribuidora");

            mockMvc.perform(post("/api/v1/empresas/" + empresa.getId() + "/tipos-empresa/" + tipo.getId()))
                    .andExpect(status().isCreated());

            mockMvc.perform(post("/api/v1/empresas/" + empresa.getId() + "/tipos-empresa/" + tipo.getId()))
                    .andExpect(status().isConflict());
        }
    }
}