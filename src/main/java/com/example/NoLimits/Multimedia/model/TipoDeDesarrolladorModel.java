// Ruta: src/main/java/com/example/NoLimits/Multimedia/model/TipoDeDesarrolladorModel.java
package com.example.NoLimits.Multimedia.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "tipo_de_desarrollador")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Tipo de desarrollador (Lead, Estudio, Publisher, Freelancer, etc.).")
public class TipoDeDesarrolladorModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID del tipo de desarrollador", example = "1")
    private Long id;

    @Column(length = 80, nullable = false, unique = true)
    @NotBlank(message = "El nombre del tipo de desarrollador es obligatorio.")
    @Schema(description = "Nombre del tipo", example = "Estudio")
    private String nombre;

    @OneToMany(mappedBy = "tipoDeDesarrollador")
    @JsonIgnore
    @Schema(
        description = "Relaciones con desarrolladores (puente 'tipos_de_desarrollador')",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    private List<TiposDeDesarrolladorModel> desarrolladores;
}