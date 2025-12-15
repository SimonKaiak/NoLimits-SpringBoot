package com.example.NoLimits.Multimedia.model;

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
@Table(name = "empresa")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Empresa (publisher, distribuidora, estudio, etc.).")
public class EmpresaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID de la empresa", example = "1")
    private Long id;

    @Column(length = 120, nullable = false, unique = true)
    @NotBlank(message = "El nombre de la empresa es obligatorio.")
    @Schema(description = "Nombre de la empresa", example = "Sony Pictures")
    private String nombre;

    @Column(nullable = false)
    @Schema(description = "Indica si la empresa está activa", example = "true")
    private Boolean activo = true;

    @OneToMany(mappedBy = "empresa")
    @JsonIgnore
    @Schema(
            description = "Relaciones con productos (puente 'empresas')",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private List<EmpresasModel> productos;

    @OneToMany(mappedBy = "empresa")
    @JsonIgnore
    @Schema(
            description = "Relaciones con tipos de empresa (puente 'tipos_empresa')",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private List<TiposEmpresaModel> tipos;
}