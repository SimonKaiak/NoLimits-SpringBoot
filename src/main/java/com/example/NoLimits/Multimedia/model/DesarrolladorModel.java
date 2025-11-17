// Ruta: src/main/java/com/example/NoLimits/Multimedia/model/DesarrolladorModel.java
package com.example.NoLimits.Multimedia.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "desarrollador")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Desarrollador (persona o estudio).")
public class DesarrolladorModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID del desarrollador", example = "1")
    private Long id;

    @Column(length = 120, nullable = false, unique = true)
    @NotBlank(message = "El nombre del desarrollador es obligatorio.")
    @Schema(description = "Nombre del desarrollador/estudio", example = "Insomniac Games")
    private String nombre;

    @OneToMany(mappedBy = "desarrollador")
    @JsonIgnore
    @Schema(
        description = "Relaciones con productos (puente 'desarrolladores')",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    private List<DesarrolladoresModel> productos;

    @OneToMany(mappedBy = "desarrollador")
    @JsonIgnore
    @Schema(
        description = "Relaciones con tipos de desarrollador (puente 'tipos_de_desarrollador')",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    private List<TiposDeDesarrolladorModel> tipos;
}