package com.example.NoLimits.Multimedia.dto.ubicacion.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Datos necesarios para crear o actualizar una comuna.")
public class ComunaRequestDTO {

    @Schema(example = "Santiago", description = "Nombre de la comuna")
    private String nombre;

    @Schema(example = "13", description = "ID de la región a la que pertenece la comuna")
    private Long regionId;
}