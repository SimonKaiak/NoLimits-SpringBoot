package com.example.NoLimits.Multimedia.dto.usuario.request;

import com.example.NoLimits.Multimedia.dto.ubicacion.request.DireccionRequestDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Datos necesarios para crear o actualizar un usuario.")
public class UsuarioRequestDTO {

    @NotBlank(message = "El nombre es obligatorio.")
    @Schema(description = "Nombre del usuario", example = "Juan")
    private String nombre;

    @NotBlank(message = "Los apellidos son obligatorios.")
    @Schema(description = "Apellidos del usuario", example = "Pérez Soto")
    private String apellidos;

    @NotBlank(message = "El correo es obligatorio.")
    @Email(message = "El correo no tiene un formato válido.")
    @Schema(description = "Correo electrónico del usuario", example = "juan.perez@example.com")
    private String correo;

    @NotNull(message = "El teléfono es obligatorio.")
    @Schema(description = "Número de teléfono del usuario", example = "987654321")
    private Long telefono;

    @NotBlank(message = "La contraseña es obligatoria.")
    @Size(max = 10, message = "La contraseña debe tener como máximo 10 caracteres.")
    @Schema(
            description = "Contraseña del usuario (máx. 10 caracteres)",
            example = "clave1234"
    )
    private String password;

    @NotNull(message = "El rol es obligatorio.")
    @Schema(
            description = "ID del rol asignado al usuario",
            example = "1"
    )
    private Long rolId;

    @NotNull(message = "La dirección es obligatoria.")
    @Valid
    private DireccionRequestDTO direccion;
}