// Ruta: src/main/java/com/example/NoLimits/Multimedia/model/UsuarioModel.java
package com.example.NoLimits.Multimedia.model.usuario;

import java.util.List;

import com.example.NoLimits.Multimedia.model.ubicacion.DireccionModel;
import com.example.NoLimits.Multimedia.model.venta.VentaModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad JPA que representa a un usuario registrado en la plataforma.
 *
 * Incluye:
 * - Datos básicos: nombre, apellidos, correo, teléfono, contraseña.
 * - Relación con rol (ManyToOne).
 * - Relación 1:1 con dirección.
 * - Relación 1:N con ventas.
 * - Getters derivados para exponer comuna y región sin tener que navegar toda la jerarquía.
 */
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
    private Long telefono;

    /**
     * Contraseña del usuario.
     *
     * - Máximo 10 caracteres (según restricción definida).
     * - WRITE_ONLY en JSON: se puede enviar desde el frontend, pero no se devuelve nunca en respuestas.
     */
    @Column(length = 255, nullable = false)
    @NotBlank(message = "El usuario requiere de una contraseña.")
    @Size(min = 8, max = 255, message = "La contraseña debe tener entre 8 y 255 caracteres.")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    /* ===================== Relaciones ===================== */

    /**
     * Rol asignado al usuario (admin, cliente, etc.).
     *
     * Solo se espera el ID en las operaciones de escritura:
     *   "rol": { "id": 1 }
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "rol_id", nullable = false)
    @NotNull(message = "El usuario debe tener un rol asignado.")
    @Schema(
            description = "Rol del usuario. Solo se requiere el ID al crear/editar.",
            example = "{\"id\": 1}",
            accessMode = Schema.AccessMode.WRITE_ONLY
    )
    private RolModel rol;

    /**
     * Dirección asociada al usuario (relación 1:1).
     *
     * - mappedBy indica que la FK está en DireccionModel.
     * - Cascade ALL + orphanRemoval: si se elimina el usuario, se elimina su dirección.
     */
    @OneToOne(mappedBy = "usuarioModel", cascade = CascadeType.ALL, orphanRemoval = true)
    @Schema(description = "Dirección del usuario", accessMode = Schema.AccessMode.READ_ONLY)
    private DireccionModel direccion;

    /**
     * Ventas asociadas al usuario.
     *
     * - Se ignoran en JSON para evitar ciclos de serialización (usuario -> ventas -> usuario...).
     * - Solo se usan internamente en el backend.
     */
    @OneToMany(mappedBy = "usuarioModel", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @Schema(description = "Ventas asociadas al usuario", accessMode = Schema.AccessMode.READ_ONLY)
    private List<VentaModel> ventas;

    /* ===================== Utilidad ===================== */

    /**
     * Nombre completo calculado a partir de nombre + apellidos.
     * Útil para mostrar datos en listas/tablas sin tener que concatenar en el frontend.
     */
    @Schema(description = "Nombre completo calculado", accessMode = Schema.AccessMode.READ_ONLY)
    public String getNombreCompleto() {
        return (nombre == null ? "" : nombre.trim()) + " " +
               (apellidos == null ? "" : apellidos.trim());
    }

    // ===== Navegación Usuario -> Dirección -> Comuna -> Región (solo lectura) =====

    /**
     * Devuelve el ID de la dirección asociada al usuario (si existe).
     */
    @Schema(description = "ID de la dirección del usuario", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    public Long getDireccionId() {
        return direccion != null ? direccion.getId() : null;
    }

    /**
     * Devuelve el ID de la comuna asociada al usuario navegando por la dirección.
     */
    @Schema(description = "ID de la comuna del usuario", example = "13101", accessMode = Schema.AccessMode.READ_ONLY)
    public Long getComunaId() {
        return (direccion != null && direccion.getComuna() != null)
                ? direccion.getComuna().getId()
                : null;
    }

    /**
     * Devuelve el nombre de la comuna asociada al usuario.
     * Campo muy útil para mostrar directamente en el frontend (perfil, listados, etc.).
     */
    @Schema(description = "Nombre de la comuna del usuario", example = "Santiago", accessMode = Schema.AccessMode.READ_ONLY)
    public String getComunaNombre() {
        return (direccion != null && direccion.getComuna() != null)
                ? direccion.getComuna().getNombre()
                : null;
    }

    /**
     * Devuelve el ID de la región asociada al usuario navegando por comuna -> región.
     */
    @Schema(description = "ID de la región del usuario", example = "13", accessMode = Schema.AccessMode.READ_ONLY)
    public Long getRegionId() {
        return (direccion != null &&
                direccion.getComuna() != null &&
                direccion.getComuna().getRegion() != null)
                ? direccion.getComuna().getRegion().getId()
                : null;
    }

    /**
     * Devuelve el nombre de la región asociada al usuario.
     * Esto es lo que estás usando en el front como `regionNombre`.
     */
    @Schema(
            description = "Nombre de la región del usuario",
            example = "Región Metropolitana de Santiago",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    public String getRegionNombre() {
        return (direccion != null &&
                direccion.getComuna() != null &&
                direccion.getComuna().getRegion() != null)
                ? direccion.getComuna().getRegion().getNombre()
                : null;
    }
}