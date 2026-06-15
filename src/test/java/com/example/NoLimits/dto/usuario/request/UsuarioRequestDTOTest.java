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

        @Test
        @DisplayName("equals retorna true cuando es el mismo objeto")
        void testEqualsMismaInstancia() {

            UsuarioRequestDTO dto = new UsuarioRequestDTO();

            assertEquals(dto, dto);
        }

        @Test
        @DisplayName("equals retorna false cuando compara con null")
        void testEqualsConNull() {

            UsuarioRequestDTO dto = new UsuarioRequestDTO();

            assertNotEquals(dto, null);
        }

        @Test
        @DisplayName("equals retorna false cuando compara con otro tipo")
        void testEqualsConOtroTipo() {

            UsuarioRequestDTO dto = new UsuarioRequestDTO();

            assertNotEquals(dto, "texto");
        }

        @Test
        @DisplayName("equals retorna false cuando cambia nombre")
        void testEqualsNombreDistinto() {

            UsuarioRequestDTO dto1 = new UsuarioRequestDTO();
            dto1.setNombre("Juan");

            UsuarioRequestDTO dto2 = new UsuarioRequestDTO();
            dto2.setNombre("Pedro");

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando cambia correo")
        void testEqualsCorreoDistinto() {

            UsuarioRequestDTO dto1 = new UsuarioRequestDTO();
            dto1.setCorreo("juan@test.cl");

            UsuarioRequestDTO dto2 = new UsuarioRequestDTO();
            dto2.setCorreo("pedro@test.cl");

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando cambia rol")
        void testEqualsRolDistinto() {

            UsuarioRequestDTO dto1 = new UsuarioRequestDTO();
            dto1.setRolId(1L);

            UsuarioRequestDTO dto2 = new UsuarioRequestDTO();
            dto2.setRolId(2L);

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando cambia telefono")
        void testEqualsTelefonoDistinto() {

            UsuarioRequestDTO dto1 = new UsuarioRequestDTO();
            dto1.setTelefono(111111111L);

            UsuarioRequestDTO dto2 = new UsuarioRequestDTO();
            dto2.setTelefono(222222222L);

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando cambia password")
        void testEqualsPasswordDistinta() {

            UsuarioRequestDTO dto1 = new UsuarioRequestDTO();
            dto1.setPassword("Clave123");

            UsuarioRequestDTO dto2 = new UsuarioRequestDTO();
            dto2.setPassword("OtraClave123");

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando cambia direccion")
        void testEqualsDireccionDistinta() {

            UsuarioRequestDTO dto1 = new UsuarioRequestDTO();
            dto1.setDireccion(new DireccionRequestDTO());

            UsuarioRequestDTO dto2 = new UsuarioRequestDTO();

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando cambia apellidos")
        void testEqualsApellidosDistintos() {

            UsuarioRequestDTO dto1 = new UsuarioRequestDTO();
            dto1.setApellidos("Pérez");

            UsuarioRequestDTO dto2 = new UsuarioRequestDTO();
            dto2.setApellidos("González");

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("hashCode cambia cuando cambia el contenido")
        void testHashCodeDistinto() {

            UsuarioRequestDTO dto1 = new UsuarioRequestDTO();
            dto1.setCorreo("juan@test.cl");

            UsuarioRequestDTO dto2 = new UsuarioRequestDTO();
            dto2.setCorreo("pedro@test.cl");

            assertNotEquals(dto1.hashCode(), dto2.hashCode());
        }
    }
}