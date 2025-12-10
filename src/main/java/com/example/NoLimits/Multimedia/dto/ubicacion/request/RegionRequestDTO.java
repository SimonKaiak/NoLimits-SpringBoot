package com.example.NoLimits.Multimedia.dto.ubicacion.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Datos necesarios para crear o actualizar una región.")
public class RegionRequestDTO {

    @Schema(example = "Región Metropolitana de Santiago", description = "Nombre de la región")
    private String nombre;
}