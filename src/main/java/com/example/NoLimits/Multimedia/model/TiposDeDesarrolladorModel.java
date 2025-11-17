// Ruta: src/main/java/com/example/NoLimits/Multimedia/model/TiposDeDesarrolladorModel.java
package com.example.NoLimits.Multimedia.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(
    name = "tipos_de_desarrollador",
    uniqueConstraints = @UniqueConstraint(columnNames = {"desarrollador_id", "tipo_de_desarrollador_id"})
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Tabla puente entre Desarrollador y TipoDeDesarrollador.")
public class TiposDeDesarrolladorModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID de la relación Desarrollador-TipoDeDesarrollador", example = "1")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "desarrollador_id", nullable = false)
    @NotNull(message = "La relación requiere un desarrollador.")
    @Schema(
        description = "Desarrollador asociado",
        accessMode = Schema.AccessMode.WRITE_ONLY
    )
    private DesarrolladorModel desarrollador;

    @ManyToOne(optional = false)
    @JoinColumn(name = "tipo_de_desarrollador_id", nullable = false)
    @NotNull(message = "La relación requiere un tipo de desarrollador.")
    @Schema(
        description = "Tipo de desarrollador asociado",
        accessMode = Schema.AccessMode.WRITE_ONLY
    )
    private TipoDeDesarrolladorModel tipoDeDesarrollador;
}