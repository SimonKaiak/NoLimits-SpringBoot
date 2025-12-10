package com.example.NoLimits.Multimedia.model.ubicacion;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "comunas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Entidad que representa una comuna perteneciente a una región.")
public class ComunaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único de la comuna", example = "13101", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Column(length = 120, nullable = false, unique = true)
    @NotBlank(message = "El nombre de la comuna es obligatorio.")
    @Schema(description = "Nombre de la comuna", example = "Santiago")
    private String nombre;

    @ManyToOne(optional = false)
    @JoinColumn(name = "region_id", nullable = false)
    @NotNull(message = "La comuna debe pertenecer a una región.")
    @Schema(description = "Región a la que pertenece la comuna", example = "{\"id\": 13}", accessMode = Schema.AccessMode.WRITE_ONLY)
    private RegionModel region;

    @OneToMany(mappedBy = "comuna", cascade = CascadeType.ALL, orphanRemoval = false)
    @JsonIgnore
    @Schema(description = "Direcciones asociadas a esta comuna", accessMode = Schema.AccessMode.READ_ONLY)
    private List<DireccionModel> direcciones;
}