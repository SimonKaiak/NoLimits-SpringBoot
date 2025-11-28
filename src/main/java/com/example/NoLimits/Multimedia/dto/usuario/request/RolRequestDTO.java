package com.example.NoLimits.Multimedia.dto.usuario.request;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Datos necesarios para crear o actualizar un rol.")
public class RolRequestDTO {

    @NotBlank(message = "El nombre del rol es obligatorio.")
    @Schema(description = "Nombre del rol", example = "CLIENTE")
    private String nombre;

    @Schema(description = "Descripción del rol", example = "Rol por defecto con permisos de compra")
    private String descripcion;

    @NotNull(message = "El estado del rol es obligatorio.")
    @Schema(description = "Indica si el rol está activo", example = "true")
    private Boolean activo;
}