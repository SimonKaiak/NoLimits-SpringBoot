package com.example.NoLimits.Multimedia.service.ai;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoEmbeddingService {

    private final JdbcTemplate jdbcTemplate;
    private final EmbeddingService embeddingService;

    public ProductoEmbeddingService(JdbcTemplate jdbcTemplate, EmbeddingService embeddingService) {
        this.jdbcTemplate = jdbcTemplate;
        this.embeddingService = embeddingService;
    }

    public void guardarEmbeddingProducto(Long productoId, String contenido) {

        List<Float> embedding = embeddingService.generarEmbedding(contenido);
        String vector = embedding.toString();

        String sql = """
                INSERT INTO producto_embeddings (producto_id, contenido, embedding)
                VALUES (?, ?, ?::vector)
                ON CONFLICT (producto_id)
                DO UPDATE SET
                    contenido = EXCLUDED.contenido,
                    embedding = EXCLUDED.embedding,
                    fecha_creacion = CURRENT_TIMESTAMP
                """;

        jdbcTemplate.update(sql, productoId, contenido, vector);
    }

    public List<String> buscarSimilares(String pregunta) {

        List<Float> embedding = embeddingService.generarEmbedding(pregunta);

        String vector = embedding.toString();

        String sql = """
                SELECT contenido
                FROM producto_embeddings
                ORDER BY embedding <=> ?::vector
                LIMIT 3
                """;

        return jdbcTemplate.queryForList(sql, String.class, vector);
    }

    public int indexarTodosLosProductos() {

        String sqlProductos = """
                SELECT 
                    p.id,
                    p.nombre,
                    p.precio,
                    p.saga,
                    p.sinopsis,
                    tp.nombre AS tipo_producto,
                    c.nombre AS clasificacion,
                    e.nombre AS estado
                FROM productos p
                LEFT JOIN tipo_productos tp ON tp.id = p.tipo_producto_id
                LEFT JOIN clasificaciones c ON c.id = p.clasificacion_id
                LEFT JOIN estados e ON e.id = p.estado_id
                """;

        List<Integer> resultado = jdbcTemplate.query(sqlProductos, rs -> {
            int contador = 0;

            while (rs.next()) {
                Long id = rs.getLong("id");

                String contenido = """
                        Nombre: %s
                        Tipo: %s
                        Clasificación: %s
                        Estado: %s
                        Precio: %s
                        Saga: %s
                        Sinopsis: %s
                        """.formatted(
                        rs.getString("nombre"),
                        rs.getString("tipo_producto"),
                        rs.getString("clasificacion"),
                        rs.getString("estado"),
                        rs.getObject("precio"),
                        rs.getString("saga"),
                        rs.getString("sinopsis")
                );

                guardarEmbeddingProducto(id, contenido);
                contador++;
            }

            return List.of(contador);
        });

        return resultado.isEmpty() ? 0 : resultado.get(0);
    }
}