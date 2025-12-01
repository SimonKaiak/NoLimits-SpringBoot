package com.example.NoLimits.Multimedia.service.catalogos;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.dto.catalogos.request.GeneroRequestDTO;
import com.example.NoLimits.Multimedia.dto.catalogos.response.GeneroResponseDTO;
import com.example.NoLimits.Multimedia.dto.catalogos.update.GeneroUpdateDTO;
import com.example.NoLimits.Multimedia.model.catalogos.GeneroModel;
import com.example.NoLimits.Multimedia.repository.catalogos.GeneroRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class GeneroService {

    @Autowired
    private GeneroRepository generoRepository;

    public List<GeneroResponseDTO> findAll() {
        return generoRepository.findAll().stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public GeneroResponseDTO findById(Long id) {
        GeneroModel g = generoRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Género no encontrado con ID: " + id));
        return toResponseDTO(g);
    }

    public List<GeneroResponseDTO> findByNombreContaining(String nombre) {
        return generoRepository.findByNombreContainingIgnoreCase(nombre).stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public GeneroResponseDTO save(GeneroRequestDTO dto) {
        if (dto.getNombre() == null || dto.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del género es obligatorio");
        }
        String nombre = dto.getNombre().trim();

        GeneroModel g = new GeneroModel();
        g.setNombre(nombre);

        GeneroModel guardado = generoRepository.save(g);
        return toResponseDTO(guardado);
    }

    public GeneroResponseDTO update(Long id, GeneroUpdateDTO in) {
        GeneroModel g = generoRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Género no encontrado con ID: " + id));

        if (in.getNombre() != null) {
            String nuevo = in.getNombre().trim();
            if (nuevo.isEmpty()) {
                throw new IllegalArgumentException("El nombre no puede estar vacío");
            }
            g.setNombre(nuevo);
        }

        GeneroModel actualizado = generoRepository.save(g);
        return toResponseDTO(actualizado);
    }

    public GeneroResponseDTO patch(Long id, GeneroUpdateDTO in) {
        // mismo comportamiento que update, pero lo dejamos separado por semántica
        return update(id, in);
    }

    public void deleteById(Long id) {
        generoRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Género no encontrado con ID: " + id));
        generoRepository.deleteById(id);
    }

    public List<Object[]> obtenerGenerosResumen() {
        return generoRepository.obtenerGenerosResumen();
    }

    // ========== HELPERS ==========

    private GeneroResponseDTO toResponseDTO(GeneroModel g) {
        GeneroResponseDTO dto = new GeneroResponseDTO();
        dto.setId(g.getId());
        dto.setNombre(g.getNombre());
        return dto;
    }
}