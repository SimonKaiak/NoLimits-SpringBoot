package com.example.NoLimits.Multimedia.dto.catalogos.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Datos de salida de un método de envío registrado.")
public class MetodoEnvioResponseDTO {

    @Schema(description = "ID único del método de envío", example = "1")
    private Long id;

    @Schema(description = "Nombre del método de envío", example = "Retiro en tienda")
    private String nombre;

    @Schema(description = "Descripción del método de envío", example = "Retiro presencial en sucursal Plaza Oeste")
    private String descripcion;

    @Schema(description = "Indica si el método está activo", example = "true")
    private Boolean activo;
}