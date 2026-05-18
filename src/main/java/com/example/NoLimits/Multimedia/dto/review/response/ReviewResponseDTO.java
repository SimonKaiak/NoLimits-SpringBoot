package com.example.NoLimits.Multimedia.dto.review.response;

import java.time.LocalDateTime;

public class ReviewResponseDTO {

    private Long id;
    private Long usuarioId;
    private String nombreUsuario;
    private String obraId;
    private String contenido;
    private Integer rating;
    private Boolean editado;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    private Long likes;
    private Long dislikes;

    public ReviewResponseDTO() {
    }

    public ReviewResponseDTO(
        Long id,
        Long usuarioId,
        String nombreUsuario,
        String obraId,
        String contenido,
        Integer rating,
        Boolean editado,
        LocalDateTime fechaCreacion,
        LocalDateTime fechaActualizacion,
        Long likes,
        Long dislikes
    ) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.nombreUsuario = nombreUsuario;
        this.obraId = obraId;
        this.contenido = contenido;
        this.rating = rating;
        this.editado = editado;
        this.fechaCreacion = fechaCreacion;
        this.fechaActualizacion = fechaActualizacion;
        this.likes = likes;
        this.dislikes = dislikes;
    }

    public Long getId() {
        return id;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public String getObraId() {
        return obraId;
    }

    public String getContenido() {
        return contenido;
    }

    public Integer getRating() {
        return rating;
    }

    public Boolean getEditado() {
        return editado;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public Long getLikes() {
        return likes;
    }

    public Long getDislikes() {
        return dislikes;
    }
}