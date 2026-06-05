package com.example.NoLimits.security;

import com.example.NoLimits.Multimedia.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("JwtUtilTest — Generación y validación de tokens JWT")
class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "expiration", 86400000L);
    }

    @Test
    @DisplayName("generateToken → genera un token no nulo ni vacío")
    void generateToken_retornaTokenValido() {
        String token = jwtUtil.generateToken("user@test.com", "ROLE_USER");
        assertNotNull(token);
        assertFalse(token.isBlank());
    }

    @Test
    @DisplayName("extractCorreo → extrae el correo del token correctamente")
    void extractCorreo_retornaCorreoCorrecto() {
        String token = jwtUtil.generateToken("user@test.com", "ROLE_USER");
        assertEquals("user@test.com", jwtUtil.extractCorreo(token));
    }

    @Test
    @DisplayName("extractRol → extrae el rol del token correctamente")
    void extractRol_retornaRolCorrecto() {
        String token = jwtUtil.generateToken("user@test.com", "ROLE_ADMIN");
        assertEquals("ROLE_ADMIN", jwtUtil.extractRol(token));
    }

    @Test
    @DisplayName("validateToken → retorna true con token válido")
    void validateToken_tokenValido_retornaTrue() {
        String token = jwtUtil.generateToken("user@test.com", "ROLE_USER");
        assertTrue(jwtUtil.validateToken(token));
    }

    @Test
    @DisplayName("validateToken → retorna false con token inválido")
    void validateToken_tokenInvalido_retornaFalse() {
        assertFalse(jwtUtil.validateToken("esto.no.es.un.token"));
    }

    @Test
    @DisplayName("validateToken → retorna false con token manipulado")
    void validateToken_tokenManipulado_retornaFalse() {
        String token = jwtUtil.generateToken("user@test.com", "ROLE_USER");
        String manipulado = token.substring(0, token.length() - 5) + "XXXXX";
        assertFalse(jwtUtil.validateToken(manipulado));
    }

    @Test
    @DisplayName("validateToken → retorna false con token con formato incorrecto")
    void validateToken_formatoIncorrecto_retornaFalse() {
        assertFalse(jwtUtil.validateToken("token.sin.firma.correcta.abc123"));
    }

    @Test
    @DisplayName("generateToken → tokens para usuarios distintos son diferentes")
    void generateToken_usuariosDistintos_sonDiferentes() {
        String token1 = jwtUtil.generateToken("user1@test.com", "ROLE_USER");
        String token2 = jwtUtil.generateToken("user2@test.com", "ROLE_USER");
        assertNotEquals(token1, token2);
    }
}