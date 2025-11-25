package com.example.NoLimits.Multimedia.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.NoLimits.Multimedia.model.ClasificacionModel;

/*
 Repositorio para la entidad ClasificacionModel.

 Esta interfaz se encarga de toda la comunicación con la base de datos
 relacionada con la tabla "clasificaciones", utilizando Spring Data JPA.

 Al extender JpaRepository se obtienen automáticamente métodos como:
 - save()
 - findAll()
 - findById()
 - deleteById()
 - existsById()
 entre otros.
*/
@Repository
public interface ClasificacionRepository extends JpaRepository<ClasificacionModel, Long> {

    /*
     ===========================
     CONSULTA PERSONALIZADA
     ===========================

     Obtiene un resumen simple de las clasificaciones, devolviendo solo
     datos básicos en lugar de la entidad completa.

     Retorna una lista de Object[], donde cada posición corresponde a:
     [0] -> id
     [1] -> nombre
     [2] -> descripcion
     [3] -> activo

     Este método se usa normalmente para construir vistas de resumen
     o tablas simplificadas.
    */
    @Query("""
        SELECT c.id, c.nombre, c.descripcion, c.activo
        FROM ClasificacionModel c
    """)
    List<Object[]> obtenerClasificacionesResumen();

    /*
     ===========================
     BÚSQUEDAS POR NOMBRE
     ===========================

     Busca clasificaciones cuyo nombre contenga el texto indicado,
     sin distinguir entre mayúsculas y minúsculas.
     Ejemplo: "adul" encontrará "Adultos".
    */
    List<ClasificacionModel> findByNombreContainingIgnoreCase(String nombre);

    /*
     Busca una clasificación cuyo nombre sea exactamente igual
     al indicado, ignorando mayúsculas/minúsculas.
     Retorna Optional para manejar el caso de no existir.
    */
    Optional<ClasificacionModel> findByNombreIgnoreCase(String nombre);

    /*
     ===========================
     FILTROS POR ESTADO
     ===========================

     Obtiene todas las clasificaciones activas.
    */
    List<ClasificacionModel> findByActivoTrue();

    /*
     Obtiene todas las clasificaciones inactivas.
    */
    List<ClasificacionModel> findByActivoFalse();

    /*
     ===========================
     VALIDACIONES DE EXISTENCIA
     ===========================

     Verifica si ya existe una clasificación con un nombre exacto.
    */
    boolean existsByNombre(String nombre);

    /*
     Verifica si ya existe una clasificación con un nombre,
     ignorando mayúsculas y minúsculas.
     Útil para evitar duplicados al crear o editar.
    */
    boolean existsByNombreIgnoreCase(String nombre);
}