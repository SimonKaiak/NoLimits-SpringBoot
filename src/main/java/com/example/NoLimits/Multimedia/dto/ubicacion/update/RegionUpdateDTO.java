package com.example.NoLimits.Multimedia.dto.ubicacion.update;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO para actualizar una región.")
public class RegionUpdateDTO {

    @Schema(description = "Nombre de la región", example = "Región de Valparaíso")
    private String nombre;
}