package com.example.NoLimits.service.igdb;

import com.example.NoLimits.Multimedia.dto.igdb.IgdbTokenResponse;
import com.example.NoLimits.Multimedia.service.igdb.IgdbTokenService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Field;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class IgdbTokenServiceTest {

    private IgdbTokenService crearServiceConMock(RestTemplate restTemplateMock) throws Exception {
        IgdbTokenService service = new IgdbTokenService();

        setPrivateField(service, "clientId", "client-demo");
        setPrivateField(service, "clientSecret", "secret-demo");
        setPrivateField(service, "restTemplate", restTemplateMock);

        return service;
    }

    private void setPrivateField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    private IgdbTokenResponse crearTokenResponse(String token, Long expiresIn) throws Exception {
        IgdbTokenResponse response = new IgdbTokenResponse();

        setPrivateField(response, "accessToken", token);
        setPrivateField(response, "expiresIn", expiresIn);

        return response;
    }

    @Nested
    @DisplayName("Unitario - getAccessToken")
    class GetAccessToken {

        @Test
        @DisplayName("renueva y retorna token cuando no existe token previo")
        void renuevaYRetornaTokenCuandoNoExisteTokenPrevio() throws Exception {
            RestTemplate restTemplateMock = mock(RestTemplate.class);
            IgdbTokenService service = crearServiceConMock(restTemplateMock);

            IgdbTokenResponse tokenResponse = crearTokenResponse("token-igdb", 3600L);

            when(restTemplateMock.postForEntity(
                    anyString(),
                    isNull(),
                    eq(IgdbTokenResponse.class)
            )).thenReturn(ResponseEntity.ok(tokenResponse));

            String resultado = service.getAccessToken();

            assertEquals("token-igdb", resultado);

            verify(restTemplateMock).postForEntity(
                    contains("https://id.twitch.tv/oauth2/token"),
                    isNull(),
                    eq(IgdbTokenResponse.class)
            );
        }

        @Test
        @DisplayName("reutiliza token existente cuando todavía no está vencido")
        void reutilizaTokenExistenteCuandoNoEstaVencido() throws Exception {
            RestTemplate restTemplateMock = mock(RestTemplate.class);
            IgdbTokenService service = crearServiceConMock(restTemplateMock);

            setPrivateField(service, "accessToken", "token-cacheado");
            setPrivateField(service, "expiresAt", Instant.now().plusSeconds(3600));

            String resultado = service.getAccessToken();

            assertEquals("token-cacheado", resultado);
            verifyNoInteractions(restTemplateMock);
        }

        @Test
        @DisplayName("lanza RuntimeException cuando la respuesta no trae access token")
        void lanzaRuntimeExceptionCuandoRespuestaNoTraeAccessToken() throws Exception {
            RestTemplate restTemplateMock = mock(RestTemplate.class);
            IgdbTokenService service = crearServiceConMock(restTemplateMock);

            IgdbTokenResponse tokenResponse = crearTokenResponse(null, 3600L);

            when(restTemplateMock.postForEntity(
                    anyString(),
                    isNull(),
                    eq(IgdbTokenResponse.class)
            )).thenReturn(ResponseEntity.ok(tokenResponse));

            RuntimeException ex = assertThrows(
                    RuntimeException.class,
                    service::getAccessToken
            );

            assertEquals("No se pudo obtener token IGDB", ex.getMessage());
        }

        @Test
        @DisplayName("lanza RuntimeException cuando falla la comunicación con Twitch OAuth")
        void lanzaRuntimeExceptionCuandoFallaComunicacionConTwitch() throws Exception {
            RestTemplate restTemplateMock = mock(RestTemplate.class);
            IgdbTokenService service = crearServiceConMock(restTemplateMock);

            when(restTemplateMock.postForEntity(
                    anyString(),
                    isNull(),
                    eq(IgdbTokenResponse.class)
            )).thenThrow(new RestClientException("Error de conexión"));

            RuntimeException ex = assertThrows(
                    RuntimeException.class,
                    service::getAccessToken
            );

            assertEquals("Error al renovar token IGDB", ex.getMessage());
        }

        @Test
        @DisplayName("lanza RuntimeException cuando la respuesta tiene body null")
        void lanzaRuntimeExceptionCuandoRespuestaTieneBodyNull() throws Exception {
            RestTemplate restTemplateMock = mock(RestTemplate.class);
            IgdbTokenService service = crearServiceConMock(restTemplateMock);

            when(restTemplateMock.postForEntity(
                    anyString(),
                    isNull(),
                    eq(IgdbTokenResponse.class)
            )).thenReturn(ResponseEntity.ok(null));

            RuntimeException ex = assertThrows(
                    RuntimeException.class,
                    service::getAccessToken
            );

            assertEquals("No se pudo obtener token IGDB", ex.getMessage());
        }

        @Test
        @DisplayName("renueva token cuando el token existente expira dentro de los próximos 60 segundos")
        void renuevaTokenCuandoTokenExistenteExpiraPronto() throws Exception {
            RestTemplate restTemplateMock = mock(RestTemplate.class);
            IgdbTokenService service = crearServiceConMock(restTemplateMock);

            setPrivateField(service, "accessToken", "token-antiguo");
            setPrivateField(service, "expiresAt", Instant.now().plusSeconds(30));

            IgdbTokenResponse tokenResponse = crearTokenResponse("token-renovado", 3600L);

            when(restTemplateMock.postForEntity(
                    anyString(),
                    isNull(),
                    eq(IgdbTokenResponse.class)
            )).thenReturn(ResponseEntity.ok(tokenResponse));

            String resultado = service.getAccessToken();

            assertEquals("token-renovado", resultado);

            verify(restTemplateMock).postForEntity(
                    contains("https://id.twitch.tv/oauth2/token"),
                    isNull(),
                    eq(IgdbTokenResponse.class)
            );
        }
    }
}