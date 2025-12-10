package com.example.NoLimits.Multimedia.dto.catalogos.update;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO para actualizar un método de envío.")
public class MetodoEnvioUpdateDTO {

    @Schema(
        description = "Nombre del método de envío",
        example = "Despacho a domicilio"
    )
    private String nombre;

    @Schema(
        description = "Descripción del método de envío",
        example = "Entrega directa a la dirección del cliente"
    )
    private String descripcion;

    @Schema(
        description = "Indica si el método está activo",
        example = "true"
    )
    private Boolean activo;
}