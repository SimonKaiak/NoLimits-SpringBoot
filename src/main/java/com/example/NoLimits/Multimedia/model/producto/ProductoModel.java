package com.example.NoLimits.Multimedia.model.producto;

import java.util.List;

import com.example.NoLimits.Multimedia.model.catalogos.ClasificacionModel;
import com.example.NoLimits.Multimedia.model.catalogos.DesarrolladoresModel;
import com.example.NoLimits.Multimedia.model.catalogos.EmpresasModel;
import com.example.NoLimits.Multimedia.model.catalogos.EstadoModel;
import com.example.NoLimits.Multimedia.model.catalogos.GenerosModel;
import com.example.NoLimits.Multimedia.model.catalogos.PlataformasModel;
import com.example.NoLimits.Multimedia.model.catalogos.TipoProductoModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    /* ====== Campos para sagas (solo aplica a películas) ====== */

    @Column(name = "saga", length = 100)
    @Schema(
        description = "Nombre de la saga a la que pertenece el producto (solo para películas).",
        example = "Spiderman"
    )
    private String saga;

    @Column(name = "portada_saga", length = 255)
    @Schema(
        description = "Ruta o URL de la imagen usada como portada de la saga.",
        example = "/assets/img/sagas/spidermanSaga.webp"
    )
    private String portadaSaga;

    
    @Column(name = "url_compra", length = 500)
    @Size(max = 500, message = "La URL no puede superar 500 caracteres.")
    @Schema(description = "URL externa para redirigir (Steam, Netflix, etc.)", example = "https://store.steampowered.com/app/1817070/Marvels_SpiderMan_Remastered/")
    private String urlCompra;

    @Column(name = "label_compra", length = 60)
    @Size(max = 60, message = "El label no puede superar 60 caracteres.")
    @Schema(description = "Texto del botón de redirección", example = "Ver en Steam")
    private String labelCompra;

    /* ====== Relaciones N:1 ====== */

    @ManyToOne(optional = false)
    @JoinColumn(name = "tipo_producto_id", nullable = false)
    @NotNull(message = "El producto debe pertenecer a un tipo.")
    @Schema(
        description = "Tipo de producto (solo ID al crear/editar).",
        example = "{\"id\": 1}",
        accessMode = Schema.AccessMode.WRITE_ONLY
    )
    private TipoProductoModel tipoProducto;

    @ManyToOne
    @JoinColumn(name = "clasificacion_id")
    @Schema(
        description = "Clasificación del producto (solo ID al crear/editar).",
        example = "{\"id\": 2}",
        accessMode = Schema.AccessMode.WRITE_ONLY
    )
    private ClasificacionModel clasificacion;

    @ManyToOne(optional = false)
    @JoinColumn(name = "estado_id", nullable = false)
    @NotNull(message = "El producto debe tener un estado.")
    @Schema(
        description = "Estado del producto (solo ID al crear/editar).",
        example = "{\"id\": 1}",
        accessMode = Schema.AccessMode.WRITE_ONLY
    )
    private EstadoModel estado;

    /* ====== Relaciones 1:N ====== */

    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @Schema(description = "Imágenes asociadas al producto", accessMode = Schema.AccessMode.READ_ONLY)
    private List<ImagenesModel> imagenes;

    /*  Backend no maneja Venta.

        @OneToMany(mappedBy = "producto")
        @JsonIgnore
        @Schema(description = "Detalles de venta donde aparece este producto", accessMode = Schema.AccessMode.READ_ONLY)
        private List<DetalleVentaModel> detallesVenta;
    */

    /* ====== Relaciones N:M vía tablas puente ====== */

    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @Schema(description = "Relación con plataformas (puente 'plataformas')", accessMode = Schema.AccessMode.READ_ONLY)
    private List<PlataformasModel> plataformas;

    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @Schema(description = "Relación con géneros (puente 'generos')", accessMode = Schema.AccessMode.READ_ONLY)
    private List<GenerosModel> generos;

    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @Schema(description = "Relación con empresas (puente 'empresas')", accessMode = Schema.AccessMode.READ_ONLY)
    private List<EmpresasModel> empresas;

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