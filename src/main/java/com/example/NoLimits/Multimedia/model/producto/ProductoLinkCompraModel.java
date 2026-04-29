package com.example.NoLimits.Multimedia.model.producto;

import java.time.LocalDateTime;

import com.example.NoLimits.Multimedia.model.catalogos.PlataformaModel;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "producto_links_compra",
       uniqueConstraints = @UniqueConstraint(columnNames = {"producto_id", "plataforma_id"}))
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor
public class ProductoLinkCompraModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "producto_id", nullable = false)
    @JsonIgnore
    private ProductoModel producto;

    @ManyToOne(optional = false)
    @JoinColumn(name = "plataforma_id", nullable = false)
    @JsonIgnore
    private PlataformaModel plataforma;

    @Column(nullable = false, length = 500)
    private String url;

    @Column(length = 60)
    private String label;

    @Column(name = "nombre_plataforma", length = 150)
    private String nombrePlataforma;

    @Column(name = "app_id", length = 80)
    private String appId;

    @Column(name = "precio_actual")
    private Double precioActual;

    @Column(name = "precio_formato", length = 80)
    private String precioFormato;

    @Column(length = 10)
    private String moneda;

    @Column(name = "fecha_ultima_actualizacion")
    private LocalDateTime fechaUltimaActualizacion;
}