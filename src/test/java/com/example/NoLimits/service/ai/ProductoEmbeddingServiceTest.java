package com.example.NoLimits.service.ai;

import com.example.NoLimits.Multimedia.service.ai.EmbeddingService;
import com.example.NoLimits.Multimedia.service.ai.ProductoEmbeddingService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class ProductoEmbeddingServiceTest {

    private final JdbcTemplate jdbcTemplate = mock(JdbcTemplate.class);
    private final EmbeddingService embeddingService = mock(EmbeddingService.class);

    private final ProductoEmbeddingService service =
            new ProductoEmbeddingService(jdbcTemplate, embeddingService);

    @Nested
    @DisplayName("Unitario - guardarEmbeddingProducto")
    class GuardarEmbeddingProducto {

        @Test
        @DisplayName("genera embedding y guarda el vector en base de datos")
        void generaEmbeddingYGuardaVector() {
            when(embeddingService.generarEmbedding("contenido demo"))
                    .thenReturn(List.of(0.1f, 0.2f, 0.3f));

            service.guardarEmbeddingProducto(10L, "contenido demo");

            verify(embeddingService).generarEmbedding("contenido demo");

            verify(jdbcTemplate).update(
                    contains("INSERT INTO producto_embeddings"),
                    eq(10L),
                    eq("contenido demo"),
                    eq("[0.1, 0.2, 0.3]")
            );
        }
    }

    @Nested
    @DisplayName("Unitario - buscarSimilares")
    class BuscarSimilares {

        @Test
        @DisplayName("usa límite por defecto 5 cuando no se envía límite")
        void usaLimitePorDefectoCinco() {
            when(embeddingService.generarEmbedding("juegos de acción"))
                    .thenReturn(List.of(0.5f, 0.6f));

            when(jdbcTemplate.queryForList(
                    contains("SELECT contenido"),
                    eq(String.class),
                    eq("[0.5, 0.6]"),
                    eq(5)
            )).thenReturn(List.of("Juego A", "Juego B"));

            List<String> resultado = service.buscarSimilares("juegos de acción");

            assertEquals(List.of("Juego A", "Juego B"), resultado);

            verify(jdbcTemplate).queryForList(
                    contains("SELECT contenido"),
                    eq(String.class),
                    eq("[0.5, 0.6]"),
                    eq(5)
            );
        }

        @Test
        @DisplayName("usa límite personalizado cuando se envía limit")
        void usaLimitePersonalizado() {
            when(embeddingService.generarEmbedding("aventura"))
                    .thenReturn(List.of(0.7f, 0.8f));

            when(jdbcTemplate.queryForList(
                    contains("SELECT contenido"),
                    eq(String.class),
                    eq("[0.7, 0.8]"),
                    eq(3)
            )).thenReturn(List.of("Juego C"));

            List<String> resultado = service.buscarSimilares("aventura", 3);

            assertEquals(List.of("Juego C"), resultado);

            verify(jdbcTemplate).queryForList(
                    contains("SELECT contenido"),
                    eq(String.class),
                    eq("[0.7, 0.8]"),
                    eq(3)
            );
        }
    }

    @Nested
    @DisplayName("Unitario - indexarTodosLosProductos")
    class IndexarTodosLosProductos {

        @Test
        @DisplayName("retorna 0 cuando no hay productos para indexar")
        void retornaCeroCuandoNoHayProductos() {
            when(jdbcTemplate.query(anyString(), any(ResultSetExtractor.class)))
                    .thenReturn(List.of());

            int resultado = service.indexarTodosLosProductos();

            assertEquals(0, resultado);
        }

        @Test
        @DisplayName("indexa productos y retorna cantidad procesada")
        void indexaProductosYRetornaCantidadProcesada() throws Exception {
            ResultSet rs = mock(ResultSet.class);

            when(rs.next()).thenReturn(true, false);
            when(rs.getLong("id")).thenReturn(10L);
            when(rs.getString("nombre")).thenReturn("Counter-Strike 2");
            when(rs.getString("tipo_producto")).thenReturn("Videojuego");
            when(rs.getString("clasificacion")).thenReturn("Mayores de 18");
            when(rs.getString("estado")).thenReturn("Disponible");
            when(rs.getObject("precio")).thenReturn(0);
            when(rs.getString("saga")).thenReturn("Counter-Strike");
            when(rs.getString("generos")).thenReturn("Acción");
            when(rs.getString("empresas")).thenReturn("Valve");
            when(rs.getString("plataformas")).thenReturn("PC");
            when(rs.getString("desarrolladores")).thenReturn("Valve");

            when(embeddingService.generarEmbedding(anyString()))
                    .thenReturn(List.of(0.1f, 0.2f));

            when(jdbcTemplate.query(anyString(), any(ResultSetExtractor.class)))
                    .thenAnswer(invocation -> {
                        ResultSetExtractor<List<Integer>> extractor = invocation.getArgument(1);
                        return extractor.extractData(rs);
                    });

            int resultado = service.indexarTodosLosProductos();

            assertEquals(1, resultado);

            verify(embeddingService).generarEmbedding(contains("Counter-Strike 2"));

            verify(jdbcTemplate).update(
                    contains("INSERT INTO producto_embeddings"),
                    eq(10L),
                    contains("Counter-Strike 2"),
                    eq("[0.1, 0.2]")
            );
        }
    }
}