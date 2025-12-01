// Ruta: src/main/java/com/example/NoLimits/Multimedia/service/producto/DetalleVentaService.java
package com.example.NoLimits.Multimedia.service.producto;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.dto.producto.request.DetalleVentaRequestDTO;
import com.example.NoLimits.Multimedia.dto.producto.response.DetalleVentaResponseDTO;
import com.example.NoLimits.Multimedia.dto.producto.update.DetalleVentaUpdateDTO;
import com.example.NoLimits.Multimedia.model.producto.DetalleVentaModel;
import com.example.NoLimits.Multimedia.model.producto.ProductoModel;
import com.example.NoLimits.Multimedia.model.venta.VentaModel;
import com.example.NoLimits.Multimedia.repository.producto.DetalleVentaRepository;
import com.example.NoLimits.Multimedia.repository.producto.ProductoRepository;
import com.example.NoLimits.Multimedia.repository.venta.VentaRepository;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class DetalleVentaService {

    @Autowired
    private DetalleVentaRepository detalleVentaRepository;

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private ProductoRepository productoRepository;

    // ===============================================================
    // MÉTODOS PÚBLICOS EXPONIENDO DTOs
    // ===============================================================

    public List<DetalleVentaResponseDTO> findAll() {
        return detalleVentaRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public DetalleVentaResponseDTO findById(Long id) {
        DetalleVentaModel detalle = detalleVentaRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Detalle de venta no encontrado con ID: " + id));

        return toResponseDTO(detalle);
    }

    /**
     * CREATE – usa DetalleVentaRequestDTO (sin ID y sin ventaId).
     * 
     * Normalmente este DTO se usa dentro del flujo de creación de una Venta,
     * donde la Venta ya está determinada en otro contexto.
     */
    public DetalleVentaResponseDTO save(DetalleVentaRequestDTO requestDTO) {

        // Validación Producto
        if (requestDTO.getProductoId() == null) {
            throw new IllegalArgumentException("Debe especificar un producto válido");
        }

        ProductoModel producto = productoRepository.findById(requestDTO.getProductoId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado"));

        // Validación cantidad
        if (requestDTO.getCantidad() == null || requestDTO.getCantidad() < 1) {
            throw new IllegalArgumentException("La cantidad mínima es 1");
        }

        // Validación precio unitario
        if (requestDTO.getPrecioUnitario() == null || requestDTO.getPrecioUnitario() < 0) {
            throw new IllegalArgumentException("Debe indicar un precio unitario válido");
        }

        // Crear entidad a partir del DTO
        DetalleVentaModel detalle = new DetalleVentaModel();
        // Venta se asigna en otro punto del flujo (por ejemplo, en VentaService)
        detalle.setProducto(producto);
        detalle.setCantidad(requestDTO.getCantidad());
        detalle.setPrecioUnitario(requestDTO.getPrecioUnitario());
        // Subtotal se calcula en la entidad, si tienes esa lógica ahí

        DetalleVentaModel guardado = detalleVentaRepository.save(detalle);
        return toResponseDTO(guardado);
    }

    /**
     * UPDATE (PUT completo) – usa DetalleVentaUpdateDTO.
     * 
     * Aquí asumimos que para un PUT los campos clave NO deben ser nulos:
     * - productoId
     * - cantidad
     * - precioUnitario
     * - opcionalmente ventaId si quieres forzar que tenga venta.
     */
    public DetalleVentaResponseDTO update(Long id, DetalleVentaUpdateDTO updateDTO) {

        DetalleVentaModel existente = detalleVentaRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Detalle de venta no encontrado con ID: " + id));

        // Validar productoId
        if (updateDTO.getProductoId() == null) {
            throw new IllegalArgumentException("Debe especificar un producto válido");
        }
        ProductoModel producto = productoRepository.findById(updateDTO.getProductoId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado"));

        // Validar cantidad
        if (updateDTO.getCantidad() == null || updateDTO.getCantidad() < 1) {
            throw new IllegalArgumentException("La cantidad mínima es 1");
        }

        // Validar precioUnitario
        if (updateDTO.getPrecioUnitario() == null || updateDTO.getPrecioUnitario() < 0) {
            throw new IllegalArgumentException("Debe indicar un precio unitario válido");
        }

        // Venta (opcional, pero para PUT puedes decidir que sea obligatoria)
        if (updateDTO.getVentaId() != null) {
            VentaModel venta = ventaRepository.findById(updateDTO.getVentaId())
                    .orElseThrow(() -> new RecursoNoEncontradoException("Venta no encontrada"));
            existente.setVenta(venta);
        }

        // Sobrescribir todo lo demás
        existente.setProducto(producto);
        existente.setCantidad(updateDTO.getCantidad());
        existente.setPrecioUnitario(updateDTO.getPrecioUnitario());

        DetalleVentaModel actualizado = detalleVentaRepository.save(existente);
        return toResponseDTO(actualizado);
    }

    /**
     * PATCH — actualización parcial con DTO de actualización.
     * 
     * Todos los campos aquí son opcionales. Solo se actualiza lo que venga no nulo.
     */
    public DetalleVentaResponseDTO patch(Long id, DetalleVentaUpdateDTO updateDTO) {

        DetalleVentaModel existente = detalleVentaRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Detalle de venta no encontrado con ID: " + id));

        // PATCH Venta
        if (updateDTO.getVentaId() != null) {
            VentaModel venta = ventaRepository.findById(updateDTO.getVentaId())
                    .orElseThrow(() -> new RecursoNoEncontradoException("Venta no encontrada"));
            existente.setVenta(venta);
        }

        // PATCH Producto
        if (updateDTO.getProductoId() != null) {
            ProductoModel producto = productoRepository.findById(updateDTO.getProductoId())
                    .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado"));
            existente.setProducto(producto);
        }

        // PATCH cantidad
        if (updateDTO.getCantidad() != null) {
            if (updateDTO.getCantidad() < 1) {
                throw new IllegalArgumentException("La cantidad mínima es 1");
            }
            existente.setCantidad(updateDTO.getCantidad());
        }

        // PATCH precioUnitario
        if (updateDTO.getPrecioUnitario() != null) {
            if (updateDTO.getPrecioUnitario() < 0) {
                throw new IllegalArgumentException("El precio unitario no puede ser negativo");
            }
            existente.setPrecioUnitario(updateDTO.getPrecioUnitario());
        }

        // Subtotal NO SE PATCHEA → es calculado en la entidad

        DetalleVentaModel actualizado = detalleVentaRepository.save(existente);
        return toResponseDTO(actualizado);
    }

    public void deleteById(Long id) {
        DetalleVentaModel existente = detalleVentaRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Detalle de venta no encontrado con ID: " + id));

        detalleVentaRepository.delete(existente);
    }

    public List<DetalleVentaResponseDTO> findByVenta(Long idVenta) {
        return detalleVentaRepository.findByVenta_Id(idVenta)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    // ===============================================================
    // MAPEOS PRIVADOS ENTRE ENTIDAD Y DTOs
    // ===============================================================

    private DetalleVentaResponseDTO toResponseDTO(DetalleVentaModel entity) {
        DetalleVentaResponseDTO dto = new DetalleVentaResponseDTO();

        dto.setId(entity.getId());
        dto.setProductoId(
                entity.getProducto() != null
                        ? entity.getProducto().getId()
                        : null
        );
        dto.setProductoNombre(
                entity.getProducto() != null
                        ? entity.getProducto().getNombre()
                        : null
        );
        dto.setCantidad(entity.getCantidad());
        dto.setPrecioUnitario(entity.getPrecioUnitario());
        dto.setSubtotal(entity.getSubtotal());

        return dto;
    }
}