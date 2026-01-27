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
    name = "desarrolladores",
    uniqueConstraints = @UniqueConstraint(columnNames = {"producto_id", "desarrollador_id"})
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"producto", "desarrollador"})
@Schema(description = "Tabla puente entre Producto y Desarrollador.")
public class DesarrolladoresModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Schema(description = "ID de la relación Producto-Desarrollador", example = "1")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "producto_id", nullable = false)
    @NotNull(message = "La relación requiere un producto.")
    @JsonIgnore
    @Schema(
        description = "Producto asociado a la relación",
        accessMode = Schema.AccessMode.WRITE_ONLY
    )
    private ProductoModel producto;

    @ManyToOne(optional = false)
    @JoinColumn(name = "desarrollador_id", nullable = false)
    @NotNull(message = "La relación requiere un desarrollador.")
    @Schema(
        description = "Desarrollador asociado a la relación",
        accessMode = Schema.AccessMode.WRITE_ONLY
    )
    private DesarrolladorModel desarrollador;
}