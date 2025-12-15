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
        name = "tipos_empresa",
        uniqueConstraints = @UniqueConstraint(columnNames = {"empresa_id", "tipo_empresa_id"})
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Tabla puente entre Empresa y TipoEmpresa.")
public class TiposEmpresaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID de la relación Empresa-TipoEmpresa", example = "1")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "empresa_id", nullable = false)
    @NotNull(message = "La relación debe tener una empresa asociada.")
    @Schema(
            description = "Empresa asociada",
            example = "{\"id\": 5}",
            accessMode = Schema.AccessMode.WRITE_ONLY
    )
    private EmpresaModel empresa;

    @ManyToOne(optional = false)
    @JoinColumn(name = "tipo_empresa_id", nullable = false)
    @NotNull(message = "La relación debe tener un tipo de empresa asociado.")
    @Schema(
            description = "Tipo de empresa asociado",
            example = "{\"id\": 2}",
            accessMode = Schema.AccessMode.WRITE_ONLY
    )
    private TipoEmpresaModel tipoEmpresa;
}