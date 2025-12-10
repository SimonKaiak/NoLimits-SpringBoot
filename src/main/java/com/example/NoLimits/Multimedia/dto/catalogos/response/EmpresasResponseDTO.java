package com.example.NoLimits.Multimedia.dto.catalogos.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO que representa la relación Producto-Empresa.")
public class EmpresasResponseDTO {

    @Schema(description = "ID de la relación", example = "7")
    private Long id;

    @Schema(description = "ID del producto asociado", example = "10")
    private Long productoId;

    @Schema(description = "ID de la empresa asociada", example = "5")
    private Long empresaId;

    @Schema(description = "Nombre de la empresa asociada", example = "Sony Pictures")
    private String empresaNombre;
}