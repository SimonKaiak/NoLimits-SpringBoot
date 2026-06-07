package com.example.NoLimits.dto.usuario.response;

import com.example.NoLimits.Multimedia.dto.usuario.response.RolResponseDTO;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RolResponseDTOTest {

    @Nested
    @DisplayName("Getters y Setters")
    class GettersSetters {

        @Test
        @DisplayName("asigna y obtiene propiedades correctamente")
        void testGettersSetters() {

            RolResponseDTO dto = new RolResponseDTO();

            dto.setId(1L);
            dto.setNombre("CLIENTE");
            dto.setDescripcion("Rol por defecto con permisos de compra");
            dto.setActivo(true);

            assertEquals(1L, dto.getId());
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

            RolResponseDTO dto = new RolResponseDTO();

            assertNull(dto.getId());
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

            RolResponseDTO dto1 = new RolResponseDTO();
            dto1.setId(1L);
            dto1.setNombre("CLIENTE");

            RolResponseDTO dto2 = new RolResponseDTO();
            dto2.setId(1L);
            dto2.setNombre("CLIENTE");

            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("genera toString correctamente")
        void testToString() {

            RolResponseDTO dto = new RolResponseDTO();
            dto.setNombre("CLIENTE");

            String resultado = dto.toString();

            assertNotNull(resultado);
            assertTrue(resultado.contains("CLIENTE"));
        }
    }
}