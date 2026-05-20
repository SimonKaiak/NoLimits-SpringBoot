package com.example.NoLimits.Multimedia.dto.usuario.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FavoritoRequestDTO {
    private String obraId;
    private String titulo;
    private String tipo;
    private String poster;
    private String source;
}