package com.example.NoLimits.Multimedia.service;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.model.EmpresaModel;
import com.example.NoLimits.Multimedia.model.EmpresasModel;
import com.example.NoLimits.Multimedia.model.ProductoModel;
import com.example.NoLimits.Multimedia.repository.EmpresaRepository;
import com.example.NoLimits.Multimedia.repository.EmpresasRepository;
import com.example.NoLimits.Multimedia.repository.ProductoRepository;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public EmpresasModel findById(Long id) {
        return empresasRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Relación Producto-Empresa no encontrada con ID: " + id));
    }

    /**
     * Crea vínculo Producto ↔ Empresa si no existe (tabla puente).
     */
    public EmpresasModel link(Long productoId, Long empresaId) {
        ProductoModel p = productoRepository.findById(productoId)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Producto no encontrado con ID: " + productoId));
        EmpresaModel e = empresaRepository.findById(empresaId)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Empresa no encontrada con ID: " + empresaId));

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

    /**
     * PATCH: actualización parcial de la relación Producto ↔ Empresa.
     * Permite cambiar el producto y/o la empresa, validando que la nueva
     * combinación no duplique otra relación existente.
     */
    public EmpresasModel patch(Long id, EmpresasModel in) {

        EmpresasModel existente = findById(id);

        Long productoIdActual = existente.getProducto().getId();
        Long empresaIdActual = existente.getEmpresa().getId();

        Long nuevoProductoId = productoIdActual;
        Long nuevaEmpresaId = empresaIdActual;

        // Actualizar producto si viene uno nuevo
        if (in.getProducto() != null && in.getProducto().getId() != null) {
            ProductoModel nuevoProducto = productoRepository.findById(in.getProducto().getId())
                    .orElseThrow(() ->
                            new RecursoNoEncontradoException("Producto no encontrado con ID: " + in.getProducto().getId()));
            existente.setProducto(nuevoProducto);
            nuevoProductoId = nuevoProducto.getId();
        }

        // Actualizar empresa si viene una nueva
        if (in.getEmpresa() != null && in.getEmpresa().getId() != null) {
            EmpresaModel nuevaEmpresa = empresaRepository.findById(in.getEmpresa().getId())
                    .orElseThrow(() ->
                            new RecursoNoEncontradoException("Empresa no encontrada con ID: " + in.getEmpresa().getId()));
            existente.setEmpresa(nuevaEmpresa);
            nuevaEmpresaId = nuevaEmpresa.getId();
        }

        boolean cambiaProducto = !nuevoProductoId.equals(productoIdActual);
        boolean cambiaEmpresa = !nuevaEmpresaId.equals(empresaIdActual);

        if (cambiaProducto || cambiaEmpresa) {
            // Si la combinación nueva ya existe en otra fila, se bloquea
            if (empresasRepository.existsByProducto_IdAndEmpresa_Id(nuevoProductoId, nuevaEmpresaId)) {
                throw new IllegalStateException("La relación Producto-Empresa ya existe");
            }
        }

        return empresasRepository.save(existente);
    }

    /**
     * Elimina el vínculo Producto ↔ Empresa (idempotente).
     */
    public void unlink(Long productoId, Long empresaId) {
        // validar entidades (mensajes 404 claros)
        productoRepository.findById(productoId)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Producto no encontrado con ID: " + productoId));
        empresaRepository.findById(empresaId)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Empresa no encontrada con ID: " + empresaId));

        if (!empresasRepository.existsByProducto_IdAndEmpresa_Id(productoId, empresaId)) {
            return; // nada que borrar, operación idempotente
        }

        empresasRepository.deleteByProducto_IdAndEmpresa_Id(productoId, empresaId);
    }
}