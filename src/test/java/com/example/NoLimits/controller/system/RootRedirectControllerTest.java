package com.example.NoLimits.controller.system;

import com.example.NoLimits.Multimedia.controller.system.RootRedirectController;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RootRedirectController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("RootRedirectControllerTest — Redirección de raíz a Swagger")
class RootRedirectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("GET / → redirige a Swagger UI (302)")
    void root_redirigueASwagger() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/doc/swagger-ui.html"));
    }

    @Test
    @DisplayName("GET / → no retorna 200 directo (es redirección)")
    void root_noRetorna200() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(result -> {
                    int status = result.getResponse().getStatus();
                    org.junit.jupiter.api.Assertions.assertNotEquals(200, status);
                });
    }

    @Test
    @DisplayName("GET / → URL de redirección apunta a swagger-ui.html")
    void root_urlRedireccionContieneSwagger() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(redirectedUrl("/doc/swagger-ui.html"));
    }
}