// Ruta: src/main/java/com/example/NoLimits/Multimedia/model/DesarrolladoresModel.java
package com.example.NoLimits.Multimedia.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

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