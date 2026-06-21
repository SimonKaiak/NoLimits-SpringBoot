package com.example.NoLimits.integration.scraping;

import com.example.NoLimits.Multimedia.service.scraping.ScrapingClientService;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@DisplayName("T-Integración · Steam Scraping")
class SteamScrapingIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ScrapingClientService scrapingClientService;

    @Test
    @DisplayName("it: consulta precio Steam y retorna datos")
    void obtenerPrecioSteam_retornaDatos() throws Exception {
        when(scrapingClientService.obtenerPrecioSteam("730"))
                .thenReturn(Map.of(
                        "nombre", "Counter-Strike 2",
                        "precio", 0,
                        "moneda", "CLP",
                        "plataforma", "Steam"
                ));

        mockMvc.perform(get("/api/scraping/steam").param("appId", "730"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Counter-Strike 2"))
                .andExpect(jsonPath("$.plataforma").value("Steam"));
    }
}