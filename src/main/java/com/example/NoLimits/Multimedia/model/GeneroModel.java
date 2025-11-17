package com.example.NoLimits.Multimedia.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "genero")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Género de un producto (Acción, Aventura, Terror, etc.).")
public class GeneroModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID del género", example = "1")
    private Long id;

    @Column(length = 80, nullable = false, unique = true)
    @NotBlank(message = "El nombre del género es obligatorio.")
    @Schema(description = "Nombre del género", example = "Acción")
    private String nombre;

    @OneToMany(mappedBy = "genero")
    @JsonIgnore
    @Schema(description = "Relaciones con productos (puente 'generos')", accessMode = Schema.AccessMode.READ_ONLY)
    private List<GenerosModel> productos;
}