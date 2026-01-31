package com.example.NoLimits.Multimedia.model.catalogos;

import java.util.ArrayList;
import java.util.List;

import com.example.NoLimits.Multimedia.model.producto.ProductoLinkCompraModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "plataforma")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Plataforma en la que puede estar disponible un producto (PC, PS5, Xbox, etc.).")
public class PlataformaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID de la plataforma", example = "1")
    private Long id;

    @Column(length = 80, nullable = false, unique = true)
    @NotBlank(message = "El nombre de la plataforma es obligatorio.")
    @Schema(description = "Nombre de la plataforma", example = "PC")
    private String nombre;

    @OneToMany(mappedBy = "plataforma")
    @JsonIgnore
    @Schema(description = "Relaciones con productos (tabla puente 'plataformas')", accessMode = Schema.AccessMode.READ_ONLY)
    private List<PlataformasModel> productos;

    @OneToMany(mappedBy = "plataforma", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<ProductoLinkCompraModel> linksCompra = new ArrayList<>();
}