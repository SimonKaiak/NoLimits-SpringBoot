package com.example.NoLimits.Multimedia.dto.producto.update;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LinkCompraUpdateDTO {

    private Long plataformaId;
    private String url;
    private String label;
}