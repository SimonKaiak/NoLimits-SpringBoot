package com.example.NoLimits.Multimedia.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Rol o perfil asignado a los usuarios (admin, vendedor, cliente, etc.).")
public class RolModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único del rol", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Column(length = 50, nullable = false, unique = true)
    @NotBlank(message = "El nombre del rol es obligatorio.")
    @Schema(description = "Nombre del rol", example = "CLIENTE")
    private String nombre;

    @Column(length = 255)
    @Schema(description = "Descripción del rol", example = "Rol por defecto con permisos de compra")
    private String descripcion;

    @Column(nullable = false)
    @NotNull(message = "El campo 'activo' es obligatorio.")
    @Schema(description = "Indica si el rol está activo", example = "true")
    private Boolean activo = true;

    // Utilidad
    public boolean esActivo() {
        return Boolean.TRUE.equals(activo);
    }
}