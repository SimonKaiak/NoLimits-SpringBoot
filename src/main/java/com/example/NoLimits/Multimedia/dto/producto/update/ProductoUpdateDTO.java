package com.example.NoLimits.Multimedia.dto.producto.update;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "DTO para actualizar un producto existente.")
public class ProductoUpdateDTO {

    @Schema(example = "Spider-Man Remaster")
    private String nombre;

    @Schema(example = "14990")
    private Double precio;

    @Schema(description = "Nuevo ID del tipo de producto", example = "2")
    private Long tipoProductoId;

    @Schema(description = "Nuevo ID de clasificación", example = "3")
    private Long clasificacionId;

    @Schema(description = "Nuevo ID del estado", example = "1")
    private Long estadoId;

    @Schema(
            description = "Rutas/URLs de las imágenes del producto (si se envía, reemplaza las actuales).", 
            example = "[\"https://.../img1.webp\", \"https://.../img2.webp\"]"
    )
    private List<String> imagenesRutas;

    private String urlCompra;
    private String labelCompra;

    // ==================== SAGAS ====================

    @Schema(
            description = "Nuevo nombre de la saga (si deseas cambiarla).",
            example = "El Señor de los Anillos"
    )
    private String saga;

    @Schema(
            description = "Nueva ruta/URL de la portada representativa de la saga.",
            example = "/assets/img/sagas/lotrSaga.webp"
    )
    private String portadaSaga;

    // ==================== Relaciones N:M ====================

    private List<Long> plataformasIds;
    private List<Long> generosIds;
    private List<Long> empresasIds;
    private List<Long> desarrolladoresIds;
}