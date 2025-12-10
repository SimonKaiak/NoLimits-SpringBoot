package com.example.NoLimits.Multimedia.dto.catalogos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "DTO para asociar un tipo a un desarrollador.")
public class TiposDeDesarrolladorRequestDTO {

    @NotNull(message = "El ID del desarrollador es obligatorio.")
    @Schema(description = "ID del desarrollador", example = "10")
    private Long desarrolladorId;

    @NotNull(message = "El ID del tipo de desarrollador es obligatorio.")
    @Schema(description = "ID del tipo de desarrollador", example = "1")
    private Long tipoDeDesarrolladorId;
}