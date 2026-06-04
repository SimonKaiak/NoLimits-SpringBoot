package com.example.NoLimits.controller.ubicacion;

import com.example.NoLimits.Multimedia.controller.ubicacion.RegionController;
import com.example.NoLimits.Multimedia.dto.ubicacion.response.RegionResponseDTO;
import com.example.NoLimits.Multimedia.service.ubicacion.RegionService;

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

@WebMvcTest(RegionController.class)
@AutoConfigureMockMvc(addFilters = false)
class RegionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RegionService regionService;

    @Test
    @DisplayName("Debe listar regiones")
    void debeListarRegiones() throws Exception {

        RegionResponseDTO region = new RegionResponseDTO();
        region.setId(1L);
        region.setNombre("Metropolitana");

        when(regionService.findAll())
                .thenReturn(List.of(region));

        mockMvc.perform(get("/api/regiones"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Debe retornar lista vacía de regiones")
    void debeRetornarListaVaciaRegiones() throws Exception {

        when(regionService.findAll())
                .thenReturn(List.of());

        mockMvc.perform(get("/api/regiones"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Debe buscar región por ID")
    void debeBuscarRegionPorId() throws Exception {

        RegionResponseDTO region = new RegionResponseDTO();
        region.setId(1L);
        region.setNombre("Metropolitana");

        when(regionService.findById(1L))
                .thenReturn(region);

        mockMvc.perform(get("/api/regiones/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Debe crear región")
    void debeCrearRegion() throws Exception {

        when(regionService.save(any()))
                .thenReturn(new RegionResponseDTO());

        mockMvc.perform(post("/api/regiones")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "nombre":"Metropolitana"
                        }
                        """))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Debe actualizar región con PUT")
    void debeActualizarRegionConPut() throws Exception {

        when(regionService.update(eq(1L), any()))
                .thenReturn(new RegionResponseDTO());

        mockMvc.perform(put("/api/regiones/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "nombre":"Valparaíso"
                        }
                        """))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Debe actualizar región con PATCH")
    void debeActualizarRegionConPatch() throws Exception {

        when(regionService.patch(eq(1L), any()))
                .thenReturn(new RegionResponseDTO());

        mockMvc.perform(patch("/api/regiones/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "nombre":"Biobío"
                        }
                        """))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Debe eliminar región")
    void debeEliminarRegion() throws Exception {

        doNothing().when(regionService).deleteById(1L);

        mockMvc.perform(delete("/api/regiones/1"))
                .andExpect(status().isNoContent());
    }
}