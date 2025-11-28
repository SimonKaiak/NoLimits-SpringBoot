package com.example.NoLimits.Multimedia.dto.producto.update;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * DTO para actualizar un detalle de venta.
 * Aplicable para PUT y PATCH.
 */
@Data
@Schema(description = "DTO para actualizar un detalle de venta.")
public class DetalleVentaUpdateDTO {

    @Schema(
        description = "ID de la venta a la que pertenece el detalle",
        example = "3"
    )
    private Long ventaId;

    @Schema(
        description = "ID del producto vendido",
        example = "10"
    )
    private Long productoId;

    @Schema(
        description = "Cantidad vendida",
        example = "2"
    )
    private Integer cantidad;

    @Schema(
        description = "Precio unitario al momento de la venta",
        example = "12990"
    )
    private Float precioUnitario;
}