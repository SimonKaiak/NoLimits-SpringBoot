package com.example.NoLimits.Multimedia.model.catalogos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Table(
        name = "tipos_empresa",
        uniqueConstraints = @UniqueConstraint(columnNames = {"empresa_id", "tipo_empresa_id"})
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"empresa","tipoEmpresa"})
@Schema(description = "Tabla puente entre Empresa y TipoEmpresa.")
public class TiposEmpresaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Schema(description = "ID de la relación Empresa-TipoEmpresa", example = "1")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "empresa_id", nullable = false)
    private EmpresaModel empresa;

    @ManyToOne(optional = false)
    @JoinColumn(name = "tipo_empresa_id", nullable = false)
    private TipoEmpresaModel tipoEmpresa;
}