package com.example.NoLimits.Multimedia.model.review;

import com.example.NoLimits.Multimedia.model.usuario.UsuarioModel;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private UsuarioModel usuario;

    @Column(name = "obra_id", nullable = false)
    private String obraId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String contenido;

    private Integer rating;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @ManyToOne
    @JoinColumn(name = "parent_review_id")
    private Review parentReview;

    @ManyToOne
    @JoinColumn(name = "root_review_id")
    private Review rootReview;

    @OneToMany(
        mappedBy = "parentReview",
        cascade = CascadeType.REMOVE,
        orphanRemoval = true
    )
    private List<Review> replies = new ArrayList<>();

    public Review getParentReview() {
        return parentReview;
    }

    public void setParentReview(Review parentReview) {
        this.parentReview = parentReview;
    }

    public Review getRootReview() {
        return rootReview;
    }

    public void setRootReview(Review rootReview) {
        this.rootReview = rootReview;
    }

    public List<Review> getReplies() {
        return replies;
    }

    public void setReplies(List<Review> replies) {
        this.replies = replies;
    }

    @PrePersist
    public void prePersist() {
        this.fechaCreacion = LocalDateTime.now();
        this.fechaActualizacion = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.fechaActualizacion = LocalDateTime.now();
    }

    public Review() {
    }

    public Long getId() {
        return id;
    }

    public UsuarioModel getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioModel usuario) {
        this.usuario = usuario;
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

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }
}