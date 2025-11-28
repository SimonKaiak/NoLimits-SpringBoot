package com.example.NoLimits.Multimedia.dto.catalogos.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO de salida que representa una plataforma registrada.")
public class PlataformaResponseDTO {

    @Schema(description = "ID de la plataforma", example = "1")
    private Long id;

    @Schema(description = "Nombre de la plataforma", example = "PC")
    private String nombre;
}