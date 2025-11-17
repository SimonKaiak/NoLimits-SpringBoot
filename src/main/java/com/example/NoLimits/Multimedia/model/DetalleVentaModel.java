package com.example.NoLimits.Multimedia.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "detalles_venta")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Línea/detalle de una venta: producto, cantidad y precios.")
public class DetalleVentaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único del detalle de venta", example = "1")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "venta_id", nullable = false)
    @NotNull(message = "El detalle debe pertenecer a una venta.")
    @Schema(description = "Venta a la que pertenece este detalle", accessMode = Schema.AccessMode.WRITE_ONLY)
    private VentaModel venta;

    @ManyToOne(optional = false)
    @JoinColumn(name = "producto_id", nullable = false)
    @NotNull(message = "El detalle requiere un producto.")
    @Schema(description = "Producto vendido (solo ID al crear/editar).", example = "{\"id\": 10}", accessMode = Schema.AccessMode.WRITE_ONLY)
    private ProductoModel producto;

    @Column(nullable = false)
    @Min(value = 1, message = "La cantidad mínima es 1.")
    @Schema(description = "Cantidad vendida", example = "2", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer cantidad = 1;

    /** Precio unitario cobrado en esta línea (congelado al momento de la venta). */
    @Column(nullable = false)
    @NotNull(message = "Debe indicarse el precio unitario.")
    @Schema(description = "Precio unitario al momento de la venta", example = "12990")
    private Float precioUnitario;

    /** Subtotal calculado (cantidad * precioUnitario). */
    @Schema(description = "Subtotal calculado de la línea", accessMode = Schema.AccessMode.READ_ONLY)
    public Float getSubtotal() {
        if (precioUnitario == null || cantidad == null) return 0f;
        return precioUnitario * cantidad;
    }
}