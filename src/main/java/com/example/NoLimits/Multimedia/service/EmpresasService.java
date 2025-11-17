// Ruta: src/main/java/com/example/NoLimits/Multimedia/service/EmpresasService.java
package com.example.NoLimits.Multimedia.service;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.model.EmpresaModel;
import com.example.NoLimits.Multimedia.model.EmpresasModel;
import com.example.NoLimits.Multimedia.model.ProductoModel;
import com.example.NoLimits.Multimedia.repository.EmpresaRepository;
import com.example.NoLimits.Multimedia.repository.EmpresasRepository;
import com.example.NoLimits.Multimedia.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class EmpresasService {

    @Autowired
    private EmpresasRepository empresasRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    public List<EmpresasModel> findByProducto(Long productoId) {
        return empresasRepository.findByProducto_Id(productoId);
    }

    public List<EmpresasModel> findByEmpresa(Long empresaId) {
        return empresasRepository.findByEmpresa_Id(empresaId);
    }

    /** Crea vínculo Producto ↔ Empresa si no existe (tabla puente). */
    public EmpresasModel link(Long productoId, Long empresaId) {
        ProductoModel p = productoRepository.findById(productoId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado con ID: " + productoId));
        EmpresaModel e = empresaRepository.findById(empresaId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Empresa no encontrada con ID: " + empresaId));

        if (empresasRepository.existsByProducto_IdAndEmpresa_Id(productoId, empresaId)) {
            // ya existe; devolver la existente para idempotencia
            return empresasRepository.findByProducto_Id(productoId).stream()
                    .filter(rel -> rel.getEmpresa().getId().equals(empresaId))
                    .findFirst()
                    .orElseGet(() -> {
                        // fallback defensivo (no se persiste porque ya existe una igual)
                        EmpresasModel rel = new EmpresasModel();
                        rel.setProducto(p);
                        rel.setEmpresa(e);
                        return rel;
                    });
        }

        EmpresasModel rel = new EmpresasModel();
        rel.setProducto(p);
        rel.setEmpresa(e);
        return empresasRepository.save(rel);
    }

    /** Elimina el vínculo Producto ↔ Empresa (idempotente). */
    public void unlink(Long productoId, Long empresaId) {
        // validar entidades (mensajes 404 claros)
        productoRepository.findById(productoId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado con ID: " + productoId));
        empresaRepository.findById(empresaId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Empresa no encontrada con ID: " + empresaId));

        if (!empresasRepository.existsByProducto_IdAndEmpresa_Id(productoId, empresaId)) {
            return; // nada que borrar, operación idempotente
        }

        empresasRepository.deleteByProducto_IdAndEmpresa_Id(productoId, empresaId);
    }
}