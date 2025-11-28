package com.example.NoLimits.Multimedia.dto.ubicacion.update;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO para actualizar una comuna.")
public class ComunaUpdateDTO {

    @Schema(
        description = "Nombre de la comuna",
        example = "Santiago Centro"
    )
    private String nombre;

    @Schema(
        description = "ID de la región a la que pertenece la comuna",
        example = "13"
    )
    private Long regionId;
}