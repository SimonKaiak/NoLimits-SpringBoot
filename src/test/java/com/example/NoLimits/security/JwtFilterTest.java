package com.example.NoLimits.security;

import com.example.NoLimits.Multimedia.security.JwtFilter;
import com.example.NoLimits.Multimedia.security.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class JwtFilterTest {

    private JwtFilter jwtFilter;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain chain;

    private AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);

        jwtFilter = new JwtFilter();
        ReflectionTestUtils.setField(jwtFilter, "jwtUtil", jwtUtil);

        SecurityContextHolder.clearContext();
    }

    @AfterEach
    void tearDown() throws Exception {
        SecurityContextHolder.clearContext();
        mocks.close();
    }

    @Nested
    @DisplayName("Cuando no viene header Authorization")
    class SinHeaderAuthorization {

        @Test
        @DisplayName("debería continuar la cadena sin autenticar")
        void deberiaContinuarCadenaSinAutenticar() throws Exception {
            when(request.getHeader("Authorization")).thenReturn(null);

            jwtFilter.doFilter(request, response, chain);

            assertNull(SecurityContextHolder.getContext().getAuthentication());
            verify(chain).doFilter(request, response);
            verifyNoInteractions(jwtUtil);
        }
    }

    @Nested
    @DisplayName("Cuando el header Authorization no usa Bearer")
    class HeaderSinBearer {

        @Test
        @DisplayName("debería continuar la cadena sin validar token")
        void deberiaContinuarCadenaSinValidarToken() throws Exception {
            when(request.getHeader("Authorization")).thenReturn("Basic abc123");

            jwtFilter.doFilter(request, response, chain);

            assertNull(SecurityContextHolder.getContext().getAuthentication());
            verify(chain).doFilter(request, response);
            verifyNoInteractions(jwtUtil);
        }
    }

    @Nested
    @DisplayName("Cuando el token es válido")
    class TokenValido {

        @Test
        @DisplayName("debería autenticar al usuario en SecurityContext")
        void deberiaAutenticarUsuarioEnSecurityContext() throws Exception {
            String token = "token_valido";

            when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
            when(jwtUtil.validateToken(token)).thenReturn(true);
            when(jwtUtil.extractCorreo(token)).thenReturn("usuario@test.com");
            when(jwtUtil.extractRol(token)).thenReturn("ROLE_USER");

            jwtFilter.doFilter(request, response, chain);

            Authentication authentication =
                    SecurityContextHolder.getContext().getAuthentication();

            assertNotNull(authentication);
            assertEquals("usuario@test.com", authentication.getPrincipal());
            assertTrue(authentication.getAuthorities()
                    .stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_USER")));

            verify(jwtUtil).validateToken(token);
            verify(jwtUtil).extractCorreo(token);
            verify(jwtUtil).extractRol(token);
            verify(chain).doFilter(request, response);
        }
    }

    @Nested
    @DisplayName("Cuando el token es inválido")
    class TokenInvalido {

        @Test
        @DisplayName("debería no autenticar y continuar la cadena")
        void deberiaNoAutenticarYContinuarCadena() throws Exception {
            String token = "token_invalido";

            when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
            when(jwtUtil.validateToken(token)).thenReturn(false);

            jwtFilter.doFilter(request, response, chain);

            assertNull(SecurityContextHolder.getContext().getAuthentication());

            verify(jwtUtil).validateToken(token);
            verify(jwtUtil, never()).extractCorreo(anyString());
            verify(jwtUtil, never()).extractRol(anyString());
            verify(chain).doFilter(request, response);
        }
    }
}