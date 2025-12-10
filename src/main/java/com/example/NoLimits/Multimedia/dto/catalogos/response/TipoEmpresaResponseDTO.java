package com.example.NoLimits.Multimedia.dto.catalogos.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO de salida que representa un tipo de empresa.")
public class TipoEmpresaResponseDTO {

    @Schema(description = "ID del tipo de empresa", example = "1")
    private Long id;

    @Schema(description = "Nombre del tipo de empresa", example = "Publisher")
    private String nombre;
}