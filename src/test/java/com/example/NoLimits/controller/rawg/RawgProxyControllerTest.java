package com.example.NoLimits.controller.rawg;

import com.example.NoLimits.Multimedia.controller.rawg.RawgProxyController;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("RawgProxyController")
class RawgProxyControllerTest {

    @Nested
    @DisplayName("describe: proxy")
    class Proxy {

        @Test
        @DisplayName("it: debería construir URL con key y query string")
        void deberiaConstruirUrlConKeyYQueryString() {
            RawgProxyController controller = new RawgProxyController();

            RestTemplate restTemplate = mock(RestTemplate.class);
            HttpServletRequest request = mock(HttpServletRequest.class);

            ReflectionTestUtils.setField(controller, "rawgKey", "test-key");
            ReflectionTestUtils.setField(controller, "restTemplate", restTemplate);

            when(request.getRequestURI()).thenReturn("/api/rawg/games");
            when(request.getQueryString()).thenReturn("search=zelda");

            when(restTemplate.getForEntity(anyString(), eq(String.class)))
                    .thenReturn(ResponseEntity.ok("{\"results\":[]}"));

            ResponseEntity<String> response = controller.proxy(request);

            ArgumentCaptor<String> urlCaptor = ArgumentCaptor.forClass(String.class);
            verify(restTemplate).getForEntity(urlCaptor.capture(), eq(String.class));

            assertEquals(200, response.getStatusCode().value());
            assertEquals("{\"results\":[]}", response.getBody());
            assertEquals(
                    "https://api.rawg.io/api/games?key=test-key&search=zelda",
                    urlCaptor.getValue()
            );
        }

        @Test
        @DisplayName("it: debería construir URL sin query string cuando viene null")
        void deberiaConstruirUrlSinQueryStringCuandoVieneNull() {
            RawgProxyController controller = new RawgProxyController();

            RestTemplate restTemplate = mock(RestTemplate.class);
            HttpServletRequest request = mock(HttpServletRequest.class);

            ReflectionTestUtils.setField(controller, "rawgKey", "test-key");
            ReflectionTestUtils.setField(controller, "restTemplate", restTemplate);

            when(request.getRequestURI()).thenReturn("/api/rawg/genres");
            when(request.getQueryString()).thenReturn(null);

            when(restTemplate.getForEntity(anyString(), eq(String.class)))
                    .thenReturn(ResponseEntity.ok("{\"genres\":[]}"));

            ResponseEntity<String> response = controller.proxy(request);

            ArgumentCaptor<String> urlCaptor = ArgumentCaptor.forClass(String.class);
            verify(restTemplate).getForEntity(urlCaptor.capture(), eq(String.class));

            assertEquals(200, response.getStatusCode().value());
            assertEquals("{\"genres\":[]}", response.getBody());
            assertEquals(
                    "https://api.rawg.io/api/genres?key=test-key",
                    urlCaptor.getValue()
            );
        }

        @Test
        @DisplayName("it: debería eliminar key expuesta dentro del campo next")
        void deberiaEliminarKeyExpuestaDentroDelCampoNext() {
            RawgProxyController controller = new RawgProxyController();

            RestTemplate restTemplate = mock(RestTemplate.class);
            HttpServletRequest request = mock(HttpServletRequest.class);

            ReflectionTestUtils.setField(controller, "rawgKey", "test-key");
            ReflectionTestUtils.setField(controller, "restTemplate", restTemplate);

            when(request.getRequestURI()).thenReturn("/api/rawg/games");
            when(request.getQueryString()).thenReturn("page=1");

            String bodyConKey = """
                    {"results":[],"next":"https://api.rawg.io/api/games?key=SECRETA&page=2"}
                    """;

            when(restTemplate.getForEntity(anyString(), eq(String.class)))
                    .thenReturn(ResponseEntity.ok(bodyConKey));

            ResponseEntity<String> response = controller.proxy(request);

            assertEquals(200, response.getStatusCode().value());
            assertNotNull(response.getBody());
            assertTrue(response.getBody().contains("\"next\":null"));
            assertFalse(response.getBody().contains("SECRETA"));
        }

        @Test
        @DisplayName("it: debería retornar body null cuando RAWG responde null")
        void deberiaRetornarBodyNullCuandoRawgRespondeNull() {
            RawgProxyController controller = new RawgProxyController();

            RestTemplate restTemplate = mock(RestTemplate.class);
            HttpServletRequest request = mock(HttpServletRequest.class);

            ReflectionTestUtils.setField(controller, "rawgKey", "test-key");
            ReflectionTestUtils.setField(controller, "restTemplate", restTemplate);

            when(request.getRequestURI()).thenReturn("/api/rawg/games");
            when(request.getQueryString()).thenReturn(null);

            when(restTemplate.getForEntity(anyString(), eq(String.class)))
                    .thenReturn(ResponseEntity.ok(null));

            ResponseEntity<String> response = controller.proxy(request);

            assertEquals(200, response.getStatusCode().value());
            assertNull(response.getBody());
        }
    }
}