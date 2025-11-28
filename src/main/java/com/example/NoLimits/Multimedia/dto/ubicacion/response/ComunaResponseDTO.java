package com.example.NoLimits.Multimedia.dto.ubicacion.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Datos de salida de una comuna registrada.")
public class ComunaResponseDTO {

    @Schema(example = "13101", description = "ID único de la comuna")
    private Long id;

    @Schema(example = "Santiago", description = "Nombre de la comuna")
    private String nombre;

    @Schema(example = "13", description = "ID de la región a la que pertenece la comuna")
    private Long regionId;

    @Schema(example = "Región Metropolitana de Santiago", description = "Nombre de la región a la que pertenece la comuna")
    private String regionNombre;
}