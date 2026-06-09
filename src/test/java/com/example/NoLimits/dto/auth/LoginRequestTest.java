package com.example.NoLimits.dto.auth;

import com.example.NoLimits.Multimedia.dto.auth.LoginRequest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("LoginRequest")
class LoginRequestTest {

    @Nested
    @DisplayName("describe: constructor vacío")
    class ConstructorVacio {

        @Test
        @DisplayName("it: debería crear una instancia con campos nulos")
        void deberiaCrearInstanciaConCamposNulos() {
            // Arrange & Act
            LoginRequest request = new LoginRequest();

            // Assert
            assertNotNull(request);
            assertNull(request.getCorreo());
            assertNull(request.getPassword());
        }
    }

    @Nested
    @DisplayName("describe: getters y setters")
    class GettersSetters {

        @Test
        @DisplayName("it: debería asignar y obtener correo correctamente")
        void deberiaAsignarYObtenerCorreoCorrectamente() {
            // Arrange
            LoginRequest request = new LoginRequest();

            // Act
            request.setCorreo("usuario@test.com");

            // Assert
            assertEquals("usuario@test.com", request.getCorreo());
        }

        @Test
        @DisplayName("it: debería asignar y obtener password correctamente")
        void deberiaAsignarYObtenerPasswordCorrectamente() {
            // Arrange
            LoginRequest request = new LoginRequest();

            // Act
            request.setPassword("clave123");

            // Assert
            assertEquals("clave123", request.getPassword());
        }

        @Test
        @DisplayName("it: debería permitir correo con espacios")
        void deberiaPermitirCorreoConEspacios() {
            // Arrange
            LoginRequest request = new LoginRequest();

            // Act
            request.setCorreo("  usuario@test.com  ");

            // Assert
            assertEquals("  usuario@test.com  ", request.getCorreo());
        }
    }
}