package com.example.NoLimits.Multimedia.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "tipo_empresa")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Tipo de empresa (Publisher, Desarrolladora, Distribuidora, etc.).")
public class TipoEmpresaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID del tipo de empresa", example = "1")
    private Long id;

    @Column(length = 80, nullable = false, unique = true)
    @NotBlank(message = "El nombre del tipo de empresa es obligatorio.")
    @Schema(description = "Nombre del tipo de empresa", example = "Publisher")
    private String nombre;

    @OneToMany(mappedBy = "tipoEmpresa")
    @JsonIgnore
    @Schema(
            description = "Relaciones con empresas (puente 'tipos_empresa')",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private List<TiposEmpresaModel> empresas;
}