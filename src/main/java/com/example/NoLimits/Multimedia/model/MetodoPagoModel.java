package com.example.NoLimits.Multimedia.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "metodos_pago")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Método de pago disponible para las ventas.")
public class MetodoPagoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único del método de pago", example = "1")
    private Long id;

    @Column(length = 50, nullable = false, unique = true)
    @NotBlank(message = "El nombre del método de pago es obligatorio.")
    @Schema(description = "Nombre del método de pago", example = "Tarjeta de Crédito")
    private String nombre;

    @Column(nullable = false)
    @NotNull(message = "El campo 'activo' es obligatorio.")
    @Schema(description = "Indica si el método está activo", example = "true")
    private Boolean activo = true;

    @OneToMany(mappedBy = "metodoPagoModel")
    @JsonIgnore
    @Schema(description = "Ventas asociadas a este método de pago", accessMode = Schema.AccessMode.READ_ONLY)
    private List<VentaModel> ventas;

    public boolean validarMetodo() {
        return nombre != null && !nombre.trim().isEmpty();
    }

    public boolean esActivo() {
        return Boolean.TRUE.equals(activo);
    }
}