package com.example.NoLimits.dto.usuario.request;

import com.example.NoLimits.Multimedia.dto.usuario.request.RolRequestDTO;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RolRequestDTOTest {

    @Nested
    @DisplayName("Getters y Setters")
    class GettersSetters {

        @Test
        @DisplayName("asigna y obtiene propiedades correctamente")
        void testGettersSetters() {

            RolRequestDTO dto = new RolRequestDTO();

            dto.setNombre("CLIENTE");
            dto.setDescripcion("Rol por defecto con permisos de compra");
            dto.setActivo(true);

            assertEquals("CLIENTE", dto.getNombre());
            assertEquals("Rol por defecto con permisos de compra", dto.getDescripcion());
            assertTrue(dto.getActivo());
        }
    }

    @Nested
    @DisplayName("Valores por defecto")
    class ValoresPorDefecto {

        @Test
        @DisplayName("inicia con campos nulos")
        void testValoresPorDefecto() {

            RolRequestDTO dto = new RolRequestDTO();

            assertNull(dto.getNombre());
            assertNull(dto.getDescripcion());
            assertNull(dto.getActivo());
        }
    }

    @Nested
    @DisplayName("Métodos Lombok")
    class MetodosLombok {

        @Test
        @DisplayName("genera equals y hashCode correctamente")
        void testEqualsYHashCode() {

            RolRequestDTO dto1 = new RolRequestDTO();
            dto1.setNombre("CLIENTE");

            RolRequestDTO dto2 = new RolRequestDTO();
            dto2.setNombre("CLIENTE");

            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("genera toString correctamente")
        void testToString() {

            RolRequestDTO dto = new RolRequestDTO();
            dto.setNombre("CLIENTE");

            String resultado = dto.toString();

            assertNotNull(resultado);
            assertTrue(resultado.contains("CLIENTE"));
        }

        @Test
        @DisplayName("equals retorna true cuando es la misma instancia")
        void testEqualsMismaInstancia() {

            RolRequestDTO dto = new RolRequestDTO();

            assertEquals(dto, dto);
        }

        @Test
        @DisplayName("equals retorna false cuando compara con null")
        void testEqualsConNull() {

            RolRequestDTO dto = new RolRequestDTO();

            assertNotEquals(dto, null);
        }

        @Test
        @DisplayName("equals retorna false cuando compara con otro tipo")
        void testEqualsConOtroTipo() {

            RolRequestDTO dto = new RolRequestDTO();

            assertNotEquals(dto, "texto");
        }

        @Test
        @DisplayName("equals retorna false cuando cambia nombre")
        void testEqualsNombreDistinto() {

            RolRequestDTO dto1 = new RolRequestDTO();
            dto1.setNombre("CLIENTE");

            RolRequestDTO dto2 = new RolRequestDTO();
            dto2.setNombre("ADMIN");

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando cambia descripcion")
        void testEqualsDescripcionDistinta() {

            RolRequestDTO dto1 = new RolRequestDTO();
            dto1.setDescripcion("Rol cliente");

            RolRequestDTO dto2 = new RolRequestDTO();
            dto2.setDescripcion("Rol administrador");

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando cambia activo")
        void testEqualsActivoDistinto() {

            RolRequestDTO dto1 = new RolRequestDTO();
            dto1.setActivo(true);

            RolRequestDTO dto2 = new RolRequestDTO();
            dto2.setActivo(false);

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("hashCode cambia cuando cambia el contenido")
        void testHashCodeDistinto() {

            RolRequestDTO dto1 = new RolRequestDTO();
            dto1.setNombre("CLIENTE");

            RolRequestDTO dto2 = new RolRequestDTO();
            dto2.setNombre("ADMIN");

            assertNotEquals(dto1.hashCode(), dto2.hashCode());
        }
    }
}