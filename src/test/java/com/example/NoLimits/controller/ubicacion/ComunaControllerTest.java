package com.example.NoLimits.controller.ubicacion;

import com.example.NoLimits.Multimedia.controller.ubicacion.ComunaController;
import com.example.NoLimits.Multimedia.dto.ubicacion.response.ComunaResponseDTO;
import com.example.NoLimits.Multimedia.service.ubicacion.ComunaService;

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

@WebMvcTest(ComunaController.class)
@AutoConfigureMockMvc(addFilters = false)
class ComunaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ComunaService comunaService;

    @Test
    @DisplayName("Debe listar comunas")
    void debeListarComunas() throws Exception {

        ComunaResponseDTO comuna = new ComunaResponseDTO();

        when(comunaService.findAll())
                .thenReturn(List.of(comuna));

        mockMvc.perform(get("/api/comunas"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Debe retornar 204 cuando no existen comunas")
    void debeRetornarNoContentCuandoNoExistenComunas() throws Exception {

        when(comunaService.findAll())
                .thenReturn(List.of());

        mockMvc.perform(get("/api/comunas"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Debe buscar comuna por ID")
    void debeBuscarComunaPorId() throws Exception {

        when(comunaService.findById(1L))
                .thenReturn(new ComunaResponseDTO());

        mockMvc.perform(get("/api/comunas/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Debe crear comuna")
    void debeCrearComuna() throws Exception {

        when(comunaService.create(any()))
                .thenReturn(new ComunaResponseDTO());

        mockMvc.perform(post("/api/comunas")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "nombre":"Santiago",
                          "regionId":1
                        }
                        """))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Debe actualizar comuna con PUT")
    void debeActualizarComunaConPut() throws Exception {

        when(comunaService.update(eq(1L), any()))
                .thenReturn(new ComunaResponseDTO());

        mockMvc.perform(put("/api/comunas/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "nombre":"Providencia"
                        }
                        """))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Debe actualizar comuna con PATCH")
    void debeActualizarComunaConPatch() throws Exception {

        when(comunaService.patch(eq(1L), any()))
                .thenReturn(new ComunaResponseDTO());

        mockMvc.perform(patch("/api/comunas/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "nombre":"Ñuñoa"
                        }
                        """))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Debe eliminar comuna")
    void debeEliminarComuna() throws Exception {

        doNothing().when(comunaService).deleteById(1L);

        mockMvc.perform(delete("/api/comunas/1"))
                .andExpect(status().isNoContent());
    }
}