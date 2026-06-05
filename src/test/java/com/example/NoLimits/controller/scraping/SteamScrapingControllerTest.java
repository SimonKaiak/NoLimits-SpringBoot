package com.example.NoLimits.controller.scraping;

import com.example.NoLimits.Multimedia.controller.scraping.SteamScrapingController;
import com.example.NoLimits.Multimedia.service.scraping.ScrapingClientService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SteamScrapingController.class)
@AutoConfigureMockMvc(addFilters = false)
class SteamScrapingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ScrapingClientService scrapingClientService;

    @Nested
    @DisplayName("Unitario - SteamScrapingController")
    class ObtenerPrecioSteam {

        @Test
        @DisplayName("retorna 200 y los datos del precio de Steam")
        void obtenerPrecioSteam_Retorna200() throws Exception {
            Map<String, Object> response = Map.of(
                    "nombre", "Counter-Strike 2",
                    "precio", 0,
                    "precioFormato", "Free To Play",
                    "moneda", "CLP",
                    "urlPlataforma", "https://store.steampowered.com/app/730",
                    "plataforma", "Steam",
                    "fechaUltimaActualizacion", "2026-06-05"
            );

            when(scrapingClientService.obtenerPrecioSteam("730"))
                    .thenReturn(response);

            mockMvc.perform(get("/api/scraping/steam")
                            .param("appId", "730"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.nombre").value("Counter-Strike 2"))
                    .andExpect(jsonPath("$.precio").value(0))
                    .andExpect(jsonPath("$.precioFormato").value("Free To Play"))
                    .andExpect(jsonPath("$.moneda").value("CLP"))
                    .andExpect(jsonPath("$.urlPlataforma").value("https://store.steampowered.com/app/730"))
                    .andExpect(jsonPath("$.plataforma").value("Steam"))
                    .andExpect(jsonPath("$.fechaUltimaActualizacion").value("2026-06-05"));

            verify(scrapingClientService).obtenerPrecioSteam("730");
        }
    }
}