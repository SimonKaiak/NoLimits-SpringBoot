package com.example.NoLimits.Multimedia.dto.producto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

import com.example.NoLimits.Multimedia.dto.producto.request.LinkCompraDTO;

@Data
@Schema(description = "DTO que representa un producto completo para el frontend.")
public class ProductoResponseDTO {

    @Schema(description = "ID del producto", example = "10")
    private Long id;

    @Schema(description = "Nombre del producto", example = "Spider-Man (2002)")
    private String nombre;

    @Schema(description = "Precio del producto", example = "12990")
    private Double precio;

    @Schema(description = "ID del tipo de producto", example = "2")
    private Long tipoProductoId;

    @Schema(description = "Nombre del tipo de producto", example = "PELÍCULA")
    private String tipoProductoNombre;

    @Schema(description = "ID de clasificación", example = "3")
    private Long clasificacionId;

    @Schema(description = "Nombre de la clasificación", example = "TE+7")
    private String clasificacionNombre;

    @Schema(description = "ID del estado", example = "1")
    private Long estadoId;

    @Schema(description = "Nombre del estado", example = "Activo")
    private String estadoNombre;

    // ==================== SAGAS ====================

    @Schema(
            description = "Nombre de la saga a la que pertenece el producto.",
            example = "Spiderman"
    )
    private String saga;

    @Schema(
            description = "Ruta/URL de la portada representativa de la saga.",
            example = "/assets/img/sagas/spidermanSaga.webp"
    )
    private String portadaSaga;

    // ==================== Relaciones N:M (nombres) ====================

    @Schema(description = "Lista de plataformas asociadas al producto.")
    private List<String> plataformas;

    @Schema(description = "Lista de géneros asociados al producto.")
    private List<String> generos;

    @Schema(description = "Lista de empresas asociadas al producto.")
    private List<String> empresas;

    @Schema(description = "Lista de desarrolladores asociados al producto.")
    private List<String> desarrolladores;

    // ==================== Imágenes ====================

    @Schema(
            description = "Rutas/URLs de las imágenes asociadas al producto.",
            example = "[\"/assets/img/peliculas/spiderman1.webp\", \"/assets/img/peliculas/spiderman1-alt.webp\"]"
    )
    private List<String> imagenes;

    // ==================== Links de compra ====================

    @Schema(
            description = "Links de compra por plataforma",
            example = """
            [
              { "plataformaId": 1, "url": "https://store.steampowered.com/app/..." },
              { "plataformaId": 2, "url": "https://www.playstation.com/..." }
            ]
            """
    )
    private List<LinkCompraDTO> linksCompra;
}