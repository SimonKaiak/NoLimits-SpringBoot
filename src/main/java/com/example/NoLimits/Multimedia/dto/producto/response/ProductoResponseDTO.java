package com.example.NoLimits.Multimedia.dto.producto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.util.List;

@Data
@Schema(description = "DTO que representa un producto completo para el frontend.")
public class ProductoResponseDTO {

    private Long id;
    private String nombre;
    private Double precio;

    private Long tipoProductoId;
    private String tipoProductoNombre;

    private Long clasificacionId;
    private String clasificacionNombre;

    private Long estadoId;
    private String estadoNombre;

    private List<String> plataformas;
    private List<String> generos;
    private List<String> empresas;
    private List<String> desarrolladores;

    private List<String> imagenes;
}