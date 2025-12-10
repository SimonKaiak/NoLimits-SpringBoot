package com.example.NoLimits.Multimedia.dto.catalogos.update;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * DTO para actualizar un Tipo de Producto.
 * Sirve tanto para operaciones PUT como PATCH.
 */
@Data
@Schema(description = "DTO para actualizar un tipo de producto.")
public class TipoProductoUpdateDTO {

    @Schema(
        description = "Nombre del tipo de producto",
        example = "Videojuego"
    )
    private String nombre;

    @Schema(
        description = "Descripción del tipo de producto",
        example = "Categoría para videojuegos en distintas plataformas"
    )
    private String descripcion;

    @Schema(
        description = "Indica si el tipo de producto está activo",
        example = "true"
    )
    private Boolean activo;
}