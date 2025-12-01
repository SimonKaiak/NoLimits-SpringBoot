package com.example.NoLimits.Multimedia.service.producto;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.dto.producto.request.ProductoRequestDTO;
import com.example.NoLimits.Multimedia.dto.producto.response.ProductoResponseDTO;
import com.example.NoLimits.Multimedia.dto.producto.update.ProductoUpdateDTO;
import com.example.NoLimits.Multimedia.model.producto.ProductoModel;
import com.example.NoLimits.Multimedia.repository.catalogos.ClasificacionRepository;
import com.example.NoLimits.Multimedia.repository.catalogos.EstadoRepository;
import com.example.NoLimits.Multimedia.repository.catalogos.TipoProductoRepository;
import com.example.NoLimits.Multimedia.repository.producto.DetalleVentaRepository;
import com.example.NoLimits.Multimedia.repository.producto.ProductoRepository;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private DetalleVentaRepository detalleVentaRepository;

    @Autowired
    private TipoProductoRepository tipoProductoRepository;

    @Autowired
    private ClasificacionRepository clasificacionRepository;

    @Autowired
    private EstadoRepository estadoRepository;


    /* ================= CRUD BÁSICO ================= */

    public List<ProductoResponseDTO> findAll() {
        return productoRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public ProductoResponseDTO findById(Long id) {
        ProductoModel model = productoRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Producto no encontrado con ID: " + id));
        return toResponseDTO(model);
    }

    public ProductoResponseDTO save(ProductoRequestDTO dto) {

        if (dto.getTipoProductoId() == null) {
            throw new RecursoNoEncontradoException("Debe indicar un tipo de producto válido.");
        }

        if (dto.getClasificacionId() == null) {
            throw new RecursoNoEncontradoException("Debe indicar una clasificación válida.");
        }

        if (dto.getEstadoId() == null) {
            throw new RecursoNoEncontradoException("Debe indicar un estado válido.");
        }

        ProductoModel producto = new ProductoModel();
        applyRequestToModel(dto, producto);

        ProductoModel guardado = productoRepository.save(producto);
        return toResponseDTO(guardado);
    }

    // PUT: reemplaza datos principales
    public ProductoResponseDTO update(Long id, ProductoRequestDTO dto) {
        ProductoModel productoExistente = productoRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Producto no encontrado con ID: " + id));

        if (dto.getTipoProductoId() == null) {
            throw new RecursoNoEncontradoException("Debe indicar un tipo de producto válido.");
        }

        if (dto.getClasificacionId() == null) {
            throw new RecursoNoEncontradoException("Debe indicar una clasificación válida.");
        }

        if (dto.getEstadoId() == null) {
            throw new RecursoNoEncontradoException("Debe indicar un estado válido.");
        }

        applyRequestToModel(dto, productoExistente);

        ProductoModel actualizado = productoRepository.save(productoExistente);
        return toResponseDTO(actualizado);
    }

    // PATCH: solo campos no nulos
    public ProductoResponseDTO patch(Long id, ProductoUpdateDTO dto) {
        ProductoModel productoExistente = productoRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Producto no encontrado con ID: " + id));

        if (dto.getNombre() != null) {
            productoExistente.setNombre(dto.getNombre());
        }
        if (dto.getPrecio() != null) {
            productoExistente.setPrecio(dto.getPrecio());
        }
        if (dto.getTipoProductoId() != null) {
            productoExistente.setTipoProducto(
                    tipoProductoRepository.findById(dto.getTipoProductoId())
                            .orElseThrow(() -> new RecursoNoEncontradoException(
                                    "Tipo de producto no encontrado con ID: " + dto.getTipoProductoId()))
            );
        }
        if (dto.getClasificacionId() != null) {
            productoExistente.setClasificacion(
                    clasificacionRepository.findById(dto.getClasificacionId())
                            .orElseThrow(() -> new RecursoNoEncontradoException(
                                    "Clasificación no encontrada con ID: " + dto.getClasificacionId()))
            );
        }
        if (dto.getEstadoId() != null) {
            productoExistente.setEstado(
                    estadoRepository.findById(dto.getEstadoId())
                            .orElseThrow(() -> new RecursoNoEncontradoException(
                                    "Estado no encontrado con ID: " + dto.getEstadoId()))
            );
        }

        ProductoModel actualizado = productoRepository.save(productoExistente);
        return toResponseDTO(actualizado);
    }


    public void deleteById(Long id) {
        // Verifica existencia
        productoRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Producto no encontrado con ID: " + id));

        boolean tieneMovimientos = !detalleVentaRepository.findByProducto_Id(id).isEmpty();
        if (tieneMovimientos) {
            throw new IllegalStateException(
                    "No se puede eliminar: el producto tiene movimientos en ventas."
            );
        }

        productoRepository.deleteById(id);
    }

    /* ================= BÚSQUEDAS ================= */

    public List<ProductoResponseDTO> findByNombre(String nombre) {
        return productoRepository.findByNombre(nombre)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<ProductoResponseDTO> findByNombreContainingIgnoreCase(String nombre) {
        return productoRepository.findByNombreContainingIgnoreCase(nombre)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<ProductoResponseDTO> findByTipoProducto(Long tipoProductoId) {
        return productoRepository.findByTipoProducto_Id(tipoProductoId)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<ProductoResponseDTO> findByClasificacion(Long clasificacionId) {
        return productoRepository.findByClasificacion_Id(clasificacionId)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<ProductoResponseDTO> findByEstado(Long estadoId) {
        return productoRepository.findByEstado_Id(estadoId)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<ProductoResponseDTO> findByTipoProductoAndEstado(Long tipoProductoId, Long estadoId) {
        return productoRepository.findByTipoProducto_IdAndEstado_Id(tipoProductoId, estadoId)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    /* ================= RESUMEN ================= */

    public List<Map<String, Object>> obtenerProductosConDatos() {
        List<Object[]> resultados = productoRepository.obtenerProductosResumen();
        List<Map<String, Object>> lista = new ArrayList<>();

        for (Object[] fila : resultados) {
            Map<String, Object> datos = new HashMap<>();
            datos.put("ID", fila[0]);
            datos.put("Nombre", fila[1]);
            datos.put("Precio", fila[2]);
            datos.put("Tipo Producto", fila[3]);
            datos.put("Estado", fila[4]);
            lista.add(datos);
        }
        return lista;
    }

    /* ================= MAPEO ENTIDAD <-> DTO ================= */

    private void applyRequestToModel(ProductoRequestDTO dto, ProductoModel producto) {
        producto.setNombre(dto.getNombre());
        producto.setPrecio(dto.getPrecio());

        producto.setTipoProducto(
                tipoProductoRepository.findById(dto.getTipoProductoId())
                        .orElseThrow(() -> new RecursoNoEncontradoException(
                                "Tipo de producto no encontrado con ID: " + dto.getTipoProductoId()))
        );

        producto.setClasificacion(
                clasificacionRepository.findById(dto.getClasificacionId())
                        .orElseThrow(() -> new RecursoNoEncontradoException(
                                "Clasificación no encontrada con ID: " + dto.getClasificacionId()))
        );

        producto.setEstado(
                estadoRepository.findById(dto.getEstadoId())
                        .orElseThrow(() -> new RecursoNoEncontradoException(
                                "Estado no encontrado con ID: " + dto.getEstadoId()))
        );
    }

    private ProductoResponseDTO toResponseDTO(ProductoModel model) {
        ProductoResponseDTO dto = new ProductoResponseDTO();

        dto.setId(model.getId());
        dto.setNombre(model.getNombre());
        dto.setPrecio(model.getPrecio());

        if (model.getTipoProducto() != null) {
            dto.setTipoProductoId(model.getTipoProducto().getId());
            dto.setTipoProductoNombre(model.getTipoProducto().getNombre());
        }

        if (model.getClasificacion() != null) {
            dto.setClasificacionId(model.getClasificacion().getId());
            dto.setClasificacionNombre(model.getClasificacion().getNombre());
        }

        if (model.getEstado() != null) {
            dto.setEstadoId(model.getEstado().getId());
            dto.setEstadoNombre(model.getEstado().getNombre());
        }

        // plataformas/géneros/empresas/desarrolladores/imagenes
        return dto;
    }
}