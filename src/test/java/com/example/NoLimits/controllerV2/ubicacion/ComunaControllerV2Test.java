package com.example.NoLimits.controllerV2.ubicacion;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.assemblers.ubicacion.ComunaModelAssembler;
import com.example.NoLimits.Multimedia.controllerV2.ubicacion.ComunaControllerV2;
import com.example.NoLimits.Multimedia.dto.ubicacion.response.ComunaResponseDTO;
import com.example.NoLimits.Multimedia.service.ubicacion.ComunaService;

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

@WebMvcTest(ComunaControllerV2.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("ComunaControllerV2 Tests")
class ComunaControllerV2Test {


@Autowired
private MockMvc mockMvc;

@MockBean
private ComunaService comunaService;

@MockBean
private ComunaModelAssembler comunaAssembler;

@Nested
@DisplayName("Consultas de comunas")
class Consultas {

    @Test
    @DisplayName("Debe listar comunas")
    void debeListarComunas() throws Exception {

        ComunaResponseDTO dto = new ComunaResponseDTO();
        dto.setId(1L);
        dto.setNombre("Santiago");
        dto.setRegionId(13L);

        when(comunaService.findAll())
                .thenReturn(List.of(dto));

        when(comunaAssembler.toModel(any()))
                .thenReturn(EntityModel.of(dto));

        mockMvc.perform(get("/api/v2/comunas"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Debe retornar 200 cuando lista de comunas está vacía")
    void debeRetornar200ListaVacia() throws Exception {

        when(comunaService.findAll())
                .thenReturn(List.of());

        mockMvc.perform(get("/api/v2/comunas"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Debe buscar comuna por ID")
    void debeBuscarComunaPorId() throws Exception {

        ComunaResponseDTO dto = new ComunaResponseDTO();
        dto.setId(1L);
        dto.setNombre("Santiago");
        dto.setRegionId(13L);

        when(comunaService.findById(1L))
                .thenReturn(dto);

        when(comunaAssembler.toModel(any()))
                .thenReturn(EntityModel.of(dto));

        mockMvc.perform(get("/api/v2/comunas/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Debe retornar 404 cuando comuna no existe")
    void debeRetornar404ComunaNoExiste() throws Exception {

        when(comunaService.findById(999L))
                .thenThrow(new RecursoNoEncontradoException("Comuna no encontrada"));

        mockMvc.perform(get("/api/v2/comunas/999"))
                .andExpect(status().isNotFound());
    }
}

@Nested
@DisplayName("Creación de comunas")
class Creacion {

    @Test
    @DisplayName("Debe crear comuna")
    void debeCrearComuna() throws Exception {

        ComunaResponseDTO dto = new ComunaResponseDTO();
        dto.setId(1L);
        dto.setNombre("Puente Alto");
        dto.setRegionId(13L);

        when(comunaService.create(any()))
                .thenReturn(dto);

        when(comunaAssembler.toModel(any()))
                .thenReturn(
                        EntityModel.of(dto)
                                .add(Link.of(
                                        "http://localhost/api/v2/comunas/1",
                                        "self"))
                );

        mockMvc.perform(post("/api/v2/comunas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "nombre":"Puente Alto",
                                    "regionId":13
                                }
                                """))
                .andExpect(status().isCreated());
    }
}

@Nested
@DisplayName("Actualización de comunas")
class Actualizacion {

    @Test
    @DisplayName("Debe actualizar comuna con PUT")
    void debeActualizarComunaPut() throws Exception {

        ComunaResponseDTO dto = new ComunaResponseDTO();
        dto.setId(1L);

        when(comunaService.update(eq(1L), any()))
                .thenReturn(dto);

        when(comunaAssembler.toModel(any()))
                .thenReturn(EntityModel.of(dto));

        mockMvc.perform(put("/api/v2/comunas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "nombre":"La Florida",
                                    "regionId":13
                                }
                                """))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Debe retornar 404 al actualizar comuna con PUT")
    void debeRetornar404Update() throws Exception {

        when(comunaService.update(eq(999L), any()))
                .thenThrow(new RecursoNoEncontradoException("Comuna no encontrada"));

        mockMvc.perform(put("/api/v2/comunas/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "nombre":"Test",
                                    "regionId":13
                                }
                                """))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Debe actualizar comuna con PATCH")
    void debeActualizarComunaPatch() throws Exception {

        ComunaResponseDTO dto = new ComunaResponseDTO();
        dto.setId(1L);

        when(comunaService.patch(eq(1L), any()))
                .thenReturn(dto);

        when(comunaAssembler.toModel(any()))
                .thenReturn(EntityModel.of(dto));

        mockMvc.perform(patch("/api/v2/comunas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "nombre":"San Bernardo"
                                }
                                """))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Debe retornar 404 al actualizar comuna con PATCH")
    void debeRetornar404Patch() throws Exception {

        when(comunaService.patch(eq(999L), any()))
                .thenThrow(new RecursoNoEncontradoException("Comuna no encontrada"));

        mockMvc.perform(patch("/api/v2/comunas/999")
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
@DisplayName("Eliminación de comunas")
class Eliminacion {

    @Test
    @DisplayName("Debe eliminar comuna")
    void debeEliminarComuna() throws Exception {

        doNothing()
                .when(comunaService)
                .deleteById(1L);

        mockMvc.perform(delete("/api/v2/comunas/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Debe retornar 404 al eliminar comuna")
    void debeRetornar404Delete() throws Exception {

        doThrow(new RecursoNoEncontradoException("Comuna no encontrada"))
                .when(comunaService)
                .deleteById(999L);

        mockMvc.perform(delete("/api/v2/comunas/999"))
                .andExpect(status().isNotFound());
    }
}

}
