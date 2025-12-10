package com.example.NoLimits.Multimedia.model.ubicacion;

import com.example.NoLimits.Multimedia.model.usuario.UsuarioModel;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "direcciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Entidad que representa una dirección física asociada a un usuario.")
public class DireccionModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único de la dirección", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Column(length = 120, nullable = false)
    @NotBlank(message = "La calle es obligatoria.")
    @Schema(description = "Nombre de la calle o avenida", example = "Av. Siempre Viva")
    private String calle;

    @Column(length = 20, nullable = false)
    @NotBlank(message = "El número es obligatorio.")
    @Schema(description = "Número de la propiedad", example = "742")
    private String numero;

    @Column(length = 30)
    @Schema(description = "Departamento, block, casa u otra especificación", example = "Depto 1204-B")
    private String complemento;

    @Column(length = 10)
    @Schema(description = "Código postal", example = "8320000")
    private String codigoPostal;

    @ManyToOne(optional = false)
    @JoinColumn(name = "comuna_id", nullable = false)
    @NotNull(message = "La dirección debe pertenecer a una comuna.")
    @Schema(description = "Comuna a la que pertenece la dirección", example = "{\"id\": 13101}", accessMode = Schema.AccessMode.WRITE_ONLY)
    private ComunaModel comuna;

    @Column(nullable = false)
    @Schema(description = "Indica si la dirección está activa", example = "true")
    private Boolean activo = true;

    @OneToOne
    @JoinColumn(name = "usuario_id", unique = true)
    @Schema(
            description = "Usuario al que pertenece la dirección (solo ID al crear/editar)",
            example = "{\"id\": 5}",
            accessMode = Schema.AccessMode.WRITE_ONLY
    )
    private UsuarioModel usuarioModel;
}