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
                    tp.nombre AS tipo_producto,
                    c.nombre AS clasificacion,
                    e.nombre AS estado,

                    COALESCE(STRING_AGG(DISTINCT g.nombre, ', '), '') AS generos,
                    COALESCE(STRING_AGG(DISTINCT emp.nombre, ', '), '') AS empresas,
                    COALESCE(STRING_AGG(DISTINCT plat.nombre, ', '), '') AS plataformas,
                    COALESCE(STRING_AGG(DISTINCT d.nombre, ', '), '') AS desarrolladores

                FROM productos p
                LEFT JOIN tipo_productos tp ON tp.id = p.tipo_producto_id
                LEFT JOIN clasificaciones c ON c.id = p.clasificacion_id
                LEFT JOIN estados e ON e.id = p.estado_id

                -- GENEROS
                LEFT JOIN generos gp ON gp.producto_id = p.id
                LEFT JOIN genero g ON g.id = gp.genero_id

                -- EMPRESAS
                LEFT JOIN empresas ep ON ep.producto_id = p.id
                LEFT JOIN empresa emp ON emp.id = ep.empresa_id

                -- PLATAFORMAS
                LEFT JOIN plataformas pp ON pp.producto_id = p.id
                LEFT JOIN plataforma plat ON plat.id = pp.plataforma_id

                -- DESARROLLADORES
                LEFT JOIN desarrolladores dp ON dp.producto_id = p.id
                LEFT JOIN desarrollador d ON d.id = dp.desarrollador_id

                GROUP BY 
                    p.id,
                    p.nombre,
                    p.precio,
                    p.saga,
                    tp.nombre,
                    c.nombre,
                    e.nombre
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
                        Genero: %s
                        Empresa: %s
                        Plataforma: %s
                        Desarrollador: %s
                        """.formatted(
                        rs.getString("nombre"),
                        rs.getString("tipo_producto"),
                        rs.getString("clasificacion"),
                        rs.getString("estado"),
                        rs.getObject("precio"),
                        rs.getString("saga"),
                        rs.getString("generos"),
                        rs.getString("empresas"),
                        rs.getString("plataformas"),
                        rs.getString("desarrolladores")
                );

                guardarEmbeddingProducto(id, contenido);
                contador++;
            }

            return List.of(contador);
        });

        return resultado.isEmpty() ? 0 : resultado.get(0);
    }
}