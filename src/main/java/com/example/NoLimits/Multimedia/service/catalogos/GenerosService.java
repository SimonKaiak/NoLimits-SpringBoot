package com.example.NoLimits.Multimedia.service.catalogos;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.dto.catalogos.response.GenerosResponseDTO;
import com.example.NoLimits.Multimedia.model.catalogos.GeneroModel;
import com.example.NoLimits.Multimedia.model.catalogos.GenerosModel;
import com.example.NoLimits.Multimedia.model.producto.ProductoModel;
import com.example.NoLimits.Multimedia.repository.catalogos.GeneroRepository;
import com.example.NoLimits.Multimedia.repository.catalogos.GenerosRepository;
import com.example.NoLimits.Multimedia.repository.producto.ProductoRepository;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servicio de lógica de negocio para la relación puente Producto–Género.
 *
 * Maneja:
 * - Consultas por producto / género.
 * - Creación / eliminación de vínculos.
 * - Patch de la relación (cambiar producto y/o género).
 * - Conversión de entidad a DTO para exponerla al exterior.
 */
@Service
@Transactional
public class GenerosService {

    @Autowired
    private GenerosRepository generosRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private GeneroRepository generoRepository;

    // ================== CONSULTAS BÁSICAS (RETORNANDO DTO) ==================

    /**
     * Obtiene todas las relaciones Producto–Género asociadas a un producto
     * y las transforma a DTO para exponer solo los datos necesarios.
     *
     * @param productoId ID del producto.
     * @return lista de DTO con la información de la relación.
     */
    public List<GenerosResponseDTO> findByProducto(Long productoId) {
        return generosRepository.findByProducto_Id(productoId)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    /**
     * Obtiene todas las relaciones Producto–Género asociadas a un género
     * y las transforma a DTO.
     *
     * @param generoId ID del género.
     * @return lista de DTO con la información de la relación.
     */
    public List<GenerosResponseDTO> findByGenero(Long generoId) {
        return generosRepository.findByGenero_Id(generoId)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    // ================== VINCULAR / DESVINCULAR ==================

    /**
     * Crea (si no existe) la relación Producto–Género entre un producto y un género.
     * Si la relación ya existe, devuelve la existente como DTO.
     *
     * @param productoId ID del producto.
     * @param generoId   ID del género.
     * @return DTO con la relación creada o existente.
     */
    public GenerosResponseDTO link(Long productoId, Long generoId) {
        ProductoModel p = productoRepository.findById(productoId)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Producto no encontrado con ID: " + productoId));

        GeneroModel g = generoRepository.findById(generoId)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Género no encontrado con ID: " + generoId));

        // Si ya existe la relación, la devolvemos tal cual (como DTO)
        if (generosRepository.existsByProducto_IdAndGenero_Id(productoId, generoId)) {
            GenerosModel existente = generosRepository.findByProducto_Id(productoId).stream()
                    .filter(rel -> rel.getGenero() != null
                            && generoId.equals(rel.getGenero().getId()))
                    .findFirst()
                    .orElseThrow(() ->
                            new IllegalStateException(
                                    "La relación Producto-Género existe en BD pero no se pudo recuperar."));

            return toDTO(existente);
        }

        // Crear una nueva relación
        GenerosModel rel = new GenerosModel();
        rel.setProducto(p);
        rel.setGenero(g);
        GenerosModel guardado = generosRepository.save(rel);

        return toDTO(guardado);
    }

    /**
     * Elimina la relación Producto–Género si existe.
     * Si el producto o el género no existen, lanza RecursoNoEncontradoException.
     *
     * @param productoId ID del producto.
     * @param generoId   ID del género.
     */
    public void unlink(Long productoId, Long generoId) {
        productoRepository.findById(productoId)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Producto no encontrado con ID: " + productoId));

        generoRepository.findById(generoId)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Género no encontrado con ID: " + generoId));

        if (generosRepository.existsByProducto_IdAndGenero_Id(productoId, generoId)) {
            generosRepository.deleteByProducto_IdAndGenero_Id(productoId, generoId);
        }
    }

    // ================== PATCH ==================

    /**
     * PATCH: Actualiza parcialmente la relación Producto–Género.
     * Se puede cambiar el producto y/o el género asociado a la relación.
     *
     * Reglas:
     * - Si se envía nuevoProductoId, se valida que exista y que no haya duplicado con el género actual.
     * - Si se envía nuevoGeneroId, se valida que exista y que no haya duplicado con el producto actual.
     *
     * @param relacionId      ID de la relación actual.
     * @param nuevoProductoId opcional, nuevo ID de producto.
     * @param nuevoGeneroId   opcional, nuevo ID de género.
     * @return DTO con la relación actualizada.
     */
    public GenerosResponseDTO patch(Long relacionId, Long nuevoProductoId, Long nuevoGeneroId) {

        GenerosModel rel = generosRepository.findById(relacionId)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Relación Producto–Género no encontrada con ID: " + relacionId));

        // Cambiar producto si corresponde
        if (nuevoProductoId != null) {
            ProductoModel nuevoProducto = productoRepository.findById(nuevoProductoId)
                    .orElseThrow(() ->
                            new RecursoNoEncontradoException("Producto no encontrado con ID: " + nuevoProductoId));

            if (rel.getGenero() != null &&
                    generosRepository.existsByProducto_IdAndGenero_Id(nuevoProductoId, rel.getGenero().getId())) {
                throw new IllegalArgumentException("Ya existe una relación con ese producto y género");
            }

            rel.setProducto(nuevoProducto);
        }

        // Cambiar género si corresponde
        if (nuevoGeneroId != null) {
            GeneroModel nuevoGenero = generoRepository.findById(nuevoGeneroId)
                    .orElseThrow(() ->
                            new RecursoNoEncontradoException("Género no encontrado con ID: " + nuevoGeneroId));

            if (rel.getProducto() != null &&
                    generosRepository.existsByProducto_IdAndGenero_Id(rel.getProducto().getId(), nuevoGeneroId)) {
                throw new IllegalArgumentException("Ya existe una relación con ese producto y género");
            }

            rel.setGenero(nuevoGenero);
        }

        GenerosModel actualizado = generosRepository.save(rel);
        return toDTO(actualizado);
    }

    // ================== RESUMEN ==================

    /**
     * Consulta de resumen en crudo (List<Object[]>), usada por los endpoints /resumen.
     *
     * Espera que el repository haga un SELECT tipo:
     *  SELECT rel.id, p.id, p.nombre, g.id, g.nombre
     */
    public List<Object[]> obtenerResumen(Long productoId, Long generoId) {
        return generosRepository.obtenerResumen(productoId, generoId);
    }

    // ================== HELPER: MAPEO ENTIDAD -> DTO ==================

    /**
     * Convierte una entidad GenerosModel a su DTO equivalente.
     *
     * @param rel entidad de la tabla puente.
     * @return DTO con IDs y nombre de género.
     */
    private GenerosResponseDTO toDTO(GenerosModel rel) {
        if (rel == null) {
            return null;
        }

        GenerosResponseDTO dto = new GenerosResponseDTO();
        dto.setId(rel.getId());

        if (rel.getProducto() != null) {
            dto.setProductoId(rel.getProducto().getId());
        }

        if (rel.getGenero() != null) {
            dto.setGeneroId(rel.getGenero().getId());
            dto.setGeneroNombre(rel.getGenero().getNombre());
        }

        return dto;
    }
}