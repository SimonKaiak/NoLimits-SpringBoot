package com.example.NoLimits.Multimedia.dto.catalogos.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO que representa la relación Producto-Plataforma.")
public class PlataformasResponseDTO {

    @Schema(description = "ID de la relación", example = "5")
    private Long id;

    @Schema(description = "ID del producto", example = "10")
    private Long productoId;

    @Schema(description = "ID de la plataforma", example = "1")
    private Long plataformaId;

    @Schema(description = "Nombre de la plataforma", example = "PC")
    private String plataformaNombre;
}