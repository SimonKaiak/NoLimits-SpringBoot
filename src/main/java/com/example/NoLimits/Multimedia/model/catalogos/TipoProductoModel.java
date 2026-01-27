package com.example.NoLimits.Multimedia.model.catalogos;

import java.util.List;

import com.example.NoLimits.Multimedia.model.producto.ProductoModel;
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
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "tipo_productos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@Schema(description = "Tipo de producto: Película, Videojuego, Accesorio, etc.")
public class TipoProductoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @ToString.Include
    @Schema(description = "ID del tipo de producto", example = "1")
    private Long id;

    @ToString.Include
    @Column(length = 100, nullable = false, unique = true)
    @NotBlank(message = "El nombre es obligatorio.")
    @Size(min = 2, max = 100)
    private String nombre;

    @Column(length = 255)
    private String descripcion;

    @Column(nullable = false)
    private Boolean activo = true;

    @OneToMany(mappedBy = "tipoProducto")
    @JsonIgnore
    private List<ProductoModel> productos;
}