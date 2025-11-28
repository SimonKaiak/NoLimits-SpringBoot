package com.example.NoLimits.Multimedia.dto.catalogos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "DTO para crear o actualizar un tipo de producto.")
public class TipoProductoRequestDTO {

    @NotBlank(message = "El nombre es obligatorio.")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres.")
    @Schema(description = "Nombre del tipo de producto", example = "Película")
    private String nombre;

    @Schema(description = "Descripción del tipo de producto", example = "Categoría general para clasificar productos")
    private String descripcion;

    @NotNull(message = "Debe indicarse si el tipo está activo.")
    @Schema(description = "Indica si el tipo está activo", example = "true")
    private Boolean activo;
}