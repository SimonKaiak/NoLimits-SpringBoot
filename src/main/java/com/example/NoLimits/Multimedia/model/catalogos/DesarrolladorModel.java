package com.example.NoLimits.Multimedia.model.catalogos;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "desarrollador")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class DesarrolladorModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @ToString.Include
    private Long id;

    @Column(length = 120, nullable = false, unique = true)
    @ToString.Include
    private String nombre;

    @Column(nullable = false)
    private boolean activo = true;

    @OneToMany(mappedBy = "desarrollador")
    @JsonIgnore
    private List<DesarrolladoresModel> productos;

    @OneToMany(mappedBy = "desarrollador")
    @JsonIgnore
    private List<TiposDeDesarrolladorModel> tipos;
}