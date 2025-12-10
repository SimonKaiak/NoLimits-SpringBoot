package com.example.NoLimits.Multimedia.dto.catalogos.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO de salida que representa un tipo de producto registrado.")
public class TipoProductoResponseDTO {

    @Schema(description = "ID del tipo de producto", example = "1")
    private Long id;

    @Schema(description = "Nombre del tipo de producto", example = "Película")
    private String nombre;

    @Schema(description = "Descripción del tipo de producto", example = "Categoría general para clasificar productos")
    private String descripcion;

    @Schema(description = "Indica si el tipo está activo", example = "true")
    private Boolean activo;
}