package com.example.NoLimits.Multimedia.dto.catalogos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "DTO para crear o actualizar una plataforma.")
public class PlataformaRequestDTO {

    @NotBlank(message = "El nombre de la plataforma es obligatorio.")
    @Schema(description = "Nombre de la plataforma", example = "PC")
    private String nombre;
}