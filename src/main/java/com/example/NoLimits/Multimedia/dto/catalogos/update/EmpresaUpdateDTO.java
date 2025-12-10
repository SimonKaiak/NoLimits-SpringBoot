package com.example.NoLimits.Multimedia.dto.catalogos.update;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO para actualizar una empresa.")
public class EmpresaUpdateDTO {

    @Schema(
        description = "Nombre de la empresa",
        example = "Warner Bros Games"
    )
    private String nombre;

    @Schema(
        description = "Indica si la empresa está activa",
        example = "true"
    )
    private Boolean activo;
}