package com.example.NoLimits.Multimedia.dto.catalogos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "DTO para asociar una empresa a un producto.")
public class EmpresasRequestDTO {

    @NotNull(message = "El ID del producto es obligatorio.")
    @Schema(description = "ID del producto asociado", example = "10")
    private Long productoId;

    @NotNull(message = "El ID de la empresa es obligatorio.")
    @Schema(description = "ID de la empresa asociada", example = "5")
    private Long empresaId;
}