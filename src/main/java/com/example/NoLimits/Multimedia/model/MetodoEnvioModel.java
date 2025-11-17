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
@Table(name = "metodos_envio")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Método de envío disponible para entregar pedidos/ventas.")
public class MetodoEnvioModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único del método de envío", example = "1")
    private Long id;

    @Column(length = 80, nullable = false, unique = true)
    @NotBlank(message = "El nombre del método de envío es obligatorio.")
    @Schema(description = "Nombre del método de envío", example = "Retiro en tienda")
    private String nombre;

    @Column(length = 255)
    @Schema(description = "Descripción del método de envío", example = "Retiro presencial en sucursal Plaza Oeste")
    private String descripcion;

    @Column(nullable = false)
    @NotNull(message = "El campo 'activo' es obligatorio.")
    @Schema(description = "Indica si el método está activo", example = "true")
    private Boolean activo = true;

    @OneToMany(mappedBy = "metodoEnvioModel")
    @JsonIgnore
    @Schema(description = "Ventas que utilizaron este método de envío", accessMode = Schema.AccessMode.READ_ONLY)
    private List<VentaModel> ventas;

    public boolean esActivo() {
        return Boolean.TRUE.equals(activo);
    }
}