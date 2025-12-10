package com.example.NoLimits.Multimedia.dto.catalogos.update;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO para actualizar la relación Producto-Género.")
public class GenerosUpdateDTO {

    @Schema(
        description = "ID del producto asociado",
        example = "10"
    )
    private Long productoId;

    @Schema(
        description = "ID del género asociado",
        example = "2"
    )
    private Long generoId;
}