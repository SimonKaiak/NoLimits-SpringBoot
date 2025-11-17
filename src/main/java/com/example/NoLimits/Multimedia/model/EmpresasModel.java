package com.example.NoLimits.Multimedia.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(
        name = "empresas",
        uniqueConstraints = @UniqueConstraint(columnNames = {"producto_id", "empresa_id"})
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Tabla puente entre Producto y Empresa.")
public class EmpresasModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID de la relación Producto-Empresa", example = "1")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "producto_id", nullable = false)
    @NotNull(message = "La relación debe tener un producto asociado.")
    @Schema(
            description = "Producto asociado a la empresa",
            example = "{\"id\": 10}",
            accessMode = Schema.AccessMode.WRITE_ONLY
    )
    private ProductoModel producto;

    @ManyToOne(optional = false)
    @JoinColumn(name = "empresa_id", nullable = false)
    @NotNull(message = "La relación debe tener una empresa asociada.")
    @Schema(
            description = "Empresa asociada al producto",
            example = "{\"id\": 5}",
            accessMode = Schema.AccessMode.WRITE_ONLY
    )
    private EmpresaModel empresa;
}