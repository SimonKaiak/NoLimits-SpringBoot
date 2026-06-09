package com.example.NoLimits.security;

import com.example.NoLimits.Multimedia.security.JwtUtil;
import org.junit.jupiter.api.*;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "expiration", 3600000L);
    }

    @Nested
    @DisplayName("generateToken")
    class GenerateToken {

        @Test
        @DisplayName("debería generar un token válido")
        void deberiaGenerarTokenValido() {
            String token = jwtUtil.generateToken("usuario@test.com", "ROLE_USER");

            assertNotNull(token);
            assertFalse(token.isBlank());
            assertTrue(jwtUtil.validateToken(token));
        }
    }

    @Nested
    @DisplayName("extractCorreo")
    class ExtractCorreo {

        @Test
        @DisplayName("debería extraer el correo del token")
        void deberiaExtraerCorreoDelToken() {
            String token = jwtUtil.generateToken("usuario@test.com", "ROLE_USER");

            String correo = jwtUtil.extractCorreo(token);

            assertEquals("usuario@test.com", correo);
        }
    }

    @Nested
    @DisplayName("extractRol")
    class ExtractRol {

        @Test
        @DisplayName("debería extraer el rol del token")
        void deberiaExtraerRolDelToken() {
            String token = jwtUtil.generateToken("usuario@test.com", "ROLE_ADMIN");

            String rol = jwtUtil.extractRol(token);

            assertEquals("ROLE_ADMIN", rol);
        }
    }

    @Nested
    @DisplayName("validateToken")
    class ValidateToken {

        @Test
        @DisplayName("debería retornar true cuando el token es válido")
        void deberiaRetornarTrueCuandoTokenEsValido() {
            String token = jwtUtil.generateToken("usuario@test.com", "ROLE_USER");

            assertTrue(jwtUtil.validateToken(token));
        }

        @Test
        @DisplayName("debería retornar false cuando el token es inválido")
        void deberiaRetornarFalseCuandoTokenEsInvalido() {
            assertFalse(jwtUtil.validateToken("token_invalido"));
        }
    }
}