package com.example.NoLimits.Multimedia.model.producto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "imagenes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Imágenes asociadas a un producto.")
public class ImagenesModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID de la imagen", example = "1")
    private Long id;

    @Column(nullable = false, length = 255)
    @NotBlank(message = "La ruta/URL de la imagen es obligatoria.")
    @Schema(
        description = "Ruta o URL de la imagen",
        example = "/assets/img/Peliculas/spiderman.webp"
    )
    private String ruta;

    @Column(length = 150)
    @Schema(
        description = "Texto alternativo de accesibilidad",
        example = "Spider-Man posando"
    )
    private String altText;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    @NotNull(message = "La imagen debe pertenecer a un producto.")
    @JsonIgnore
    @Schema(
        description = "Producto al que pertenece la imagen",
        example = "Producto ID: 10",
        accessMode = Schema.AccessMode.WRITE_ONLY
    )
    private ProductoModel producto;
}