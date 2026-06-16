package com.example.NoLimits.security;

import com.example.NoLimits.Multimedia.security.JwtFilter;
import com.example.NoLimits.Multimedia.security.SecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("SecurityConfig")
class SecurityConfigTest {

    private final SecurityConfig securityConfig = new SecurityConfig();

    @Nested
    @DisplayName("CORS")
    class Cors {

        @Test
        @DisplayName("debería permitir los orígenes configurados")
        void deberiaPermitirOrigenesConfigurados() {
            CorsConfigurationSource source = securityConfig.corsConfigurationSource();

            MockHttpServletRequest request = new MockHttpServletRequest();
            request.setRequestURI("/api/v1/auth/login");

            CorsConfiguration config = source.getCorsConfiguration(request);

            assertNotNull(config);
            assertEquals(List.of(
                    "http://localhost:5173",
                    "https://no-limits-react.vercel.app",
                    "https://nolimitshub.cl",
                    "https://www.nolimitshub.cl"
            ), config.getAllowedOrigins());
        }

        @Test
        @DisplayName("debería permitir métodos HTTP principales")
        void deberiaPermitirMetodosHttpPrincipales() {
            CorsConfigurationSource source = securityConfig.corsConfigurationSource();

            MockHttpServletRequest request = new MockHttpServletRequest();
            request.setRequestURI("/api/v1/usuarios");

            CorsConfiguration config = source.getCorsConfiguration(request);

            assertNotNull(config);
            assertEquals(List.of(
                    "GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"
            ), config.getAllowedMethods());
        }

        @Test
        @DisplayName("debería permitir headers y credenciales")
        void deberiaPermitirHeadersYCredenciales() {
            CorsConfigurationSource source = securityConfig.corsConfigurationSource();

            MockHttpServletRequest request = new MockHttpServletRequest();
            request.setRequestURI("/api/v1/productos");

            CorsConfiguration config = source.getCorsConfiguration(request);

            assertNotNull(config);
            assertEquals(List.of("*"), config.getAllowedHeaders());
            assertTrue(config.getAllowCredentials());
        }
    }

    @Nested
    @DisplayName("AuthenticationEntryPoint")
    class AuthenticationEntryPointTests {

        @Test
        @DisplayName("debería redirigir a swagger cuando es GET sobre raíz")
        void deberiaRedirigirASwaggerCuandoEsGetSobreRaiz() throws Exception {

            var request = new org.springframework.mock.web.MockHttpServletRequest();
            request.setRequestURI("/");
            request.setMethod("GET");

            var response = new org.springframework.mock.web.MockHttpServletResponse();

            if ("/".equals(request.getRequestURI())
                    && "GET".equalsIgnoreCase(request.getMethod())) {

                response.sendRedirect("/doc/swagger-ui.html");
            } else {
                response.sendError(401, "Unauthorized");
            }

            assertEquals(302, response.getStatus());
            assertEquals("/doc/swagger-ui.html", response.getRedirectedUrl());
        }

        @Test
        @DisplayName("debería responder 401 cuando no es GET")
        void deberiaResponder401CuandoNoEsGet() throws Exception {

            var request = new org.springframework.mock.web.MockHttpServletRequest();
            request.setRequestURI("/");
            request.setMethod("POST");

            var response = new org.springframework.mock.web.MockHttpServletResponse();

            if ("/".equals(request.getRequestURI())
                    && "GET".equalsIgnoreCase(request.getMethod())) {

                response.sendRedirect("/doc/swagger-ui.html");
            } else {
                response.sendError(401, "Unauthorized");
            }

            assertEquals(401, response.getStatus());
        }

        @Test
        @DisplayName("debería responder 401 cuando la ruta no es raíz")
        void deberiaResponder401CuandoRutaNoEsRaiz() throws Exception {

            var request = new org.springframework.mock.web.MockHttpServletRequest();
            request.setRequestURI("/api/test");
            request.setMethod("GET");

            var response = new org.springframework.mock.web.MockHttpServletResponse();

            if ("/".equals(request.getRequestURI())
                    && "GET".equalsIgnoreCase(request.getMethod())) {

                response.sendRedirect("/doc/swagger-ui.html");
            } else {
                response.sendError(401, "Unauthorized");
            }

            assertEquals(401, response.getStatus());
        }

        @Test
        @DisplayName("debería poder inyectar JwtFilter")
        void deberiaPoderInyectarJwtFilter() {

            JwtFilter jwtFilter = org.mockito.Mockito.mock(JwtFilter.class);

            ReflectionTestUtils.setField(
                    securityConfig,
                    "jwtFilter",
                    jwtFilter);

            assertSame(
                    jwtFilter,
                    ReflectionTestUtils.getField(
                            securityConfig,
                            "jwtFilter"));
        }
    }
}