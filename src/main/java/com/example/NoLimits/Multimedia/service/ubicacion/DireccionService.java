// Ruta: src/main/java/com/example/NoLimits/Multimedia/service/ubicacion/DireccionService.java
package com.example.NoLimits.Multimedia.service.ubicacion;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.dto.ubicacion.request.DireccionRequestDTO;
import com.example.NoLimits.Multimedia.dto.ubicacion.response.DireccionResponseDTO;
import com.example.NoLimits.Multimedia.dto.ubicacion.update.DireccionUpdateDTO;
import com.example.NoLimits.Multimedia.model.ubicacion.ComunaModel;
import com.example.NoLimits.Multimedia.model.ubicacion.DireccionModel;
import com.example.NoLimits.Multimedia.model.usuario.UsuarioModel;
import com.example.NoLimits.Multimedia.repository.ubicacion.ComunaRepository;
import com.example.NoLimits.Multimedia.repository.ubicacion.DireccionRepository;
import com.example.NoLimits.Multimedia.repository.usuario.UsuarioRepository;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class DireccionService {

    @Autowired
    private DireccionRepository direccionRepository;

    @Autowired
    private ComunaRepository comunaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // ===============================================================
    // MÉTODOS PÚBLICOS EXPONIENDO DTOs
    // ===============================================================

    public List<DireccionResponseDTO> findAll() {
        return direccionRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public DireccionResponseDTO findById(Long id) {
        DireccionModel direccion = findEntityById(id);
        return toResponseDTO(direccion);
    }

    /**
     * CREATE – usa DireccionRequestDTO (sin ID).
     */
    public DireccionResponseDTO save(DireccionRequestDTO requestDTO) {

        // Validar calle
        if (requestDTO.getCalle() == null || requestDTO.getCalle().isBlank()) {
            throw new IllegalArgumentException("La calle es obligatoria");
        }
        String calle = requestDTO.getCalle().trim();

        // Validar número
        if (requestDTO.getNumero() == null || requestDTO.getNumero().isBlank()) {
            throw new IllegalArgumentException("El número es obligatorio");
        }
        String numero = requestDTO.getNumero().trim();

        String complemento = requestDTO.getComplemento();
        String codigoPostal = requestDTO.getCodigoPostal();

        // 1) Validar usuario primero (lo exige el test)
        if (requestDTO.getUsuarioId() == null) {
            throw new IllegalArgumentException("Debe especificar un usuario válido");
        }

        // 2) Recién después validar comuna
        if (requestDTO.getComunaId() == null) {
            throw new IllegalArgumentException("Debe especificar una comuna válida");
        }

        ComunaModel comuna = comunaRepository.findById(requestDTO.getComunaId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Comuna no encontrada"));

        UsuarioModel usuario = usuarioRepository.findById(requestDTO.getUsuarioId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado"));

        Boolean activo = requestDTO.getActivo() != null ? requestDTO.getActivo() : Boolean.TRUE;

        DireccionModel direccion = new DireccionModel();
        direccion.setCalle(calle);
        direccion.setNumero(numero);
        direccion.setComplemento(complemento);
        direccion.setCodigoPostal(codigoPostal);
        direccion.setComuna(comuna);
        direccion.setUsuarioModel(usuario);
        direccion.setActivo(activo);

        DireccionModel guardada = direccionRepository.save(direccion);
        return toResponseDTO(guardada);
    }

    /**
     * UPDATE (PUT completo) – usa DireccionUpdateDTO.
     * Aquí asumimos que un PUT debería traer todos los campos relevantes.
     */
    public DireccionResponseDTO update(Long id, DireccionUpdateDTO updateDTO) {

        DireccionModel existente = findEntityById(id);

        // Validar calle
        if (updateDTO.getCalle() == null || updateDTO.getCalle().isBlank()) {
            throw new IllegalArgumentException("La calle es obligatoria");
        }
        String calle = updateDTO.getCalle().trim();
        if (calle.isEmpty()) {
            throw new IllegalArgumentException("La calle no puede estar vacía");
        }

        // Validar número
        if (updateDTO.getNumero() == null || updateDTO.getNumero().isBlank()) {
            throw new IllegalArgumentException("El número es obligatorio");
        }
        String numero = updateDTO.getNumero().trim();
        if (numero.isEmpty()) {
            throw new IllegalArgumentException("El número no puede estar vacío");
        }

        // Complemento opcional
        String complemento = updateDTO.getComplemento();

        // Código postal opcional
        String codigoPostal = updateDTO.getCodigoPostal();

        // Validar comuna
        if (updateDTO.getComunaId() == null) {
            throw new IllegalArgumentException("Debe especificar una comuna válida");
        }
        ComunaModel comuna = comunaRepository.findById(updateDTO.getComunaId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Comuna no encontrada"));

        // Validar usuario
        if (updateDTO.getUsuarioId() == null) {
            throw new IllegalArgumentException("Debe especificar un usuario válido");
        }
        UsuarioModel usuario = usuarioRepository.findById(updateDTO.getUsuarioId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado"));

        // activo: si viene null, asumimos true (o mantenemos actual, según prefieras)
        Boolean activo = updateDTO.getActivo() != null ? updateDTO.getActivo() : Boolean.TRUE;

        // Sobrescribir datos
        existente.setCalle(calle);
        existente.setNumero(numero);
        existente.setComplemento(complemento);
        existente.setCodigoPostal(codigoPostal);
        existente.setComuna(comuna);
        existente.setUsuarioModel(usuario);
        existente.setActivo(activo);

        DireccionModel actualizada = direccionRepository.save(existente);
        return toResponseDTO(actualizada);
    }

    /**
     * PATCH – actualización parcial de una dirección.
     * Usa también DireccionUpdateDTO, pero solo aplica los campos no nulos.
     */
    public DireccionResponseDTO patch(Long id, DireccionUpdateDTO entrada) {

        DireccionModel existente = findEntityById(id);

        // calle
        if (entrada.getCalle() != null) {
            String calle = entrada.getCalle().trim();
            if (calle.isEmpty()) {
                throw new IllegalArgumentException("La calle no puede estar vacía");
            }
            existente.setCalle(calle);
        }

        // número
        if (entrada.getNumero() != null) {
            String numero = entrada.getNumero().trim();
            if (numero.isEmpty()) {
                throw new IllegalArgumentException("El número no puede estar vacío");
            }
            existente.setNumero(numero);
        }

        // complemento (depto, block, etc.)
        if (entrada.getComplemento() != null) {
            existente.setComplemento(entrada.getComplemento());
        }

        // código postal
        if (entrada.getCodigoPostal() != null) {
            existente.setCodigoPostal(entrada.getCodigoPostal());
        }

        // comuna
        if (entrada.getComunaId() != null) {
            ComunaModel comuna = comunaRepository.findById(entrada.getComunaId())
                    .orElseThrow(() -> new RecursoNoEncontradoException("Comuna no encontrada"));
            existente.setComuna(comuna);
        }

        // usuario
        if (entrada.getUsuarioId() != null) {
            UsuarioModel usuario = usuarioRepository.findById(entrada.getUsuarioId())
                    .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado"));
            existente.setUsuarioModel(usuario);
        }

        // activo
        if (entrada.getActivo() != null) {
            existente.setActivo(entrada.getActivo());
        }

        DireccionModel actualizada = direccionRepository.save(existente);
        return toResponseDTO(actualizada);
    }

    public void deleteById(Long id) {
        DireccionModel existente = findEntityById(id);
        direccionRepository.delete(existente);
    }

    // ===============================================================
    // MÉTODOS PRIVADOS DE APOYO (ENTIDAD + MAPEOS)
    // ===============================================================

    private DireccionModel findEntityById(Long id) {
        return direccionRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Dirección no encontrada con ID: " + id));
    }

    private DireccionResponseDTO toResponseDTO(DireccionModel entity) {
        DireccionResponseDTO dto = new DireccionResponseDTO();

        dto.setId(entity.getId());
        dto.setCalle(entity.getCalle());
        dto.setNumero(entity.getNumero());
        dto.setComplemento(entity.getComplemento());
        dto.setCodigoPostal(entity.getCodigoPostal());
        dto.setActivo(entity.getActivo());

        // Nombre de la comuna (si existe)
        if (entity.getComuna() != null) {
            dto.setComuna(entity.getComuna().getNombre());

            // Nombre de la región (si existe)
            if (entity.getComuna().getRegion() != null) {
                dto.setRegion(entity.getComuna().getRegion().getNombre());
            }
        }

        return dto;
    }
}