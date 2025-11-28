package com.example.NoLimits.Multimedia.dto.producto.update;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.util.List;

@Data
@Schema(description = "DTO para actualizar un producto existente.")
public class ProductoUpdateDTO {

    @Schema(example = "Spider-Man Remaster")
    private String nombre;

    @Schema(example = "14990")
    private Double precio;

    private Long tipoProductoId;
    private Long clasificacionId;
    private Long estadoId;

    private List<Long> plataformasIds;
    private List<Long> generosIds;
    private List<Long> empresasIds;
    private List<Long> desarrolladoresIds;
}