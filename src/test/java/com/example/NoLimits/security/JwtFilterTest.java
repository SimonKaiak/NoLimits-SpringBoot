package com.example.NoLimits.security;

import com.example.NoLimits.Multimedia.security.JwtFilter;
import com.example.NoLimits.Multimedia.security.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("JwtFilter")
class JwtFilterTest {

    private JwtFilter filter;
    private JwtUtil jwtUtilMock;

    @BeforeEach
    void setUp() {
        jwtUtilMock = mock(JwtUtil.class);
        filter = new JwtFilter();
        ReflectionTestUtils.setField(filter, "jwtUtil", jwtUtilMock);
        SecurityContextHolder.clearContext();
    }

    @Nested
    @DisplayName("sin header Authorization")
    class SinHeader {

        @Test
        @DisplayName("pasa la cadena sin autenticar")
        void sinHeader_pasaCadenaSinAutenticar() throws Exception {
            MockHttpServletRequest request   = new MockHttpServletRequest();
            MockHttpServletResponse response = new MockHttpServletResponse();
            FilterChain chain = mock(FilterChain.class);

            filter.doFilter(request, response, chain);

            verify(chain).doFilter(request, response);
            assertNull(SecurityContextHolder.getContext().getAuthentication());
            verifyNoInteractions(jwtUtilMock);
        }
    }

    @Nested
    @DisplayName("header Authorization sin prefijo Bearer")
    class HeaderSinBearer {

        @Test
        @DisplayName("header Basic → se ignora, cadena sigue")
        void headerSinBearer_seIgnora() throws Exception {
            MockHttpServletRequest request = new MockHttpServletRequest();
            request.addHeader("Authorization", "Basic dXNlcjpwYXNz");
            MockHttpServletResponse response = new MockHttpServletResponse();
            FilterChain chain = mock(FilterChain.class);

            filter.doFilter(request, response, chain);

            verify(chain).doFilter(request, response);
            assertNull(SecurityContextHolder.getContext().getAuthentication());
            verifyNoInteractions(jwtUtilMock);
        }
    }

    @Nested
    @DisplayName("header con Bearer token válido")
    class TokenValido {

        @Test
        @DisplayName("token válido → usuario queda autenticado en SecurityContext")
        void tokenValido_usuarioAutenticado() throws Exception {
            MockHttpServletRequest request = new MockHttpServletRequest();
            request.addHeader("Authorization", "Bearer token.valido.jwt");
            MockHttpServletResponse response = new MockHttpServletResponse();
            FilterChain chain = mock(FilterChain.class);

            when(jwtUtilMock.validateToken("token.valido.jwt")).thenReturn(true);
            when(jwtUtilMock.extractCorreo("token.valido.jwt")).thenReturn("user@test.com");
            when(jwtUtilMock.extractRol("token.valido.jwt")).thenReturn("ROLE_USER");

            filter.doFilter(request, response, chain);

            verify(chain).doFilter(request, response);
            assertNotNull(SecurityContextHolder.getContext().getAuthentication());
            assertEquals("user@test.com",
                    SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        }
    }

    @Nested
    @DisplayName("header con Bearer token inválido")
    class TokenInvalido {

        @Test
        @DisplayName("token inválido → cadena sigue sin autenticar")
        void tokenInvalido_sinAutenticar() throws Exception {
            MockHttpServletRequest request = new MockHttpServletRequest();
            request.addHeader("Authorization", "Bearer token.mal.formado");
            MockHttpServletResponse response = new MockHttpServletResponse();
            FilterChain chain = mock(FilterChain.class);

            when(jwtUtilMock.validateToken("token.mal.formado")).thenReturn(false);

            filter.doFilter(request, response, chain);

            verify(chain).doFilter(request, response);
            assertNull(SecurityContextHolder.getContext().getAuthentication());
            verify(jwtUtilMock, never()).extractCorreo(any());
        }
    }
}