package com.example.NoLimits.Multimedia.dto.producto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO simple para plataformas")
public class PlataformaSimpleDTO {

    @Schema(description = "ID de la plataforma", example = "1")
    private Long id;

    @Schema(description = "Nombre de la plataforma", example = "Netflix")
    private String nombre;
}