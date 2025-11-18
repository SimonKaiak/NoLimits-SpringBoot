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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tipo_productos")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Tipo de producto: Película, Videojuego, Accesorio, etc.")
public class TipoProductoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID del tipo de producto", example = "1")
    private Long id;

    @Column(length = 100, nullable = false, unique = true)
    @NotBlank(message = "El nombre es obligatorio.")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres.")
    @Schema(description = "Nombre del tipo de producto", example = "Película")
    private String nombre;

    @Column(length = 255)
    @Schema(description = "Descripción del tipo de producto", example = "Categoría general para clasificar productos")
    private String descripcion;

    @Column(nullable = false)
    @NotNull(message = "El campo 'activo' es obligatorio.")
    @Schema(description = "Indica si el tipo está activo", example = "true")
    private Boolean activo = true;

    @OneToMany(mappedBy = "tipoProducto")
    @JsonIgnore
    @Schema(description = "Productos que pertenecen a este tipo", accessMode = Schema.AccessMode.READ_ONLY)
    private List<ProductoModel> productos;

    public boolean esActivo() {
        return Boolean.TRUE.equals(activo);
    }
}