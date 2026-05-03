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
import com.example.NoLimits.Multimedia.service.ai.ProductoEmbeddingService;
import com.example.NoLimits.Multimedia.service.scraping.ScrapingClientService;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    @Autowired
    private TipoEmpresaRepository tipoEmpresaRepository;

    @Autowired
    private TipoDeDesarrolladorRepository tipoDeDesarrolladorRepository;
    // scraping Steam
    @Autowired private ScrapingClientService scrapingClientService;

    // embeddings IA
    @Autowired private ProductoEmbeddingService productoEmbeddingService;

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

        try {
            actualizarPrecioDesdeSteam(guardado.getId());
        } catch (Exception e) {
            System.out.println("No se pudo actualizar precio desde Steam al crear producto: " + e.getMessage());
        }

        ProductoModel recargado = productoRepository.findByIdFull(guardado.getId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado con ID: " + guardado.getId()));

        try {
            actualizarEmbeddingProducto(recargado);
        } catch (Exception e) {
            System.err.println("Embedding falló pero producto guardado: " + e.getMessage());
        }

        return ProductoMapper.toResponseDTO(recargado);
    }

    public ProductoResponseDTO update(Long id, ProductoRequestDTO dto) {
        ProductoModel productoExistente = productoRepository.findByIdFull(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado con ID: " + id));

        validarRequestObligatorio(dto);

        applyRequestToModel(dto, productoExistente);
        productoRepository.save(productoExistente);

        ProductoModel recargado = productoRepository.findByIdFull(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado con ID: " + id));

        // actualizarEmbeddingProducto(recargado);

        return ProductoMapper.toResponseDTO(recargado);
    }

    public ProductoResponseDTO patch(Long id, ProductoUpdateDTO dto) {
        ProductoModel productoExistente = productoRepository.findByIdFull(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado con ID: " + id));

        if (dto.getNombre() != null) productoExistente.setNombre(dto.getNombre());
        if (dto.getPrecio() != null) productoExistente.setPrecio(dto.getPrecio());
        // Nuevos campos agregados Sinopsis y URL del trailer
        if (dto.getSinopsis() != null) productoExistente.setSinopsis(dto.getSinopsis());
        if (dto.getUrlTrailer() != null) productoExistente.setUrlTrailer(dto.getUrlTrailer());
        if (dto.getSaga() != null) productoExistente.setSaga(dto.getSaga());
        if (dto.getPortadaSaga() != null) productoExistente.setPortadaSaga(dto.getPortadaSaga());

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

        if (dto.getTipoEmpresaId() != null) {
            TipoEmpresaModel tipoEmpresa = tipoEmpresaRepository.findById(dto.getTipoEmpresaId())
                    .orElseThrow(() -> new RecursoNoEncontradoException(
                            "Tipo de empresa no encontrado con ID: " + dto.getTipoEmpresaId()));
            productoExistente.setTipoEmpresa(tipoEmpresa);
        }

        if (dto.getTipoDesarrolladorId() != null) {
            TipoDeDesarrolladorModel tipoDesarrollador = tipoDeDesarrolladorRepository.findById(dto.getTipoDesarrolladorId())
                    .orElseThrow(() -> new RecursoNoEncontradoException(
                            "Tipo de desarrollador no encontrado con ID: " + dto.getTipoDesarrolladorId()));
            productoExistente.setTipoDesarrollador(tipoDesarrollador);
        }

        syncPlataformas(productoExistente, dto.getPlataformasIds());
        syncGeneros(productoExistente, dto.getGenerosIds());
        syncEmpresas(productoExistente, dto.getEmpresasIds());
        syncDesarrolladores(productoExistente, dto.getDesarrolladoresIds());

        if (dto.getImagenesRutas() != null) {
            if (productoExistente.getImagenes() == null) productoExistente.setImagenes(new ArrayList<>());
            else productoExistente.getImagenes().clear();

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

        syncLinksCompra(productoExistente, dto.getLinksCompra());

        productoRepository.save(productoExistente);

        ProductoModel recargado = productoRepository.findByIdFull(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado con ID: " + id));

        // actualizarEmbeddingProducto(recargado);

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

    /* ================= BÚSQUEDAS / FILTROS ================= */

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
        producto.setSinopsis(dto.getSinopsis());
        producto.setUrlTrailer(dto.getUrlTrailer());
        producto.setSaga(dto.getSaga());
        producto.setPortadaSaga(dto.getPortadaSaga());

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

        if (dto.getTipoEmpresaId() != null) {
            TipoEmpresaModel tipoEmpresa = tipoEmpresaRepository.findById(dto.getTipoEmpresaId())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                    "Tipo de empresa no encontrado con ID: " + dto.getTipoEmpresaId()));
            producto.setTipoEmpresa(tipoEmpresa);
        } else {
            producto.setTipoEmpresa(null);
        }

        if (dto.getTipoDesarrolladorId() != null) {
            TipoDeDesarrolladorModel tipoDesarrollador = tipoDeDesarrolladorRepository
                .findById(dto.getTipoDesarrolladorId())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                    "Tipo de desarrollador no encontrado con ID: " + dto.getTipoDesarrolladorId()));
            producto.setTipoDesarrollador(tipoDesarrollador);
        } else {
            producto.setTipoDesarrollador(null);
        }

        syncPlataformas(producto, dto.getPlataformasIds());
        syncGeneros(producto, dto.getGenerosIds());
        syncEmpresas(producto, dto.getEmpresasIds());
        syncDesarrolladores(producto, dto.getDesarrolladoresIds());

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

        syncLinksCompra(producto, dto.getLinksCompra());
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

    /* ================= EMBEDDINGS IA ================= */

    @org.springframework.transaction.annotation.Transactional(
        propagation = org.springframework.transaction.annotation.Propagation.REQUIRES_NEW
    )
    public void actualizarEmbeddingProducto(ProductoModel producto) {
        try {
            String contenido = """
                    Nombre: %s
                    Tipo: %s
                    Clasificación: %s
                    Estado: %s
                    Precio: %s
                    Saga: %s
                    Genero: %s
                    Empresa: %s
                    Plataforma: %s
                    Desarrollador: %s
                    """.formatted(
                    producto.getNombre(),
                    producto.getTipoProducto() != null ? producto.getTipoProducto().getNombre() : "",
                    producto.getClasificacion() != null ? producto.getClasificacion().getNombre() : "",
                    producto.getEstado() != null ? producto.getEstado().getNombre() : "",
                    producto.getPrecio(),
                    producto.getSaga(),
                    producto.getGeneros() != null
                            ? producto.getGeneros().stream()
                                .map(g -> g.getGenero().getNombre())
                                .collect(Collectors.joining(", "))
                            : "",
                    producto.getEmpresas() != null
                            ? producto.getEmpresas().stream()
                                .map(e -> e.getEmpresa().getNombre())
                                .collect(Collectors.joining(", "))
                            : "",
                    producto.getPlataformas() != null
                            ? producto.getPlataformas().stream()
                                .map(p -> p.getPlataforma().getNombre())
                                .collect(Collectors.joining(", "))
                            : "",
                    producto.getDesarrolladores() != null
                            ? producto.getDesarrolladores().stream()
                                .map(d -> d.getDesarrollador().getNombre())
                                .collect(Collectors.joining(", "))
                            : ""
            );

            productoEmbeddingService.guardarEmbeddingProducto(producto.getId(), contenido);

        } catch (Exception e) {
            System.err.println("No se pudo actualizar el embedding del producto ID "
                    + producto.getId() + ": " + e.getMessage());
        }
    }

    /* ================= LINKS COMPRA ================= */

    private void syncLinksCompra(
            ProductoModel producto,
            List<com.example.NoLimits.Multimedia.dto.producto.request.LinkCompraDTO> nuevosLinks
    ) {
        if (nuevosLinks == null) return;

        if (producto.getLinksCompra() == null) {
            producto.setLinksCompra(new HashSet<>());
        }

        Set<Long> plataformasRecibidas = nuevosLinks.stream()
                .map(link -> {
                    if (link.getPlataformaId() == null) {
                        throw new IllegalArgumentException("plataformaId es obligatorio");
                    }
                    return link.getPlataformaId();
                })
                .collect(Collectors.toSet());

        producto.getLinksCompra().removeIf(link ->
                link.getPlataforma() != null &&
                link.getPlataforma().getId() != null &&
                !plataformasRecibidas.contains(link.getPlataforma().getId())
        );

        Map<Long, ProductoLinkCompraModel> existentesMap = producto.getLinksCompra().stream()
                .filter(link -> link.getPlataforma() != null && link.getPlataforma().getId() != null)
                .collect(Collectors.toMap(
                        link -> link.getPlataforma().getId(),
                        link -> link,
                        (a, b) -> a
                ));

        for (com.example.NoLimits.Multimedia.dto.producto.request.LinkCompraDTO l : nuevosLinks) {
            if (l.getPlataformaId() == null) {
                throw new IllegalArgumentException("plataformaId es obligatorio");
            }

            if (l.getUrl() == null || l.getUrl().isBlank()) {
                throw new IllegalArgumentException("url es obligatoria");
            }

            ProductoLinkCompraModel existente = existentesMap.get(l.getPlataformaId());

            if (existente != null) {
                existente.setUrl(l.getUrl().trim());
                existente.setLabel(
                        l.getLabel() != null && !l.getLabel().isBlank()
                                ? l.getLabel().trim()
                                : existente.getPlataforma().getNombre()
                );
                existente.setAppId(
                        l.getAppId() != null && !l.getAppId().isBlank()
                                ? l.getAppId().trim()
                                : existente.getAppId()
                );
            } else {
                PlataformaModel plat = plataformaRepository.findById(l.getPlataformaId())
                        .orElseThrow(() -> new RecursoNoEncontradoException(
                                "Plataforma no encontrada con ID: " + l.getPlataformaId()
                        ));

                ProductoLinkCompraModel nuevo = new ProductoLinkCompraModel();
                nuevo.setProducto(producto);
                nuevo.setPlataforma(plat);
                nuevo.setUrl(l.getUrl().trim());
                nuevo.setLabel(
                        l.getLabel() != null && !l.getLabel().isBlank()
                                ? l.getLabel().trim()
                                : plat.getNombre()
                );
                nuevo.setAppId(
                        l.getAppId() != null && !l.getAppId().isBlank()
                                ? l.getAppId().trim()
                                : null
                );

                producto.getLinksCompra().add(nuevo);
            }
        }
    }

    /* ================= RELACIONES N:M ================= */

    private void syncDesarrolladores(ProductoModel producto, List<Long> nuevosIds) {
        if (nuevosIds == null) return;

        if (producto.getDesarrolladores() == null) {
            producto.setDesarrolladores(new HashSet<>());
        }

        Set<Long> nuevosSet = nuevosIds.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        producto.getDesarrolladores().removeIf(rel ->
                rel.getDesarrollador() != null &&
                rel.getDesarrollador().getId() != null &&
                !nuevosSet.contains(rel.getDesarrollador().getId())
        );

        Set<Long> existentesSet = producto.getDesarrolladores().stream()
                .map(rel -> rel.getDesarrollador().getId())
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

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
        Page<ProductoModel> result = productoRepository.findAllFullPaged(pageable);

        // Carga cada producto con sus relaciones por separado
        List<ProductoResponseDTO> contenido = result.getContent()
                .stream()
                .map(p -> productoRepository.findByIdFull(p.getId())
                        .map(ProductoMapper::toResponseDTO)
                        .orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return new PagedResponse<>(contenido, page, result.getTotalPages(), result.getTotalElements());
    }

    /* ================= SCRAPING STEAM ================= */

    public ProductoResponseDTO actualizarPrecioDesdeSteam(Long productoId) {
        ProductoModel producto = productoRepository.findByIdFull(productoId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado con ID: " + productoId));

        ProductoLinkCompraModel linkSteam = producto.getLinksCompra()
                .stream()
                .filter(link -> link.getAppId() != null && !link.getAppId().isBlank())
                .findFirst()
                .orElseThrow(() -> new RecursoNoEncontradoException("El producto no tiene appId de Steam configurado."));

        Map<String, Object> datosSteam = scrapingClientService.obtenerPrecioSteam(linkSteam.getAppId());

        Double precio = Double.valueOf(datosSteam.get("precio").toString()) / 100;

        producto.setPrecio(precio);

        linkSteam.setNombrePlataforma((String) datosSteam.get("nombre"));
        linkSteam.setPrecioActual(precio);
        linkSteam.setPrecioFormato((String) datosSteam.get("precioFormato"));
        linkSteam.setMoneda((String) datosSteam.get("moneda"));
        linkSteam.setUrl((String) datosSteam.get("urlPlataforma"));
        linkSteam.setFechaUltimaActualizacion(LocalDateTime.now());

        productoRepository.save(producto);

        ProductoModel recargado = productoRepository.findByIdFull(productoId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado con ID: " + productoId));

        try {
            actualizarEmbeddingProducto(recargado);
        } catch (Exception e) {
            System.err.println("Embedding falló en steam update: " + e.getMessage());
        }

        return ProductoMapper.toResponseDTO(recargado);
    }

    public List<Long> obtenerIdsProductosConAppId() {
        return productoRepository.findAll()
                .stream()
                .filter(p -> p.getLinksCompra() != null &&
                        p.getLinksCompra().stream().anyMatch(l ->
                                l.getAppId() != null && !l.getAppId().isBlank()))
                .map(ProductoModel::getId)
                .toList();
    }
}