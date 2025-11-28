package com.example.NoLimits.Multimedia.dto.catalogos.request;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Datos necesarios para crear o actualizar un método de envío.")
public class MetodoEnvioRequestDTO {

    @NotBlank(message = "El nombre del método de envío es obligatorio.")
    @Schema(description = "Nombre del método de envío", example = "Retiro en tienda")
    private String nombre;

    @Schema(description = "Descripción del método de envío", example = "Retiro presencial en sucursal Plaza Oeste")
    private String descripcion;

    @NotNull(message = "El estado del método de envío es obligatorio.")
    @Schema(description = "Indica si el método está activo", example = "true")
    private Boolean activo;
}