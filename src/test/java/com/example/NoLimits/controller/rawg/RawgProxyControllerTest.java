package com.example.NoLimits.controller.rawg;

import com.example.NoLimits.Multimedia.controller.rawg.RawgProxyController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RawgProxyController.class)
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(properties = "rawg.key=test-key")
@DisplayName("RawgProxyControllerTest — Proxy hacia API de RAWG")
class RawgProxyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("GET /api/rawg/games → endpoint existe y responde (no 404)")
    void proxy_games_endpointExiste() throws Exception {
        mockMvc.perform(get("/api/rawg/games"))
                .andExpect(result -> {
                    int status = result.getResponse().getStatus();
                    org.junit.jupiter.api.Assertions.assertNotEquals(404, status,
                        "El endpoint /api/rawg/games no debe retornar 404");
                });
    }

    @Test
    @DisplayName("GET /api/rawg/games?search=zelda → endpoint acepta query string")
    void proxy_games_aceptaQueryString() throws Exception {
        mockMvc.perform(get("/api/rawg/games").queryParam("search", "zelda"))
                .andExpect(result -> {
                    int status = result.getResponse().getStatus();
                    org.junit.jupiter.api.Assertions.assertNotEquals(404, status,
                        "El endpoint debe aceptar query string sin retornar 404");
                });
    }

    @Test
    @DisplayName("GET /api/rawg/genres → endpoint wildcard existe")
    void proxy_otroPath_endpointWildcard() throws Exception {
        mockMvc.perform(get("/api/rawg/genres"))
                .andExpect(result -> {
                    int status = result.getResponse().getStatus();
                    org.junit.jupiter.api.Assertions.assertNotEquals(404, status,
                        "El wildcard /** debe manejar cualquier path bajo /api/rawg/");
                });
    }
}