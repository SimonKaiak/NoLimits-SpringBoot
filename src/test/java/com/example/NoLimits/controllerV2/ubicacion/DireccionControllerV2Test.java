package com.example.NoLimits.controllerV2.ubicacion;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.assemblers.ubicacion.DireccionModelAssembler;
import com.example.NoLimits.Multimedia.controllerV2.ubicacion.DireccionControllerV2;
import com.example.NoLimits.Multimedia.dto.ubicacion.response.DireccionResponseDTO;
import com.example.NoLimits.Multimedia.service.ubicacion.DireccionService;

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

@WebMvcTest(DireccionControllerV2.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("DireccionControllerV2 Tests")
class DireccionControllerV2Test {

@Autowired
private MockMvc mockMvc;

@MockBean
private DireccionService direccionService;

@MockBean
private DireccionModelAssembler direccionAssembler;

@Nested
@DisplayName("Consultas de direcciones")
class Consultas {

    @Test
    @DisplayName("Debe listar direcciones")
    void debeListarDirecciones() throws Exception {

        DireccionResponseDTO dto = new DireccionResponseDTO();
        dto.setId(1L);

        when(direccionService.findAll())
                .thenReturn(List.of(dto));

        when(direccionAssembler.toModel(any()))
                .thenReturn(EntityModel.of(dto));

        mockMvc.perform(get("/api/v2/direcciones"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Debe retornar 200 cuando lista está vacía")
    void debeRetornar200ListaVacia() throws Exception {

        when(direccionService.findAll())
                .thenReturn(List.of());

        mockMvc.perform(get("/api/v2/direcciones"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Debe buscar dirección por ID")
    void debeBuscarDireccionPorId() throws Exception {

        DireccionResponseDTO dto = new DireccionResponseDTO();
        dto.setId(1L);

        when(direccionService.findById(1L))
                .thenReturn(dto);

        when(direccionAssembler.toModel(any()))
                .thenReturn(EntityModel.of(dto));

        mockMvc.perform(get("/api/v2/direcciones/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Debe retornar 404 cuando dirección no existe")
    void debeRetornar404DireccionNoExiste() throws Exception {

        when(direccionService.findById(999L))
                .thenThrow(new RecursoNoEncontradoException("No encontrada"));

        mockMvc.perform(get("/api/v2/direcciones/999"))
                .andExpect(status().isNotFound());
    }
}

@Nested
@DisplayName("Creación de direcciones")
class Creacion {

    @Test
    @DisplayName("Debe crear dirección")
    void debeCrearDireccion() throws Exception {

        DireccionResponseDTO dto = new DireccionResponseDTO();
        dto.setId(1L);

        when(direccionService.save(any()))
                .thenReturn(dto);

        when(direccionAssembler.toModel(any()))
                .thenReturn(
                        EntityModel.of(dto)
                                .add(Link.of(
                                        "http://localhost/api/v2/direcciones/1",
                                        "self"))
                );

        mockMvc.perform(post("/api/v2/direcciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "calle":"Av. Siempre Viva",
                                  "numero":"742",
                                  "comunaId":1
                                }
                                """))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Debe retornar 400 cuando calle es vacía")
    void debeRetornar400CalleVacia() throws Exception {

        mockMvc.perform(post("/api/v2/direcciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "calle":"",
                                  "numero":"742",
                                  "comunaId":1
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Debe retornar 400 cuando comunaId es nulo")
    void debeRetornar400ComunaIdNulo() throws Exception {

        mockMvc.perform(post("/api/v2/direcciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "calle":"Av. Siempre Viva",
                                  "numero":"742"
                                }
                                """))
                .andExpect(status().isBadRequest());
    }
}

@Nested
@DisplayName("Actualización de direcciones")
class Actualizacion {

    @Test
    @DisplayName("Debe actualizar dirección con PATCH")
    void debeActualizarDireccionPatch() throws Exception {

        DireccionResponseDTO dto = new DireccionResponseDTO();
        dto.setId(1L);

        when(direccionService.patch(eq(1L), any()))
                .thenReturn(dto);

        when(direccionAssembler.toModel(any()))
                .thenReturn(EntityModel.of(dto));

        mockMvc.perform(patch("/api/v2/direcciones/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "calle":"Av. Providencia"
                                }
                                """))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Debe retornar 404 al actualizar dirección")
    void debeRetornar404Patch() throws Exception {

        when(direccionService.patch(eq(999L), any()))
                .thenThrow(new RecursoNoEncontradoException("No encontrada"));

        mockMvc.perform(patch("/api/v2/direcciones/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "calle":"Test"
                                }
                                """))
                .andExpect(status().isNotFound());
    }
}

@Nested
@DisplayName("Eliminación de direcciones")
class Eliminacion {

    @Test
    @DisplayName("Debe eliminar dirección")
    void debeEliminarDireccion() throws Exception {

        doNothing()
                .when(direccionService)
                .deleteById(1L);

        mockMvc.perform(delete("/api/v2/direcciones/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Debe retornar 404 al eliminar dirección")
    void debeRetornar404Delete() throws Exception {

        doThrow(new RecursoNoEncontradoException("No encontrada"))
                .when(direccionService)
                .deleteById(999L);

        mockMvc.perform(delete("/api/v2/direcciones/999"))
                .andExpect(status().isNotFound());
    }
}


}
