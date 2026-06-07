package com.example.NoLimits.model.usuario;

import com.example.NoLimits.Multimedia.model.usuario.RolModel;
import com.example.NoLimits.Multimedia.model.usuario.UsuarioModel;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("RolModel Tests")
class RolModelTest {

    @Nested
    @DisplayName("Constructor completo")
    class ConstructorCompleto {

        @Test
        @DisplayName("debe crear rol correctamente")
        void debeCrearRolCorrectamente() {

            List<UsuarioModel> usuarios = new ArrayList<>();

            RolModel rol = new RolModel(
                    1L,
                    "CLIENTE",
                    "Rol de compras",
                    true,
                    usuarios
            );

            assertEquals(1L, rol.getId());
            assertEquals("CLIENTE", rol.getNombre());
            assertEquals("Rol de compras", rol.getDescripcion());
            assertTrue(rol.getActivo());
            assertEquals(usuarios, rol.getUsuarios());
        }
    }

    @Nested
    @DisplayName("Getters y Setters")
    class GettersSetters {

        @Test
        @DisplayName("debe asignar y obtener propiedades")
        void debeAsignarYObtenerPropiedades() {

            List<UsuarioModel> usuarios = new ArrayList<>();

            RolModel rol = new RolModel();

            rol.setId(10L);
            rol.setNombre("ADMIN");
            rol.setDescripcion("Administrador");
            rol.setActivo(false);
            rol.setUsuarios(usuarios);

            assertEquals(10L, rol.getId());
            assertEquals("ADMIN", rol.getNombre());
            assertEquals("Administrador", rol.getDescripcion());
            assertFalse(rol.getActivo());
            assertEquals(usuarios, rol.getUsuarios());
        }
    }

    @Nested
    @DisplayName("Valor por defecto")
    class ValorPorDefecto {

        @Test
        @DisplayName("activo inicia en true")
        void activoIniciaEnTrue() {

            RolModel rol = new RolModel();

            assertTrue(rol.getActivo());
        }
    }

    @Nested
    @DisplayName("Metodo esActivo")
    class EsActivo {

        @Test
        void retornaTrueCuandoActivoEsTrue() {

            RolModel rol = new RolModel();
            rol.setActivo(true);

            assertTrue(rol.esActivo());
        }

        @Test
        void retornaFalseCuandoActivoEsFalse() {

            RolModel rol = new RolModel();
            rol.setActivo(false);

            assertFalse(rol.esActivo());
        }

        @Test
        void retornaFalseCuandoActivoEsNull() {

            RolModel rol = new RolModel();
            rol.setActivo(null);

            assertFalse(rol.esActivo());
        }
    }

    @Nested
    @DisplayName("Equals HashCode y ToString")
    class EqualsHashCodeToString {

        @Test
        void objetosConMismoContenidoSonIguales() {

            RolModel r1 = new RolModel();
            r1.setId(1L);

            RolModel r2 = new RolModel();
            r2.setId(1L);

            assertEquals(r1, r2);
            assertEquals(r1.hashCode(), r2.hashCode());
        }

        @Test
        void toStringNoDebeSerNull() {

            RolModel rol = new RolModel();

            assertNotNull(rol.toString());
        }
    }
}
