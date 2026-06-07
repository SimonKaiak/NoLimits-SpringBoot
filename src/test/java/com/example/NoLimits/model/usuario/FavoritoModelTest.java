package com.example.NoLimits.model.usuario;

import com.example.NoLimits.Multimedia.model.usuario.FavoritoModel;
import com.example.NoLimits.Multimedia.model.usuario.UsuarioModel;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("FavoritoModel Tests")
class FavoritoModelTest {

    @Nested
    @DisplayName("Constructor completo")
    class ConstructorCompleto {

        @Test
        @DisplayName("debe crear favorito correctamente")
        void debeCrearFavoritoCorrectamente() {

            UsuarioModel usuario = new UsuarioModel();

            FavoritoModel favorito = new FavoritoModel(
                    1L,
                    usuario,
                    "123",
                    "Spider-Man",
                    "PELICULA",
                    "poster.jpg",
                    "TMDB"
            );

            assertEquals(1L, favorito.getId());
            assertEquals(usuario, favorito.getUsuario());
            assertEquals("123", favorito.getObraId());
            assertEquals("Spider-Man", favorito.getTitulo());
            assertEquals("PELICULA", favorito.getTipo());
            assertEquals("poster.jpg", favorito.getPoster());
            assertEquals("TMDB", favorito.getSource());
        }
    }

    @Nested
    @DisplayName("Getters y Setters")
    class GettersSetters {

        @Test
        @DisplayName("debe asignar y obtener propiedades")
        void debeAsignarYObtenerPropiedades() {

            UsuarioModel usuario = new UsuarioModel();

            FavoritoModel favorito = new FavoritoModel();

            favorito.setId(10L);
            favorito.setUsuario(usuario);
            favorito.setObraId("999");
            favorito.setTitulo("Batman");
            favorito.setTipo("COMIC");
            favorito.setPoster("batman.jpg");
            favorito.setSource("DC");

            assertEquals(10L, favorito.getId());
            assertEquals(usuario, favorito.getUsuario());
            assertEquals("999", favorito.getObraId());
            assertEquals("Batman", favorito.getTitulo());
            assertEquals("COMIC", favorito.getTipo());
            assertEquals("batman.jpg", favorito.getPoster());
            assertEquals("DC", favorito.getSource());
        }
    }

    @Nested
    @DisplayName("Constructor vacío")
    class ConstructorVacio {

        @Test
        @DisplayName("debe crear objeto vacío")
        void debeCrearObjetoVacio() {

            FavoritoModel favorito = new FavoritoModel();

            assertNull(favorito.getId());
            assertNull(favorito.getUsuario());
            assertNull(favorito.getObraId());
            assertNull(favorito.getTitulo());
            assertNull(favorito.getTipo());
            assertNull(favorito.getPoster());
            assertNull(favorito.getSource());
        }
    }

    @Test
    @DisplayName("toString no debe ser null")
    void toStringNoDebeSerNull() {

        FavoritoModel favorito = new FavoritoModel();

        assertNotNull(favorito.toString());
    }

    @Test
    @DisplayName("objetos distintos pueden almacenar mismos datos")
    void objetosDistintosConMismosDatos() {

        FavoritoModel f1 = new FavoritoModel();
        f1.setId(1L);

        FavoritoModel f2 = new FavoritoModel();
        f2.setId(1L);

        assertEquals(f1.getId(), f2.getId());
    }
}