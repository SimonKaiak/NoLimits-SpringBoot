package com.example.NoLimits.Multimedia.dto.catalogos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "DTO para crear o actualizar un tipo de empresa.")
public class TipoEmpresaRequestDTO {

    @NotBlank(message = "El nombre del tipo de empresa es obligatorio.")
    @Schema(description = "Nombre del tipo de empresa", example = "Publisher")
    private String nombre;
}