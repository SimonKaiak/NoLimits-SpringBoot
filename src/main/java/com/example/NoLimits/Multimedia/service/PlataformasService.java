package com.example.NoLimits.Multimedia.service;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.model.PlataformaModel;
import com.example.NoLimits.Multimedia.model.PlataformasModel;
import com.example.NoLimits.Multimedia.model.ProductoModel;
import com.example.NoLimits.Multimedia.repository.PlataformaRepository;
import com.example.NoLimits.Multimedia.repository.PlataformasRepository;
import com.example.NoLimits.Multimedia.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class PlataformasService {

    @Autowired
    private PlataformasRepository plataformasRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private PlataformaRepository plataformaRepository;

    public List<PlataformasModel> findByProducto(Long productoId) {
        return plataformasRepository.findByProducto_Id(productoId);
    }

    public List<PlataformasModel> findByPlataforma(Long plataformaId) {
        return plataformasRepository.findByPlataforma_Id(plataformaId);
    }

    /** Crea el vínculo Producto ↔ Plataforma si no existe. */
    public PlataformasModel link(Long productoId, Long plataformaId) {
        ProductoModel p = productoRepository.findById(productoId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado con ID: " + productoId));
        PlataformaModel pl = plataformaRepository.findById(plataformaId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Plataforma no encontrada con ID: " + plataformaId));

        if (plataformasRepository.existsByProducto_IdAndPlataforma_Id(productoId, plataformaId)) {
            return plantasExisting(productoId, plataformaId, p, pl);
        }

        PlataformasModel rel = new PlataformasModel();
        rel.setProducto(p);
        rel.setPlataforma(pl);
        return plataformasRepository.save(rel);
    }

    private PlataformasModel plantasExisting(Long productoId, Long plataformaId, ProductoModel p, PlataformaModel pl) {
        return plataformasRepository.findByProducto_Id(productoId).stream()
                .filter(r -> r.getPlataforma().getId().equals(plataformaId))
                .findFirst()
                .orElseGet(() -> {
                    PlataformasModel r = new PlataformasModel();
                    r.setProducto(p);
                    r.setPlataforma(pl);
                    return r;
                });
    }

    /** Elimina el vínculo Producto ↔ Plataforma. */
    public void unlink(Long productoId, Long plataformaId) {
        productoRepository.findById(productoId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado con ID: " + productoId));
        plataformaRepository.findById(plataformaId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Plataforma no encontrada con ID: " + plataformaId));

        if (plataformasRepository.existsByProducto_IdAndPlataforma_Id(productoId, plataformaId)) {
            plataformasRepository.deleteByProducto_IdAndPlataforma_Id(productoId, plataformaId);
        }
    }
}