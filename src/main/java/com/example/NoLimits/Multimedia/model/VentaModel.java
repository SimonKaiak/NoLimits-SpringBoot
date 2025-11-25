// Ruta: src/main/java/com/example/NoLimits/Multimedia/model/VentaModel.java
package com.example.NoLimits.Multimedia.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

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
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 Clase que representa una venta dentro de la plataforma.

 La idea es que una venta tenga:
 - Una fecha y una hora en que se realizó.
 - Un usuario que la hizo.
 - Un método de pago.
 - Un método de envío.
 - Un estado (por ejemplo: pendiente, pagada, enviada).
 - Una lista de detalles, donde cada detalle es un producto del carrito.

 El total de la venta no se guarda como campo, sino que se calcula
 sumando los subtotales de sus detalles.
*/
@Entity
@Table(name = "ventas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Entidad que representa una venta realizada en la plataforma.")
public class VentaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único de la venta", example = "1")
    private Long id;

    /*
     Fecha en la que se hizo la compra.
     Por ejemplo: el día en que el usuario confirmó el pago.
    */
    @Column(nullable = false)
    @NotNull(message = "La venta requiere de una fecha de compra.")
    @Schema(description = "Fecha en que se realizó la venta", example = "2025-07-06")
    private LocalDate fechaCompra;

    /*
     Hora en la que se concretó la venta.
     Sirve para tener un registro más preciso del momento de la compra.
    */
    @Column(nullable = false)
    @NotNull(message = "La venta requiere de una hora de compra.")
    @Schema(description = "Hora exacta en la que se concretó la venta", example = "14:30")
    private LocalTime horaCompra;

    /*
     Usuario que realizó la compra.

     En la práctica, al crear la venta desde el backend, se asocia
     usando el id del usuario que viene desde la sesión o desde el request.
    */
    @ManyToOne(optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    @NotNull(message = "La venta debe estar asociada a un usuario.")
    @Schema(
        description = "Usuario que realizó la venta. Solo se requiere el ID al crear/editar.",
        example = "{\"id\": 1}",
        accessMode = Schema.AccessMode.WRITE_ONLY
    )
    private UsuarioModel usuarioModel;

    /*
     Método de pago que se usó en la venta
     (tarjeta, OnePay, billetera, etc.).
    */
    @ManyToOne(optional = false)
    @JoinColumn(name = "metodo_pago_id", nullable = false)
    @NotNull(message = "La venta debe tener un método de pago.")
    @Schema(
        description = "Método de pago utilizado. Solo se requiere el ID al crear/editar.",
        example = "{\"id\": 1}",
        accessMode = Schema.AccessMode.WRITE_ONLY
    )
    private MetodoPagoModel metodoPagoModel;

    /*
     Método de envío asociado a la venta
     (retiro en tienda, despacho, etc.).
    */
    @ManyToOne(optional = false)
    @JoinColumn(name = "metodo_envio_id", nullable = false)
    @NotNull(message = "La venta debe tener un método de envío.")
    @Schema(
        description = "Método de envío seleccionado. Solo se requiere el ID al crear/editar.",
        example = "{\"id\": 1}",
        accessMode = Schema.AccessMode.WRITE_ONLY
    )
    private MetodoEnvioModel metodoEnvioModel;

    /*
     Estado actual de la venta.

     Por ejemplo:
     - Pendiente
     - Pagada
     - Enviada
     - Cancelada
    */
    @ManyToOne(optional = false)
    @JoinColumn(name = "estado_id", nullable = false)
    @NotNull(message = "La venta debe tener un estado.")
    @Schema(
        description = "Estado de la venta (pendiente, pagada, enviada, etc.). Solo se requiere el ID al crear/editar.",
        example = "{\"id\": 1}",
        accessMode = Schema.AccessMode.WRITE_ONLY
    )
    private EstadoModel estado;

    /*
     Lista de detalles de la venta.

     Cada DetalleVentaModel representa un producto del carrito,
     con su cantidad y su subtotal.
     Se usa cascade = ALL para que, si se guarda o elimina la venta,
     también se guarden o eliminen sus detalles.
    */
    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true)
    @Schema(description = "Detalles asociados a la venta", accessMode = Schema.AccessMode.READ_ONLY)
    private List<DetalleVentaModel> detalles;

    /*
     Total de la venta calculado en base a sus detalles.

     No se guarda como columna en la base de datos. Cada vez que se llama
     a este getter, se suman los subtotales de todos los detalles.
     Si la lista de detalles está vacía o es nula, el total es 0.
    */
    @Schema(description = "Total calculado de la venta", accessMode = Schema.AccessMode.READ_ONLY)
    public Float getTotalVenta() {
        if (detalles == null || detalles.isEmpty()) return 0f;
        return detalles.stream()
                .map(DetalleVentaModel::getSubtotal)
                .reduce(0f, Float::sum);
    }
}