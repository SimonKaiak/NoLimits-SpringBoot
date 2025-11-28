package com.example.NoLimits.Multimedia.dto.producto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "DTO para registrar o actualizar una imagen de un producto.")
public class ImagenRequestDTO {

    @NotBlank(message = "La ruta de la imagen es obligatoria.")
    @Schema(
        description = "Ruta o URL de la imagen",
        example = "/assets/img/Peliculas/spiderman.webp"
    )
    private String ruta;

    @Schema(
        description = "Texto alternativo de accesibilidad",
        example = "Spider-Man posando"
    )
    private String altText;

    @NotNull(message = "El producto asociado es obligatorio.")
    @Schema(
        description = "ID del producto al que pertenece la imagen",
        example = "10"
    )
    private Long productoId;
}