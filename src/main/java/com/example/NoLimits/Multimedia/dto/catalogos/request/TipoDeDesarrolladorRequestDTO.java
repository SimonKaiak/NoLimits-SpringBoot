package com.example.NoLimits.Multimedia.dto.catalogos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "DTO para crear o actualizar un tipo de desarrollador.")
public class TipoDeDesarrolladorRequestDTO {

    @NotBlank(message = "El nombre del tipo de desarrollador es obligatorio.")
    @Schema(description = "Nombre del tipo de desarrollador", example = "Estudio")
    private String nombre;
}