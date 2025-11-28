package com.example.NoLimits.Multimedia.dto.producto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.util.List;

@Data
@Schema(description = "DTO para crear un producto.")
public class ProductoRequestDTO {

    @NotBlank(message = "El nombre del producto es obligatorio.")
    @Size(min = 2, max = 100)
    @Schema(example = "Spider-Man (2002)")
    private String nombre;

    @NotNull(message = "El precio es obligatorio.")
    @Schema(example = "12990")
    private Double precio;

    @NotNull
    @Schema(description = "ID del tipo de producto", example = "1")
    private Long tipoProductoId;

    @Schema(description = "ID de clasificación", example = "2")
    private Long clasificacionId;

    @NotNull
    @Schema(description = "ID del estado", example = "1")
    private Long estadoId;

    // Relaciones N:M
    private List<Long> plataformasIds;
    private List<Long> generosIds;
    private List<Long> empresasIds;
    private List<Long> desarrolladoresIds;
}