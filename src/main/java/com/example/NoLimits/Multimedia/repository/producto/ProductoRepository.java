package com.example.NoLimits.Multimedia.repository.producto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.NoLimits.Multimedia.model.producto.ProductoModel;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<ProductoModel, Long> {

    // =========================================================
    // RESUMEN DE PRODUCTOS
    // Ahora incluye saga y portadaSaga para poder usarlos en el front
    // (ej: carrusel de sagas de películas)
    // =========================================================
    @Query("""
        SELECT p.id, p.nombre, p.precio, tp.nombre, e.nombre, p.saga, p.portadaSaga
        FROM ProductoModel p
        JOIN p.tipoProducto tp
        JOIN p.estado e
    """)
    List<Object[]> obtenerProductosResumen();

    // =========================================================
    // BÚSQUEDAS POR NOMBRE
    // =========================================================
    List<ProductoModel> findByNombre(String nombre);
    List<ProductoModel> findByNombreContainingIgnoreCase(String nombre);

    // =========================================================
    // POR TIPO
    // =========================================================
    List<ProductoModel> findByTipoProducto_Id(Long tipoProductoId);

    // =========================================================
    // POR CLASIFICACIÓN
    // =========================================================
    List<ProductoModel> findByClasificacion_Id(Long clasificacionId);

    // =========================================================
    // POR ESTADO
    // =========================================================
    List<ProductoModel> findByEstado_Id(Long estadoId);

    // =========================================================
    // COMBINADO TIPO + ESTADO (ej: juegos activos de cierto tipo)
    // =========================================================
    List<ProductoModel> findByTipoProducto_IdAndEstado_Id(Long tipoProductoId, Long estadoId);

    // =========================================================
    // EXISTENCIA
    // =========================================================
    boolean existsByNombre(String nombre);
    boolean existsByNombreIgnoreCase(String nombre);
    boolean existsByTipoProducto_Id(Long tipoId);
    boolean existsByEstado_Id(Long estadoId);

    // =========================================================
    // SAGAS
    // =========================================================

    // Productos por saga (ej: todas las pelis de "Spiderman")
    List<ProductoModel> findBySaga(String saga);
    List<ProductoModel> findBySagaIgnoreCase(String saga);

    // Productos por saga y tipo (ej: solo películas de una saga concreta)
    List<ProductoModel> findBySagaAndTipoProducto_Id(String saga, Long tipoProductoId);

    // Listado de nombres de sagas distintos (para armar el carrusel de sagas)
    @Query("""
        SELECT DISTINCT p.saga
        FROM ProductoModel p
        WHERE p.saga IS NOT NULL AND p.saga <> ''
    """)
    List<String> findDistinctSagas();

    // Listado de sagas filtrado por tipo de producto (ej: solo sagas de PELÍCULAS)
    @Query("""
        SELECT DISTINCT p.saga
        FROM ProductoModel p
        WHERE p.saga IS NOT NULL
          AND p.saga <> ''
          AND p.tipoProducto.id = :tipoProductoId
    """)
    List<String> findDistinctSagasByTipoProductoId(Long tipoProductoId);
}