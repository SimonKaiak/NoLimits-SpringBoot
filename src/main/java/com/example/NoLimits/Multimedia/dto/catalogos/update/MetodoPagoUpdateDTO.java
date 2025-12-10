package com.example.NoLimits.Multimedia.dto.catalogos.update;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO para actualizar un método de pago.")
public class MetodoPagoUpdateDTO {

    @Schema(
        description = "Nombre del método de pago",
        example = "Transferencia Bancaria"
    )
    private String nombre;

    @Schema(
        description = "Indica si el método de pago está activo",
        example = "true"
    )
    private Boolean activo;
}