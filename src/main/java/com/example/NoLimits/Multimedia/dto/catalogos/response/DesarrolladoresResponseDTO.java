package com.example.NoLimits.Multimedia.dto.catalogos.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO que representa la relación Producto-Desarrollador.")
public class DesarrolladoresResponseDTO {

    @Schema(description = "ID de la relación", example = "5")
    private Long id;

    @Schema(description = "ID del producto", example = "10")
    private Long productoId;

    @Schema(description = "ID del desarrollador", example = "1")
    private Long desarrolladorId;

    @Schema(description = "Nombre del desarrollador", example = "Insomniac Games")
    private String desarrolladorNombre;
}