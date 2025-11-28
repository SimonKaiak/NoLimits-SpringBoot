package com.example.NoLimits.Multimedia.dto.catalogos.update;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO para actualizar una plataforma.")
public class PlataformaUpdateDTO {

    @Schema(
        description = "Nombre de la plataforma",
        example = "PlayStation 5"
    )
    private String nombre;
}