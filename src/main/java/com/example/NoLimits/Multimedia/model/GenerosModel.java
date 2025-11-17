package com.example.NoLimits.Multimedia.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(
    name = "generos",
    uniqueConstraints = @UniqueConstraint(columnNames = {"producto_id", "genero_id"})
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Tabla puente entre Producto y Género.")
public class GenerosModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID de la relación Producto-Género", example = "1")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "producto_id", nullable = false)
    @NotNull(message = "El producto es obligatorio en la relación Producto-Género.")
    @Schema(
        description = "Producto asociado (solo ID al crear/editar).",
        example = "{\"id\": 10}",
        accessMode = Schema.AccessMode.WRITE_ONLY
    )
    private ProductoModel producto;

    @ManyToOne(optional = false)
    @JoinColumn(name = "genero_id", nullable = false)
    @NotNull(message = "El género es obligatorio en la relación Producto-Género.")
    @Schema(
        description = "Género asociado (solo ID al crear/editar).",
        example = "{\"id\": 1}",
        accessMode = Schema.AccessMode.WRITE_ONLY
    )
    private GeneroModel genero;
}