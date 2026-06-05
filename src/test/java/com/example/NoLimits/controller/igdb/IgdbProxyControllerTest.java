package com.example.NoLimits.controller.igdb;

import com.example.NoLimits.Multimedia.controller.igdb.IgdbProxyController;
import com.example.NoLimits.Multimedia.service.igdb.IgdbTokenService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(IgdbProxyController.class)
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(properties = "igdb.client-id=test-client-id")
@DisplayName("IgdbProxyControllerTest — Proxy hacia API de IGDB")
class IgdbProxyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IgdbTokenService igdbTokenService;

    @Test
    @DisplayName("GET /api/igdb/games → cuando token falla retorna 500 con mensaje JSON")
    void getGames_tokenFalla_retorna500ConMensaje() throws Exception {
        when(igdbTokenService.getAccessToken())
                .thenThrow(new RuntimeException("Token service error"));

        mockMvc.perform(get("/api/igdb/games"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("No se pudo consultar IGDB")));
    }

    @Test
    @DisplayName("POST /api/igdb/games → cuando token falla retorna 500 con mensaje JSON")
    void queryGames_tokenFalla_retorna500ConMensaje() throws Exception {
        when(igdbTokenService.getAccessToken())
                .thenThrow(new RuntimeException("Token expirado"));

        mockMvc.perform(post("/api/igdb/games")
                        .contentType(MediaType.TEXT_PLAIN)
                        .content("fields name; limit 5;"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("No se pudo consultar IGDB")));
    }

    @Test
    @DisplayName("GET /api/igdb/games/search → cuando token falla retorna 500 con mensaje JSON")
    void searchGames_tokenFalla_retorna500ConMensaje() throws Exception {
        when(igdbTokenService.getAccessToken())
                .thenThrow(new RuntimeException("Token inválido"));

        mockMvc.perform(get("/api/igdb/games/search").queryParam("q", "zelda"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("No se pudo consultar IGDB")));
    }

    @Test
    @DisplayName("POST /api/igdb/games sin body → cuando token falla retorna 500")
    void queryGames_sinBody_tokenFalla_retorna500() throws Exception {
        when(igdbTokenService.getAccessToken())
                .thenThrow(new RuntimeException("Sin token"));

        mockMvc.perform(post("/api/igdb/games"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("GET /api/igdb/games → endpoint existe (no retorna 404)")
    void getGames_endpointExiste() throws Exception {
        when(igdbTokenService.getAccessToken()).thenReturn("fake-token");

        mockMvc.perform(get("/api/igdb/games"))
                .andExpect(result -> {
                    int status = result.getResponse().getStatus();
                    org.junit.jupiter.api.Assertions.assertNotEquals(404, status,
                        "El endpoint /api/igdb/games no debe retornar 404");
                });
    }
}