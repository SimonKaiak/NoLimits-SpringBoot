package com.example.NoLimits.Multimedia.dto.catalogos.update;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO para actualizar un desarrollador.")
public class DesarrolladorUpdateDTO {

    @Schema(
        description = "Nombre del desarrollador o estudio",
        example = "Insomniac Games"
    )
    private String nombre;

    @Schema(
        description = "Indica si el desarrollador está activo",
        example = "true"
    )
    private Boolean activo;
}