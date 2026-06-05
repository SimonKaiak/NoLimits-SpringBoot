package com.example.NoLimits.controller.system;

import com.example.NoLimits.Multimedia.controller.system.HealthController;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HealthController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("HealthControllerTest — Endpoint de salud del sistema")
class HealthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("GET /health → 200 OK con body 'OK'")
    void health_retorna200ConBodyOK() throws Exception {
        mockMvc.perform(get("/health"))
                .andExpect(status().isOk())
                .andExpect(content().string("OK"));
    }

    @Test
    @DisplayName("GET /health → Content-Type es text/plain")
    void health_retornaContentTypeCorrecto() throws Exception {
        mockMvc.perform(get("/health"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("text/plain"));
    }

    @Test
    @DisplayName("GET /health → no requiere autenticación")
    void health_esPublico_noRequiereToken() throws Exception {
        mockMvc.perform(get("/health"))
                .andExpect(status().isOk());
    }
}