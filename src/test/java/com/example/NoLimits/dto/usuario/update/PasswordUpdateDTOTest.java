package com.example.NoLimits.dto.usuario.update;

import com.example.NoLimits.Multimedia.dto.usuario.update.PasswordUpdateDTO;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordUpdateDTOTest {

    @Nested
    @DisplayName("Getters y Setters")
    class GettersSetters {

        @Test
        @DisplayName("asigna y obtiene propiedades correctamente")
        void testGettersSetters() {

            PasswordUpdateDTO dto = new PasswordUpdateDTO();

            dto.setPasswordActual("PasswordActual123");
            dto.setNuevaPassword("NuevaPassword456");

            assertEquals("PasswordActual123", dto.getPasswordActual());
            assertEquals("NuevaPassword456", dto.getNuevaPassword());
        }
    }

    @Nested
    @DisplayName("Valores por defecto")
    class ValoresPorDefecto {

        @Test
        @DisplayName("inicia con campos nulos")
        void testValoresPorDefecto() {

            PasswordUpdateDTO dto = new PasswordUpdateDTO();

            assertNull(dto.getPasswordActual());
            assertNull(dto.getNuevaPassword());
        }
    }

    @Nested
    @DisplayName("Métodos Lombok")
    class MetodosLombok {

        @Test
        @DisplayName("genera equals y hashCode correctamente")
        void testEqualsYHashCode() {

            PasswordUpdateDTO dto1 = new PasswordUpdateDTO();
            dto1.setNuevaPassword("NuevaPassword456");

            PasswordUpdateDTO dto2 = new PasswordUpdateDTO();
            dto2.setNuevaPassword("NuevaPassword456");

            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("genera toString correctamente")
        void testToString() {

            PasswordUpdateDTO dto = new PasswordUpdateDTO();
            dto.setNuevaPassword("NuevaPassword456");

            String resultado = dto.toString();

            assertNotNull(resultado);
            assertTrue(resultado.contains("NuevaPassword456"));
        }

        @Test
        @DisplayName("equals retorna true cuando es la misma instancia")
        void testEqualsMismaInstancia() {

            PasswordUpdateDTO dto = new PasswordUpdateDTO();

            assertEquals(dto, dto);
        }

        @Test
        @DisplayName("equals retorna false cuando compara con null")
        void testEqualsConNull() {

            PasswordUpdateDTO dto = new PasswordUpdateDTO();

            assertNotEquals(dto, null);
        }

        @Test
        @DisplayName("equals retorna false cuando compara con otro tipo")
        void testEqualsConOtroTipo() {

            PasswordUpdateDTO dto = new PasswordUpdateDTO();

            assertNotEquals(dto, "texto");
        }

        @Test
        @DisplayName("equals retorna false cuando cambia passwordActual")
        void testEqualsPasswordActualDistinta() {

            PasswordUpdateDTO dto1 = new PasswordUpdateDTO();
            dto1.setPasswordActual("Actual123");

            PasswordUpdateDTO dto2 = new PasswordUpdateDTO();
            dto2.setPasswordActual("Actual456");

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando cambia nuevaPassword")
        void testEqualsNuevaPasswordDistinta() {

            PasswordUpdateDTO dto1 = new PasswordUpdateDTO();
            dto1.setNuevaPassword("Nueva123");

            PasswordUpdateDTO dto2 = new PasswordUpdateDTO();
            dto2.setNuevaPassword("Nueva456");

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("hashCode cambia cuando cambia el contenido")
        void testHashCodeDistinto() {

            PasswordUpdateDTO dto1 = new PasswordUpdateDTO();
            dto1.setNuevaPassword("Nueva123");

            PasswordUpdateDTO dto2 = new PasswordUpdateDTO();
            dto2.setNuevaPassword("Nueva456");

            assertNotEquals(dto1.hashCode(), dto2.hashCode());
        }
    }
}