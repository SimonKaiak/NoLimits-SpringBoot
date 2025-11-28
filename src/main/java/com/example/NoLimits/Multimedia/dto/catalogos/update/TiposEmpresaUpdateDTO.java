package com.example.NoLimits.Multimedia.dto.catalogos.update;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * DTO para actualizar la relación entre Empresa y TipoEmpresa.
 * Aplica para PUT y PATCH.
 */
@Data
@Schema(description = "DTO para actualizar la relación Empresa - TipoEmpresa.")
public class TiposEmpresaUpdateDTO {

    @Schema(
        description = "ID de la empresa asociada",
        example = "5"
    )
    private Long empresaId;

    @Schema(
        description = "ID del tipo de empresa asociado",
        example = "2"
    )
    private Long tipoEmpresaId;
}