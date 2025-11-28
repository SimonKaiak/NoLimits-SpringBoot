package com.example.NoLimits.Multimedia.dto.usuario.update;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO para actualizar un rol de usuario.")
public class RolUpdateDTO {

    @Schema(description = "Nombre del rol", example = "ADMIN")
    private String nombre;

    @Schema(description = "Descripción del rol", example = "Rol con acceso completo al sistema")
    private String descripcion;

    @Schema(description = "Indica si el rol está activo", example = "true")
    private Boolean activo;
}