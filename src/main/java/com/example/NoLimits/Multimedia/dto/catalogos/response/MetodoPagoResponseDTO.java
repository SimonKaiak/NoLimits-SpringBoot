package com.example.NoLimits.Multimedia.dto.catalogos.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Datos de salida de un método de pago registrado.")
public class MetodoPagoResponseDTO {

    @Schema(description = "ID único del método de pago", example = "1")
    private Long id;

    @Schema(description = "Nombre del método de pago", example = "Tarjeta de Crédito")
    private String nombre;

    @Schema(description = "Indica si el método está activo", example = "true")
    private Boolean activo;
}