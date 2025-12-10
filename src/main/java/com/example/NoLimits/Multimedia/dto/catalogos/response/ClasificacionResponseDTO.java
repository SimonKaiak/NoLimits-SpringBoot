package com.example.NoLimits.Multimedia.dto.catalogos.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO de salida que representa una clasificación registrada.")
public class ClasificacionResponseDTO {

    @Schema(description = "ID de la clasificación", example = "1")
    private Long id;

    @Schema(description = "Nombre corto de la clasificación", example = "T")
    private String nombre;

    @Schema(description = "Descripción de la clasificación", example = "Contenido apto para adolescentes.")
    private String descripcion;

    @Schema(description = "Indica si la clasificación está activa", example = "true")
    private Boolean activo;
}