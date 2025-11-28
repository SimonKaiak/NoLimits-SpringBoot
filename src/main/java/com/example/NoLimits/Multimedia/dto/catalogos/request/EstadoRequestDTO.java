package com.example.NoLimits.Multimedia.dto.catalogos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "DTO para crear o actualizar un estado del sistema.")
public class EstadoRequestDTO {

    @NotBlank(message = "El nombre del estado es obligatorio.")
    @Schema(description = "Nombre del estado", example = "Activo")
    private String nombre;

    @Schema(description = "Descripción del estado", example = "Producto disponible para su compra")
    private String descripcion;

    @NotNull(message = "El estado debe indicar si está activo.")
    @Schema(description = "Indica si el estado está disponible", example = "true")
    private Boolean activo;
}