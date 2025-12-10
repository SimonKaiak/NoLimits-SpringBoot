package com.example.NoLimits.Multimedia.dto.catalogos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "DTO para crear o actualizar un género.")
public class GeneroRequestDTO {

    @NotBlank(message = "El nombre del género es obligatorio.")
    @Schema(description = "Nombre del género", example = "Acción")
    private String nombre;
}