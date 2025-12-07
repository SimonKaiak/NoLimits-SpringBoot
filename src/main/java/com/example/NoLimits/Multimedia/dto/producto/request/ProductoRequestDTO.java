// Ruta: src/main/java/com/example/NoLimits/Multimedia/dto/producto/request/ProductoRequestDTO.java
package com.example.NoLimits.Multimedia.dto.producto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "DTO de entrada para crear/actualizar productos.")
public class ProductoRequestDTO {

    @Schema(description = "Nombre del producto", example = "Spider-Man (2002)")
    private String nombre;

    @Schema(description = "Precio del producto", example = "12990")
    private Double precio;

    @Schema(description = "ID del tipo de producto", example = "2")
    private Long tipoProductoId;

    @Schema(description = "ID de la clasificación", example = "3")
    private Long clasificacionId;

    @Schema(description = "ID del estado", example = "1")
    private Long estadoId;

    /* ====== Sagas ====== */

    @Schema(
            description = "Nombre de la saga a la que pertenece el producto.",
            example = "Minecraft"
    )
    private String saga;

    @Schema(
            description = "Ruta/URL de la portada de la saga.",
            example = "sagas/SagaMinecraft.webp"
    )
    private String portadaSaga;

    /* ====== Relaciones N:M (IDs) ====== */

    @Schema(
            description = "IDs de plataformas asociadas al producto.",
            example = "[1, 2]"
    )
    private List<Long> plataformasIds;

    @Schema(
            description = "IDs de géneros asociados al producto.",
            example = "[3, 4]"
    )
    private List<Long> generosIds;

    @Schema(
            description = "IDs de empresas asociadas al producto.",
            example = "[5]"
    )
    private List<Long> empresasIds;

    @Schema(
            description = "IDs de desarrolladores asociados al producto.",
            example = "[7, 8]"
    )
    private List<Long> desarrolladoresIds;

    /* ====== Imágenes ====== */

    @Schema(
            description = "Rutas/URLs de las imágenes del producto.",
            example = "[\"peliculas/minecraft/PMinecraft.webp\"]"
    )
    private List<String> imagenesRutas;
}