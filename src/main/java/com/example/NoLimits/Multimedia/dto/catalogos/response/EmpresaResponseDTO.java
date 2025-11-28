package com.example.NoLimits.Multimedia.dto.catalogos.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO de salida que representa una empresa registrada.")
public class EmpresaResponseDTO {

    @Schema(description = "ID de la empresa", example = "1")
    private Long id;

    @Schema(description = "Nombre de la empresa", example = "Sony Pictures")
    private String nombre;

    @Schema(description = "Indica si la empresa está activa", example = "true")
    private Boolean activo;
}