package com.example.NoLimits.Multimedia.dto.catalogos.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO de salida que representa un estado registrado.")
public class EstadoResponseDTO {

    @Schema(description = "ID del estado", example = "1")
    private Long id;

    @Schema(description = "Nombre del estado", example = "Activo")
    private String nombre;

    @Schema(description = "Descripción del estado", example = "Producto disponible para su compra")
    private String descripcion;

    @Schema(description = "Indica si el estado está activo", example = "true")
    private Boolean activo;
}