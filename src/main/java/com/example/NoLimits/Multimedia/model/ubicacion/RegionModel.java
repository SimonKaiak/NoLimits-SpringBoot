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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "regiones")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Entidad que representa una región administrativa de Chile.")
public class RegionModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único de la región", example = "13", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Column(length = 100, nullable = false, unique = true)
    @NotBlank(message = "El nombre de la región es obligatorio.")
    @Schema(description = "Nombre de la región", example = "Región Metropolitana de Santiago")
    private String nombre;

    @OneToMany(mappedBy = "region", cascade = CascadeType.ALL, orphanRemoval = false)
    @JsonIgnore
    @Schema(description = "Lista de comunas pertenecientes a la región", accessMode = Schema.AccessMode.READ_ONLY)
    private List<ComunaModel> comunas;
}