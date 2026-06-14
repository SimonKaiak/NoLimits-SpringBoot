package com.example.NoLimits.dto.usuario.update;

import com.example.NoLimits.Multimedia.dto.usuario.update.CambiarCorreoDTO;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CambiarCorreoDTOTest {

    @Nested
    @DisplayName("Getters y Setters")
    class GettersSetters {

        @Test
        @DisplayName("asigna y obtiene propiedades correctamente")
        void testGettersSetters() {

            CambiarCorreoDTO dto = new CambiarCorreoDTO();

            dto.setNuevoCorreo("nuevo@correo.com");
            dto.setPasswordActual("MiPassword123");

            assertEquals("nuevo@correo.com", dto.getNuevoCorreo());
            assertEquals("MiPassword123", dto.getPasswordActual());
        }
    }

    @Nested
    @DisplayName("Valores por defecto")
    class ValoresPorDefecto {

        @Test
        @DisplayName("inicia con campos nulos")
        void testValoresPorDefecto() {

            CambiarCorreoDTO dto = new CambiarCorreoDTO();

            assertNull(dto.getNuevoCorreo());
            assertNull(dto.getPasswordActual());
        }
    }

    @Nested
    @DisplayName("Métodos Lombok")
    class MetodosLombok {

        @Test
        @DisplayName("genera equals y hashCode correctamente")
        void testEqualsYHashCode() {

            CambiarCorreoDTO dto1 = new CambiarCorreoDTO();
            dto1.setNuevoCorreo("nuevo@correo.com");

            CambiarCorreoDTO dto2 = new CambiarCorreoDTO();
            dto2.setNuevoCorreo("nuevo@correo.com");

            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("genera toString correctamente")
        void testToString() {

            CambiarCorreoDTO dto = new CambiarCorreoDTO();
            dto.setNuevoCorreo("nuevo@correo.com");

            String resultado = dto.toString();

            assertNotNull(resultado);
            assertTrue(resultado.contains("nuevo@correo.com"));
        }

        @Test
        @DisplayName("equals retorna true cuando es la misma instancia")
        void testEqualsMismaInstancia() {

            CambiarCorreoDTO dto = new CambiarCorreoDTO();

            assertEquals(dto, dto);
        }

        @Test
        @DisplayName("equals retorna false cuando compara con null")
        void testEqualsConNull() {

            CambiarCorreoDTO dto = new CambiarCorreoDTO();

            assertNotEquals(dto, null);
        }

        @Test
        @DisplayName("equals retorna false cuando compara con otro tipo")
        void testEqualsConOtroTipo() {

            CambiarCorreoDTO dto = new CambiarCorreoDTO();

            assertNotEquals(dto, "texto");
        }

        @Test
        @DisplayName("equals retorna false cuando cambia nuevoCorreo")
        void testEqualsNuevoCorreoDistinto() {

            CambiarCorreoDTO dto1 = new CambiarCorreoDTO();
            dto1.setNuevoCorreo("correo1@test.cl");

            CambiarCorreoDTO dto2 = new CambiarCorreoDTO();
            dto2.setNuevoCorreo("correo2@test.cl");

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando cambia passwordActual")
        void testEqualsPasswordActualDistinta() {

            CambiarCorreoDTO dto1 = new CambiarCorreoDTO();
            dto1.setPasswordActual("Password123");

            CambiarCorreoDTO dto2 = new CambiarCorreoDTO();
            dto2.setPasswordActual("Password456");

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("hashCode cambia cuando cambia el contenido")
        void testHashCodeDistinto() {

            CambiarCorreoDTO dto1 = new CambiarCorreoDTO();
            dto1.setNuevoCorreo("correo1@test.cl");

            CambiarCorreoDTO dto2 = new CambiarCorreoDTO();
            dto2.setNuevoCorreo("correo2@test.cl");

            assertNotEquals(dto1.hashCode(), dto2.hashCode());
        }
    }
}