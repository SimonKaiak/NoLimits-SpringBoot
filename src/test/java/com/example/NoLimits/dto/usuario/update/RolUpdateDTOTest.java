package com.example.NoLimits.dto.usuario.update;

import com.example.NoLimits.Multimedia.dto.usuario.update.RolUpdateDTO;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RolUpdateDTOTest {

    @Nested
    @DisplayName("Getters y Setters")
    class GettersSetters {

        @Test
        @DisplayName("asigna y obtiene propiedades correctamente")
        void testGettersSetters() {

            RolUpdateDTO dto = new RolUpdateDTO();

            dto.setNombre("ADMIN");
            dto.setDescripcion("Rol con acceso completo al sistema");
            dto.setActivo(true);

            assertEquals("ADMIN", dto.getNombre());
            assertEquals("Rol con acceso completo al sistema", dto.getDescripcion());
            assertTrue(dto.getActivo());
        }
    }

    @Nested
    @DisplayName("Valores por defecto")
    class ValoresPorDefecto {

        @Test
        @DisplayName("inicia con campos nulos")
        void testValoresPorDefecto() {

            RolUpdateDTO dto = new RolUpdateDTO();

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

            RolUpdateDTO dto1 = new RolUpdateDTO();
            dto1.setNombre("ADMIN");

            RolUpdateDTO dto2 = new RolUpdateDTO();
            dto2.setNombre("ADMIN");

            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("genera toString correctamente")
        void testToString() {

            RolUpdateDTO dto = new RolUpdateDTO();
            dto.setNombre("ADMIN");

            String resultado = dto.toString();

            assertNotNull(resultado);
            assertTrue(resultado.contains("ADMIN"));
        }

        @Test
        @DisplayName("equals retorna true cuando es la misma instancia")
        void testEqualsMismaInstancia() {

            RolUpdateDTO dto = new RolUpdateDTO();

            assertEquals(dto, dto);
        }

        @Test
        @DisplayName("equals retorna false cuando compara con null")
        void testEqualsConNull() {

            RolUpdateDTO dto = new RolUpdateDTO();

            assertNotEquals(dto, null);
        }

        @Test
        @DisplayName("equals retorna false cuando compara con otro tipo")
        void testEqualsConOtroTipo() {

            RolUpdateDTO dto = new RolUpdateDTO();

            assertNotEquals(dto, "texto");
        }

        @Test
        @DisplayName("equals retorna false cuando cambia nombre")
        void testEqualsNombreDistinto() {

            RolUpdateDTO dto1 = new RolUpdateDTO();
            dto1.setNombre("ADMIN");

            RolUpdateDTO dto2 = new RolUpdateDTO();
            dto2.setNombre("CLIENTE");

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando cambia descripcion")
        void testEqualsDescripcionDistinta() {

            RolUpdateDTO dto1 = new RolUpdateDTO();
            dto1.setDescripcion("Administrador");

            RolUpdateDTO dto2 = new RolUpdateDTO();
            dto2.setDescripcion("Cliente");

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando cambia activo")
        void testEqualsActivoDistinto() {

            RolUpdateDTO dto1 = new RolUpdateDTO();
            dto1.setActivo(true);

            RolUpdateDTO dto2 = new RolUpdateDTO();
            dto2.setActivo(false);

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("hashCode cambia cuando cambia el contenido")
        void testHashCodeDistinto() {

            RolUpdateDTO dto1 = new RolUpdateDTO();
            dto1.setNombre("ADMIN");

            RolUpdateDTO dto2 = new RolUpdateDTO();
            dto2.setNombre("CLIENTE");

            assertNotEquals(dto1.hashCode(), dto2.hashCode());
        }
    }
}