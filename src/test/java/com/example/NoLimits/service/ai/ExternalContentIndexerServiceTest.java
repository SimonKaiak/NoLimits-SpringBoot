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
import static org.junit.jupiter.api.Assertions.assertTrue;
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
        @DisplayName("retorna 0 cuando series TMDB devuelve body null")
        void indexarSeriesTMDB_BodyNull() throws Exception {

                RestTemplate restTemplateMock = mock(RestTemplate.class);
                ExternalContentIndexerService service = crearService(restTemplateMock);

                when(restTemplateMock.getForEntity(anyString(), eq(Map.class)))
                        .thenReturn(new ResponseEntity<>(null, HttpStatus.OK));

                int resultado = service.indexarSeriesTMDB();

                assertEquals(0, resultado);
        }

        @Test
        @DisplayName("retorna 0 cuando series TMDB no trae results")
        void indexarSeriesTMDB_ResultsNull() throws Exception {

                RestTemplate restTemplateMock = mock(RestTemplate.class);
                ExternalContentIndexerService service = crearService(restTemplateMock);

                Map<String, Object> body = Map.of(
                        "page", 1
                );

                when(restTemplateMock.getForEntity(anyString(), eq(Map.class)))
                        .thenReturn(ResponseEntity.ok(body));

                int resultado = service.indexarSeriesTMDB();

                assertEquals(0, resultado);
        }

        @Test
        @DisplayName("retorna 0 cuando TMDB devuelve body null")
        void indexarPeliculasTMDB_BodyNull() throws Exception {

                RestTemplate restTemplateMock = mock(RestTemplate.class);
                ExternalContentIndexerService service = crearService(restTemplateMock);

                when(restTemplateMock.getForEntity(anyString(), eq(Map.class)))
                        .thenReturn(new ResponseEntity<>(null, HttpStatus.OK));

                int resultado = service.indexarPeliculasTMDB();

                assertEquals(0, resultado);
        }

        @Test
        @DisplayName("retorna 0 cuando TMDB no trae results")
        void indexarPeliculasTMDB_ResultsNull() throws Exception {

                RestTemplate restTemplateMock = mock(RestTemplate.class);
                ExternalContentIndexerService service = crearService(restTemplateMock);

                Map<String, Object> body = Map.of(
                        "page", 1
                );

                when(restTemplateMock.getForEntity(anyString(), eq(Map.class)))
                        .thenReturn(ResponseEntity.ok(body));

                int resultado = service.indexarPeliculasTMDB();

                assertEquals(0, resultado);
        }

        @Test
        @DisplayName("continua procesando cuando una película genera excepción")
        void indexarPeliculasTMDB_ItemConError() throws Exception {

                // Arrange
                RestTemplate restTemplateMock = mock(RestTemplate.class);
                ExternalContentIndexerService service = crearService(restTemplateMock);

                Map<String, Object> peliculaMala = Map.of(
                        "title", List.of("error")
                );

                Map<String, Object> peliculaBuena = Map.of(
                        "title", "Inception",
                        "overview", "Película de ciencia ficción",
                        "vote_average", 8.8,
                        "popularity", 100,
                        "release_date", "2010-07-16"
                );

                Map<String, Object> body = Map.of(
                        "results", List.of(peliculaMala, peliculaBuena)
                );

                when(restTemplateMock.getForEntity(anyString(), eq(Map.class)))
                        .thenReturn(ResponseEntity.ok(body))
                        .thenReturn(new ResponseEntity<>(null, HttpStatus.OK))
                        .thenReturn(new ResponseEntity<>(null, HttpStatus.OK))
                        .thenReturn(new ResponseEntity<>(null, HttpStatus.OK))
                        .thenReturn(new ResponseEntity<>(null, HttpStatus.OK));

                // Act
                int resultado = service.indexarPeliculasTMDB();

                // Assert
                assertEquals(1, resultado);
        }

        @Test
        @DisplayName("continua procesando cuando una serie genera excepción")
        void indexarSeriesTMDB_ItemConError() throws Exception {

                // Arrange
                RestTemplate restTemplateMock = mock(RestTemplate.class);
                ExternalContentIndexerService service = crearService(restTemplateMock);

                Map<String, Object> serieMala = Map.of(
                        "name", List.of("error")
                );

                Map<String, Object> serieBuena = Map.of(
                        "name", "Breaking Bad",
                        "overview", "Serie de drama",
                        "vote_average", 9.5,
                        "popularity", 200,
                        "first_air_date", "2008-01-20"
                );

                Map<String, Object> body = Map.of(
                        "results", List.of(serieMala, serieBuena)
                );

                when(restTemplateMock.getForEntity(anyString(), eq(Map.class)))
                        .thenReturn(ResponseEntity.ok(body))
                        .thenReturn(new ResponseEntity<>(null, HttpStatus.OK))
                        .thenReturn(new ResponseEntity<>(null, HttpStatus.OK));

                // Act
                int resultado = service.indexarSeriesTMDB();

                // Assert
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
        @DisplayName("retorna 0 cuando RAWG devuelve body null")
        void indexarJuegosRAWG_BodyNull() throws Exception {

                RestTemplate restTemplateMock = mock(RestTemplate.class);
                ExternalContentIndexerService service = crearService(restTemplateMock);

                when(restTemplateMock.getForEntity(anyString(), eq(Map.class)))
                        .thenReturn(new ResponseEntity<>(null, HttpStatus.OK));

                int resultado = service.indexarJuegosRAWG();

                assertEquals(0, resultado);
        }

        @Test
        @DisplayName("retorna 0 cuando RAWG no trae results")
        void indexarJuegosRAWG_ResultsNull() throws Exception {

        RestTemplate restTemplateMock = mock(RestTemplate.class);
        ExternalContentIndexerService service = crearService(restTemplateMock);

                Map<String, Object> body = Map.of(
                        "page", 1
                );

                when(restTemplateMock.getForEntity(anyString(), eq(Map.class)))
                        .thenReturn(ResponseEntity.ok(body));

                int resultado = service.indexarJuegosRAWG();

                assertEquals(0, resultado);
        }

        @Test
        @DisplayName("indexa juego RAWG con género sin nombre")
        void indexarJuegosRAWG_GeneroSinNombre() throws Exception {

                // Arrange
                RestTemplate restTemplateMock = mock(RestTemplate.class);
                ExternalContentIndexerService service = crearService(restTemplateMock);

                Map<String, Object> juego = Map.of(
                        "name", "Juego",
                        "rating", 4.5,
                        "released", "2024",
                        "added", 100,
                        "genres", List.of(
                                Map.of("otroCampo", "RPG")
                        ),
                        "platforms", List.of(
                                Map.of("platform", Map.of("name", "PC"))
                        )
                );

                Map<String, Object> body = Map.of(
                        "results", List.of(juego)
                );

                when(restTemplateMock.getForEntity(anyString(), eq(Map.class)))
                        .thenReturn(ResponseEntity.ok(body))
                        .thenReturn(new ResponseEntity<>(null, HttpStatus.OK))
                        .thenReturn(new ResponseEntity<>(null, HttpStatus.OK))
                        .thenReturn(new ResponseEntity<>(null, HttpStatus.OK))
                        .thenReturn(new ResponseEntity<>(null, HttpStatus.OK));

                // Act
                int resultado = service.indexarJuegosRAWG();

                // Assert
                assertEquals(1, resultado);
        }

        @Test
        @DisplayName("continua procesando cuando un juego genera excepción")
        void indexarJuegosRAWGItemConError() throws Exception {

                RestTemplate restTemplateMock = mock(RestTemplate.class);
                ExternalContentIndexerService service = crearService(restTemplateMock);

                Map<String, Object> juegoMalo = Map.of(
                        "name", "Juego roto",
                        "genres", "esto no es una lista"
                );

                Map<String, Object> juegoBueno = Map.of(
                        "name", "Elden Ring",
                        "rating", 4.8,
                        "released", "2022-02-25",
                        "added", 5000,
                        "genres", List.of(Map.of("name", "RPG")),
                        "platforms", List.of(Map.of("platform", Map.of("name", "PC")))
                );

                Map<String, Object> body = Map.of(
                        "results", List.of(juegoMalo, juegoBueno)
                );

                when(restTemplateMock.getForEntity(anyString(), eq(Map.class)))
                        .thenReturn(ResponseEntity.ok(body))
                        .thenReturn(new ResponseEntity<>(null, HttpStatus.OK))
                        .thenReturn(new ResponseEntity<>(null, HttpStatus.OK))
                        .thenReturn(new ResponseEntity<>(null, HttpStatus.OK))
                        .thenReturn(new ResponseEntity<>(null, HttpStatus.OK));

                int resultado = service.indexarJuegosRAWG();

                assertEquals(1, resultado);
        }

        @Test
        @DisplayName("indexa juego RAWG con género vacío")
        void indexarJuegosRAWG_GeneroVacio() throws Exception {

                // Arrange
                RestTemplate restTemplateMock = mock(RestTemplate.class);
                ExternalContentIndexerService service = crearService(restTemplateMock);

                Map<String, Object> juego = Map.of(
                        "name", "Juego",
                        "rating", 4.5,
                        "released", "2024",
                        "added", 100,
                        "genres", List.of(
                                Map.of("name", "")
                        ),
                        "platforms", List.of(
                                Map.of("platform", Map.of("name", "PC"))
                        )
                );

                Map<String, Object> body = Map.of(
                        "results", List.of(juego)
                );

                when(restTemplateMock.getForEntity(anyString(), eq(Map.class)))
                        .thenReturn(ResponseEntity.ok(body))
                        .thenReturn(new ResponseEntity<>(null, HttpStatus.OK))
                        .thenReturn(new ResponseEntity<>(null, HttpStatus.OK))
                        .thenReturn(new ResponseEntity<>(null, HttpStatus.OK))
                        .thenReturn(new ResponseEntity<>(null, HttpStatus.OK));

                // Act
                int resultado = service.indexarJuegosRAWG();

                // Assert
                assertEquals(1, resultado);
        }

        @Test
        @DisplayName("indexa juego RAWG con plataforma vacía")
        void indexarJuegosRAWG_PlataformaVacia() throws Exception {

                // Arrange
                RestTemplate restTemplateMock = mock(RestTemplate.class);
                ExternalContentIndexerService service = crearService(restTemplateMock);

                Map<String, Object> juego = Map.of(
                        "name", "Juego",
                        "rating", 4.5,
                        "released", "2024",
                        "added", 100,
                        "genres", List.of(
                                Map.of("name", "RPG")
                        ),
                        "platforms", List.of(
                                Map.of("platform", Map.of("name", ""))
                        )
                );

                Map<String, Object> body = Map.of(
                        "results", List.of(juego)
                );

                when(restTemplateMock.getForEntity(anyString(), eq(Map.class)))
                        .thenReturn(ResponseEntity.ok(body))
                        .thenReturn(new ResponseEntity<>(null, HttpStatus.OK))
                        .thenReturn(new ResponseEntity<>(null, HttpStatus.OK))
                        .thenReturn(new ResponseEntity<>(null, HttpStatus.OK))
                        .thenReturn(new ResponseEntity<>(null, HttpStatus.OK));

                // Act
                int resultado = service.indexarJuegosRAWG();

                // Assert
                assertEquals(1, resultado);
        }

        @Test
        @DisplayName("indexa juego RAWG con múltiples géneros y plataformas")
        void indexarJuegosRAWG_MultiplesValores() throws Exception {

                // Arrange
                RestTemplate restTemplateMock = mock(RestTemplate.class);
                ExternalContentIndexerService service = crearService(restTemplateMock);

                Map<String, Object> juego = Map.of(
                        "name", "Elden Ring",
                        "rating", 4.8,
                        "released", "2022-02-25",
                        "added", 5000,
                        "genres", List.of(
                                Map.of("name", "RPG"),
                                Map.of("name", "Action")
                        ),
                        "platforms", List.of(
                                Map.of("platform", Map.of("name", "PC")),
                                Map.of("platform", Map.of("name", "Xbox"))
                        )
                );

                Map<String, Object> body = Map.of(
                        "results", List.of(juego)
                );

                when(restTemplateMock.getForEntity(anyString(), eq(Map.class)))
                        .thenReturn(ResponseEntity.ok(body))
                        .thenReturn(new ResponseEntity<>(null, HttpStatus.OK))
                        .thenReturn(new ResponseEntity<>(null, HttpStatus.OK))
                        .thenReturn(new ResponseEntity<>(null, HttpStatus.OK))
                        .thenReturn(new ResponseEntity<>(null, HttpStatus.OK));

                // Act
                int resultado = service.indexarJuegosRAWG();

                // Assert
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
        @DisplayName("retorna 0 cuando IGDB devuelve body null")
        void indexarJuegosIGDB_BodyNull() throws Exception {

                // Arrange
                RestTemplate restTemplateMock = mock(RestTemplate.class);
                ExternalContentIndexerService service = crearService(restTemplateMock);

                when(restTemplateMock.exchange(
                        anyString(),
                        eq(HttpMethod.POST),
                        any(HttpEntity.class),
                        eq(List.class)
                )).thenReturn(new ResponseEntity<>(null, HttpStatus.OK));

                // Act
                int resultado = service.indexarJuegosIGDB();

                // Assert
                assertEquals(0, resultado);
        }

        @Test
        @DisplayName("indexa juego IGDB con datos vacíos")
        void indexarJuegosIGDB_DatosVacios() throws Exception {

                // Arrange
                RestTemplate restTemplateMock = mock(RestTemplate.class);
                ExternalContentIndexerService service = crearService(restTemplateMock);

                Map<String, Object> juego = Map.of(
                        "name", "God of War",
                        "summary", "Juego",
                        "rating", 95,
                        "rating_count", 1000,
                        "genres", List.of(
                                Map.of("name", "")
                        ),
                        "platforms", List.of(
                                Map.of("name", "")
                        )
                );

                when(restTemplateMock.exchange(
                        anyString(),
                        eq(HttpMethod.POST),
                        any(HttpEntity.class),
                        eq(List.class)
                )).thenReturn(ResponseEntity.ok(List.of(juego)));

                // Act
                int resultado = service.indexarJuegosIGDB();

                // Assert
                assertEquals(1, resultado);
        }

        @Test
        @DisplayName("indexa juego IGDB con múltiples géneros y plataformas")
        void indexarJuegosIGDB_MultiplesValores() throws Exception {

                // Arrange
                RestTemplate restTemplateMock = mock(RestTemplate.class);
                ExternalContentIndexerService service = crearService(restTemplateMock);

                Map<String, Object> juego = Map.of(
                        "name", "God of War",
                        "summary", "Juego de acción",
                        "rating", 95,
                        "rating_count", 1000,
                        "genres", List.of(
                                Map.of("name", "Action"),
                                Map.of("name", "Adventure")
                        ),
                        "platforms", List.of(
                                Map.of("name", "PS5"),
                                Map.of("name", "PC")
                        )
                );

                when(restTemplateMock.exchange(
                        anyString(),
                        eq(HttpMethod.POST),
                        any(HttpEntity.class),
                        eq(List.class)
                )).thenReturn(ResponseEntity.ok(List.of(juego)));

                // Act
                int resultado = service.indexarJuegosIGDB();

                // Assert
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
        @DisplayName("indexa libros OpenLibrary")
        void indexaLibrosOpenLibrary() throws Exception {

                RestTemplate restTemplateMock = mock(RestTemplate.class);
                ExternalContentIndexerService service = crearService(restTemplateMock);

                Map<String, Object> libro = Map.of(
                        "title", "Harry Potter",
                        "authors", List.of(Map.of("name", "J. K. Rowling")),
                        "first_publish_year", 1997
                );

                Map<String, Object> body = Map.of(
                        "works", List.of(libro)
                );

                when(restTemplateMock.getForEntity(anyString(), eq(Map.class)))
                        .thenReturn(ResponseEntity.ok(body));

                int resultado = service.indexarLibrosOpenLibrary();

                assertEquals(resultado > 0, true);
        }

        @Test
        @DisplayName("retorna 0 cuando OpenLibrary búsqueda devuelve body null")
        void indexarLibrosBusqueda_BodyNull() throws Exception {

                // Arrange
                RestTemplate restTemplateMock = mock(RestTemplate.class);
                ExternalContentIndexerService service = crearService(restTemplateMock);

                when(restTemplateMock.getForEntity(anyString(), eq(Map.class)))
                        .thenReturn(new ResponseEntity<>(null, HttpStatus.OK));

                // Act
                int resultado = service.indexarLibrosBusqueda("dune");

                // Assert
                assertEquals(0, resultado);
        }

        @Test
        @DisplayName("retorna 0 cuando OpenLibrary búsqueda no trae docs")
        void indexarLibrosBusqueda_DocsNull() throws Exception {

                // Arrange
                RestTemplate restTemplateMock = mock(RestTemplate.class);
                ExternalContentIndexerService service = crearService(restTemplateMock);

                Map<String, Object> body = Map.of(
                        "numFound", 0
                );

                when(restTemplateMock.getForEntity(anyString(), eq(Map.class)))
                        .thenReturn(ResponseEntity.ok(body));

                // Act
                int resultado = service.indexarLibrosBusqueda("dune");

                // Assert
                assertEquals(0, resultado);
        }

        @Test
        @DisplayName("continua cuando OpenLibrary no trae works")
        void indexarLibrosOpenLibrary_WorksNull() throws Exception {

                // Arrange
                RestTemplate restTemplateMock = mock(RestTemplate.class);
                ExternalContentIndexerService service = crearService(restTemplateMock);

                Map<String, Object> body = Map.of(
                        "name", "fantasy"
                );

                when(restTemplateMock.getForEntity(anyString(), eq(Map.class)))
                        .thenReturn(ResponseEntity.ok(body));

                // Act
                int resultado = service.indexarLibrosOpenLibrary();

                // Assert
                assertEquals(0, resultado);
        }

        @Test
        @DisplayName("indexa libro OpenLibrary sin autores")
        void indexaLibrosOpenLibrary_SinAutores() throws Exception {

                // Arrange
                RestTemplate restTemplateMock = mock(RestTemplate.class);
                ExternalContentIndexerService service = crearService(restTemplateMock);

                Map<String, Object> libro = Map.of(
                        "title", "Libro Sin Autor",
                        "authors", List.of(),
                        "first_publish_year", 2020
                );

                Map<String, Object> body = Map.of(
                        "works", List.of(libro)
                );

                when(restTemplateMock.getForEntity(anyString(), eq(Map.class)))
                        .thenReturn(ResponseEntity.ok(body));

                // Act
                int resultado = service.indexarLibrosOpenLibrary();

                // Assert
                assertTrue(resultado > 0);
        }

        @Test
        @DisplayName("indexa libro OpenLibrary con autor vacío")
        void indexaLibrosOpenLibrary_AutorVacio() throws Exception {

                // Arrange
                RestTemplate restTemplateMock = mock(RestTemplate.class);
                ExternalContentIndexerService service = crearService(restTemplateMock);

                Map<String, Object> libro = Map.of(
                        "title", "Libro",
                        "authors", List.of(
                                Map.of("name", "")
                        ),
                        "first_publish_year", 2020
                );

                Map<String, Object> body = Map.of(
                         "works", List.of(libro)
                );

                when(restTemplateMock.getForEntity(anyString(), eq(Map.class)))
                        .thenReturn(ResponseEntity.ok(body));

                // Act
                int resultado = service.indexarLibrosOpenLibrary();

                // Assert
                assertEquals(7, resultado);
        }

        @Test
        @DisplayName("indexa libro búsqueda sin autores")
        void indexarLibrosBusqueda_SinAutores() throws Exception {

                // Arrange
                RestTemplate restTemplateMock = mock(RestTemplate.class);
                ExternalContentIndexerService service = crearService(restTemplateMock);

                Map<String, Object> libro = Map.of(
                        "title", "Dune",
                        "author_name", List.of(),
                        "first_publish_year", 1965,
                        "subject", List.of("Science fiction")
                );

                Map<String, Object> body = Map.of(
                        "docs", List.of(libro)
                );

                when(restTemplateMock.getForEntity(anyString(), eq(Map.class)))
                        .thenReturn(ResponseEntity.ok(body));

                // Act
                int resultado = service.indexarLibrosBusqueda("dune");

                // Assert
                assertEquals(1, resultado);
        }

        @Test
        @DisplayName("continua procesando cuando un libro OpenLibrary genera excepción")
        void indexarLibrosOpenLibrary_ItemConError() throws Exception {

                // Arrange
                RestTemplate restTemplateMock = mock(RestTemplate.class);
                ExternalContentIndexerService service = crearService(restTemplateMock);

                Map<String, Object> libroMalo = Map.of(
                        "authors", "error"
                );

                Map<String, Object> libroBueno = Map.of(
                        "title", "Harry Potter",
                        "authors", List.of(Map.of("name", "J. K. Rowling")),
                        "first_publish_year", 1997
                );

                Map<String, Object> body = Map.of(
                        "works", List.of(libroMalo, libroBueno)
                );

                when(restTemplateMock.getForEntity(anyString(), eq(Map.class)))
                        .thenReturn(ResponseEntity.ok(body));

                // Act
                int resultado = service.indexarLibrosOpenLibrary();

                // Assert
                assertTrue(resultado > 0);
        }

        @Test
        @DisplayName("continua procesando cuando un libro de búsqueda genera excepción")
        void indexarLibrosBusqueda_ItemConError() throws Exception {

                // Arrange
                RestTemplate restTemplateMock = mock(RestTemplate.class);
                ExternalContentIndexerService service = crearService(restTemplateMock);

                Map<String, Object> libroMalo = Map.of(
                        "author_name", "error"
                );

                Map<String, Object> libroBueno = Map.of(
                        "title", "Dune",
                        "author_name", List.of("Frank Herbert"),
                        "first_publish_year", 1965,
                        "subject", List.of("Science fiction", "Adventure")
                );

                Map<String, Object> body = Map.of(
                        "docs", List.of(libroMalo, libroBueno)
                );

                when(restTemplateMock.getForEntity(anyString(), eq(Map.class)))
                        .thenReturn(ResponseEntity.ok(body));

                // Act
                int resultado = service.indexarLibrosBusqueda("dune");

                // Assert
                assertEquals(1, resultado);
        }

        @Test
        @DisplayName("indexa libro OpenLibrary con múltiples autores")
        void indexarLibrosOpenLibrary_MultiplesAutores() throws Exception {

                // Arrange
                RestTemplate restTemplateMock = mock(RestTemplate.class);
                ExternalContentIndexerService service = crearService(restTemplateMock);

                Map<String, Object> libro = Map.of(
                        "title", "Harry Potter",
                        "authors", List.of(
                                Map.of("name", "Autor 1"),
                                Map.of("name", "Autor 2")
                        ),
                        "first_publish_year", 1997
                );

                Map<String, Object> body = Map.of(
                        "works", List.of(libro)
                );

                when(restTemplateMock.getForEntity(anyString(), eq(Map.class)))
                        .thenReturn(ResponseEntity.ok(body));

                // Act
                int resultado = service.indexarLibrosOpenLibrary();

                // Assert
                assertTrue(resultado > 0);
        }

        @Test
        @DisplayName("indexa libro búsqueda con múltiples autores")
        void indexarLibrosBusqueda_MultiplesAutores() throws Exception {

                // Arrange
                RestTemplate restTemplateMock = mock(RestTemplate.class);
                ExternalContentIndexerService service = crearService(restTemplateMock);

                Map<String, Object> libro = Map.of(
                        "title", "Dune",
                        "author_name", List.of(
                                "Frank Herbert",
                                "Brian Herbert"
                        ),
                        "first_publish_year", 1965,
                        "subject", List.of(
                                "Science fiction",
                                "Adventure",
                                "Fantasy"
                        )
                );

                Map<String, Object> body = Map.of(
                        "docs", List.of(libro)
                );

                when(restTemplateMock.getForEntity(anyString(), eq(Map.class)))
                        .thenReturn(ResponseEntity.ok(body));

                // Act
                int resultado = service.indexarLibrosBusqueda("dune");

                // Assert
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

        @Test
        @DisplayName("continua procesando cuando un juego genera excepción")
        void indexarJuegosRAWG_ItemConError() throws Exception {

                RestTemplate restTemplateMock = mock(RestTemplate.class);
                ExternalContentIndexerService service = crearService(restTemplateMock);

                Map<String, Object> juegoMalo = Map.of(
                        "name", "Roto",
                        "genres", "error"
                );

                Map<String, Object> juegoBueno = Map.of(
                        "name", "Elden Ring",
                        "rating", 4.8,
                        "released", "2022-02-25",
                        "added", 5000,
                        "genres", List.of(Map.of("name", "RPG")),
                        "platforms", List.of(Map.of("platform", Map.of("name", "PC")))
                );

                Map<String, Object> body = Map.of(
                        "results", List.of(juegoMalo, juegoBueno)
                );

                when(restTemplateMock.getForEntity(anyString(), eq(Map.class)))
                        .thenReturn(ResponseEntity.ok(body))
                        .thenReturn(new ResponseEntity<>(null, HttpStatus.OK))
                        .thenReturn(new ResponseEntity<>(null, HttpStatus.OK))
                        .thenReturn(new ResponseEntity<>(null, HttpStatus.OK))
                        .thenReturn(new ResponseEntity<>(null, HttpStatus.OK));

                int resultado = service.indexarJuegosRAWG();

                assertEquals(1, resultado);
        }

        @Test
        @DisplayName("indexa anime Jikan y retorna cantidad procesada")
        void indexaAnimeJikan() throws Exception {

                RestTemplate restTemplateMock = mock(RestTemplate.class);
                ExternalContentIndexerService service = crearService(restTemplateMock);

                Map<String, Object> anime = Map.of(
                        "title", "Naruto",
                        "title_english", "Naruto",
                        "synopsis", "Anime ninja",
                        "score", 8.5,
                        "members", 1000000,
                        "year", 2002,
                        "genres", List.of(Map.of("name", "Shounen"))
                );

                Map<String, Object> body = Map.of(
                        "data", List.of(anime)
                );

                when(restTemplateMock.getForEntity(anyString(), eq(Map.class)))
                        .thenReturn(ResponseEntity.ok(body))
                        .thenReturn(new ResponseEntity<>(null, HttpStatus.OK))
                        .thenReturn(new ResponseEntity<>(null, HttpStatus.OK))
                        .thenReturn(new ResponseEntity<>(null, HttpStatus.OK))
                        .thenReturn(new ResponseEntity<>(null, HttpStatus.OK));

                int resultado = service.indexarAnimeJikan();

                assertEquals(1, resultado);
        }

        @Test
        @DisplayName("retorna 0 cuando Jikan devuelve body null")
        void indexarAnimeJikan_BodyNull() throws Exception {

                // Arrange
                RestTemplate restTemplateMock = mock(RestTemplate.class);
                ExternalContentIndexerService service = crearService(restTemplateMock);

                when(restTemplateMock.getForEntity(anyString(), eq(Map.class)))
                        .thenReturn(new ResponseEntity<>(null, HttpStatus.OK));

                // Act
                int resultado = service.indexarAnimeJikan();

                // Assert
                assertEquals(0, resultado);
        }

        @Test
        @DisplayName("retorna 0 cuando Jikan no trae data")
        void indexarAnimeJikan_DataNull() throws Exception {

                // Arrange
                RestTemplate restTemplateMock = mock(RestTemplate.class);
                ExternalContentIndexerService service = crearService(restTemplateMock);

                Map<String, Object> body = Map.of(
                        "page", 1
                );

                when(restTemplateMock.getForEntity(anyString(), eq(Map.class)))
                        .thenReturn(ResponseEntity.ok(body));

                // Act
                int resultado = service.indexarAnimeJikan();

                // Assert
                assertEquals(0, resultado);
        }

        @Test
        @DisplayName("indexa anime con género vacío")
        void indexarAnimeJikan_GeneroVacio() throws Exception {

                // Arrange
                RestTemplate restTemplateMock = mock(RestTemplate.class);
                ExternalContentIndexerService service = crearService(restTemplateMock);

                Map<String, Object> anime = Map.of(
                        "title", "Naruto",
                        "title_english", "Naruto",
                        "synopsis", "Anime",
                        "score", 8.5,
                        "members", 1000,
                        "year", 2002,
                        "genres", List.of(
                                Map.of("name", "")
                        )
                );

                Map<String, Object> body = Map.of(
                        "data", List.of(anime)
                );

                when(restTemplateMock.getForEntity(anyString(), eq(Map.class)))
                        .thenReturn(ResponseEntity.ok(body))
                        .thenReturn(new ResponseEntity<>(null, HttpStatus.OK))
                        .thenReturn(new ResponseEntity<>(null, HttpStatus.OK))
                        .thenReturn(new ResponseEntity<>(null, HttpStatus.OK))
                        .thenReturn(new ResponseEntity<>(null, HttpStatus.OK));

                // Act
                int resultado = service.indexarAnimeJikan();

                // Assert
                assertEquals(1, resultado);
        }

        @Test
        @DisplayName("continua procesando cuando un anime genera excepción")
        void indexarAnimeJikan_ItemConError() throws Exception {

                // Arrange
                RestTemplate restTemplateMock = mock(RestTemplate.class);
                ExternalContentIndexerService service = crearService(restTemplateMock);

                Map<String, Object> animeMalo = Map.of(
                        "genres", "error"
                );

                Map<String, Object> animeBueno = Map.of(
                        "title", "Naruto",
                        "title_english", "Naruto",
                        "synopsis", "Anime ninja",
                        "score", 8.5,
                        "members", 1000000,
                        "year", 2002,
                        "genres", List.of(Map.of("name", "Shounen"))
                );

                Map<String, Object> body = Map.of(
                        "data", List.of(animeMalo, animeBueno)
                );

                when(restTemplateMock.getForEntity(anyString(), eq(Map.class)))
                        .thenReturn(ResponseEntity.ok(body))
                        .thenReturn(new ResponseEntity<>(null, HttpStatus.OK))
                        .thenReturn(new ResponseEntity<>(null, HttpStatus.OK))
                        .thenReturn(new ResponseEntity<>(null, HttpStatus.OK))
                        .thenReturn(new ResponseEntity<>(null, HttpStatus.OK));

                // Act
                int resultado = service.indexarAnimeJikan();

                // Assert
                assertEquals(1, resultado);
        }

        @Test
        @DisplayName("indexa anime con múltiples géneros")
        void indexarAnimeJikan_MultiplesGeneros() throws Exception {

                // Arrange
                RestTemplate restTemplateMock = mock(RestTemplate.class);
                ExternalContentIndexerService service = crearService(restTemplateMock);

                Map<String, Object> anime = Map.of(
                        "title", "Naruto",
                        "title_english", "Naruto",
                        "synopsis", "Anime ninja",
                        "score", 8.5,
                        "members", 1000000,
                        "year", 2002,
                        "genres", List.of(
                                Map.of("name", "Shounen"),
                                Map.of("name", "Adventure")
                        )
                );

                Map<String, Object> body = Map.of(
                        "data", List.of(anime)
                );

                when(restTemplateMock.getForEntity(anyString(), eq(Map.class)))
                        .thenReturn(ResponseEntity.ok(body))
                        .thenReturn(new ResponseEntity<>(null, HttpStatus.OK))
                        .thenReturn(new ResponseEntity<>(null, HttpStatus.OK))
                        .thenReturn(new ResponseEntity<>(null, HttpStatus.OK))
                        .thenReturn(new ResponseEntity<>(null, HttpStatus.OK));

                // Act
                int resultado = service.indexarAnimeJikan();

                // Assert
                assertEquals(1, resultado);
        }
    }
}