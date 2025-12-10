package com.example.NoLimits.Multimedia.dto.catalogos.update;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO para actualizar un Estado.")
public class EstadoUpdateDTO {

    @Schema(description = "Nombre del estado", example = "Agotado")
    private String nombre;

    @Schema(description = "Descripción del estado", example = "Producto sin stock disponible")
    private String descripcion;

    @Schema(description = "Indica si el estado está activo", example = "true")
    private Boolean activo;
}