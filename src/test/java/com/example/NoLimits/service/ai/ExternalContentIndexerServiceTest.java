package com.example.NoLimits.service.ai;

import com.example.NoLimits.Multimedia.service.ai.EmbeddingService;
import com.example.NoLimits.Multimedia.service.ai.ExternalContentIndexerService;
import com.example.NoLimits.Multimedia.service.igdb.IgdbTokenService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import org.springframework.http.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class ExternalContentIndexerServiceTest {

    private ExternalContentIndexerService crearService(RestTemplate restTemplateMock) throws Exception {
        JdbcTemplate jdbcTemplate = mock(JdbcTemplate.class);
        EmbeddingService embeddingService = mock(EmbeddingService.class);
        IgdbTokenService igdbTokenService = mock(IgdbTokenService.class);

        when(embeddingService.generarEmbedding(anyString()))
                .thenReturn(List.of(0.1f, 0.2f));

        when(igdbTokenService.getAccessToken())
                .thenReturn("token-igdb");

        ExternalContentIndexerService service =
                new ExternalContentIndexerService(jdbcTemplate, embeddingService, igdbTokenService);

        setPrivateField(service, "restTemplate", restTemplateMock);
        setPrivateField(service, "tmdbToken", "tmdb-token");
        setPrivateField(service, "igdbClientId", "igdb-client");
        setPrivateField(service, "rawgKey", "rawg-key");

        return service;
    }

    private void setPrivateField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    @Nested
    @DisplayName("Unitario - indexadores externos")
    class IndexadoresExternos {

        @Test
        @DisplayName("indexa películas TMDB y retorna cantidad procesada")
        void indexaPeliculasTMDB() throws Exception {
            RestTemplate restTemplateMock = mock(RestTemplate.class);
            ExternalContentIndexerService service = crearService(restTemplateMock);

            Map<String, Object> pelicula = Map.of(
                    "title", "Inception",
                    "overview", "Película de ciencia ficción",
                    "vote_average", 8.8,
                    "popularity", 100,
                    "release_date", "2010-07-16"
            );

            Map<String, Object> bodyConDatos = Map.of("results", List.of(pelicula));

            when(restTemplateMock.getForEntity(anyString(), eq(Map.class)))
                    .thenReturn(ResponseEntity.ok(bodyConDatos))
                    .thenReturn(new ResponseEntity<>(null, HttpStatus.OK))
                    .thenReturn(new ResponseEntity<>(null, HttpStatus.OK))
                    .thenReturn(new ResponseEntity<>(null, HttpStatus.OK))
                    .thenReturn(new ResponseEntity<>(null, HttpStatus.OK));

            int resultado = service.indexarPeliculasTMDB();

            assertEquals(1, resultado);
        }

        @Test
        @DisplayName("indexa series TMDB y retorna cantidad procesada")
        void indexaSeriesTMDB() throws Exception {
            RestTemplate restTemplateMock = mock(RestTemplate.class);
            ExternalContentIndexerService service = crearService(restTemplateMock);

            Map<String, Object> serie = Map.of(
                    "name", "Breaking Bad",
                    "overview", "Serie de drama",
                    "vote_average", 9.5,
                    "popularity", 200,
                    "first_air_date", "2008-01-20"
            );

            Map<String, Object> bodyConDatos = Map.of("results", List.of(serie));

            when(restTemplateMock.getForEntity(anyString(), eq(Map.class)))
                    .thenReturn(ResponseEntity.ok(bodyConDatos))
                    .thenReturn(new ResponseEntity<>(null, HttpStatus.OK))
                    .thenReturn(new ResponseEntity<>(null, HttpStatus.OK));

            int resultado = service.indexarSeriesTMDB();

            assertEquals(1, resultado);
        }

        @Test
        @DisplayName("indexa juegos RAWG y retorna cantidad procesada")
        void indexaJuegosRAWG() throws Exception {
            RestTemplate restTemplateMock = mock(RestTemplate.class);
            ExternalContentIndexerService service = crearService(restTemplateMock);

            Map<String, Object> juego = Map.of(
                    "name", "Elden Ring",
                    "rating", 4.8,
                    "released", "2022-02-25",
                    "added", 5000,
                    "genres", List.of(Map.of("name", "RPG")),
                    "platforms", List.of(Map.of("platform", Map.of("name", "PC")))
            );

            Map<String, Object> bodyConDatos = Map.of("results", List.of(juego));

            when(restTemplateMock.getForEntity(anyString(), eq(Map.class)))
                    .thenReturn(ResponseEntity.ok(bodyConDatos))
                    .thenReturn(new ResponseEntity<>(null, HttpStatus.OK))
                    .thenReturn(new ResponseEntity<>(null, HttpStatus.OK))
                    .thenReturn(new ResponseEntity<>(null, HttpStatus.OK))
                    .thenReturn(new ResponseEntity<>(null, HttpStatus.OK));

            int resultado = service.indexarJuegosRAWG();

            assertEquals(1, resultado);
        }

        @Test
        @DisplayName("indexa juegos IGDB y retorna cantidad procesada")
        void indexaJuegosIGDB() throws Exception {
            RestTemplate restTemplateMock = mock(RestTemplate.class);
            ExternalContentIndexerService service = crearService(restTemplateMock);

            Map<String, Object> juego = Map.of(
                    "name", "God of War",
                    "summary", "Juego de acción",
                    "rating", 95,
                    "rating_count", 1000,
                    "genres", List.of(Map.of("name", "Acción")),
                    "platforms", List.of(Map.of("name", "PlayStation"))
            );

            when(restTemplateMock.exchange(
                    anyString(),
                    eq(HttpMethod.POST),
                    any(HttpEntity.class),
                    eq(List.class)
            )).thenReturn(ResponseEntity.ok(List.of(juego)));

            int resultado = service.indexarJuegosIGDB();

            assertEquals(1, resultado);
        }

        @Test
        @DisplayName("indexa libros por búsqueda y retorna cantidad procesada")
        void indexaLibrosBusqueda() throws Exception {
            RestTemplate restTemplateMock = mock(RestTemplate.class);
            ExternalContentIndexerService service = crearService(restTemplateMock);

            Map<String, Object> libro = Map.of(
                    "title", "Dune",
                    "author_name", List.of("Frank Herbert"),
                    "first_publish_year", 1965,
                    "subject", List.of("Science fiction", "Adventure")
            );

            Map<String, Object> bodyConDatos = Map.of("docs", List.of(libro));

            when(restTemplateMock.getForEntity(anyString(), eq(Map.class)))
                    .thenReturn(ResponseEntity.ok(bodyConDatos));

            int resultado = service.indexarLibrosBusqueda("dune");

            assertEquals(1, resultado);
        }

        @Test
        @DisplayName("indexarTodo retorna resumen total")
        void indexarTodoRetornaResumenTotal() throws Exception {
            RestTemplate restTemplateMock = mock(RestTemplate.class);
            ExternalContentIndexerService realService = crearService(restTemplateMock);

            ExternalContentIndexerService service = spy(realService);

            doReturn(1).when(service).indexarPeliculasTMDB();
            doReturn(2).when(service).indexarSeriesTMDB();
            doReturn(3).when(service).indexarJuegosRAWG();
            doReturn(4).when(service).indexarJuegosIGDB();
            doReturn(5).when(service).indexarAnimeJikan();
            doReturn(6).when(service).indexarLibrosOpenLibrary();

            Map<String, Integer> resultado = service.indexarTodo();

            assertEquals(1, resultado.get("peliculas_tmdb"));
            assertEquals(2, resultado.get("series_tmdb"));
            assertEquals(3, resultado.get("juegos_rawg"));
            assertEquals(4, resultado.get("juegos_igdb"));
            assertEquals(5, resultado.get("anime_jikan"));
            assertEquals(6, resultado.get("libros_openlibrary"));
            assertEquals(21, resultado.get("total"));
        }
    }
}