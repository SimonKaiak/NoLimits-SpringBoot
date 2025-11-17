package com.example.NoLimits.Multimedia.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Entidad que representa a un usuario registrado en la plataforma.")
public class UsuarioModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único del usuario", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "El usuario requiere de un nombre.")
    @Schema(description = "Nombre del usuario", example = "Juan")
    private String nombre;

    @Column(nullable = false)
    @NotBlank(message = "El usuario requiere de sus apellidos.")
    @Schema(description = "Apellidos del usuario", example = "Pérez Soto")
    private String apellidos;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "El usuario requiere de un correo.")
    @Email(message = "El correo no tiene un formato válido.")
    @Schema(description = "Correo electrónico del usuario", example = "juan.perez@example.com")
    private String correo;

    @Column(nullable = false)
    @NotNull(message = "El usuario requiere de un teléfono.")
    @Schema(description = "Número de teléfono del usuario", example = "987654321")
    private Integer telefono;

    @Column(length = 10, nullable = false)
    @NotBlank(message = "El usuario requiere de una contraseña.")
    @Size(max = 10, message = "La contraseña debe tener como máximo 10 caracteres.")
    @Schema(description = "Contraseña del usuario (máx. 10 caracteres)", example = "clave1234")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    /* ===================== Relaciones ===================== */

    /** Rol asignado al usuario (N:1). */
    @ManyToOne(optional = false)
    @JoinColumn(name = "rol_id", nullable = false)
    @NotNull(message = "El usuario debe tener un rol asignado.")
    @Schema(
        description = "Rol del usuario. Solo se requiere el ID al crear/editar.",
        example = "{\"id\": 1}",
        accessMode = Schema.AccessMode.WRITE_ONLY
    )
    private RolModel rol;

    /** Dirección principal del usuario (1:1, lado inverso). */
    @OneToOne(mappedBy = "usuarioModel", cascade = CascadeType.ALL, orphanRemoval = true)
    @Schema(description = "Dirección del usuario", accessMode = Schema.AccessMode.READ_ONLY)
    private DireccionModel direccion;

    /** Ventas asociadas al usuario (1:N). */
    @OneToMany(mappedBy = "usuarioModel", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @Schema(description = "Ventas asociadas al usuario", accessMode = Schema.AccessMode.READ_ONLY)
    private List<VentaModel> ventas;

    /* ===================== Utilidad ===================== */

    @Schema(description = "Nombre completo calculado", accessMode = Schema.AccessMode.READ_ONLY)
    public String getNombreCompleto() {
        return (nombre == null ? "" : nombre.trim()) +
               " " +
               (apellidos == null ? "" : apellidos.trim());
    }
}