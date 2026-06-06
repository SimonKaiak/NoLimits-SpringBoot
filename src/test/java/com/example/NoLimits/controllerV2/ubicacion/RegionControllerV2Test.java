package com.example.NoLimits.controllerV2.ubicacion;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.assemblers.ubicacion.RegionModelAssembler;
import com.example.NoLimits.Multimedia.controllerV2.ubicacion.RegionControllerV2;
import com.example.NoLimits.Multimedia.dto.ubicacion.response.RegionResponseDTO;
import com.example.NoLimits.Multimedia.service.ubicacion.RegionService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RegionControllerV2.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("RegionControllerV2 Tests")
class RegionControllerV2Test {

@Autowired
private MockMvc mockMvc;

@MockBean
private RegionService regionService;

@MockBean
private RegionModelAssembler regionAssembler;

@Nested
@DisplayName("Consultas de regiones")
class Consultas {

    @Test
    @DisplayName("Debe listar regiones")
    void debeListarRegiones() throws Exception {

        RegionResponseDTO dto = new RegionResponseDTO();
        dto.setId(13L);
        dto.setNombre("Metropolitana");

        when(regionService.findAll())
                .thenReturn(List.of(dto));

        when(regionAssembler.toModel(any()))
                .thenReturn(EntityModel.of(dto));

        mockMvc.perform(get("/api/v2/regiones"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Debe retornar 200 cuando lista está vacía")
    void debeRetornar200ListaVacia() throws Exception {

        when(regionService.findAll())
                .thenReturn(List.of());

        mockMvc.perform(get("/api/v2/regiones"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Debe buscar región por ID")
    void debeBuscarRegionPorId() throws Exception {

        RegionResponseDTO dto = new RegionResponseDTO();
        dto.setId(13L);
        dto.setNombre("Metropolitana");

        when(regionService.findById(13L))
                .thenReturn(dto);

        when(regionAssembler.toModel(any()))
                .thenReturn(EntityModel.of(dto));

        mockMvc.perform(get("/api/v2/regiones/13"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Debe retornar 404 cuando región no existe")
    void debeRetornar404RegionNoExiste() throws Exception {

        when(regionService.findById(999L))
                .thenThrow(new RecursoNoEncontradoException("No encontrada"));

        mockMvc.perform(get("/api/v2/regiones/999"))
                .andExpect(status().isNotFound());
    }
}

@Nested
@DisplayName("Creación de regiones")
class Creacion {

    @Test
    @DisplayName("Debe crear región")
    void debeCrearRegion() throws Exception {

        RegionResponseDTO dto = new RegionResponseDTO();
        dto.setId(13L);
        dto.setNombre("Metropolitana");

        when(regionService.save(any()))
                .thenReturn(dto);

        when(regionAssembler.toModel(any()))
                .thenReturn(
                EntityModel.of(dto)
                        .add(Link.of(
                                "http://localhost/api/v2/regiones/13",
                                "self"))
        );

        mockMvc.perform(post("/api/v2/regiones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "nombre":"Región Metropolitana"
                                }
                                """))
                .andExpect(status().isCreated());
    }
}

@Nested
@DisplayName("Actualización de regiones")
class Actualizacion {

    @Test
    @DisplayName("Debe actualizar región con PUT")
    void debeActualizarRegionPut() throws Exception {

        RegionResponseDTO dto = new RegionResponseDTO();
        dto.setId(13L);

        when(regionService.update(eq(13L), any()))
                .thenReturn(dto);

        when(regionAssembler.toModel(any()))
                .thenReturn(EntityModel.of(dto));

        mockMvc.perform(put("/api/v2/regiones/13")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "nombre":"Nueva Región"
                                }
                                """))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Debe retornar 404 al actualizar región con PUT")
    void debeRetornar404Update() throws Exception {

        when(regionService.update(eq(999L), any()))
                .thenThrow(new RecursoNoEncontradoException("No encontrada"));

        mockMvc.perform(put("/api/v2/regiones/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "nombre":"Test"
                                }
                                """))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Debe actualizar región con PATCH")
    void debeActualizarRegionPatch() throws Exception {

        RegionResponseDTO dto = new RegionResponseDTO();
        dto.setId(13L);

        when(regionService.patch(eq(13L), any()))
                .thenReturn(dto);

        when(regionAssembler.toModel(any()))
                .thenReturn(EntityModel.of(dto));

        mockMvc.perform(patch("/api/v2/regiones/13")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "nombre":"Región Actualizada"
                                }
                                """))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Debe retornar 404 al actualizar región con PATCH")
    void debeRetornar404Patch() throws Exception {

        when(regionService.patch(eq(999L), any()))
                .thenThrow(new RecursoNoEncontradoException("No encontrada"));

        mockMvc.perform(patch("/api/v2/regiones/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "nombre":"Test"
                                }
                                """))
                .andExpect(status().isNotFound());
    }
}

@Nested
@DisplayName("Eliminación de regiones")
class Eliminacion {

    @Test
    @DisplayName("Debe eliminar región")
    void debeEliminarRegion() throws Exception {

        doNothing()
                .when(regionService)
                .deleteById(13L);

        mockMvc.perform(delete("/api/v2/regiones/13"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Debe retornar 404 al eliminar región")
    void debeRetornar404Delete() throws Exception {

        doThrow(new RecursoNoEncontradoException("No encontrada"))
                .when(regionService)
                .deleteById(999L);

        mockMvc.perform(delete("/api/v2/regiones/999"))
                .andExpect(status().isNotFound());
    }
}


}
