package com.example.NoLimits.Multimedia.service.producto;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.dto.pagination.PagedResponse;
import com.example.NoLimits.Multimedia.dto.producto.mapper.ProductoMapper;
import com.example.NoLimits.Multimedia.dto.producto.request.ProductoRequestDTO;
import com.example.NoLimits.Multimedia.dto.producto.response.ProductoResponseDTO;
import com.example.NoLimits.Multimedia.dto.producto.update.ProductoUpdateDTO;
import com.example.NoLimits.Multimedia.model.catalogos.*;
import com.example.NoLimits.Multimedia.model.producto.ImagenesModel;
import com.example.NoLimits.Multimedia.model.producto.ProductoLinkCompraModel;
import com.example.NoLimits.Multimedia.model.producto.ProductoModel;
import com.example.NoLimits.Multimedia.repository.catalogos.*;
import com.example.NoLimits.Multimedia.repository.producto.DetalleVentaRepository;
import com.example.NoLimits.Multimedia.repository.producto.ProductoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductoService {

    @Autowired private ProductoRepository productoRepository;
    @Autowired private DetalleVentaRepository detalleVentaRepository;

    @Autowired private TipoProductoRepository tipoProductoRepository;
    @Autowired private ClasificacionRepository clasificacionRepository;
    @Autowired private EstadoRepository estadoRepository;

    @Autowired private PlataformaRepository plataformaRepository;
    @Autowired private GeneroRepository generoRepository;
    @Autowired private EmpresaRepository empresaRepository;
    @Autowired private DesarrolladorRepository desarrolladorRepository;

    /* ================= CRUD BÁSICO ================= */

    public List<ProductoResponseDTO> findAll() {
        return productoRepository.findAllFull()
                .stream()
                .map(ProductoMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public ProductoResponseDTO findById(Long id) {
        ProductoModel model = productoRepository.findByIdFull(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado con ID: " + id));
        return ProductoMapper.toResponseDTO(model);
    }

    public ProductoResponseDTO save(ProductoRequestDTO dto) {
        validarRequestObligatorio(dto);

        ProductoModel producto = new ProductoModel();
        applyRequestToModel(dto, producto);

        ProductoModel guardado = productoRepository.save(producto);

        ProductoModel recargado = productoRepository.findByIdFull(guardado.getId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado con ID: " + guardado.getId()));

        return ProductoMapper.toResponseDTO(recargado);
    }

    // PUT: reemplaza datos principales
    public ProductoResponseDTO update(Long id, ProductoRequestDTO dto) {
        ProductoModel productoExistente = productoRepository.findByIdFull(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado con ID: " + id));

        validarRequestObligatorio(dto);

        applyRequestToModel(dto, productoExistente);
        productoRepository.save(productoExistente);

        ProductoModel recargado = productoRepository.findByIdFull(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado con ID: " + id));

        return ProductoMapper.toResponseDTO(recargado);
    }

    // PATCH: solo campos no nulos
    public ProductoResponseDTO patch(Long id, ProductoUpdateDTO dto) {
        ProductoModel productoExistente = productoRepository.findByIdFull(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado con ID: " + id));

        // ================== CAMPOS SIMPLES ==================
        if (dto.getNombre() != null) productoExistente.setNombre(dto.getNombre());
        if (dto.getPrecio() != null) productoExistente.setPrecio(dto.getPrecio());
        if (dto.getSaga() != null) productoExistente.setSaga(dto.getSaga());
        if (dto.getPortadaSaga() != null) productoExistente.setPortadaSaga(dto.getPortadaSaga());


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

        // ================== PLATAFORMAS ==================
        syncPlataformas(productoExistente, dto.getPlataformasIds());

        // ================== GÉNEROS ==================
        syncGeneros(productoExistente, dto.getGenerosIds());

        // ================== EMPRESAS ==================
        syncEmpresas(productoExistente, dto.getEmpresasIds());

        // ================== DESARROLLADORES ==================
        syncDesarrolladores(productoExistente, dto.getDesarrolladoresIds());


        // ================== IMÁGENES ==================
        if (dto.getImagenesRutas() != null) {
            if (productoExistente.getImagenes() == null) productoExistente.setImagenes(new ArrayList<>());
            else productoExistente.getImagenes().clear();

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

        // ================== LINKS COMPRA ==================
        if (dto.getLinksCompra() != null) {
            if (productoExistente.getLinksCompra() == null) {
                productoExistente.setLinksCompra(new HashSet<>());
            } else {
                productoExistente.getLinksCompra().clear();
            }

            if (!dto.getLinksCompra().isEmpty()) {
                Set<ProductoLinkCompraModel> nuevosLinks = dto.getLinksCompra()
                        .stream()
                        .map(l -> {
                            if (l.getPlataformaId() == null)
                                throw new IllegalArgumentException("plataformaId es obligatorio");

                            if (l.getUrl() == null || l.getUrl().isBlank())
                                throw new IllegalArgumentException("url es obligatoria");

                            PlataformaModel plat = plataformaRepository.findById(l.getPlataformaId())
                                    .orElseThrow(() -> new RecursoNoEncontradoException(
                                            "Plataforma no encontrada con ID: " + l.getPlataformaId()));

                            ProductoLinkCompraModel link = new ProductoLinkCompraModel();
                            link.setProducto(productoExistente);
                            link.setPlataforma(plat);
                            link.setUrl(l.getUrl());
                            link.setLabel(l.getLabel());
                            return link;
                        })
                        .collect(Collectors.toSet());

                productoExistente.getLinksCompra().addAll(nuevosLinks);
            }
        }

        // ================== GUARDAR + RECARGAR ==================
        productoRepository.save(productoExistente);

        ProductoModel recargado = productoRepository.findByIdFull(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado con ID: " + id));

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
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado con ID: " + id));

        boolean tieneMovimientos = !detalleVentaRepository.findByProducto_Id(id).isEmpty();
        if (tieneMovimientos) {
            throw new IllegalStateException("No se puede eliminar: el producto tiene movimientos en ventas.");
        }

        productoRepository.deleteById(id);
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

        // ===== Relaciones N:M (usar sync para evitar duplicados) =====
        syncPlataformas(producto, dto.getPlataformasIds());
        syncGeneros(producto, dto.getGenerosIds());
        syncEmpresas(producto, dto.getEmpresasIds());
        syncDesarrolladores(producto, dto.getDesarrolladoresIds());

        // IMÁGENES
        if (dto.getImagenesRutas() != null) {
            if (producto.getImagenes() == null) producto.setImagenes(new ArrayList<>());
            else producto.getImagenes().clear();

            if (!dto.getImagenesRutas().isEmpty()) {
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

        // ================== LINKS COMPRA ==================
        if (dto.getLinksCompra() != null) {
            if (producto.getLinksCompra() == null) {
                producto.setLinksCompra(new HashSet<>());
            } else {
                producto.getLinksCompra().clear();
            }
            
            if (!dto.getLinksCompra().isEmpty()) {
                Set<ProductoLinkCompraModel> nuevosLinks = dto.getLinksCompra()
                        .stream()
                        .map(l -> {

                            // VALIDACIONES
                            if (l.getPlataformaId() == null)
                                throw new IllegalArgumentException("plataformaId es obligatorio");

                            if (l.getUrl() == null || l.getUrl().isBlank())
                                throw new IllegalArgumentException("url es obligatoria");

                            PlataformaModel plat = plataformaRepository.findById(l.getPlataformaId())
                                    .orElseThrow(() -> new RecursoNoEncontradoException(
                                            "Plataforma no encontrada con ID: " + l.getPlataformaId()));

                            ProductoLinkCompraModel link = new ProductoLinkCompraModel();
                            link.setProducto(producto);
                            link.setPlataforma(plat);
                            link.setUrl(l.getUrl());
                            link.setLabel(l.getLabel());
                            return link;
                        })
                        .collect(Collectors.toSet());

                producto.getLinksCompra().addAll(nuevosLinks);
            }
        }
    }

    private void validarRequestObligatorio(ProductoRequestDTO dto) {
        if (dto.getTipoProductoId() == null) {
            throw new RecursoNoEncontradoException("Debe indicar un tipo de producto válido.");
        }
        if (dto.getClasificacionId() == null) {
            throw new RecursoNoEncontradoException("Debe indicar una clasificación válida.");
        }
        if (dto.getEstadoId() == null) {
            throw new RecursoNoEncontradoException("Debe indicar un estado válido.");
        }
    }

    private void syncDesarrolladores(ProductoModel producto, List<Long> nuevosIds) {

        if (nuevosIds == null) return;

        if (producto.getDesarrolladores() == null) {
            producto.setDesarrolladores(new HashSet<>());
        }

        Set<Long> nuevosSet = nuevosIds.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        // 1) eliminar los que ya no vienen
        producto.getDesarrolladores().removeIf(rel ->
                rel.getDesarrollador() != null &&
                rel.getDesarrollador().getId() != null &&
                !nuevosSet.contains(rel.getDesarrollador().getId())
        );

        // 2) recalcular existentes después del remove
        Set<Long> existentesSet = producto.getDesarrolladores().stream()
                .map(rel -> rel.getDesarrollador().getId())
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        // 3) agregar solo los que faltan
        for (Long idDev : nuevosSet) {
            if (existentesSet.contains(idDev)) continue;

            DesarrolladorModel dev = desarrolladorRepository.findById(idDev)
                    .orElseThrow(() -> new RecursoNoEncontradoException(
                            "Desarrollador no encontrado con ID: " + idDev));

            DesarrolladoresModel puente = new DesarrolladoresModel();
            puente.setProducto(producto);
            puente.setDesarrollador(dev);

            producto.getDesarrolladores().add(puente);
        }
    }

    private void syncPlataformas(ProductoModel producto, List<Long> nuevosIds) {
        if (nuevosIds == null) return;

        if (producto.getPlataformas() == null) {
            producto.setPlataformas(new HashSet<>());
        }

        Set<Long> nuevosSet = nuevosIds.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        producto.getPlataformas().removeIf(rel ->
                rel.getPlataforma() != null &&
                rel.getPlataforma().getId() != null &&
                !nuevosSet.contains(rel.getPlataforma().getId())
        );

        Set<Long> existentesSet = producto.getPlataformas().stream()
                .map(rel -> rel.getPlataforma().getId())
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        for (Long idPlat : nuevosSet) {
            if (existentesSet.contains(idPlat)) continue;

            PlataformaModel plat = plataformaRepository.findById(idPlat)
                    .orElseThrow(() -> new RecursoNoEncontradoException(
                            "Plataforma no encontrada con ID: " + idPlat));

            PlataformasModel puente = new PlataformasModel();
            puente.setProducto(producto);
            puente.setPlataforma(plat);

            producto.getPlataformas().add(puente);
        }
    }

    private void syncGeneros(ProductoModel producto, List<Long> nuevosIds) {
        if (nuevosIds == null) return;

        if (producto.getGeneros() == null) {
            producto.setGeneros(new HashSet<>());
        }

        Set<Long> nuevosSet = nuevosIds.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        producto.getGeneros().removeIf(rel ->
                rel.getGenero() != null &&
                rel.getGenero().getId() != null &&
                !nuevosSet.contains(rel.getGenero().getId())
        );

        Set<Long> existentesSet = producto.getGeneros().stream()
                .map(rel -> rel.getGenero().getId())
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        for (Long idGen : nuevosSet) {
            if (existentesSet.contains(idGen)) continue;

            GeneroModel gen = generoRepository.findById(idGen)
                    .orElseThrow(() -> new RecursoNoEncontradoException(
                            "Género no encontrado con ID: " + idGen));

            GenerosModel puente = new GenerosModel();
            puente.setProducto(producto);
            puente.setGenero(gen);

            producto.getGeneros().add(puente);
        }
    }

    private void syncEmpresas(ProductoModel producto, List<Long> nuevosIds) {
        if (nuevosIds == null) return;

        if (producto.getEmpresas() == null) {
            producto.setEmpresas(new HashSet<>());
        }

        Set<Long> nuevosSet = nuevosIds.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        producto.getEmpresas().removeIf(rel ->
                rel.getEmpresa() != null &&
                rel.getEmpresa().getId() != null &&
                !nuevosSet.contains(rel.getEmpresa().getId())
        );

        Set<Long> existentesSet = producto.getEmpresas().stream()
                .map(rel -> rel.getEmpresa().getId())
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        for (Long idEmp : nuevosSet) {
            if (existentesSet.contains(idEmp)) continue;

            EmpresaModel emp = empresaRepository.findById(idEmp)
                    .orElseThrow(() -> new RecursoNoEncontradoException(
                            "Empresa no encontrada con ID: " + idEmp));

            EmpresasModel puente = new EmpresasModel();
            puente.setProducto(producto);
            puente.setEmpresa(emp);

            producto.getEmpresas().add(puente);
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

        return new PagedResponse<>(contenido, page, result.getTotalPages(), result.getTotalElements());
    }
}