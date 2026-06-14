package com.example.NoLimits.dto.usuario.response;

import com.example.NoLimits.Multimedia.dto.usuario.response.UsuarioResponseDTO;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioResponseDTOTest {

    @Nested
    @DisplayName("Getters y Setters")
    class GettersSetters {

        @Test
        @DisplayName("asigna y obtiene propiedades correctamente")
        void testGettersSetters() {

            UsuarioResponseDTO dto = new UsuarioResponseDTO();

            dto.setId(1L);
            dto.setNombre("Juan");
            dto.setApellidos("Pérez Soto");
            dto.setNombreCompleto("Juan Pérez Soto");
            dto.setCorreo("juan.perez@example.com");
            dto.setTelefono(987654321L);
            dto.setFotoPerfil("https://example.com/foto.jpg");

            dto.setRolId(1L);
            dto.setRolNombre("CLIENTE");

            dto.setDireccionId(10L);

            dto.setComunaId(13101L);
            dto.setComunaNombre("Santiago");

            dto.setRegionId(13L);
            dto.setRegionNombre("Región Metropolitana de Santiago");

            assertEquals(1L, dto.getId());
            assertEquals("Juan", dto.getNombre());
            assertEquals("Pérez Soto", dto.getApellidos());
            assertEquals("Juan Pérez Soto", dto.getNombreCompleto());
            assertEquals("juan.perez@example.com", dto.getCorreo());
            assertEquals(987654321L, dto.getTelefono());
            assertEquals("https://example.com/foto.jpg", dto.getFotoPerfil());

            assertEquals(1L, dto.getRolId());
            assertEquals("CLIENTE", dto.getRolNombre());

            assertEquals(10L, dto.getDireccionId());

            assertEquals(13101L, dto.getComunaId());
            assertEquals("Santiago", dto.getComunaNombre());

            assertEquals(13L, dto.getRegionId());
            assertEquals("Región Metropolitana de Santiago", dto.getRegionNombre());
        }
    }

    @Nested
    @DisplayName("Valores por defecto")
    class ValoresPorDefecto {

        @Test
        @DisplayName("inicia con campos nulos")
        void testValoresPorDefecto() {

            UsuarioResponseDTO dto = new UsuarioResponseDTO();

            assertNull(dto.getId());
            assertNull(dto.getNombre());
            assertNull(dto.getApellidos());
            assertNull(dto.getNombreCompleto());
            assertNull(dto.getCorreo());
            assertNull(dto.getTelefono());
            assertNull(dto.getFotoPerfil());

            assertNull(dto.getRolId());
            assertNull(dto.getRolNombre());

            assertNull(dto.getDireccionId());

            assertNull(dto.getComunaId());
            assertNull(dto.getComunaNombre());

            assertNull(dto.getRegionId());
            assertNull(dto.getRegionNombre());
        }
    }

    @Nested
    @DisplayName("Métodos Lombok")
    class MetodosLombok {

        @Test
        @DisplayName("genera equals y hashCode correctamente")
        void testEqualsYHashCode() {

            UsuarioResponseDTO dto1 = new UsuarioResponseDTO();
            dto1.setId(1L);
            dto1.setCorreo("juan.perez@example.com");

            UsuarioResponseDTO dto2 = new UsuarioResponseDTO();
            dto2.setId(1L);
            dto2.setCorreo("juan.perez@example.com");

            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("genera toString correctamente")
        void testToString() {

            UsuarioResponseDTO dto = new UsuarioResponseDTO();
            dto.setCorreo("juan.perez@example.com");

            String resultado = dto.toString();

            assertNotNull(resultado);
            assertTrue(resultado.contains("juan.perez@example.com"));
        }

        @Test
        @DisplayName("equals retorna true consigo mismo")
        void testEqualsMismaInstancia() {

            UsuarioResponseDTO dto = new UsuarioResponseDTO();
            dto.setId(1L);

            assertEquals(dto, dto);
        }

        @Test
        @DisplayName("equals retorna false con null")
        void testEqualsNull() {

            UsuarioResponseDTO dto = new UsuarioResponseDTO();
            dto.setId(1L);

            assertNotEquals(dto, null);
        }

        @Test
        @DisplayName("equals retorna false con otra clase")
        void testEqualsOtraClase() {

            UsuarioResponseDTO dto = new UsuarioResponseDTO();
            dto.setId(1L);

            assertNotEquals(dto, "texto");
        }

        @Test
        @DisplayName("equals retorna false cuando cambia id")
        void testEqualsIdDistinto() {

            UsuarioResponseDTO dto1 = new UsuarioResponseDTO();
            dto1.setId(1L);

            UsuarioResponseDTO dto2 = new UsuarioResponseDTO();
            dto2.setId(2L);

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando cambia correo")
        void testEqualsCorreoDistinto() {

            UsuarioResponseDTO dto1 = new UsuarioResponseDTO();
            dto1.setCorreo("juan@test.cl");

            UsuarioResponseDTO dto2 = new UsuarioResponseDTO();
            dto2.setCorreo("pedro@test.cl");

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando cambia rol")
        void testEqualsRolDistinto() {

            UsuarioResponseDTO dto1 = new UsuarioResponseDTO();
            dto1.setRolId(1L);

            UsuarioResponseDTO dto2 = new UsuarioResponseDTO();
            dto2.setRolId(2L);

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando cambia región")
        void testEqualsRegionDistinta() {

            // Arrange
            UsuarioResponseDTO dto1 = crearDTO();
            UsuarioResponseDTO dto2 = crearDTO();

            dto2.setRegionId(5L);

            // Assert
            assertNotEquals(dto1, dto2);
        }
    }

       // ==========================================
    // MÉTODO AUXILIAR
    // ==========================================

    private UsuarioResponseDTO crearDTO() {

        UsuarioResponseDTO dto = new UsuarioResponseDTO();
        dto.setId(1L);
        dto.setNombre("Juan");
        dto.setApellidos("Pérez Soto");
        dto.setNombreCompleto("Juan Pérez Soto");
        dto.setCorreo("juan.perez@example.com");
        dto.setTelefono(987654321L);
        dto.setFotoPerfil("foto.jpg");
        dto.setRolId(1L);
        dto.setRolNombre("CLIENTE");
        dto.setDireccionId(10L);
        dto.setComunaId(13101L);
        dto.setComunaNombre("Santiago");
        dto.setRegionId(13L);
        dto.setRegionNombre("Región Metropolitana");

        return dto;
    }
}
