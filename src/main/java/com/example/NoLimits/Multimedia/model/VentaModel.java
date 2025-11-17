package com.example.NoLimits.Multimedia.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @Column(nullable = false)
    @NotNull(message = "La venta requiere de una fecha de compra.")
    @Schema(description = "Fecha en que se realizó la venta", example = "2025-07-06")
    private LocalDate fechaCompra;

    @Column(nullable = false)
    @NotNull(message = "La venta requiere de una hora de compra.")
    @Schema(description = "Hora exacta en la que se concretó la venta", example = "14:30")
    private LocalTime horaCompra;

    /* ===== Relaciones principales ===== */

    @ManyToOne(optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    @Schema(description = "Usuario que realizó la venta", accessMode = Schema.AccessMode.READ_ONLY)
    private UsuarioModel usuarioModel;

    @ManyToOne(optional = false)
    @JoinColumn(name = "metodo_pago_id", nullable = false)
    @Schema(description = "Método de pago utilizado", accessMode = Schema.AccessMode.READ_ONLY)
    private MetodoPagoModel metodoPagoModel;

    @ManyToOne(optional = false)
    @JoinColumn(name = "metodo_envio_id", nullable = false)
    @Schema(description = "Método de envío seleccionado", accessMode = Schema.AccessMode.READ_ONLY)
    private MetodoEnvioModel metodoEnvioModel;

    @ManyToOne(optional = false)
    @JoinColumn(name = "estado_id", nullable = false)
    @Schema(description = "Estado de la venta (pendiente, pagada, enviada, etc.)",
            accessMode = Schema.AccessMode.READ_ONLY)
    private EstadoModel estado;

    /* Una venta tiene varios detalles */
    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @Schema(description = "Detalles asociados a la venta", accessMode = Schema.AccessMode.READ_ONLY)
    private List<DetalleVentaModel> detalles;

    /** Total calculado como suma de subtotales de los detalles. */
    @Schema(description = "Total calculado de la venta", accessMode = Schema.AccessMode.READ_ONLY)
    public Float getTotalVenta() {
        if (detalles == null || detalles.isEmpty()) return 0f;
        return detalles.stream()
                .map(DetalleVentaModel::getSubtotal)
                .reduce(0f, Float::sum);
    }
}