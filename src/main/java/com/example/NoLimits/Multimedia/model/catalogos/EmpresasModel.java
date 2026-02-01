package com.example.NoLimits.Multimedia.model.catalogos;

import com.example.NoLimits.Multimedia.model.producto.ProductoModel;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(
    name = "empresas",
    uniqueConstraints = @UniqueConstraint(columnNames = {"producto_id", "empresa_id"})
)
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"producto", "empresa"})
public class EmpresasModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "producto_id", nullable = false)
    @JsonIgnore
    private ProductoModel producto;

    @ManyToOne(optional = false)
    @JoinColumn(name = "empresa_id", nullable = false)
    private EmpresaModel empresa;

    @EqualsAndHashCode.Include
    private Long productoId() {
        return producto == null ? null : producto.getId();
    }

    @EqualsAndHashCode.Include
    private Long empresaId() {
        return empresa == null ? null : empresa.getId();
    }
}