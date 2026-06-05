package com.example.NoLimits.controller.translate;

import com.example.NoLimits.Multimedia.controller.translate.TranslateController;
import com.example.NoLimits.Multimedia.service.translate.TranslateService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TranslateController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("TranslateControllerTest — Endpoint de traducción al español")
class TranslateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TranslateService translateService;

    @Test
    @DisplayName("POST /api/translate → 200 OK con texto traducido")
    void translate_valido_retorna200() throws Exception {
        when(translateService.translateToSpanish("Hello World"))
                .thenReturn("Hola Mundo");

        mockMvc.perform(post("/api/translate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"text\":\"Hello World\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.translated").value("Hola Mundo"));
    }

    @Test
    @DisplayName("POST /api/translate → respuesta contiene el campo 'translated'")
    void translate_respuesta_tieneFieldTranslated() throws Exception {
        when(translateService.translateToSpanish(anyString()))
                .thenReturn("Texto traducido");

        mockMvc.perform(post("/api/translate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"text\":\"Some text\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.translated").exists());
    }

    @Test
    @DisplayName("POST /api/translate → traduce texto en otro idioma")
    void translate_otroIdioma_retorna200() throws Exception {
        when(translateService.translateToSpanish("Bonjour le monde"))
                .thenReturn("Hola mundo");

        mockMvc.perform(post("/api/translate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"text\":\"Bonjour le monde\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.translated").value("Hola mundo"));
    }

    @Test
    @DisplayName("POST /api/translate → texto ya en español se devuelve tal cual")
    void translate_textoEnEspanol_seDevuelveIgual() throws Exception {
        when(translateService.translateToSpanish("Hola"))
                .thenReturn("Hola");

        mockMvc.perform(post("/api/translate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"text\":\"Hola\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.translated").value("Hola"));
    }

    @Test
    @DisplayName("POST /api/translate → texto vacío retorna 200")
    void translate_textoVacio_retorna200() throws Exception {
        when(translateService.translateToSpanish(""))
                .thenReturn("");

        mockMvc.perform(post("/api/translate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"text\":\"\"}"))
                .andExpect(status().isOk());
    }
}