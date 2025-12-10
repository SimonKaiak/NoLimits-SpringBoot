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
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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