package com.example.NoLimits.dto.usuario.request;

import com.example.NoLimits.Multimedia.dto.ubicacion.request.DireccionRequestDTO;
import com.example.NoLimits.Multimedia.dto.usuario.request.UsuarioRequestDTO;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioRequestDTOTest {

    @Nested
    @DisplayName("Getters y Setters")
    class GettersSetters {

        @Test
        @DisplayName("asigna y obtiene propiedades correctamente")
        void testGettersSetters() {

            UsuarioRequestDTO dto = new UsuarioRequestDTO();

            DireccionRequestDTO direccion = new DireccionRequestDTO();

            dto.setNombre("Juan");
            dto.setApellidos("Pérez Soto");
            dto.setCorreo("juan.perez@example.com");
            dto.setTelefono(987654321L);
            dto.setPassword("ClaveSegura123!");
            dto.setRolId(1L);
            dto.setDireccion(direccion);

            assertEquals("Juan", dto.getNombre());
            assertEquals("Pérez Soto", dto.getApellidos());
            assertEquals("juan.perez@example.com", dto.getCorreo());
            assertEquals(987654321L, dto.getTelefono());
            assertEquals("ClaveSegura123!", dto.getPassword());
            assertEquals(1L, dto.getRolId());
            assertEquals(direccion, dto.getDireccion());
        }
    }

    @Nested
    @DisplayName("Valores por defecto")
    class ValoresPorDefecto {

        @Test
        @DisplayName("inicia con campos nulos")
        void testValoresPorDefecto() {

            UsuarioRequestDTO dto = new UsuarioRequestDTO();

            assertNull(dto.getNombre());
            assertNull(dto.getApellidos());
            assertNull(dto.getCorreo());
            assertNull(dto.getTelefono());
            assertNull(dto.getPassword());
            assertNull(dto.getRolId());
            assertNull(dto.getDireccion());
        }
    }

    @Nested
    @DisplayName("Métodos Lombok")
    class MetodosLombok {

        @Test
        @DisplayName("genera equals y hashCode correctamente")
        void testEqualsYHashCode() {

            UsuarioRequestDTO dto1 = new UsuarioRequestDTO();
            dto1.setCorreo("juan.perez@example.com");

            UsuarioRequestDTO dto2 = new UsuarioRequestDTO();
            dto2.setCorreo("juan.perez@example.com");

            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("genera toString correctamente")
        void testToString() {

            UsuarioRequestDTO dto = new UsuarioRequestDTO();
            dto.setCorreo("juan.perez@example.com");

            String resultado = dto.toString();

            assertNotNull(resultado);
            assertTrue(resultado.contains("juan.perez@example.com"));
        }
    }
}