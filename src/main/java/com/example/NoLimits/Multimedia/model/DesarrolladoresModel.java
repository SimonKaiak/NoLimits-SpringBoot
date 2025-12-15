package com.example.NoLimits.Multimedia.model;

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
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(
    name = "desarrolladores",
    uniqueConstraints = @UniqueConstraint(columnNames = {"producto_id", "desarrollador_id"})
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Tabla puente entre Producto y Desarrollador.")
public class DesarrolladoresModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID de la relación Producto-Desarrollador", example = "1")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "producto_id", nullable = false)
    @NotNull(message = "La relación requiere un producto.")
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