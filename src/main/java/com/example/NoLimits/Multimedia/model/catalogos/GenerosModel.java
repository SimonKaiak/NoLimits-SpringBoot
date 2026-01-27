package com.example.NoLimits.Multimedia.model.catalogos;

import com.example.NoLimits.Multimedia.model.producto.ProductoModel;
import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(
    name = "generos",
    uniqueConstraints = @UniqueConstraint(columnNames = {"producto_id", "genero_id"})
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"producto", "genero"})
@Schema(description = "Tabla puente entre Producto y Género.")
public class GenerosModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Schema(description = "ID de la relación Producto-Género", example = "1")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "producto_id", nullable = false)
    @NotNull(message = "El producto es obligatorio en la relación Producto-Género.")
    @JsonIgnore
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