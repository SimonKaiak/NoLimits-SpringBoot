package com.example.NoLimits.Multimedia.dto.producto.update;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * DTO para actualizar una imagen de producto.
 * Compatible con PUT y PATCH.
 */
@Data
@Schema(description = "DTO para actualizar una imagen asociada a un producto.")
public class ImagenesUpdateDTO {

    @Schema(
        description = "Ruta o URL de la imagen",
        example = "/assets/img/Peliculas/nueva-imagen.webp"
    )
    private String ruta;

    @Schema(
        description = "Texto alternativo de la imagen",
        example = "Portada alternativa del producto"
    )
    private String altText;

    @Schema(
        description = "ID del producto al que pertenece la imagen",
        example = "10"
    )
    private Long productoId;
}