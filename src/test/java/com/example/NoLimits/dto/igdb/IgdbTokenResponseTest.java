package com.example.NoLimits.dto.igdb;

import com.example.NoLimits.Multimedia.dto.igdb.IgdbTokenResponse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("IgdbTokenResponse Tests")
class IgdbTokenResponseTest {

    @Nested
    @DisplayName("Getters")
    class Getters {

        @Test
        @DisplayName("retorna accessToken correctamente")
        void retornaAccessTokenCorrectamente() throws Exception {

            IgdbTokenResponse dto = new IgdbTokenResponse();

            Field field = IgdbTokenResponse.class.getDeclaredField("accessToken");
            field.setAccessible(true);
            field.set(dto, "token123");

            assertEquals("token123", dto.getAccessToken());
        }

        @Test
        @DisplayName("retorna expiresIn correctamente")
        void retornaExpiresInCorrectamente() throws Exception {

            IgdbTokenResponse dto = new IgdbTokenResponse();

            Field field = IgdbTokenResponse.class.getDeclaredField("expiresIn");
            field.setAccessible(true);
            field.set(dto, 3600L);

            assertEquals(3600L, dto.getExpiresIn());
        }

        @Test
        @DisplayName("retorna tokenType correctamente")
        void retornaTokenTypeCorrectamente() throws Exception {

            IgdbTokenResponse dto = new IgdbTokenResponse();

            Field field = IgdbTokenResponse.class.getDeclaredField("tokenType");
            field.setAccessible(true);
            field.set(dto, "bearer");

            assertEquals("bearer", dto.getTokenType());
        }
    }

    @Nested
    @DisplayName("Valores por defecto")
    class ValoresPorDefecto {

        @Test
        @DisplayName("inicia con campos nulos")
        void iniciaConCamposNulos() {

            IgdbTokenResponse dto = new IgdbTokenResponse();

            assertNull(dto.getAccessToken());
            assertNull(dto.getExpiresIn());
            assertNull(dto.getTokenType());
        }
    }
}
