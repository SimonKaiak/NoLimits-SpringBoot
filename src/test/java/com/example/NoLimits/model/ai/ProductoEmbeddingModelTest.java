package com.example.NoLimits.model.ai;

import com.example.NoLimits.Multimedia.model.ai.ProductoEmbeddingModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ProductoEmbeddingModel Tests")
class ProductoEmbeddingModelTest {

    @Nested
    @DisplayName("Constructor completo")
    class ConstructorCompleto {

        @Test
        void debeCrearProductoEmbeddingCorrectamente() {

            LocalDateTime fecha = LocalDateTime.now();

            ProductoEmbeddingModel model =
                    new ProductoEmbeddingModel(
                            1L,
                            10L,
                            "Spider-Man",
                            "Película de superhéroes",
                            "[0.1,0.2,0.3]",
                            "TMDB",
                            fecha
                    );

            assertEquals(1L, model.getId());
            assertEquals(10L, model.getProductoId());
            assertEquals("Spider-Man", model.getTitulo());
            assertEquals("Película de superhéroes", model.getContenido());
            assertEquals("[0.1,0.2,0.3]", model.getEmbedding());
            assertEquals("TMDB", model.getFuente());
            assertEquals(fecha, model.getFechaCreacion());
        }
    }

    @Nested
    @DisplayName("Getters y Setters")
    class GettersSetters {

        @Test
        void debeAsignarYObtenerPropiedades() {

            ProductoEmbeddingModel model = new ProductoEmbeddingModel();

            LocalDateTime fecha = LocalDateTime.now();

            model.setId(2L);
            model.setProductoId(20L);
            model.setTitulo("Batman");
            model.setContenido("Película DC");
            model.setEmbedding("[1,2,3]");
            model.setFuente("IGDB");
            model.setFechaCreacion(fecha);

            assertEquals(2L, model.getId());
            assertEquals(20L, model.getProductoId());
            assertEquals("Batman", model.getTitulo());
            assertEquals("Película DC", model.getContenido());
            assertEquals("[1,2,3]", model.getEmbedding());
            assertEquals("IGDB", model.getFuente());
            assertEquals(fecha, model.getFechaCreacion());
        }
    }

    @Nested
    @DisplayName("Constructor vacío")
    class ConstructorVacio {

        @Test
        void debeCrearObjetoVacio() {

            ProductoEmbeddingModel model =
                    new ProductoEmbeddingModel();

            assertNull(model.getId());
            assertNull(model.getProductoId());
            assertNull(model.getTitulo());
            assertNull(model.getContenido());
            assertNull(model.getEmbedding());
            assertNull(model.getFuente());
            assertNull(model.getFechaCreacion());
        }
    }

    @Nested
    @DisplayName("Equals HashCode y ToString")
    class EqualsHashCodeToString {

        @Test
        void toStringNoDebeSerNull() {

            ProductoEmbeddingModel model =
                    new ProductoEmbeddingModel();

            assertNotNull(model.toString());
        }
    }
}