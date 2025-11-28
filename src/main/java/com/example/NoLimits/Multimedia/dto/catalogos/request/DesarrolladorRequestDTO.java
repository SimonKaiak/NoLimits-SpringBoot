package com.example.NoLimits.Multimedia.dto.catalogos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "DTO para crear o actualizar un desarrollador.")
public class DesarrolladorRequestDTO {

    @NotBlank(message = "El nombre del desarrollador es obligatorio.")
    @Schema(description = "Nombre del desarrollador/estudio", example = "Insomniac Games")
    private String nombre;

    @Schema(description = "Indica si el desarrollador está activo", example = "true")
    private Boolean activo;
}