package com.example.NoLimits.dto.usuario.update;

import com.example.NoLimits.Multimedia.dto.ubicacion.request.DireccionRequestDTO;
import com.example.NoLimits.Multimedia.dto.usuario.update.UsuarioUpdateDTO;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioUpdateDTOTest {

    @Nested
    @DisplayName("Getters y Setters")
    class GettersSetters {

        @Test
        @DisplayName("asigna y obtiene propiedades correctamente")
        void testGettersSetters() {

            UsuarioUpdateDTO dto = new UsuarioUpdateDTO();

            DireccionRequestDTO direccion = new DireccionRequestDTO();

            dto.setNombre("Juan");
            dto.setApellidos("Pérez Soto");
            dto.setCorreo("juan.perez@example.com");
            dto.setTelefono(987654321L);
            dto.setPassword("NuevaPassword123");
            dto.setFotoPerfil("https://example.com/foto.jpg");
            dto.setRolId(2L);
            dto.setDireccion(direccion);

            assertEquals("Juan", dto.getNombre());
            assertEquals("Pérez Soto", dto.getApellidos());
            assertEquals("juan.perez@example.com", dto.getCorreo());
            assertEquals(987654321L, dto.getTelefono());
            assertEquals("NuevaPassword123", dto.getPassword());
            assertEquals("https://example.com/foto.jpg", dto.getFotoPerfil());
            assertEquals(2L, dto.getRolId());
            assertEquals(direccion, dto.getDireccion());
        }
    }

    @Nested
    @DisplayName("Valores por defecto")
    class ValoresPorDefecto {

        @Test
        @DisplayName("inicia con campos nulos")
        void testValoresPorDefecto() {

            UsuarioUpdateDTO dto = new UsuarioUpdateDTO();

            assertNull(dto.getNombre());
            assertNull(dto.getApellidos());
            assertNull(dto.getCorreo());
            assertNull(dto.getTelefono());
            assertNull(dto.getPassword());
            assertNull(dto.getFotoPerfil());
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

            UsuarioUpdateDTO dto1 = new UsuarioUpdateDTO();
            dto1.setCorreo("juan.perez@example.com");

            UsuarioUpdateDTO dto2 = new UsuarioUpdateDTO();
            dto2.setCorreo("juan.perez@example.com");

            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("genera toString correctamente")
        void testToString() {

            UsuarioUpdateDTO dto = new UsuarioUpdateDTO();
            dto.setCorreo("juan.perez@example.com");

            String resultado = dto.toString();

            assertNotNull(resultado);
            assertTrue(resultado.contains("juan.perez@example.com"));
        }
    }
}