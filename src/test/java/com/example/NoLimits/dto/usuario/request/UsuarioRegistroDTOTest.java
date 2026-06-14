package com.example.NoLimits.dto.usuario.request;

import com.example.NoLimits.Multimedia.dto.ubicacion.request.DireccionRequestDTO;
import com.example.NoLimits.Multimedia.dto.usuario.request.UsuarioRegistroDTO;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioRegistroDTOTest {

    @Nested
    @DisplayName("Getters y Setters")
    class GettersSetters {

        @Test
        @DisplayName("asigna y obtiene propiedades correctamente")
        void testGettersSetters() {

            UsuarioRegistroDTO dto = new UsuarioRegistroDTO();

            DireccionRequestDTO direccion = new DireccionRequestDTO();

            dto.setNombre("Juan");
            dto.setApellidos("Pérez Soto");
            dto.setCorreo("juan.perez@example.com");
            dto.setTelefono(987654321L);
            dto.setPassword("ClaveSegura123!");
            dto.setDireccion(direccion);

            assertEquals("Juan", dto.getNombre());
            assertEquals("Pérez Soto", dto.getApellidos());
            assertEquals("juan.perez@example.com", dto.getCorreo());
            assertEquals(987654321L, dto.getTelefono());
            assertEquals("ClaveSegura123!", dto.getPassword());
            assertEquals(direccion, dto.getDireccion());
        }
    }

    @Nested
    @DisplayName("Valores por defecto")
    class ValoresPorDefecto {

        @Test
        @DisplayName("inicia con campos nulos")
        void testValoresPorDefecto() {

            UsuarioRegistroDTO dto = new UsuarioRegistroDTO();

            assertNull(dto.getNombre());
            assertNull(dto.getApellidos());
            assertNull(dto.getCorreo());
            assertNull(dto.getTelefono());
            assertNull(dto.getPassword());
            assertNull(dto.getDireccion());
        }
    }

    @Nested
    @DisplayName("Métodos Lombok")
    class MetodosLombok {

        @Test
        @DisplayName("genera equals y hashCode correctamente")
        void testEqualsYHashCode() {

            UsuarioRegistroDTO dto1 = new UsuarioRegistroDTO();
            dto1.setCorreo("juan.perez@example.com");

            UsuarioRegistroDTO dto2 = new UsuarioRegistroDTO();
            dto2.setCorreo("juan.perez@example.com");

            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("genera toString correctamente")
        void testToString() {

            UsuarioRegistroDTO dto = new UsuarioRegistroDTO();
            dto.setCorreo("juan.perez@example.com");

            String resultado = dto.toString();

            assertNotNull(resultado);
            assertTrue(resultado.contains("juan.perez@example.com"));
        }

        @Test
        @DisplayName("equals retorna true cuando es la misma instancia")
        void testEqualsMismaInstancia() {

            UsuarioRegistroDTO dto = new UsuarioRegistroDTO();

            assertEquals(dto, dto);
        }

        @Test
        @DisplayName("equals retorna false cuando compara con null")
        void testEqualsConNull() {

            UsuarioRegistroDTO dto = new UsuarioRegistroDTO();

            assertNotEquals(dto, null);
        }

        @Test
        @DisplayName("equals retorna false cuando compara con otro tipo")
        void testEqualsConOtroTipo() {

            UsuarioRegistroDTO dto = new UsuarioRegistroDTO();

            assertNotEquals(dto, "texto");
        }

        @Test
        @DisplayName("equals retorna false cuando cambia nombre")
        void testEqualsNombreDistinto() {

            UsuarioRegistroDTO dto1 = new UsuarioRegistroDTO();
            dto1.setNombre("Juan");

            UsuarioRegistroDTO dto2 = new UsuarioRegistroDTO();
            dto2.setNombre("Pedro");

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando cambia apellidos")
        void testEqualsApellidosDistintos() {

            UsuarioRegistroDTO dto1 = new UsuarioRegistroDTO();
            dto1.setApellidos("Pérez");

            UsuarioRegistroDTO dto2 = new UsuarioRegistroDTO();
            dto2.setApellidos("González");

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando cambia correo")
        void testEqualsCorreoDistinto() {

            UsuarioRegistroDTO dto1 = new UsuarioRegistroDTO();
            dto1.setCorreo("juan@test.cl");

            UsuarioRegistroDTO dto2 = new UsuarioRegistroDTO();
            dto2.setCorreo("pedro@test.cl");

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando cambia telefono")
        void testEqualsTelefonoDistinto() {

            UsuarioRegistroDTO dto1 = new UsuarioRegistroDTO();
            dto1.setTelefono(111111111L);

            UsuarioRegistroDTO dto2 = new UsuarioRegistroDTO();
            dto2.setTelefono(222222222L);

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando cambia password")
        void testEqualsPasswordDistinta() {

            UsuarioRegistroDTO dto1 = new UsuarioRegistroDTO();
            dto1.setPassword("Clave123");

            UsuarioRegistroDTO dto2 = new UsuarioRegistroDTO();
            dto2.setPassword("OtraClave123");

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando cambia direccion")
        void testEqualsDireccionDistinta() {

            UsuarioRegistroDTO dto1 = new UsuarioRegistroDTO();
            dto1.setDireccion(new DireccionRequestDTO());

            UsuarioRegistroDTO dto2 = new UsuarioRegistroDTO();

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("hashCode cambia cuando cambia el contenido")
        void testHashCodeDistinto() {

            UsuarioRegistroDTO dto1 = new UsuarioRegistroDTO();
            dto1.setCorreo("juan@test.cl");

            UsuarioRegistroDTO dto2 = new UsuarioRegistroDTO();
            dto2.setCorreo("pedro@test.cl");

            assertNotEquals(dto1.hashCode(), dto2.hashCode());
        }
    }
}