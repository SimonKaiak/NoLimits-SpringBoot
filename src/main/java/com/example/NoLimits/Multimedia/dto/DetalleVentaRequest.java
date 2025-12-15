// Ruta: src/main/java/com/example/NoLimits/Multimedia/dto/DetalleVentaRequest.java
package com.example.NoLimits.Multimedia.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Detalle de producto enviado desde el frontend para crear una venta.")
public class DetalleVentaRequest {

    @Schema(description = "ID del producto vendido", example = "10", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long productoId;

    @Schema(description = "Cantidad vendida", example = "2", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer cantidad;

    @Schema(description = "Precio unitario al momento de la compra", example = "12990", requiredMode = Schema.RequiredMode.REQUIRED)
    private Float precioUnitario;
}