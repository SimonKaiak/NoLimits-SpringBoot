package com.example.NoLimits.Multimedia.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(
    name = "plataformas",
    uniqueConstraints = @UniqueConstraint(columnNames = {"producto_id", "plataforma_id"})
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Tabla puente entre Producto y Plataforma.")
public class PlataformasModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID de la relación Producto-Plataforma", example = "1")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "producto_id", nullable = false)
    @NotNull
    @Schema(description = "Producto", example = "{\"id\": 10}", accessMode = Schema.AccessMode.WRITE_ONLY)
    private ProductoModel producto;

    @ManyToOne(optional = false)
    @JoinColumn(name = "plataforma_id", nullable = false)
    @NotNull
    @Schema(description = "Plataforma", example = "{\"id\": 1}", accessMode = Schema.AccessMode.WRITE_ONLY)
    private PlataformaModel plataforma;
}