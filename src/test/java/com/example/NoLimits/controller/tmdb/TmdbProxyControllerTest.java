package com.example.NoLimits.controller.tmdb;

import com.example.NoLimits.Multimedia.controller.tmdb.TmdbProxyController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@DisplayName("TmdbProxyController — unitario")
class TmdbProxyControllerTest {

    private TmdbProxyController controller;
    private RestTemplate restTemplateMock;

    @BeforeEach
    void setUp() {
        controller       = new TmdbProxyController();
        restTemplateMock = mock(RestTemplate.class);
        ReflectionTestUtils.setField(controller, "tmdbToken",    "TEST_TOKEN");
        ReflectionTestUtils.setField(controller, "restTemplate", restTemplateMock);
    }

    @Nested
    @DisplayName("sanitización del body")
    class SanitizacionBody {

        @Test
        @DisplayName("body sin campo 'next' → se devuelve intacto")
        void bodySinNext_seDevuelveIntacto() {
            String body = "{\"results\":[{\"id\":1}]}";
            when(restTemplateMock.getForEntity(anyString(), eq(String.class)))
                    .thenReturn(ResponseEntity.ok(body));

            MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/tmdb/movie/popular");
            ResponseEntity<String> response = controller.proxy(request);

            assertEquals(200, response.getStatusCode().value());
            assertEquals(body, response.getBody());
        }

        @Test
        @DisplayName("body con 'next' que contiene api_key → se reemplaza por null")
        void bodyConNext_apiKeyEliminada() {
            String body = "{\"results\":[],\"next\":\"https://api.themoviedb.org/3/movie?api_key=SECRETO&page=2\"}";
            when(restTemplateMock.getForEntity(anyString(), eq(String.class)))
                    .thenReturn(ResponseEntity.ok(body));

            MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/tmdb/movie/popular");
            ResponseEntity<String> response = controller.proxy(request);

            assertNotNull(response.getBody());
            assertTrue(response.getBody().contains("\"next\":null"));
            assertFalse(response.getBody().contains("SECRETO"));
        }

        @Test
        @DisplayName("body null de TMDB → response con body null sin lanzar excepción")
        void bodyNullDeTmdb_noLanzaExcepcion() {
            when(restTemplateMock.getForEntity(anyString(), eq(String.class)))
                    .thenReturn(ResponseEntity.ok(null));

            MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/tmdb/movie/popular");
            ResponseEntity<String> response = controller.proxy(request);

            assertNull(response.getBody());
        }
    }

    @Nested
    @DisplayName("construcción de URL")
    class ConstruccionUrl {

        @Test
        @DisplayName("sin queryString → URL contiene token pero no parámetros extra")
        void sinQueryString_urlContieneToken() {
            when(restTemplateMock.getForEntity(anyString(), eq(String.class)))
                    .thenReturn(ResponseEntity.ok("{}"));

            MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/tmdb/movie/popular");
            controller.proxy(request);

            ArgumentCaptor<String> urlCaptor = ArgumentCaptor.forClass(String.class);
            verify(restTemplateMock).getForEntity(urlCaptor.capture(), eq(String.class));
            assertTrue(urlCaptor.getValue().contains("TEST_TOKEN"));
        }

        @Test
        @DisplayName("con queryString → URL incluye los parámetros")
        void conQueryString_urlContieneParametros() {
            when(restTemplateMock.getForEntity(anyString(), eq(String.class)))
                    .thenReturn(ResponseEntity.ok("{}"));

            MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/tmdb/search/movie");
            request.setQueryString("query=matrix&page=1");
            controller.proxy(request);

            ArgumentCaptor<String> urlCaptor = ArgumentCaptor.forClass(String.class);
            verify(restTemplateMock).getForEntity(urlCaptor.capture(), eq(String.class));
            String url = urlCaptor.getValue();
            assertTrue(url.contains("query=matrix"));
            assertTrue(url.contains("page=1"));
        }
    }
}