package com.example.NoLimits.Multimedia.dto.venta.response;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.example.NoLimits.Multimedia.dto.producto.response.DetalleVentaResponseDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO de salida que representa una venta registrada con sus detalles.")
public class VentaResponseDTO {

    @Schema(description = "ID único de la venta", example = "10")
    private Long id;

    @Schema(description = "Fecha en que se realizó la venta", example = "2025-07-06")
    private LocalDate fechaCompra;

    @Schema(description = "Hora de la venta", example = "14:30")
    private LocalTime horaCompra;

    @Schema(description = "ID del usuario que realizó la compra", example = "1")
    private Long usuarioId;

    @Schema(description = "Nombre completo del usuario", example = "Juan Pérez Soto")
    private String usuarioNombre;

    @Schema(description = "ID del método de pago", example = "1")
    private Long metodoPagoId;

    @Schema(description = "Nombre del método de pago", example = "Tarjeta de Crédito")
    private String metodoPagoNombre;

    @Schema(description = "ID del método de envío", example = "2")
    private Long metodoEnvioId;

    @Schema(description = "Nombre del método de envío", example = "Despacho a domicilio")
    private String metodoEnvioNombre;

    @Schema(description = "ID del estado de la venta", example = "1")
    private Long estadoId;

    @Schema(description = "Nombre del estado de la venta", example = "PENDIENTE")
    private String estadoNombre;

    @Schema(description = "Total calculado de la venta", example = "45990")
    private Float totalVenta;

    @Schema(description = "Detalles de productos incluidos en la venta")
    private List<DetalleVentaResponseDTO> detalles;
}