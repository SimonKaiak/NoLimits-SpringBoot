package com.example.NoLimits.Multimedia.dto.catalogos.update;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO para actualizar parcialmente una clasificación de producto.")
public class ClasificacionUpdateDTO {

    @Schema(description = "Nombre corto de la clasificación", example = "T")
    private String nombre;

    @Schema(description = "Descripción de la clasificación", example = "Contenido apto para adolescentes.")
    private String descripcion;

    @Schema(description = "Indica si la clasificación está activa", example = "true")
    private Boolean activo;
}