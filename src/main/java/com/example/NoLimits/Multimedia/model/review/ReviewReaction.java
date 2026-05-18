package com.example.NoLimits.Multimedia.model.review;

import com.example.NoLimits.Multimedia.model.usuario.UsuarioModel;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(
    name = "review_reactions",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"review_id", "usuario_id"})
    }
)
public class ReviewReaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "review_id", nullable = false)
    private Review review;

    @ManyToOne(optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private UsuarioModel usuario;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_reaccion", nullable = false)
    private TipoReaccion tipoReaccion;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @PrePersist
    public void prePersist() {
        this.fechaCreacion = LocalDateTime.now();
    }

    public ReviewReaction() {
    }

    public Long getId() {
        return id;
    }

    public Review getReview() {
        return review;
    }

    public void setReview(Review review) {
        this.review = review;
    }

    public UsuarioModel getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioModel usuario) {
        this.usuario = usuario;
    }

    public TipoReaccion getTipoReaccion() {
        return tipoReaccion;
    }

    public void setTipoReaccion(TipoReaccion tipoReaccion) {
        this.tipoReaccion = tipoReaccion;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }
}