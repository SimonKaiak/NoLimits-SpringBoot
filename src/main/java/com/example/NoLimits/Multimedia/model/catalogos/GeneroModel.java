package com.example.NoLimits.Multimedia.model.catalogos;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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