package com.example.NoLimits.Multimedia.dto.usuario.response;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Datos de salida de un rol registrado.")
public class RolResponseDTO {

    @Schema(description = "ID único del rol", example = "1")
    private Long id;

    @Schema(description = "Nombre del rol", example = "CLIENTE")
    private String nombre;

    @Schema(description = "Descripción del rol", example = "Rol por defecto con permisos de compra")
    private String descripcion;

    @Schema(description = "Indica si el rol está activo", example = "true")
    private Boolean activo;
}