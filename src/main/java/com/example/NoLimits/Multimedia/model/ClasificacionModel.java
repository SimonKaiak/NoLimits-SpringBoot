package com.example.NoLimits.Multimedia.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "clasificaciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Clasificación por edad/contenido del producto (Ej: +13, +18, E, T, M).")
public class ClasificacionModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID de la clasificación", example = "1")
    private Long id;

    @Column(length = 50, nullable = false, unique = true)
    @NotBlank(message = "El nombre de la clasificación es obligatorio.")
    @Schema(description = "Nombre corto de la clasificación", example = "T")
    private String nombre;

    @Column(length = 255)
    @Schema(description = "Descripción corta de la clasificación", example = "Contenido apto para adolescentes.")
    private String descripcion;

    @Column(nullable = false)
    @Schema(description = "Indica si la clasificación está activa.", example = "true")
    private boolean activo = true;

    // Relación con Producto
    @OneToMany(mappedBy = "clasificacion", fetch = FetchType.LAZY)
    @JsonIgnore
    @Schema(description = "Lista de productos asociados", accessMode = Schema.AccessMode.READ_ONLY)
    private List<ProductoModel> productos;

    // Constructor útil sin lista de productos
    public ClasificacionModel(Long id, String nombre, String descripcion, boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.activo = activo;
    }
}