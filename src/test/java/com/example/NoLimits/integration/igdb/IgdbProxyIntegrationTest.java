package com.example.NoLimits.integration.igdb;

import com.example.NoLimits.Multimedia.service.igdb.IgdbTokenService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@TestPropertySource(properties = {
        "igdb.client-id=test-client-id",
        "igdb.client-secret=test-client-secret"
})
@DisplayName("T-Integración · IGDB Proxy")
class IgdbProxyIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IgdbTokenService igdbTokenService;

    @Nested
    @DisplayName("describe: integración Controller → TokenService → manejo de error externo")
    class IntegracionIgdbProxy {

        @Test
        @DisplayName("it: GET /api/igdb/games existe y controla error externo")
        void getGames_endpointExiste_controlaErrorExterno() throws Exception {
            when(igdbTokenService.getAccessToken())
                    .thenReturn("fake-token");

            mockMvc.perform(get("/api/igdb/games"))
                    .andExpect(status().isInternalServerError())
                    .andExpect(content().string(org.hamcrest.Matchers.containsString("No se pudo consultar IGDB")));
        }

        @Test
        @DisplayName("it: POST /api/igdb/games acepta query personalizada")
        void queryGames_aceptaQueryPersonalizada_controlaErrorExterno() throws Exception {
            when(igdbTokenService.getAccessToken())
                    .thenReturn("fake-token");

            mockMvc.perform(post("/api/igdb/games")
                            .contentType("text/plain")
                            .content("fields name; limit 5;"))
                    .andExpect(status().isInternalServerError())
                    .andExpect(content().string(org.hamcrest.Matchers.containsString("Servicio temporalmente no disponible")));
        }

        @Test
        @DisplayName("it: GET /api/igdb/games/search acepta parámetro q")
        void searchGames_aceptaParametroQ_controlaErrorExterno() throws Exception {
            when(igdbTokenService.getAccessToken())
                    .thenReturn("fake-token");

            mockMvc.perform(get("/api/igdb/games/search")
                            .param("q", "zelda"))
                    .andExpect(status().isInternalServerError())
                    .andExpect(content().string(org.hamcrest.Matchers.containsString("No se pudo consultar IGDB")));
        }
    }
}