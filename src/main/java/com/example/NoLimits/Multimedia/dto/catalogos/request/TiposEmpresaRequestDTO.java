package com.example.NoLimits.Multimedia.dto.catalogos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "DTO para asociar un tipo de empresa a una empresa.")
public class TiposEmpresaRequestDTO {

    @NotNull(message = "El ID de la empresa es obligatorio.")
    @Schema(description = "ID de la empresa", example = "5")
    private Long empresaId;

    @NotNull(message = "El ID del tipo de empresa es obligatorio.")
    @Schema(description = "ID del tipo de empresa", example = "2")
    private Long tipoEmpresaId;
}