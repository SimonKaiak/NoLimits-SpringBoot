package com.example.NoLimits.controller.books;

import com.example.NoLimits.Multimedia.controller.GoogleBooks.GoogleBooksProxyController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(GoogleBooksProxyController.class)
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(properties = "google.books.key=test-books-key")
@DisplayName("GoogleBooksProxyControllerTest — Proxy hacia Google Books API")
class GoogleBooksProxyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("GET /api/books/volumes → endpoint existe y responde (no 404)")
    void proxy_volumes_endpointExiste() throws Exception {
        mockMvc.perform(get("/api/books/volumes"))
                .andExpect(result -> {
                    int status = result.getResponse().getStatus();
                    org.junit.jupiter.api.Assertions.assertNotEquals(404, status,
                        "El endpoint /api/books/volumes no debe retornar 404");
                });
    }

    @Test
    @DisplayName("GET /api/books/volumes?q=java → acepta query string (no 404)")
    void proxy_volumesConBusqueda_aceptaQueryString() throws Exception {
        mockMvc.perform(get("/api/books/volumes").queryParam("q", "java"))
                .andExpect(result -> {
                    int status = result.getResponse().getStatus();
                    org.junit.jupiter.api.Assertions.assertNotEquals(404, status,
                        "El endpoint debe aceptar parámetros de búsqueda");
                });
    }

    @Test
    @DisplayName("GET /api/books/mylib → wildcard cubre cualquier path bajo /api/books/")
    void proxy_otroPath_wildcardCubre() throws Exception {
        mockMvc.perform(get("/api/books/mylib"))
                .andExpect(result -> {
                    int status = result.getResponse().getStatus();
                    org.junit.jupiter.api.Assertions.assertNotEquals(404, status,
                        "El wildcard /** debe manejar cualquier path bajo /api/books/");
                });
    }
}