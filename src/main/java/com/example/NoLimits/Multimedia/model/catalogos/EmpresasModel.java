package com.example.NoLimits.Multimedia.model.catalogos;

import com.example.NoLimits.Multimedia.model.producto.ProductoModel;
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
        name = "empresas",
        uniqueConstraints = @UniqueConstraint(columnNames = {"producto_id", "empresa_id"})
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"producto","empresa"})
@Schema(description = "Tabla puente entre Producto y Empresa.")
public class EmpresasModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Schema(description = "ID de la relación Producto-Empresa", example = "1")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "producto_id", nullable = false)
    private ProductoModel producto;

    @ManyToOne(optional = false)
    @JoinColumn(name = "empresa_id", nullable = false)
    private EmpresaModel empresa;
}