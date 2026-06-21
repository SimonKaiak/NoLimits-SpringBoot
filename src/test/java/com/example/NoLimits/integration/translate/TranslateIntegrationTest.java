package com.example.NoLimits.integration.translate;

import com.example.NoLimits.Multimedia.dto.translate.response.TranslateResponse;
import com.example.NoLimits.Multimedia.service.translate.TranslateService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@DisplayName("T-Integración · Translate")
class TranslateIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TranslateService translateService;

    @Nested
    @DisplayName("describe: integración Controller → Service")
    class IntegracionTranslate {

        @Test
        @DisplayName("it: traduce texto correctamente")
        void traducirTexto_retornaRespuestaTraducida() throws Exception {

            when(translateService.translateToSpanish("Hello world"))
                    .thenReturn("Hola mundo");

            String body = """
                    {
                      "text": "Hello world"
                    }
                    """;

            mockMvc.perform(post("/api/translate")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.translated")
                            .value("Hola mundo"));
        }

        @Test
        @DisplayName("it: acepta texto vacío")
        void traducirTextoVacio_retornaVacio() throws Exception {

            when(translateService.translateToSpanish(""))
                    .thenReturn("");

            String body = """
                    {
                      "text": ""
                    }
                    """;

            mockMvc.perform(post("/api/translate")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.translated")
                            .value(""));
        }

        @Test
        @DisplayName("it: acepta texto en otro idioma")
        void traducirTextoOtroIdioma_retornaTraduccion() throws Exception {

            when(translateService.translateToSpanish("Bonjour"))
                    .thenReturn("Hola");

            String body = """
                    {
                      "text": "Bonjour"
                    }
                    """;

            mockMvc.perform(post("/api/translate")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.translated")
                            .value("Hola"));
        }
    }
}