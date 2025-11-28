package com.example.NoLimits.Multimedia.dto.catalogos.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO de salida que representa un género registrado.")
public class GeneroResponseDTO {

    @Schema(description = "ID del género", example = "1")
    private Long id;

    @Schema(description = "Nombre del género", example = "Acción")
    private String nombre;
}