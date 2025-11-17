package com.example.NoLimits.Multimedia.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "productos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Producto vendible en la plataforma (película, videojuego, accesorio).")
public class ProductoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID del producto", example = "10")
    private Long id;

    @Column(nullable = false, length = 100)
    @NotBlank(message = "El nombre del producto es obligatorio.")
    @Size(min = 2, max = 100)
    @Schema(description = "Nombre del producto", example = "Spider-Man (2002)")
    private String nombre;

    @Column(nullable = false)
    @NotNull(message = "El precio es obligatorio.")
    @Schema(description = "Precio del producto", example = "12990")
    private Double precio;

    /* ====== Relaciones N:1 ====== */

    // PRODUCTO -> TIPO_PRODUCTO
    @ManyToOne(optional = false)
    @JoinColumn(name = "tipo_producto_id", nullable = false)
    @NotNull(message = "El producto debe pertenecer a un tipo.")
    @Schema(
        description = "Tipo de producto (solo ID al crear/editar).",
        example = "{\"id\": 1}",
        accessMode = Schema.AccessMode.WRITE_ONLY
    )
    private TipoProductoModel tipoProducto;

    // PRODUCTO -> CLASIFICACION
    @ManyToOne
    @JoinColumn(name = "clasificacion_id")
    @Schema(
        description = "Clasificación del producto (solo ID al crear/editar).",
        example = "{\"id\": 2}",
        accessMode = Schema.AccessMode.WRITE_ONLY
    )
    private ClasificacionModel clasificacion;

    // PRODUCTO -> ESTADO
    @ManyToOne(optional = false)
    @JoinColumn(name = "estado_id", nullable = false)
    @NotNull(message = "El producto debe tener un estado.")
    @Schema(
        description = "Estado del producto (solo ID al crear/editar).",
        example = "{\"id\": 1}",
        accessMode = Schema.AccessMode.WRITE_ONLY
    )
    private EstadoModel estado;

    /* ====== Relaciones 1:N (propias del producto) ====== */

    // PRODUCTO -> IMAGENES
    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @Schema(description = "Imágenes asociadas al producto", accessMode = Schema.AccessMode.READ_ONLY)
    private List<ImagenesModel> imagenes;

    // PRODUCTO -> DETALLE_VENTA (la venta se modela desde DetalleVentaModel/VentaModel)
    @OneToMany(mappedBy = "producto")
    @JsonIgnore
    @Schema(description = "Detalles de venta donde aparece este producto", accessMode = Schema.AccessMode.READ_ONLY)
    private List<DetalleVentaModel> detallesVenta;

    /* ====== Relaciones N:M via tablas puente ====== */

    // PRODUCTO <-> PLATAFORMA (puente: PLATAFORMAS)
    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @Schema(description = "Relación con plataformas (puente 'plataformas')", accessMode = Schema.AccessMode.READ_ONLY)
    private List<PlataformasModel> plataformas;

    // PRODUCTO <-> GENERO (puente: GENEROS)
    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @Schema(description = "Relación con géneros (puente 'generos')", accessMode = Schema.AccessMode.READ_ONLY)
    private List<GenerosModel> generos;

    // PRODUCTO <-> EMPRESA (puente: EMPRESAS)
    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @Schema(description = "Relación con empresas (puente 'empresas')", accessMode = Schema.AccessMode.READ_ONLY)
    private List<EmpresasModel> empresas;

    // PRODUCTO <-> DESARROLLADOR (puente: DESARROLLADORES)
    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @Schema(description = "Relación con desarrolladores (puente 'desarrolladores')", accessMode = Schema.AccessMode.READ_ONLY)
    private List<DesarrolladoresModel> desarrolladores;

    /* ====== Reglas simples ====== */
    public void aplicarDescuento(double porcentaje) {
        if (precio != null && porcentaje > 0) {
            precio -= precio * (porcentaje / 100d);
        }
    }

    public boolean esDisponible() {
        return precio != null && precio > 0;
    }
}