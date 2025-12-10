package com.example.NoLimits.Multimedia.dto.venta.update;

import java.time.LocalDate;
import java.time.LocalTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO para actualizar una venta existente (PUT/PATCH).")
public class VentaUpdateDTO {

    @Schema(
        description = "Fecha de la compra. Si no se envía, se mantiene la actual.",
        example = "2025-07-06"
    )
    private LocalDate fechaCompra;

    @Schema(
        description = "Hora de la compra. Si no se envía, se mantiene la actual.",
        example = "14:30"
    )
    private LocalTime horaCompra;

    @Schema(
        description = "ID del método de pago utilizado en la venta.",
        example = "1"
    )
    private Long metodoPagoId;

    @Schema(
        description = "ID del método de envío asociado a la venta.",
        example = "2"
    )
    private Long metodoEnvioId;

    @Schema(
        description = "ID del estado de la venta (pendiente, pagada, enviada, etc.).",
        example = "3"
    )
    private Long estadoId;
}