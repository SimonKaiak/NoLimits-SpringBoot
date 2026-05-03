// CAMBIOS REALIZADOS:
//   1. findAllFull()         → MANTENIDA pero marcada claramente como PROHIBIDA para listados
//   2. obtenerProductosResumen() → EXTENDIDA con imagenPortada (primera imagen)
//   3. NUEVA: obtenerResumenPaginado(Pageable)  → para GET /productos/paginacion
//   4. NUEVA: obtenerResumenPorTipo(Long, Pageable)
//   5. NUEVA: obtenerResumenPorEstado(Long, Pageable)
//   6. NUEVA: obtenerResumenPorSaga(String, Pageable)
//   7. NUEVA: obtenerResumenPorNombre(String, Pageable)
//   8. NUEVA: findIdsConAppId()  → reemplaza findAllFull() solo para obtener IDs de Steam

package com.example.NoLimits.Multimedia.repository.producto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.NoLimits.Multimedia.model.producto.ProductoModel;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<ProductoModel, Long> {

    // =========================================================
    // RESUMEN LIVIANO — USAR PARA TODOS LOS LISTADOS
    // Incluye imagenPortada (primera imagen) para mostrar en tarjetas
    // =========================================================

    /**
     * Resumen completo sin paginación.
     * Úsalo solo cuando necesites TODA la lista de una vez
     * (ej: /productos/resumen para el admin).
     * Para el frontend de catálogo, preferir obtenerResumenPaginado().
     */
    @Query("""
        SELECT p.id, p.nombre, p.precio, tp.nombre, e.nombre, p.saga, p.portadaSaga,
               (SELECT img.ruta FROM ImagenesModel img WHERE img.producto = p ORDER BY img.id ASC LIMIT 1)
        FROM ProductoModel p
        JOIN p.tipoProducto tp
        JOIN p.estado e
    """)
    List<Object[]> obtenerProductosResumen();

    /**
     * Resumen paginado — USAR PARA GET /productos/paginacion
     * Solo trae campos básicos + primera imagen.
     * Sin JOINs pesados. Sin N+1.
     */
    @Query(value = """
        SELECT p.id, p.nombre, p.precio, tp.nombre, e.nombre, p.saga, p.portadaSaga,
               (SELECT img.ruta FROM ImagenesModel img WHERE img.producto = p ORDER BY img.id ASC LIMIT 1)
        FROM ProductoModel p
        JOIN p.tipoProducto tp
        JOIN p.estado e
    """,
    countQuery = "SELECT COUNT(p) FROM ProductoModel p")
    Page<Object[]> obtenerResumenPaginado(Pageable pageable);

    /**
     * Resumen paginado filtrado por tipo de producto.
     * USAR PARA GET /productos/tipo/{id}?page=&size=
     */
    @Query(value = """
        SELECT p.id, p.nombre, p.precio, tp.nombre, e.nombre, p.saga, p.portadaSaga,
               (SELECT img.ruta FROM ImagenesModel img WHERE img.producto = p ORDER BY img.id ASC LIMIT 1)
        FROM ProductoModel p
        JOIN p.tipoProducto tp
        JOIN p.estado e
        WHERE tp.id = :tipoId
    """,
    countQuery = "SELECT COUNT(p) FROM ProductoModel p WHERE p.tipoProducto.id = :tipoId")
    Page<Object[]> obtenerResumenPorTipo(@Param("tipoId") Long tipoId, Pageable pageable);

    /**
     * Resumen paginado filtrado por estado.
     * USAR PARA GET /productos/estado/{id}?page=&size=
     */
    @Query(value = """
        SELECT p.id, p.nombre, p.precio, tp.nombre, e.nombre, p.saga, p.portadaSaga,
               (SELECT img.ruta FROM ImagenesModel img WHERE img.producto = p ORDER BY img.id ASC LIMIT 1)
        FROM ProductoModel p
        JOIN p.tipoProducto tp
        JOIN p.estado e
        WHERE e.id = :estadoId
    """,
    countQuery = "SELECT COUNT(p) FROM ProductoModel p WHERE p.estado.id = :estadoId")
    Page<Object[]> obtenerResumenPorEstado(@Param("estadoId") Long estadoId, Pageable pageable);

    /**
     * Resumen paginado filtrado por saga (case-insensitive).
     * USAR PARA GET /productos/sagas/{saga}?page=&size=
     */
    @Query(value = """
        SELECT p.id, p.nombre, p.precio, tp.nombre, e.nombre, p.saga, p.portadaSaga,
               (SELECT img.ruta FROM ImagenesModel img WHERE img.producto = p ORDER BY img.id ASC LIMIT 1)
        FROM ProductoModel p
        JOIN p.tipoProducto tp
        JOIN p.estado e
        WHERE LOWER(p.saga) = LOWER(:saga)
    """,
    countQuery = "SELECT COUNT(p) FROM ProductoModel p WHERE LOWER(p.saga) = LOWER(:saga)")
    Page<Object[]> obtenerResumenPorSaga(@Param("saga") String saga, Pageable pageable);

    /**
     * Resumen paginado filtrado por nombre (contiene, case-insensitive).
     * USAR PARA GET /productos/buscar?nombre=&page=&size=
     */
    @Query(value = """
        SELECT p.id, p.nombre, p.precio, tp.nombre, e.nombre, p.saga, p.portadaSaga,
               (SELECT img.ruta FROM ImagenesModel img WHERE img.producto = p ORDER BY img.id ASC LIMIT 1)
        FROM ProductoModel p
        JOIN p.tipoProducto tp
        JOIN p.estado e
        WHERE LOWER(p.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))
    """,
    countQuery = "SELECT COUNT(p) FROM ProductoModel p WHERE LOWER(p.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    Page<Object[]> obtenerResumenPorNombre(@Param("nombre") String nombre, Pageable pageable);

    // =========================================================
    // DETALLE COMPLETO — SOLO PARA GET /productos/{id}
    // Nunca usar en listados
    // =========================================================

    @Query("""
        SELECT DISTINCT p FROM ProductoModel p
        LEFT JOIN FETCH p.imagenes
        LEFT JOIN FETCH p.plataformas pp
        LEFT JOIN FETCH pp.plataforma
        LEFT JOIN FETCH p.generos gg
        LEFT JOIN FETCH gg.genero
        LEFT JOIN FETCH p.empresas ee
        LEFT JOIN FETCH ee.empresa
        LEFT JOIN FETCH p.desarrolladores dd
        LEFT JOIN FETCH dd.desarrollador
        LEFT JOIN FETCH p.linksCompra lc
        LEFT JOIN FETCH lc.plataforma
        WHERE p.id = :id
    """)
    Optional<ProductoModel> findByIdFull(@Param("id") Long id);

    // =========================================================
    // SOLO PARA findAllWithImagenes — uso interno (no listados masivos)
    // =========================================================

    @Query("""
        SELECT DISTINCT p FROM ProductoModel p
        LEFT JOIN FETCH p.imagenes
        WHERE p.id = :id
    """)
    Optional<ProductoModel> findByIdWithImagenes(@Param("id") Long id);

    // =========================================================
    // STEAM — obtener IDs con appId SIN cargar entidades completas
    // =========================================================

    /**
     * Devuelve solo los IDs de productos que tienen al menos un link con appId de Steam.
     * Reemplaza el uso de findAllFull() solo para filtrar IDs.
     */
    @Query("""
        SELECT DISTINCT p.id FROM ProductoModel p
        JOIN p.linksCompra lc
        WHERE lc.appId IS NOT NULL AND lc.appId <> ''
    """)
    List<Long> findIdsConAppId();

    // =========================================================
    // EXISTENCIA
    // =========================================================
    boolean existsByNombre(String nombre);
    boolean existsByNombreIgnoreCase(String nombre);
    boolean existsByTipoProducto_Id(Long tipoId);
    boolean existsByEstado_Id(Long estadoId);

    // =========================================================
    // BÚSQUEDAS SIMPLES — para uso interno del service
    // =========================================================
    List<ProductoModel> findByNombre(String nombre);
    List<ProductoModel> findByNombreContainingIgnoreCase(String nombre);
    List<ProductoModel> findByClasificacion_Id(Long clasificacionId);
    List<ProductoModel> findByTipoProducto_IdAndEstado_Id(Long tipoProductoId, Long estadoId);

    // =========================================================
    // SAGAS — queries livianas (solo strings, no entidades)
    // =========================================================

    @Query("""
        SELECT DISTINCT p.saga
        FROM ProductoModel p
        WHERE p.saga IS NOT NULL AND p.saga <> ''
        ORDER BY p.saga ASC
    """)
    List<String> findDistinctSagas();

    @Query("""
        SELECT DISTINCT p.saga
        FROM ProductoModel p
        WHERE p.saga IS NOT NULL
          AND p.saga <> ''
          AND p.tipoProducto.id = :tipoProductoId
    """)
    List<String> findDistinctSagasByTipoProductoId(Long tipoProductoId);

    @Query("""
        SELECT p.saga, MAX(p.portadaSaga)
        FROM ProductoModel p
        WHERE p.saga IS NOT NULL
        AND p.saga <> ''
        GROUP BY p.saga
        ORDER BY p.saga ASC
    """)
    List<Object[]> findDistinctSagasWithPortada();

    // =========================================================
    // PROHIBIDO EN LISTADOS — Solo para operaciones admin o debugging
    // =========================================================

    /**
     * NO usar en endpoints de listado del frontend.
     * Genera explosión cartesiana de filas.
     * Solo usar en contextos muy acotados (admin, 1 producto puntual ya filtrado).
     */
    @Query("""
        SELECT DISTINCT p FROM ProductoModel p
        LEFT JOIN FETCH p.imagenes
        LEFT JOIN FETCH p.plataformas pp
        LEFT JOIN FETCH pp.plataforma
        LEFT JOIN FETCH p.generos gg
        LEFT JOIN FETCH gg.genero
        LEFT JOIN FETCH p.empresas ee
        LEFT JOIN FETCH ee.empresa
        LEFT JOIN FETCH p.desarrolladores dd
        LEFT JOIN FETCH dd.desarrollador
        LEFT JOIN FETCH p.linksCompra lc
        LEFT JOIN FETCH lc.plataforma
    """)
    List<ProductoModel> findAllFull();

    // =========================================================
    // SAGA COMPLETO — para carrusel de detalle
    // =========================================================

    /**
     * Devuelve los IDs de productos de una saga, ordenados por id ASC.
     * Usar junto a findByIdFull() para cargar el detalle completo de cada producto.
     */
    @Query("""
        SELECT p.id FROM ProductoModel p
        WHERE LOWER(p.saga) = LOWER(:saga)
        ORDER BY p.id ASC
    """)
    List<Long> findIdsBySagaIgnoreCase(@Param("saga") String saga);
}