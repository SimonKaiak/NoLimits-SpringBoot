package com.example.NoLimits.Multimedia.dto.catalogos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "DTO para crear o actualizar una clasificación de producto.")
public class ClasificacionRequestDTO {

    @NotBlank(message = "El nombre de la clasificación es obligatorio.")
    @Schema(description = "Nombre corto de la clasificación", example = "T")
    private String nombre;

    @Schema(description = "Descripción de la clasificación", example = "Contenido apto para adolescentes.")
    private String descripcion;

    @Schema(description = "Indica si la clasificación está activa", example = "true")
    private Boolean activo;
}