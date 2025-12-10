package com.example.NoLimits.Multimedia.dto.catalogos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "DTO para crear o actualizar una empresa.")
public class EmpresaRequestDTO {

    @NotBlank(message = "El nombre de la empresa es obligatorio.")
    @Schema(description = "Nombre de la empresa", example = "Sony Pictures")
    private String nombre;

    @NotNull(message = "Debe indicarse si la empresa está activa.")
    @Schema(description = "Indica si la empresa está activa", example = "true")
    private Boolean activo;
}