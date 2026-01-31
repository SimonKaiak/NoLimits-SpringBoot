package com.example.NoLimits.Multimedia.dto.producto.request;

import lombok.Data;

@Data
public class LinkCompraDTO {
    private Long plataformaId;
    private String url;
    private String label; 
}