package com.example.NoLimits.Multimedia.dto.catalogos.update;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * DTO para actualizar la relación Producto - Plataforma.
 * Sirve tanto para PUT como para PATCH.
 */
@Data
@Schema(description = "DTO para actualizar la relación entre Producto y Plataforma.")
public class PlataformasUpdateDTO {

    @NotNull(message = "Debe indicar el ID del producto.")
    @Schema(
        description = "ID del producto asociado",
        example = "10",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Long productoId;

    @NotNull(message = "Debe indicar el ID de la plataforma.")
    @Schema(
        description = "ID de la plataforma asociada",
        example = "1",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Long plataformaId;
}