package com.example.NoLimits.Multimedia.dto.catalogos.update;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * DTO para actualizar un Tipo de Desarrollador.
 * Aplica tanto para PUT como para PATCH.
 */
@Data
@Schema(description = "DTO para actualizar un tipo de desarrollador.")
public class TipoDeDesarrolladorUpdateDTO {

    @NotBlank(message = "El nombre del tipo de desarrollador es obligatorio.")
    @Schema(
        description = "Nombre del tipo de desarrollador",
        example = "Publisher",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String nombre;
}