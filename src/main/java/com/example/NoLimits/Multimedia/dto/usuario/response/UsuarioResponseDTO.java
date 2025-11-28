package com.example.NoLimits.Multimedia.dto.usuario.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Datos de salida de un usuario registrado.")
public class UsuarioResponseDTO {

    @Schema(description = "ID único del usuario", example = "1")
    private Long id;

    @Schema(description = "Nombre del usuario", example = "Juan")
    private String nombre;

    @Schema(description = "Apellidos del usuario", example = "Pérez Soto")
    private String apellidos;

    @Schema(description = "Nombre completo del usuario", example = "Juan Pérez Soto")
    private String nombreCompleto;

    @Schema(description = "Correo electrónico del usuario", example = "juan.perez@example.com")
    private String correo;

    @Schema(description = "Número de teléfono del usuario", example = "987654321")
    private Long telefono;

    @Schema(description = "ID del rol del usuario", example = "1")
    private Long rolId;

    @Schema(description = "Nombre del rol del usuario", example = "CLIENTE")
    private String rolNombre;

    @Schema(description = "ID de la dirección del usuario", example = "10")
    private Long direccionId;

    @Schema(description = "ID de la comuna del usuario", example = "13101")
    private Long comunaId;

    @Schema(description = "Nombre de la comuna del usuario", example = "Santiago")
    private String comunaNombre;

    @Schema(description = "ID de la región del usuario", example = "13")
    private Long regionId;

    @Schema(
            description = "Nombre de la región del usuario",
            example = "Región Metropolitana de Santiago"
    )
    private String regionNombre;
}