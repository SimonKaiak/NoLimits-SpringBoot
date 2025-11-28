package com.example.NoLimits.Multimedia.dto.ubicacion.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Datos de salida de una dirección registrada.")
public class DireccionResponseDTO {

    @Schema(example = "1", description = "ID único de la dirección")
    private Long id;

    @Schema(example = "Av. Siempre Viva", description = "Nombre de la calle o avenida")
    private String calle;

    @Schema(example = "742", description = "Número de la propiedad")
    private String numero;

    @Schema(example = "Depto 1204-B", description = "Complemento de la dirección")
    private String complemento;

    @Schema(example = "8320000", description = "Código postal")
    private String codigoPostal;

    @Schema(example = "Santiago Centro", description = "Nombre de la comuna")
    private String comuna;

    @Schema(example = "Metropolitana", description = "Nombre de la región")
    private String region;

    @Schema(example = "true", description = "Indica si la dirección está activa")
    private Boolean activo;
}