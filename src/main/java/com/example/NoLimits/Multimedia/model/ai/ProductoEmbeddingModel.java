package com.example.NoLimits.Multimedia.model.ai;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "producto_embeddings")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class ProductoEmbeddingModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Nullable: contenido externo (TMDB, IGDB, etc.) no tiene producto en BD
    @Column(name = "producto_id")
    private Long productoId;

    @Column(name = "titulo", columnDefinition = "TEXT")
    private String titulo;

    @Column(name = "contenido", columnDefinition = "TEXT")
    private String contenido;

    // El tipo "vector" de pgvector se guarda como TEXT en JPA
    // Hibernate lo mapea a TEXT en ddl-auto=update, pero la columna
    // ya existe como vector(1536) si se creó manualmente en Neon.
    // Para que Hibernate NO intente recrearla, usamos columnDefinition explícito.
    @Column(name = "embedding", columnDefinition = "vector(1536)")
    private String embedding;

    @Column(name = "fuente")
    private String fuente;

    @CreationTimestamp
    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion;
}