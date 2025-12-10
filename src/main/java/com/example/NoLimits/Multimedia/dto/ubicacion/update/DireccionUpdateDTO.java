package com.example.NoLimits.Multimedia.dto.ubicacion.update;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO para actualizar una dirección.")
public class DireccionUpdateDTO {

    @Schema(description = "Nombre de la calle", example = "Av. Providencia")
    private String calle;

    @Schema(description = "Número de la propiedad", example = "1234")
    private String numero;

    @Schema(description = "Complemento de la dirección", example = "Depto 402")
    private String complemento;

    @Schema(description = "Código postal", example = "7500000")
    private String codigoPostal;

    @Schema(description = "ID de la comuna asociada", example = "13114")
    private Long comunaId;

    @Schema(description = "Indica si la dirección está activa", example = "true")
    private Boolean activo;

    @Schema(description = "ID del usuario asociado", example = "5")
    private Long usuarioId;
}