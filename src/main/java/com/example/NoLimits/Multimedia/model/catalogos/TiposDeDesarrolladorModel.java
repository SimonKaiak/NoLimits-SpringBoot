package com.example.NoLimits.Multimedia.model.catalogos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Table(
    name = "tipos_de_desarrollador",
    uniqueConstraints = @UniqueConstraint(columnNames = {"desarrollador_id", "tipo_de_desarrollador_id"})
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"desarrollador","tipoDeDesarrollador"})
@Schema(description = "Tabla puente entre Desarrollador y TipoDeDesarrollador.")
public class TiposDeDesarrolladorModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Schema(description = "ID de la relación Desarrollador-TipoDeDesarrollador", example = "1")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "desarrollador_id", nullable = false)
    private DesarrolladorModel desarrollador;

    @ManyToOne(optional = false)
    @JoinColumn(name = "tipo_de_desarrollador_id", nullable = false)
    private TipoDeDesarrolladorModel tipoDeDesarrollador;
}