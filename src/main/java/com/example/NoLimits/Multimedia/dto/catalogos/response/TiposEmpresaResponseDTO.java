package com.example.NoLimits.Multimedia.dto.catalogos.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO que representa la relación Empresa - TipoEmpresa.")
public class TiposEmpresaResponseDTO {

    @Schema(description = "ID de la relación", example = "10")
    private Long id;

    @Schema(description = "ID de la empresa", example = "5")
    private Long empresaId;

    @Schema(description = "ID del tipo de empresa", example = "2")
    private Long tipoEmpresaId;

    @Schema(description = "Nombre del tipo de empresa", example = "Publisher")
    private String tipoEmpresaNombre;
}