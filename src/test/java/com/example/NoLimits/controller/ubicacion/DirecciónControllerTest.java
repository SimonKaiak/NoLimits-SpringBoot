package com.example.NoLimits.controller.ubicacion;

import com.example.NoLimits.Multimedia.controller.ubicacion.DireccionController;
import com.example.NoLimits.Multimedia.dto.ubicacion.response.DireccionResponseDTO;
import com.example.NoLimits.Multimedia.service.ubicacion.DireccionService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DireccionController.class)
@AutoConfigureMockMvc(addFilters = false)
class DireccionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DireccionService direccionService;

    @Test
    @DisplayName("Debe listar direcciones")
    void debeListarDirecciones() throws Exception {

        when(direccionService.findAll())
                .thenReturn(List.of(new DireccionResponseDTO()));

        mockMvc.perform(get("/api/direcciones"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Debe retornar 204 cuando no existen direcciones")
    void debeRetornarNoContentCuandoNoExistenDirecciones() throws Exception {

        when(direccionService.findAll())
                .thenReturn(List.of());

        mockMvc.perform(get("/api/direcciones"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Debe buscar dirección por ID")
    void debeBuscarDireccionPorId() throws Exception {

        when(direccionService.findById(1L))
                .thenReturn(new DireccionResponseDTO());

        mockMvc.perform(get("/api/direcciones/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Debe crear dirección")
    void debeCrearDireccion() throws Exception {

        when(direccionService.save(any()))
                .thenReturn(new DireccionResponseDTO());

        mockMvc.perform(post("/api/direcciones")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "calle":"Alameda",
                          "numero":"123",
                          "comunaId":1
                        }
                        """))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Debe actualizar dirección con PUT")
    void debeActualizarDireccionConPut() throws Exception {

        when(direccionService.update(eq(1L), any()))
                .thenReturn(new DireccionResponseDTO());

        mockMvc.perform(put("/api/direcciones/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "calle":"Providencia"
                        }
                        """))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Debe actualizar dirección con PATCH")
    void debeActualizarDireccionConPatch() throws Exception {

        when(direccionService.patch(eq(1L), any()))
                .thenReturn(new DireccionResponseDTO());

        mockMvc.perform(patch("/api/direcciones/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "calle":"Las Condes"
                        }
                        """))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Debe eliminar dirección")
    void debeEliminarDireccion() throws Exception {

        doNothing().when(direccionService).deleteById(1L);

        mockMvc.perform(delete("/api/direcciones/1"))
                .andExpect(status().isNoContent());
    }
}