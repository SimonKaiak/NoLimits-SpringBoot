package com.example.NoLimits.Multimedia.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "plataforma")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Plataforma en la que puede estar disponible un producto (PC, PS5, Xbox, etc.).")
public class PlataformaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID de la plataforma", example = "1")
    private Long id;

    @Column(length = 80, nullable = false, unique = true)
    @NotBlank(message = "El nombre de la plataforma es obligatorio.")
    @Schema(description = "Nombre de la plataforma", example = "PC")
    private String nombre;

    @OneToMany(mappedBy = "plataforma")
    @JsonIgnore
    @Schema(description = "Relaciones con productos (tabla puente 'plataformas')", accessMode = Schema.AccessMode.READ_ONLY)
    private List<PlataformasModel> productos;
}