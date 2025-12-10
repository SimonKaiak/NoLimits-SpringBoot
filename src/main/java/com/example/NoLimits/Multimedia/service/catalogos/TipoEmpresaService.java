package com.example.NoLimits.Multimedia.service.catalogos;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.dto.catalogos.request.TipoEmpresaRequestDTO;
import com.example.NoLimits.Multimedia.dto.catalogos.response.TipoEmpresaResponseDTO;
import com.example.NoLimits.Multimedia.dto.catalogos.update.TipoEmpresaUpdateDTO;
import com.example.NoLimits.Multimedia.dto.pagination.PagedResponse;
import com.example.NoLimits.Multimedia.model.catalogos.TipoEmpresaModel;
import com.example.NoLimits.Multimedia.repository.catalogos.TipoEmpresaRepository;
import com.example.NoLimits.Multimedia.repository.catalogos.TiposEmpresaRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class TipoEmpresaService {

    @Autowired
    private TipoEmpresaRepository repository;

    @Autowired
    private TiposEmpresaRepository tiposEmpresaRepository;

    public List<TipoEmpresaResponseDTO> findAll() {
        return repository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public TipoEmpresaResponseDTO findById(Long id) {
        return toDTO(repository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Tipo empresa no encontrado: " + id)));
    }

    public TipoEmpresaResponseDTO save(TipoEmpresaRequestDTO dto) {
        TipoEmpresaModel model = new TipoEmpresaModel();
        model.setNombre(dto.getNombre());
        return toDTO(repository.save(model));
    }

    public TipoEmpresaResponseDTO update(Long id, TipoEmpresaRequestDTO dto) {
        TipoEmpresaModel model = repository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Tipo empresa no encontrado: " + id));

        model.setNombre(dto.getNombre());
        return toDTO(repository.save(model));
    }

    public TipoEmpresaResponseDTO patch(Long id, TipoEmpresaUpdateDTO dto) {
        TipoEmpresaModel model = repository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Tipo empresa no encontrado: " + id));

        if (dto.getNombre() != null) {
            model.setNombre(dto.getNombre());
        }

        return toDTO(repository.save(model));
    }

    public void deleteById(Long id) {

        if (tiposEmpresaRepository.existsByTipoEmpresa_Id(id)) {
            throw new IllegalStateException("No se puede eliminar, existen relaciones asociadas");
        }

        repository.deleteById(id);
    }

    private TipoEmpresaResponseDTO toDTO(TipoEmpresaModel model) {
        TipoEmpresaResponseDTO dto = new TipoEmpresaResponseDTO();
        dto.setId(model.getId());
        dto.setNombre(model.getNombre());
        return dto;
    }

    public PagedResponse<TipoEmpresaResponseDTO> findAllPaged(int pagina, int size) {
    Pageable pageable = PageRequest.of(pagina - 1, size);

    Page<TipoEmpresaModel> page = repository.findAll(pageable);

    List<TipoEmpresaResponseDTO> contenido = page.getContent()
            .stream()
            .map(this::toDTO)
            .toList();

    return new PagedResponse<>(
            contenido,
            pagina,
            page.getTotalPages(),
            page.getTotalElements()
        );
    }
}