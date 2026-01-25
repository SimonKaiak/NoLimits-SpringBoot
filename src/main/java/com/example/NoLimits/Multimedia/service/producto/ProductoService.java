// Ruta: src/main/java/com/example/NoLimits/Multimedia/service/producto/ProductoService.java
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
        return productoRepository.findAllWithImagenes()
                .stream()
                .map(ProductoMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public ProductoResponseDTO findById(Long id) {
        ProductoModel model = productoRepository.findByIdWithImagenes(id)
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

        ProductoModel recargado = productoRepository.findByIdWithImagenes(guardado.getId())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Producto no encontrado con ID: " + guardado.getId()));

        return ProductoMapper.toResponseDTO(recargado);
    }

    // PUT: reemplaza datos principales
    public ProductoResponseDTO update(Long id, ProductoRequestDTO dto) {
        ProductoModel productoExistente = productoRepository.findByIdWithImagenes(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Producto no encontrado con ID: " + id));

        if (dto.getTipoProductoId() == null) {
            throw new RecursoNoEncontradoException("Debe indicar un tipo de producto válido.");
        }

        if (dto.getClasificacionId() == null) {
            throw new RecursoNoEncontradoException("Debe indicar una clasificación válida.");
        }

        if (dto.getEstadoId() == null) {
            throw new RecursoNoEncontradoException("Debe indicar un estadao válido.");
        }

        applyRequestToModel(dto, productoExistente);

        productoRepository.save(productoExistente);

        ProductoModel recargado = productoRepository.findByIdWithImagenes(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Producto no encontrado con ID: " + id));

        return ProductoMapper.toResponseDTO(recargado);
    }

    // PATCH: solo campos no nulos
    public ProductoResponseDTO patch(Long id, ProductoUpdateDTO dto) {
        ProductoModel productoExistente = productoRepository.findByIdWithImagenes(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Producto no encontrado con ID: " + id));

        // ================== CAMPOS SIMPLES ==================
        if (dto.getNombre() != null) {
            productoExistente.setNombre(dto.getNombre());
        }

        if (dto.getPrecio() != null) {
            productoExistente.setPrecio(dto.getPrecio());
        }

        if (dto.getSaga() != null) {
            productoExistente.setSaga(dto.getSaga());
        }

        if (dto.getPortadaSaga() != null) {
            productoExistente.setPortadaSaga(dto.getPortadaSaga());
        }

        // ✅ NUEVO: campos HUB (redirigir)
        if (dto.getUrlCompra() != null) {
            productoExistente.setUrlCompra(dto.getUrlCompra());
        }

        if (dto.getLabelCompra() != null) {
            productoExistente.setLabelCompra(dto.getLabelCompra());
        }

        // ================== N:1 (FKs) ==================
        if (dto.getTipoProductoId() != null) {
            TipoProductoModel tipo = tipoProductoRepository.findById(dto.getTipoProductoId())
                    .orElseThrow(() -> new RecursoNoEncontradoException(
                            "Tipo de producto no encontrado con ID: " + dto.getTipoProductoId()));
            productoExistente.setTipoProducto(tipo);
        }

        if (dto.getClasificacionId() != null) {
            ClasificacionModel clasificacion = clasificacionRepository.findById(dto.getClasificacionId())
                    .orElseThrow(() -> new RecursoNoEncontradoException(
                            "Clasificación no encontrada con ID: " + dto.getClasificacionId()));
            productoExistente.setClasificacion(clasificacion);
        }

        if (dto.getEstadoId() != null) {
            EstadoModel estado = estadoRepository.findById(dto.getEstadoId())
                    .orElseThrow(() -> new RecursoNoEncontradoException(
                            "Estado no encontrado con ID: " + dto.getEstadoId()));
            productoExistente.setEstado(estado);
        }

        // ================== N:M: PLATAFORMAS ==================
        if (dto.getPlataformasIds() != null) {
            if (productoExistente.getPlataformas() == null) {
                productoExistente.setPlataformas(new ArrayList<>());
            } else {
                productoExistente.getPlataformas().clear();
            }

            if (!dto.getPlataformasIds().isEmpty()) {
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
        }

        // ================== N:M: GÉNEROS ==================
        if (dto.getGenerosIds() != null) {
            if (productoExistente.getGeneros() == null) {
                productoExistente.setGeneros(new ArrayList<>());
            } else {
                productoExistente.getGeneros().clear();
            }

            if (!dto.getGenerosIds().isEmpty()) {
                List<GenerosModel> nuevosGeneros = dto.getGenerosIds()
                        .stream()
                        .map(idGen -> {
                            GeneroModel gen = generoRepository.findById(idGen)
                                    .orElseThrow(() -> new RecursoNoEncontradoException(
                                            "Género no encontrado con ID: " + idGen));
                            GenerosModel puente = new GenerosModel();
                            puente.setProducto(productoExistente);
                            puente.setGenero(gen);
                            return puente;
                        })
                        .collect(Collectors.toList());

                productoExistente.getGeneros().addAll(nuevosGeneros);
            }
        }

        // ================== N:M: EMPRESAS ==================
        if (dto.getEmpresasIds() != null) {
            if (productoExistente.getEmpresas() == null) {
                productoExistente.setEmpresas(new ArrayList<>());
            } else {
                productoExistente.getEmpresas().clear();
            }

            if (!dto.getEmpresasIds().isEmpty()) {
                List<EmpresasModel> nuevasEmpresas = dto.getEmpresasIds()
                        .stream()
                        .map(idEmp -> {
                            EmpresaModel emp = empresaRepository.findById(idEmp)
                                    .orElseThrow(() -> new RecursoNoEncontradoException(
                                            "Empresa no encontrada con ID: " + idEmp));
                            EmpresasModel puente = new EmpresasModel();
                            puente.setProducto(productoExistente);
                            puente.setEmpresa(emp);
                            return puente;
                        })
                        .collect(Collectors.toList());

                productoExistente.getEmpresas().addAll(nuevasEmpresas);
            }
        }

        // ================== N:M: DESARROLLADORES ==================
        if (dto.getDesarrolladoresIds() != null) {
            if (productoExistente.getDesarrolladores() == null) {
                productoExistente.setDesarrolladores(new ArrayList<>());
            } else {
                productoExistente.getDesarrolladores().clear();
            }

            if (!dto.getDesarrolladoresIds().isEmpty()) {
                List<DesarrolladoresModel> nuevosDevs = dto.getDesarrolladoresIds()
                        .stream()
                        .map(idDev -> {
                            DesarrolladorModel dev = desarrolladorRepository.findById(idDev)
                                    .orElseThrow(() -> new RecursoNoEncontradoException(
                                            "Desarrollador no encontrado con ID: " + idDev));
                            DesarrolladoresModel puente = new DesarrolladoresModel();
                            puente.setProducto(productoExistente);
                            puente.setDesarrollador(dev);
                            return puente;
                        })
                        .collect(Collectors.toList());

                productoExistente.getDesarrolladores().addAll(nuevosDevs);
            }
        }

        // ================== IMÁGENES ==================
        if (dto.getImagenesRutas() != null) {

            // si no existe la lista, crearla; si existe, reemplazar
            if (productoExistente.getImagenes() == null) {
                productoExistente.setImagenes(new ArrayList<>());
            } else {
                productoExistente.getImagenes().clear();
            }

            // si viene vacía [], queda sin imágenes (válido)
            if (!dto.getImagenesRutas().isEmpty()) {
                List<ImagenesModel> nuevasImagenes = dto.getImagenesRutas()
                        .stream()
                        .map(ruta -> {
                            ImagenesModel img = new ImagenesModel();
                            img.setRuta(ruta);
                            img.setAltText(productoExistente.getNombre());
                            img.setProducto(productoExistente);
                            return img;
                        })
                        .collect(Collectors.toList());

                productoExistente.getImagenes().addAll(nuevasImagenes);
            }
        }

        // ================== GUARDAR ==================
        productoRepository.save(productoExistente);

        // ================== RECARGAR CON IMÁGENES ==================
        ProductoModel recargado = productoRepository.findByIdWithImagenes(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Producto no encontrado con ID: " + id));

        // ================== DEVOLVER ==================
        return ProductoMapper.toResponseDTO(recargado);
    }

    /* ================= SAGAS ================= */

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

    public List<Map<String, Object>> obtenerSagasConPortada() {
        List<Object[]> filas = productoRepository.findDistinctSagasWithPortada();
        List<Map<String, Object>> lista = new ArrayList<>();

        for (Object[] fila : filas) {
            Map<String, Object> datos = new HashMap<>();
            datos.put("nombre", fila[0]);
            datos.put("portadaSaga", fila[1]);
            lista.add(datos);
        }

        return lista;
    }

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
            datos.put("Saga", fila[5]);
            datos.put("Portada Saga", fila[6]);
            lista.add(datos);
        }

        return lista;
    }
    
    // ================= BÚSQUEDAS / FILTROS (PARA EL CONTROLLER) =================

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

    /* ================= MAPEO DTO -> ENTIDAD ================= */

    private void applyRequestToModel(ProductoRequestDTO dto, ProductoModel producto) {

        producto.setNombre(dto.getNombre());
        producto.setPrecio(dto.getPrecio());

        // ✅ NUEVO: campos HUB (redirigir)
        producto.setUrlCompra(dto.getUrlCompra());
        producto.setLabelCompra(dto.getLabelCompra());

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

        if (dto.getImagenesRutas() != null) {

            if(producto.getImagenes() == null) {
                producto.setImagenes(new ArrayList<>());
            } else {
                producto.getImagenes().clear();
            }

            if(!dto.getImagenesRutas().isEmpty()){
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

                producto.getImagenes().addAll(imagenes);
            }
            
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