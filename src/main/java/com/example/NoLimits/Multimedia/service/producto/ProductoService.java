package com.example.NoLimits.Multimedia.service.producto;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.dto.pagination.PagedResponse;
import com.example.NoLimits.Multimedia.dto.producto.mapper.ProductoMapper;
import com.example.NoLimits.Multimedia.dto.producto.request.ProductoRequestDTO;
import com.example.NoLimits.Multimedia.dto.producto.response.ProductoResponseDTO;
import com.example.NoLimits.Multimedia.dto.producto.update.ProductoUpdateDTO;
import com.example.NoLimits.Multimedia.model.catalogos.*;
import com.example.NoLimits.Multimedia.model.producto.ImagenesModel;
import com.example.NoLimits.Multimedia.model.producto.ProductoModel;
import com.example.NoLimits.Multimedia.repository.catalogos.*;
import com.example.NoLimits.Multimedia.repository.producto.DetalleVentaRepository;
import com.example.NoLimits.Multimedia.repository.producto.ProductoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.*;
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

    // Repositorios de catálogos N:M (ajusta los nombres si en tu proyecto son otros)
    @Autowired
    private PlataformaRepository plataformaRepository;

    @Autowired
    private GeneroRepository generoRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private DesarrolladorRepository desarrolladorRepository;

    /* ================= CRUD BÁSICO ================= */

    public List<ProductoResponseDTO> findAll() {
        return productoRepository.findAll()
                .stream()
                .map(ProductoMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public ProductoResponseDTO findById(Long id) {
        ProductoModel model = productoRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Producto no encontrado con ID: " + id));
        return ProductoMapper.toResponseDTO(model);
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
        return ProductoMapper.toResponseDTO(guardado);
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
        return ProductoMapper.toResponseDTO(actualizado);
    }

    // PATCH: solo campos no nulos (no toca las listas N:M)
    public ProductoResponseDTO patch(Long id, ProductoUpdateDTO dto) {
        ProductoModel productoExistente = productoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Producto no encontrado con ID: " + id));

        // ... lo que ya tienes (nombre, precio, etc.)

        // ================== PATCH de plataformas (N:M) ==================
        if (dto.getPlataformasIds() != null) {
            // Limpio la lista actual (para que no queden duplicados)
            if (productoExistente.getPlataformas() != null) {
                productoExistente.getPlataformas().clear();
            } else {
                productoExistente.setPlataformas(new ArrayList<>());
            }

            List<PlataformasModel> nuevasPlataformas = dto.getPlataformasIds()
                    .stream()
                    .map(idPlat -> {
                        PlataformaModel plat = plataformaRepository.findById(idPlat)
                                .orElseThrow(() -> new RecursoNoEncontradoException(
                                        "Plataforma no encontrada con ID: " + idPlat));
                        PlataformasModel puente = new PlataformasModel();
                        puente.setProducto(productoExistente);
                        puente.setPlataforma(plat);
                        return puente;
                    })
                    .collect(Collectors.toList());

            productoExistente.getPlataformas().addAll(nuevasPlataformas);
        }

        ProductoModel actualizado = productoRepository.save(productoExistente);
        return ProductoMapper.toResponseDTO(actualizado);
    }

    public void deleteById(Long id) {
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
                .map(ProductoMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<ProductoResponseDTO> findByNombreContainingIgnoreCase(String nombre) {
        return productoRepository.findByNombreContainingIgnoreCase(nombre)
                .stream()
                .map(ProductoMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<ProductoResponseDTO> findByTipoProducto(Long tipoProductoId) {
        return productoRepository.findByTipoProducto_Id(tipoProductoId)
                .stream()
                .map(ProductoMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<ProductoResponseDTO> findByClasificacion(Long clasificacionId) {
        return productoRepository.findByClasificacion_Id(clasificacionId)
                .stream()
                .map(ProductoMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<ProductoResponseDTO> findByEstado(Long estadoId) {
        return productoRepository.findByEstado_Id(estadoId)
                .stream()
                .map(ProductoMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<ProductoResponseDTO> findByTipoProductoAndEstado(Long tipoProductoId, Long estadoId) {
        return productoRepository.findByTipoProducto_IdAndEstado_Id(tipoProductoId, estadoId)
                .stream()
                .map(ProductoMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    /* ================= SAGAS ================= */

    // Sagas con su portada (si existe)
    public List<Map<String, Object>> obtenerSagasConPortada() {
        List<Object[]> filas = productoRepository.findDistinctSagasWithPortada();
        List<Map<String, Object>> lista = new ArrayList<>();

        for (Object[] fila : filas) {
            Map<String, Object> datos = new HashMap<>();
            datos.put("nombre", fila[0]);        // saga (String)
            datos.put("portadaSaga", fila[1]);   // portada (String o null)
            lista.add(datos);
        }

        return lista;
    }

    public List<ProductoResponseDTO> findBySaga(String saga) {
        return productoRepository.findBySaga(saga)
                .stream()
                .map(ProductoMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<ProductoResponseDTO> findBySagaIgnoreCase(String saga) {
        return productoRepository.findBySagaIgnoreCase(saga)
                .stream()
                .map(ProductoMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<String> obtenerSagasDistinct() {
        return productoRepository.findDistinctSagas();
    }

    public List<String> obtenerSagasDistinctPorTipoProducto(Long tipoProductoId) {
        return productoRepository.findDistinctSagasByTipoProductoId(tipoProductoId);
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
            // Nuevos campos: saga y portadaSaga
            datos.put("Saga", fila[5]);
            datos.put("Portada Saga", fila[6]);
            lista.add(datos);
        }
        return lista;
    }

    /* ================= MAPEO DTO -> ENTIDAD ================= */

    private void applyRequestToModel(ProductoRequestDTO dto, ProductoModel producto) {

        producto.setNombre(dto.getNombre());
        producto.setPrecio(dto.getPrecio());

        // saga y portadaSaga
        producto.setSaga(dto.getSaga());
        producto.setPortadaSaga(dto.getPortadaSaga());

        // Tipo, clasificación y estado
        TipoProductoModel tipo = tipoProductoRepository.findById(dto.getTipoProductoId())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Tipo de producto no encontrado con ID: " + dto.getTipoProductoId()));

        ClasificacionModel clasificacion = clasificacionRepository.findById(dto.getClasificacionId())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Clasificación no encontrada con ID: " + dto.getClasificacionId()));

        EstadoModel estado = estadoRepository.findById(dto.getEstadoId())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Estado no encontrado con ID: " + dto.getEstadoId()));

        producto.setTipoProducto(tipo);
        producto.setClasificacion(clasificacion);
        producto.setEstado(estado);

        // ===== Relaciones N:M =====
        // Plataformas
        if (dto.getPlataformasIds() != null) {
            List<PlataformasModel> plataformas = dto.getPlataformasIds()
                    .stream()
                    .map(idPlat -> {
                        PlataformaModel plat = plataformaRepository.findById(idPlat)
                                .orElseThrow(() -> new RecursoNoEncontradoException(
                                        "Plataforma no encontrada con ID: " + idPlat));
                        PlataformasModel puente = new PlataformasModel();
                        puente.setProducto(producto);
                        puente.setPlataforma(plat);
                        return puente;
                    })
                    .collect(Collectors.toList());
            producto.setPlataformas(plataformas);
        }

        // Géneros
        if (dto.getGenerosIds() != null) {
            List<GenerosModel> generos = dto.getGenerosIds()
                    .stream()
                    .map(idGen -> {
                        GeneroModel gen = generoRepository.findById(idGen)
                                .orElseThrow(() -> new RecursoNoEncontradoException(
                                        "Género no encontrado con ID: " + idGen));
                        GenerosModel puente = new GenerosModel();
                        puente.setProducto(producto);
                        puente.setGenero(gen);
                        return puente;
                    })
                    .collect(Collectors.toList());
            producto.setGeneros(generos);
        }

        // Empresas
        if (dto.getEmpresasIds() != null) {
            List<EmpresasModel> empresas = dto.getEmpresasIds()
                    .stream()
                    .map(idEmp -> {
                        EmpresaModel emp = empresaRepository.findById(idEmp)
                                .orElseThrow(() -> new RecursoNoEncontradoException(
                                        "Empresa no encontrada con ID: " + idEmp));
                        EmpresasModel puente = new EmpresasModel();
                        puente.setProducto(producto);
                        puente.setEmpresa(emp);
                        return puente;
                    })
                    .collect(Collectors.toList());
            producto.setEmpresas(empresas);
        }

        // Desarrolladores
        if (dto.getDesarrolladoresIds() != null) {
            List<DesarrolladoresModel> devs = dto.getDesarrolladoresIds()
                    .stream()
                    .map(idDev -> {
                        DesarrolladorModel dev = desarrolladorRepository.findById(idDev)
                                .orElseThrow(() -> new RecursoNoEncontradoException(
                                        "Desarrollador no encontrado con ID: " + idDev));
                        DesarrolladoresModel puente = new DesarrolladoresModel();
                        puente.setProducto(producto);
                        puente.setDesarrollador(dev);
                        return puente;
                    })
                    .collect(Collectors.toList());
            producto.setDesarrolladores(devs);
        }

        // Imágenes (rutas simples)
        if (dto.getImagenesRutas() != null) {
            List<ImagenesModel> imagenes = dto.getImagenesRutas()
                    .stream()
                    .map(ruta -> {
                        ImagenesModel img = new ImagenesModel();
                        img.setRuta(ruta);
                        img.setAltText(producto.getNombre());
                        img.setProducto(producto);
                        return img;
                    })
                    .collect(Collectors.toList());
            producto.setImagenes(imagenes);
        }
    }

    /* ================= PAGINACIÓN ================= */

    public PagedResponse<ProductoResponseDTO> findAllPaged(int page, int size) {

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("id").descending());

        Page<ProductoModel> result = productoRepository.findAll(pageable);

        List<ProductoResponseDTO> contenido = result.getContent()
                .stream()
                .map(ProductoMapper::toResponseDTO)
                .collect(Collectors.toList());

        return new PagedResponse<>(
                contenido,
                page,
                result.getTotalPages(),
                result.getTotalElements()
        );
    }
}