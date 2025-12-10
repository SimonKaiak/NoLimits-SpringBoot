package com.example.NoLimits.Multimedia.dto.ubicacion.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Datos de salida de una región registrada.")
public class RegionResponseDTO {

    @Schema(example = "13", description = "ID único de la región")
    private Long id;

    @Schema(example = "Región Metropolitana de Santiago", description = "Nombre de la región")
    private String nombre;
}