package com.example.NoLimits.Multimedia.service.catalogos;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.dto.catalogos.response.EmpresasResponseDTO;
import com.example.NoLimits.Multimedia.dto.catalogos.update.EmpresasUpdateDTO;
import com.example.NoLimits.Multimedia.model.catalogos.EmpresaModel;
import com.example.NoLimits.Multimedia.model.catalogos.EmpresasModel;
import com.example.NoLimits.Multimedia.model.producto.ProductoModel;
import com.example.NoLimits.Multimedia.repository.catalogos.EmpresaRepository;
import com.example.NoLimits.Multimedia.repository.catalogos.EmpresasRepository;
import com.example.NoLimits.Multimedia.repository.producto.ProductoRepository;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class EmpresasService {

    @Autowired private EmpresasRepository empresasRepository;
    @Autowired private ProductoRepository productoRepository;
    @Autowired private EmpresaRepository empresaRepository;

    // ================== MAPPER ==================

    private EmpresasResponseDTO toResponseDTO(EmpresasModel rel) {
        EmpresasResponseDTO dto = new EmpresasResponseDTO();
        dto.setId(rel.getId());
        dto.setProductoId(rel.getProducto().getId());
        dto.setEmpresaId(rel.getEmpresa().getId());
        dto.setEmpresaNombre(rel.getEmpresa().getNombre());
        return dto;
    }

    // ================== CONSULTAS ==================

    public List<EmpresasResponseDTO> findByProducto(Long productoId) {
        return empresasRepository.findByProducto_Id(productoId)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    // ================== LINK ==================

    public EmpresasResponseDTO link(Long productoId, Long empresaId) {

        ProductoModel p = productoRepository.findById(productoId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado con ID: " + productoId));

        EmpresaModel e = empresaRepository.findById(empresaId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Empresa no encontrada con ID: " + empresaId));

        if (empresasRepository.existsByProducto_IdAndEmpresa_Id(productoId, empresaId)) {
            return empresasRepository.findByProducto_Id(productoId).stream()
                    .filter(rel -> rel.getEmpresa().getId().equals(empresaId))
                    .findFirst()
                    .map(this::toResponseDTO)
                    .orElseThrow();
        }

        EmpresasModel rel = new EmpresasModel();
        rel.setProducto(p);
        rel.setEmpresa(e);

        return toResponseDTO(empresasRepository.save(rel));
    }

    // ================== PATCH ==================

    public EmpresasResponseDTO patch(Long id, EmpresasUpdateDTO dto) {

        EmpresasModel rel = empresasRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Relación no encontrada con ID: " + id));

        if (dto.getProductoId() != null) {
            ProductoModel producto = productoRepository.findById(dto.getProductoId())
                    .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado con ID: " + dto.getProductoId()));
            rel.setProducto(producto);
        }

        if (dto.getEmpresaId() != null) {
            EmpresaModel empresa = empresaRepository.findById(dto.getEmpresaId())
                    .orElseThrow(() -> new RecursoNoEncontradoException("Empresa no encontrada con ID: " + dto.getEmpresaId()));
            rel.setEmpresa(empresa);
        }

        return toResponseDTO(empresasRepository.save(rel));
    }

    // ================== DELETE ==================

    public void unlink(Long productoId, Long empresaId) {
        empresasRepository.deleteByProducto_IdAndEmpresa_Id(productoId, empresaId);
    }
}