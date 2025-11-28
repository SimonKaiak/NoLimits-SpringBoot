package com.example.NoLimits.Multimedia.repository.producto;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.NoLimits.Multimedia.model.producto.ImagenesModel;

@Repository
public interface ImagenesRepository extends JpaRepository<ImagenesModel, Long> {

    // ====== Resumen (id, ruta, alt, productoId) ======
    @Query("""
        SELECT i.id, i.ruta, i.altText, i.producto.id
        FROM ImagenesModel i
    """)
    List<Object[]> obtenerImagenesResumen();

    // ====== Búsquedas por producto ======
    List<ImagenesModel> findByProducto_Id(Long productoId);

    // Elimina todas las imágenes de un producto y devuelve cuántas se borraron
    long deleteByProducto_Id(Long productoId);

    // ====== Búsquedas / validaciones extra ======
    List<ImagenesModel> findByRutaContainingIgnoreCase(String ruta);

    boolean existsByProducto_Id(Long productoId);

    boolean existsByRuta(String ruta);
}