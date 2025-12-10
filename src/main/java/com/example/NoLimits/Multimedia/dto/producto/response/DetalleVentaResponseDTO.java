package com.example.NoLimits.Multimedia.dto.producto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Detalle de producto dentro de una venta registrada.")
public class DetalleVentaResponseDTO {

    @Schema(description = "ID del detalle de venta", example = "1")
    private Long id;

    @Schema(description = "ID del producto vendido", example = "10")
    private Long productoId;

    @Schema(description = "Nombre del producto vendido", example = "Control Xbox Series X")
    private String productoNombre;

    @Schema(description = "Cantidad comprada", example = "2")
    private Integer cantidad;

    @Schema(description = "Precio unitario al momento de la venta", example = "12990")
    private Float precioUnitario;

    @Schema(description = "Subtotal calculado (precio * cantidad)", example = "25980")
    private Float subtotal;
}