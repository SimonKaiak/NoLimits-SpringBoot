package com.example.NoLimits.model.usuario;

import com.example.NoLimits.Multimedia.model.usuario.UsuarioModel;
import com.example.NoLimits.Multimedia.model.usuario.RolModel;
import com.example.NoLimits.Multimedia.model.ubicacion.ComunaModel;
import com.example.NoLimits.Multimedia.model.ubicacion.DireccionModel;
import com.example.NoLimits.Multimedia.model.ubicacion.RegionModel;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("UsuarioModel Tests")
class UsuarioModelTest {

    @Nested
    @DisplayName("Nombre completo")
    class NombreCompleto {

        @Test
        void debeRetornarNombreCompleto() {

            UsuarioModel usuario = new UsuarioModel();
            usuario.setNombre("Juan");
            usuario.setApellidos("Pérez Soto");

            assertEquals(
                    "Juan Pérez Soto",
                    usuario.getNombreCompleto()
            );
        }

        @Test
        void debeManejarNombreNull() {

            UsuarioModel usuario = new UsuarioModel();
            usuario.setApellidos("Pérez");

            assertEquals(
                    " Pérez",
                    usuario.getNombreCompleto()
            );
        }

        @Test
        void debeManejarApellidosNull() {

            UsuarioModel usuario = new UsuarioModel();
            usuario.setNombre("Juan");

            assertEquals(
                    "Juan ",
                    usuario.getNombreCompleto()
            );
        }
    }

    @Nested
    @DisplayName("Direccion")
    class Direccion {

        @Test
        void debeRetornarDireccionId() {

            DireccionModel direccion = new DireccionModel();
            direccion.setId(10L);

            UsuarioModel usuario = new UsuarioModel();
            usuario.setDireccion(direccion);

            assertEquals(10L, usuario.getDireccionId());
        }

        @Test
        void direccionNullRetornaNull() {

            UsuarioModel usuario = new UsuarioModel();

            assertNull(usuario.getDireccionId());
        }
    }

    @Nested
    @DisplayName("Comuna")
    class Comuna {

        @Test
        void debeRetornarDatosComuna() {

            ComunaModel comuna = new ComunaModel();
            comuna.setId(13101L);
            comuna.setNombre("Santiago");

            DireccionModel direccion = new DireccionModel();
            direccion.setComuna(comuna);

            UsuarioModel usuario = new UsuarioModel();
            usuario.setDireccion(direccion);

            assertEquals(13101L, usuario.getComunaId());
            assertEquals("Santiago", usuario.getComunaNombre());
        }

        @Test
        void comunaNullRetornaNull() {

            DireccionModel direccion = new DireccionModel();

            UsuarioModel usuario = new UsuarioModel();
            usuario.setDireccion(direccion);

            assertNull(usuario.getComunaId());
            assertNull(usuario.getComunaNombre());
        }
    }

    @Nested
    @DisplayName("Region")
    class Region {

        @Test
        void debeRetornarDatosRegion() {

            RegionModel region = new RegionModel();
            region.setId(13L);
            region.setNombre("Metropolitana");

            ComunaModel comuna = new ComunaModel();
            comuna.setRegion(region);

            DireccionModel direccion = new DireccionModel();
            direccion.setComuna(comuna);

            UsuarioModel usuario = new UsuarioModel();
            usuario.setDireccion(direccion);

            assertEquals(13L, usuario.getRegionId());
            assertEquals("Metropolitana", usuario.getRegionNombre());
        }

        @Test
        void regionNullRetornaNull() {

            ComunaModel comuna = new ComunaModel();

            DireccionModel direccion = new DireccionModel();
            direccion.setComuna(comuna);

            UsuarioModel usuario = new UsuarioModel();
            usuario.setDireccion(direccion);

            assertNull(usuario.getRegionId());
            assertNull(usuario.getRegionNombre());
        }
    }

    @Nested
    @DisplayName("Getters y Setters")
    class GettersSetters {

        @Test
        void debeAsignarPropiedades() {

            RolModel rol = new RolModel();

            UsuarioModel usuario = new UsuarioModel();

            usuario.setId(1L);
            usuario.setNombre("Juan");
            usuario.setApellidos("Pérez");
            usuario.setCorreo("juan@test.cl");
            usuario.setTelefono(123456789L);
            usuario.setFotoPerfil("foto.jpg");
            usuario.setPassword("12345678");
            usuario.setRol(rol);

            assertEquals(1L, usuario.getId());
            assertEquals("Juan", usuario.getNombre());
            assertEquals("Pérez", usuario.getApellidos());
            assertEquals("juan@test.cl", usuario.getCorreo());
            assertEquals(123456789L, usuario.getTelefono());
            assertEquals("foto.jpg", usuario.getFotoPerfil());
            assertEquals("12345678", usuario.getPassword());
            assertEquals(rol, usuario.getRol());
        }
    }

    @Nested
    @DisplayName("Equals y ToString")
    class EqualsToString {

        @Test
        void objetosConMismoContenidoSonIguales() {

            UsuarioModel u1 = new UsuarioModel();
            u1.setId(1L);

            UsuarioModel u2 = new UsuarioModel();
            u2.setId(1L);

            assertEquals(u1, u2);
            assertEquals(u1.hashCode(), u2.hashCode());
        }

        @Test
        void toStringNoDebeSerNull() {

            UsuarioModel usuario = new UsuarioModel();

            assertNotNull(usuario.toString());
        }
    }
}