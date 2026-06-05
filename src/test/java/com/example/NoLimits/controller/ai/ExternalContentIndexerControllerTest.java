package com.example.NoLimits.controller.ai;

import com.example.NoLimits.Multimedia.controller.ai.ExternalContentIndexerController;
import com.example.NoLimits.Multimedia.service.ai.ExternalContentIndexerService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ExternalContentIndexerController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("ExternalContentIndexerControllerTest — Endpoints de indexación externa")
class ExternalContentIndexerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ExternalContentIndexerService indexerService;

    @Test
    @DisplayName("POST /indexar-externas → 200 OK con mapa de resultados")
    void indexarExternas_retorna200() throws Exception {
        when(indexerService.indexarTodo())
                .thenReturn(Map.of("peliculas", 10, "series", 5, "juegos", 20));

        mockMvc.perform(post("/api/embeddings/indexar-externas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.peliculas").value(10))
                .andExpect(jsonPath("$.series").value(5));
    }

    @Test
    @DisplayName("POST /indexar-tmdb-peliculas → 200 OK con mensaje de películas")
    void indexarPeliculas_retorna200() throws Exception {
        when(indexerService.indexarPeliculasTMDB()).thenReturn(42);

        mockMvc.perform(post("/api/embeddings/indexar-tmdb-peliculas"))
                .andExpect(status().isOk())
                .andExpect(content().string("Películas TMDB indexadas: 42"));
    }

    @Test
    @DisplayName("POST /indexar-tmdb-series → 200 OK con mensaje de series")
    void indexarSeries_retorna200() throws Exception {
        when(indexerService.indexarSeriesTMDB()).thenReturn(15);

        mockMvc.perform(post("/api/embeddings/indexar-tmdb-series"))
                .andExpect(status().isOk())
                .andExpect(content().string("Series TMDB indexadas: 15"));
    }

    @Test
    @DisplayName("POST /indexar-rawg → 200 OK con mensaje de juegos RAWG")
    void indexarRawg_retorna200() throws Exception {
        when(indexerService.indexarJuegosRAWG()).thenReturn(30);

        mockMvc.perform(post("/api/embeddings/indexar-rawg"))
                .andExpect(status().isOk())
                .andExpect(content().string("Juegos RAWG indexados: 30"));
    }

    @Test
    @DisplayName("POST /indexar-igdb → 200 OK con mensaje de juegos IGDB")
    void indexarIgdb_retorna200() throws Exception {
        when(indexerService.indexarJuegosIGDB()).thenReturn(25);

        mockMvc.perform(post("/api/embeddings/indexar-igdb"))
                .andExpect(status().isOk())
                .andExpect(content().string("Juegos IGDB indexados: 25"));
    }

    @Test
    @DisplayName("POST /indexar-jikan → 200 OK con mensaje de animes")
    void indexarJikan_retorna200() throws Exception {
        when(indexerService.indexarAnimeJikan()).thenReturn(50);

        mockMvc.perform(post("/api/embeddings/indexar-jikan"))
                .andExpect(status().isOk())
                .andExpect(content().string("Animes Jikan indexados: 50"));
    }

    @Test
    @DisplayName("POST /indexar-libros → 200 OK con mensaje de libros")
    void indexarLibros_retorna200() throws Exception {
        when(indexerService.indexarLibrosOpenLibrary()).thenReturn(100);

        mockMvc.perform(post("/api/embeddings/indexar-libros"))
                .andExpect(status().isOk())
                .andExpect(content().string("Libros OpenLibrary indexados: 100"));
    }

    @Test
    @DisplayName("POST /indexar-libro?q=java → 200 OK con mensaje de búsqueda")
    void indexarLibroBusqueda_retorna200() throws Exception {
        when(indexerService.indexarLibrosBusqueda("java")).thenReturn(8);

        mockMvc.perform(post("/api/embeddings/indexar-libro")
                        .param("q", "java"))
                .andExpect(status().isOk())
                .andExpect(content().string("Libros indexados para 'java': 8"));
    }
}