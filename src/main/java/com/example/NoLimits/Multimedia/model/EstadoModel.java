package com.example.NoLimits.Multimedia.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(
    name = "estados",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = "nombre")
    }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Catálogo de estados posibles para un producto o flujo (Activo, Agotado, etc.).")
public class EstadoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único del estado", example = "1")
    private Long id;

    @Column(length = 50, nullable = false, unique = true)
    @NotBlank(message = "El nombre del estado es obligatorio.")
    @Schema(description = "Nombre del estado", example = "Activo")
    private String nombre;

    @Column(length = 255)
    @Schema(description = "Descripción del estado", example = "Producto disponible para su compra")
    private String descripcion;

    @Column(nullable = false)
    @NotNull(message = "El campo 'activo' es obligatorio.")
    @Schema(description = "Indica si el estado está disponible", example = "true")
    private Boolean activo = true;

    @OneToMany(mappedBy = "estado")
    @JsonIgnore
    @Schema(description = "Productos asociados a este estado", accessMode = Schema.AccessMode.READ_ONLY)
    private List<ProductoModel> productos;

    public boolean esActivo() {
        return Boolean.TRUE.equals(activo);
    }
}