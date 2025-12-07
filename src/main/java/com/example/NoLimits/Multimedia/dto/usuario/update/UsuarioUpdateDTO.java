package com.example.NoLimits.Multimedia.dto.usuario.update;

import com.example.NoLimits.Multimedia.dto.ubicacion.request.DireccionRequestDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO para actualizar datos de un usuario (PUT/PATCH).")
public class UsuarioUpdateDTO {

    @Schema(description = "Nombre del usuario", example = "Juan")
    private String nombre;

    @Schema(description = "Apellidos del usuario", example = "Pérez Soto")
    private String apellidos;

    @Schema(description = "Correo electrónico del usuario", example = "juan.perez@example.com")
    private String correo;

    @Schema(description = "Número de teléfono del usuario", example = "987654321")
    private Long telefono;

    @Schema(description = "Nueva contraseña del usuario (máx. 10 caracteres)", example = "nuevaClave")
    private String password;

    @Schema(
        description = "ID del rol asignado al usuario",
        example = "2"
    )
    private Long rolId;

    @Schema(description = "Datos de dirección del usuario", example = "Av. Avenida 921")
    private DireccionRequestDTO direccion;
}