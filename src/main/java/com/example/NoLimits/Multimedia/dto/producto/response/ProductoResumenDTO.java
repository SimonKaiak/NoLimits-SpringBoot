//   ¿Para qué sirve?
//   DTO liviano para listar productos en grilla/carrusel.
//   Solo trae los campos que el frontend necesita para mostrar la tarjeta del producto.
//   NO trae relaciones pesadas (géneros, plataformas, desarrolladores, empresas).
//
// ¿Cuándo usarlo?
//   - GET /productos           (listado general)
//   - GET /productos/paginacion (paginación)
//   - GET /productos/tipo/{id}  (filtro por tipo)
//   - GET /productos/saga/{saga} (filtro por saga)
//   - Cualquier endpoint que liste VARIOS productos a la vez
//
// ¿Cuándo NO usarlo?
//   - GET /productos/{id}  → ahí sí usar ProductoResponseDTO (completo)

package com.example.NoLimits.Multimedia.dto.producto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductoResumenDTO {

    private Long id;
    private String nombre;
    private Double precio;
    private String tipoProductoNombre;  // ej: "PELÍCULA", "VIDEOJUEGO"
    private String estadoNombre;        // ej: "Activo", "Agotado"
    private String saga;                // puede ser null
    private String portadaSaga;         // puede ser null
    private String imagenPortada;       // primera imagen del producto (puede ser null)
}