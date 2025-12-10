package com.example.NoLimits.Multimedia.dto.catalogos.update;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO para actualizar un Género.")
public class GeneroUpdateDTO {

    @Schema(description = "Nombre del género", example = "Aventura")
    private String nombre;
}