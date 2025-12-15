// Ruta: src/main/java/com/example/NoLimits/Multimedia/dto/VentaRequest.java
package com.example.NoLimits.Multimedia.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO para registrar una venta con sus detalles desde el frontend.")
public class VentaRequest {

    @Schema(description = "ID del usuario que realiza la compra", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long usuarioId;

    @Schema(description = "ID del método de pago utilizado", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long metodoPagoId;

    @Schema(description = "ID del método de envío", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long metodoEnvioId;

    @Schema(description = "ID del estado inicial de la venta", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long estadoId;

    @Schema(description = "Lista de detalles (productos del carrito)", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<DetalleVentaRequest> detalles;
}