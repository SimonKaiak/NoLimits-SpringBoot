package com.example.NoLimits.Multimedia.dto.catalogos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "DTO para asociar una plataforma a un producto.")
public class PlataformasRequestDTO {

    @NotNull(message = "El ID del producto es obligatorio.")
    @Schema(description = "ID del producto", example = "10")
    private Long productoId;

    @NotNull(message = "El ID de la plataforma es obligatorio.")
    @Schema(description = "ID de la plataforma", example = "1")
    private Long plataformaId;
}