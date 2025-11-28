package com.example.NoLimits.Multimedia.dto.producto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO de salida que representa una imagen asociada a un producto.")
public class ImagenResponseDTO {

    @Schema(description = "ID único de la imagen", example = "1")
    private Long id;

    @Schema(description = "Ruta o URL de la imagen", example = "/assets/img/Peliculas/spiderman.webp")
    private String ruta;

    @Schema(description = "Texto alternativo de accesibilidad", example = "Spider-Man posando")
    private String altText;

    @Schema(description = "ID del producto asociado", example = "10")
    private Long productoId;
}