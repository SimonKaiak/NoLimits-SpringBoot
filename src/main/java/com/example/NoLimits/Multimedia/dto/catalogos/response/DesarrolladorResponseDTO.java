package com.example.NoLimits.Multimedia.dto.catalogos.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO de salida que representa un desarrollador registrado.")
public class DesarrolladorResponseDTO {

    @Schema(description = "ID del desarrollador", example = "1")
    private Long id;

    @Schema(description = "Nombre del desarrollador/estudio", example = "Insomniac Games")
    private String nombre;

    @Schema(description = "Indica si el desarrollador está activo", example = "true")
    private Boolean activo;
}