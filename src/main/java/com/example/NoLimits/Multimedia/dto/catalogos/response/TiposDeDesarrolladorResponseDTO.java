package com.example.NoLimits.Multimedia.dto.catalogos.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO que representa la relación Desarrollador - TipoDeDesarrollador.")
public class TiposDeDesarrolladorResponseDTO {

    @Schema(description = "ID de la relación", example = "5")
    private Long id;

    @Schema(description = "ID del desarrollador", example = "10")
    private Long desarrolladorId;

    @Schema(description = "ID del tipo de desarrollador", example = "1")
    private Long tipoDeDesarrolladorId;

    @Schema(description = "Nombre del tipo de desarrollador", example = "Estudio")
    private String tipoDeDesarrolladorNombre;
}