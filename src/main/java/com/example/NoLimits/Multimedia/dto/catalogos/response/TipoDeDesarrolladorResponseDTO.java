package com.example.NoLimits.Multimedia.dto.catalogos.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO de salida que representa un tipo de desarrollador.")
public class TipoDeDesarrolladorResponseDTO {

    @Schema(description = "ID del tipo de desarrollador", example = "1")
    private Long id;

    @Schema(description = "Nombre del tipo de desarrollador", example = "Estudio")
    private String nombre;
}