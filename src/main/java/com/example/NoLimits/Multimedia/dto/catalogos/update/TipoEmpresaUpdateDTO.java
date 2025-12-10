package com.example.NoLimits.Multimedia.dto.catalogos.update;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * DTO para actualizar un Tipo de Empresa.
 * Aplica tanto para PUT como para PATCH.
 */
@Data
@Schema(description = "DTO para actualizar un tipo de empresa.")
public class TipoEmpresaUpdateDTO {

    @NotBlank(message = "El nombre del tipo de empresa es obligatorio.")
    @Schema(
        description = "Nombre del tipo de empresa",
        example = "Distribuidora",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String nombre;
}