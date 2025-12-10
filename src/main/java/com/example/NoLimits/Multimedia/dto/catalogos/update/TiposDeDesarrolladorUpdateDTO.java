package com.example.NoLimits.Multimedia.dto.catalogos.update;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * DTO para actualizar la relación entre Desarrollador y TipoDeDesarrollador.
 * Se usa en operaciones PUT y PATCH.
 */
@Data
@Schema(description = "DTO para actualizar la relación Desarrollador - Tipo de Desarrollador.")
public class TiposDeDesarrolladorUpdateDTO {

    @Schema(
        description = "ID del desarrollador asociado",
        example = "3"
    )
    private Long desarrolladorId;

    @Schema(
        description = "ID del tipo de desarrollador asociado",
        example = "2"
    )
    private Long tipoDeDesarrolladorId;
}