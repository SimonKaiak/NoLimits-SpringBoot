package com.example.NoLimits.service.scraping;

import com.example.NoLimits.Multimedia.service.scraping.ScrapingClientService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Field;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ScrapingClientServiceTest {

    @Nested
    @DisplayName("Unitario - obtenerPrecioSteam")
    class ObtenerPrecioSteam {

        @Test
        @DisplayName("retorna los datos del precio desde el microservicio de scraping")
        void retornaDatosDelPrecioDesdeMicroservicio() throws Exception {
            ScrapingClientService service = new ScrapingClientService();

            RestTemplate restTemplateMock = mock(RestTemplate.class);

            Field field = ScrapingClientService.class.getDeclaredField("restTemplate");
            field.setAccessible(true);
            field.set(service, restTemplateMock);

            Map<String, Object> respuestaMock = Map.of(
                    "nombre", "Counter-Strike 2",
                    "precio", 0,
                    "precioFormato", "Free To Play",
                    "moneda", "CLP",
                    "urlPlataforma", "https://store.steampowered.com/app/730",
                    "plataforma", "Steam"
            );

            String urlEsperada = "https://nolimits-scraping-service.onrender.com/api/precios?appId=730";

            when(restTemplateMock.getForObject(eq(urlEsperada), eq(Map.class)))
                    .thenReturn(respuestaMock);

            Map<String, Object> resultado = service.obtenerPrecioSteam("730");

            assertNotNull(resultado);
            assertEquals("Counter-Strike 2", resultado.get("nombre"));
            assertEquals(0, resultado.get("precio"));
            assertEquals("Free To Play", resultado.get("precioFormato"));
            assertEquals("CLP", resultado.get("moneda"));
            assertEquals("Steam", resultado.get("plataforma"));

            verify(restTemplateMock).getForObject(eq(urlEsperada), eq(Map.class));
        }
    }
}