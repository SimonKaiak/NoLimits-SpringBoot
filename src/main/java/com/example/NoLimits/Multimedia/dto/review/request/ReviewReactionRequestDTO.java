package com.example.NoLimits.Multimedia.dto.review.request;

import com.example.NoLimits.Multimedia.model.review.TipoReaccion;

public class ReviewReactionRequestDTO {

    private TipoReaccion tipoReaccion;

    public ReviewReactionRequestDTO() {
    }

    public TipoReaccion getTipoReaccion() {
        return tipoReaccion;
    }

    public void setTipoReaccion(TipoReaccion tipoReaccion) {
        this.tipoReaccion = tipoReaccion;
    }
}