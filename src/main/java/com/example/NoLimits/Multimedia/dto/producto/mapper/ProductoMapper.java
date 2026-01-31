// Ruta: src/main/java/com/example/NoLimits/Multimedia/dto/producto/mapper/ProductoMapper.java
package com.example.NoLimits.Multimedia.dto.producto.mapper;

import com.example.NoLimits.Multimedia.dto.producto.request.LinkCompraDTO;
import com.example.NoLimits.Multimedia.dto.producto.response.ProductoResponseDTO;
import com.example.NoLimits.Multimedia.model.producto.ProductoModel;

import java.util.stream.Collectors;

/**
 * Mapper encargado de convertir entidades ProductoModel a
 * DTOs de salida ProductoResponseDTO para ser enviados al frontend.
 */
public class ProductoMapper {

    /**
     * Convierte un ProductoModel completo en un ProductoResponseDTO.
     * Incluye la conversión de relaciones N:M a listas simples de String
     * para facilitar la serialización hacia el cliente.
     *
     * @param model entidad ProductoModel obtenida desde la base de datos
     * @return DTO adaptado para el frontend
     */
    public static ProductoResponseDTO toResponseDTO(ProductoModel model) {
        ProductoResponseDTO dto = new ProductoResponseDTO();

        // Datos básicos del producto
        dto.setId(model.getId());
        dto.setNombre(model.getNombre());
        dto.setPrecio(model.getPrecio());

        // Datos del tipo de producto
        if (model.getTipoProducto() != null) {
            dto.setTipoProductoId(model.getTipoProducto().getId());
            dto.setTipoProductoNombre(model.getTipoProducto().getNombre());
        }

        // Datos de clasificación
        if (model.getClasificacion() != null) {
            dto.setClasificacionId(model.getClasificacion().getId());
            dto.setClasificacionNombre(model.getClasificacion().getNombre());
        }

        // Datos de estado
        if (model.getEstado() != null) {
            dto.setEstadoId(model.getEstado().getId());
            dto.setEstadoNombre(model.getEstado().getNombre());
        }

        // Información de saga, solo si existe una asociada
        dto.setSaga(model.getSaga());
        dto.setPortadaSaga(model.getPortadaSaga());

        /*
         * Conversión de relaciones N:M:
         * Cada lista de entidades puente se transforma en una lista de String
         * tomando el "nombre" de la entidad de catálogo asociada.
         * Si la lista es null, se deja null en el DTO.
         */

        // plataformas → nombre de la PlataformaModel asociada
        dto.setPlataformas(
                model.getPlataformas() == null ? null :
                        model.getPlataformas()
                                .stream()
                                .map(p -> p.getPlataforma().getNombre())
                                .collect(Collectors.toList())
        );

        // géneros → nombre del GeneroModel asociado
        dto.setGeneros(
                model.getGeneros() == null ? null :
                        model.getGeneros()
                                .stream()
                                .map(g -> g.getGenero().getNombre())
                                .collect(Collectors.toList())
        );

        // empresas → nombre del EmpresaModel asociado
        dto.setEmpresas(
                model.getEmpresas() == null ? null :
                        model.getEmpresas()
                                .stream()
                                .map(e -> e.getEmpresa().getNombre())
                                .collect(Collectors.toList())
        );

        // desarrolladores → nombre del DesarrolladorModel asociado
        dto.setDesarrolladores(
                model.getDesarrolladores() == null ? null :
                        model.getDesarrolladores()
                                .stream()
                                .map(d -> d.getDesarrollador().getNombre())
                                .collect(Collectors.toList())
        );

        /*
         * Conversión de las imágenes asociadas al producto:
         * Se extrae únicamente la ruta de la imagen (campo 'ruta')
         * para ser utilizada en el frontend.
         */
        dto.setImagenes(
                model.getImagenes() == null ? null :
                        model.getImagenes()
                                .stream()
                                .map(img -> img.getRuta())
                                .collect(Collectors.toList())
        );

        // ==================== Links de compra por plataforma ====================
        dto.setLinksCompra(
        model.getLinksCompra() == null ? java.util.List.of() :
                model.getLinksCompra()
                .stream()
                .map(link -> {
                        LinkCompraDTO l = new LinkCompraDTO();
                        l.setPlataformaId(link.getPlataforma().getId());
                        l.setUrl(link.getUrl());
                        l.setLabel(link.getLabel());
                        return l;
                })
                .collect(Collectors.toList())
        );

        return dto;
    }
}