// Ruta: src/main/java/com/example/NoLimits/Multimedia/model/catalogos/PlataformaModel.java
package com.example.NoLimits.Multimedia.model.catalogos;

import java.util.ArrayList;
import java.util.List;

import com.example.NoLimits.Multimedia.model.producto.ProductoLinkCompraModel;
import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "plataforma")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"productos", "linksCompra"})
@Schema(description = "Plataforma en la que puede estar disponible un producto (PC, PS5, Xbox, etc.).")
public class PlataformaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Schema(description = "ID de la plataforma", example = "1")
    private Long id;

    @Column(length = 80, nullable = false, unique = true)
    @NotBlank(message = "El nombre de la plataforma es obligatorio.")
    @Size(max = 80, message = "El nombre no puede superar 80 caracteres.")
    @Schema(description = "Nombre de la plataforma", example = "PC")
    private String nombre;

    /** Relación hacia la tabla puente (Producto <-> Plataforma) */
    @OneToMany(mappedBy = "plataforma", fetch = FetchType.LAZY)
    @JsonIgnore
    @Schema(description = "Relaciones con productos (tabla puente 'plataformas')",
            accessMode = Schema.AccessMode.READ_ONLY)
    private List<PlataformasModel> productos = new ArrayList<>();

    /** Si usas links de compra por plataforma */
    @OneToMany(mappedBy = "plataforma", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<ProductoLinkCompraModel> linksCompra = new ArrayList<>();
}