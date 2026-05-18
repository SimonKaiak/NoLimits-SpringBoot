package com.example.NoLimits.Multimedia.dto.review.request;

public class ReviewRequestDTO {

    private String obraId;
    private String contenido;
    private Integer rating;

    public ReviewRequestDTO() {
    }

    public String getObraId() {
        return obraId;
    }

    public void setObraId(String obraId) {
        this.obraId = obraId;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }
}