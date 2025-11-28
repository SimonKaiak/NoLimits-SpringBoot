package com.example.NoLimits.Multimedia.dto.ubicacion.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Datos necesarios para crear o actualizar una dirección.")
public class DireccionRequestDTO {

    @Schema(example = "Av. Siempre Viva", description = "Nombre de la calle o avenida")
    private String calle;

    @Schema(example = "742", description = "Número de la propiedad")
    private String numero;

    @Schema(example = "Depto 1204-B", description = "Departamento, block u otra especificación")
    private String complemento;

    @Schema(example = "8320000", description = "Código postal")
    private String codigoPostal;

    @Schema(example = "13101", description = "ID de la comuna a la que pertenece la dirección")
    private Long comunaId;

    @Schema(example = "5", description = "ID del usuario dueño de esta dirección")
    private Long usuarioId;

    @Schema(example = "true", description = "Si no se envía, se puede asumir true por defecto")
    private Boolean activo;
}