package com.example.NoLimits.Multimedia.dto.catalogos.update;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO para actualizar la relación Producto - Desarrollador.")
public class DesarrolladoresUpdateDTO {

    @Schema(
        description = "ID del producto asociado",
        example = "10"
    )
    private Long productoId;

    @Schema(
        description = "ID del desarrollador asociado",
        example = "3"
    )
    private Long desarrolladorId;
}