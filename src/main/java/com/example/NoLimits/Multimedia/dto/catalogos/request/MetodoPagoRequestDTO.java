package com.example.NoLimits.Multimedia.dto.catalogos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Datos necesarios para crear o actualizar un método de pago.")
public class MetodoPagoRequestDTO {

    @NotBlank(message = "El nombre del método de pago es obligatorio.")
    @Schema(description = "Nombre del método de pago", example = "Tarjeta de Crédito")
    private String nombre;

    @NotNull(message = "El estado del método de pago es obligatorio.")
    @Schema(description = "Indica si el método está activo", example = "true")
    private Boolean activo;
}