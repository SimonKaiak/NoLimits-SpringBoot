package com.example.NoLimits.Multimedia.service.venta;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.dto.pagination.PagedResponse;
import com.example.NoLimits.Multimedia.dto.producto.request.DetalleVentaRequestDTO;
import com.example.NoLimits.Multimedia.dto.producto.response.DetalleVentaResponseDTO;
import com.example.NoLimits.Multimedia.dto.venta.request.VentaRequestDTO;
import com.example.NoLimits.Multimedia.dto.venta.response.VentaResponseDTO;
import com.example.NoLimits.Multimedia.dto.venta.update.VentaUpdateDTO;
import com.example.NoLimits.Multimedia.model.catalogos.EstadoModel;
import com.example.NoLimits.Multimedia.model.catalogos.MetodoEnvioModel;
import com.example.NoLimits.Multimedia.model.catalogos.MetodoPagoModel;
import com.example.NoLimits.Multimedia.model.producto.DetalleVentaModel;
import com.example.NoLimits.Multimedia.model.producto.ProductoModel;
import com.example.NoLimits.Multimedia.model.usuario.UsuarioModel;
import com.example.NoLimits.Multimedia.model.venta.VentaModel;
import com.example.NoLimits.Multimedia.repository.catalogos.EstadoRepository;
import com.example.NoLimits.Multimedia.repository.catalogos.MetodoEnvioRepository;
import com.example.NoLimits.Multimedia.repository.catalogos.MetodoPagoRepository;
import com.example.NoLimits.Multimedia.repository.producto.ProductoRepository;
import com.example.NoLimits.Multimedia.repository.usuario.UsuarioRepository;
import com.example.NoLimits.Multimedia.repository.venta.VentaRepository;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

/*
 Servicio encargado de manejar toda la lógica relacionada con las ventas.

 Desde aquí:
 - Se consultan ventas existentes.
 - Se crean nuevas ventas.
 - Se actualizan o eliminan ventas.
 - Se arma la venta completa usando DTOs que vienen del frontend.
*/
@Service
@Transactional
public class VentaService {

    @Autowired private VentaRepository ventaRepository;
    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private MetodoPagoRepository metodoPagoRepository;
    @Autowired private MetodoEnvioRepository metodoEnvioRepository;
    @Autowired private EstadoRepository estadoRepository;
    @Autowired private ProductoRepository productoRepository;

    /* ================= CRUD CLÁSICO (operaciones básicas) ================= */

    // Listar todas las ventas (devuelve DTOs)
    public List<VentaResponseDTO> findAll() {
        return ventaRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    // Buscar una venta por ID (devuelve DTO). Si no existe, lanzo excepción.
    public VentaResponseDTO findById(Long id) {
        VentaModel venta = findEntityById(id);
        return toResponseDTO(venta);
    }

    // Buscar ventas filtrando por el ID del método de pago
    public List<VentaResponseDTO> findByMetodoPago(Long metodoPagoId) {
        return ventaRepository.findByMetodoPagoModel_Id(metodoPagoId)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    /*
     Guardar una venta recibiendo la entidad completa.

     OJO: este método sigue recibiendo la entidad VentaModel, pero
     ahora devuelve un DTO de salida (VentaResponseDTO).

     Se valida que vengan los IDs de usuario, método de pago, método de envío y estado.
     Luego se cargan las entidades completas desde los repositorios para asegurar que existen.
    */
    public VentaResponseDTO save(VentaModel venta) {

        if (venta.getUsuarioModel() == null || venta.getUsuarioModel().getId() == null)
            throw new RecursoNoEncontradoException("Debe proporcionar un ID de usuario válido.");

        if (venta.getMetodoPagoModel() == null || venta.getMetodoPagoModel().getId() == null)
            throw new RecursoNoEncontradoException("Debe proporcionar un ID de método de pago válido.");

        if (venta.getMetodoEnvioModel() == null || venta.getMetodoEnvioModel().getId() == null)
            throw new RecursoNoEncontradoException("Debe proporcionar un ID de método de envío válido.");

        if (venta.getEstado() == null || venta.getEstado().getId() == null)
            throw new RecursoNoEncontradoException("Debe proporcionar un ID de estado válido.");

        // Cargar las entidades completas desde sus repositorios
        venta.setUsuarioModel(obtenerUsuario(venta.getUsuarioModel().getId()));
        venta.setMetodoPagoModel(obtenerMetodoPago(venta.getMetodoPagoModel().getId()));
        venta.setMetodoEnvioModel(obtenerMetodoEnvio(venta.getMetodoEnvioModel().getId()));
        venta.setEstado(obtenerEstado(venta.getEstado().getId()));

        // Si no viene fecha/hora, se asignan los valores actuales
        if (venta.getFechaCompra() == null) venta.setFechaCompra(LocalDate.now());
        if (venta.getHoraCompra() == null)  venta.setHoraCompra(LocalTime.now());

        VentaModel guardada = ventaRepository.save(venta);
        return toResponseDTO(guardada);
    }

    // Eliminar una venta por ID, validando primero que exista
    public void deleteById(Long id) {
        if (!ventaRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("Venta no encontrada con ID: " + id);
        }
        ventaRepository.deleteById(id);
    }

    // Buscar ventas por fecha de compra
    public List<VentaResponseDTO> findByFechaCompra(LocalDate fechaCompra) {
        return ventaRepository.findByFechaCompra(fechaCompra)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    // Buscar ventas por hora de compra
    public List<VentaResponseDTO> findByHoraCompra(LocalTime horaCompra) {
        return ventaRepository.findByHoraCompra(horaCompra)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    // Buscar ventas filtrando por usuario y método de pago
    public List<VentaResponseDTO> findByUsuarioYMetodoPago(Long usuarioId, Long metodoPagoId) {
        return ventaRepository.findByUsuarioModel_IdAndMetodoPagoModel_Id(usuarioId, metodoPagoId)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    /*
     Actualización (PUT) usando VentaUpdateDTO.

     Reemplaza/ajusta los datos principales de la venta:
     - Fecha y hora (si vienen).
     - Método de pago, método de envío y estado (si vienen con ID).

     Nota: el usuario de la venta no se modifica aquí (el DTO no lo expone).
    */
    public VentaResponseDTO update(Long id, VentaUpdateDTO d) {

        VentaModel v = findEntityById(id);

        // Fecha y hora (si vienen)
        if (d.getFechaCompra() != null) v.setFechaCompra(d.getFechaCompra());
        if (d.getHoraCompra() != null)  v.setHoraCompra(d.getHoraCompra());

        // Método de pago
        if (d.getMetodoPagoId() != null) {
            v.setMetodoPagoModel(obtenerMetodoPago(d.getMetodoPagoId()));
        }

        // Método de envío
        if (d.getMetodoEnvioId() != null) {
            v.setMetodoEnvioModel(obtenerMetodoEnvio(d.getMetodoEnvioId()));
        }

        // Estado
        if (d.getEstadoId() != null) {
            v.setEstado(obtenerEstado(d.getEstadoId()));
        }

        VentaModel actualizada = ventaRepository.save(v);
        return toResponseDTO(actualizada);
    }

    /*
     Actualización parcial (PATCH) usando el mismo VentaUpdateDTO.

     Solo se modifican los campos que vienen no nulos.
    */
    public VentaResponseDTO patch(Long id, VentaUpdateDTO d) {

        // Misma lógica que update, campos opcionales:
        return update(id, d);
    }

    /*
     Construye una lista de mapas con un resumen de ventas.

     Cada mapa contiene claves simples:
     - ID
     - Fecha Compra
     - Hora Compra
     - UsuarioID
     - Usuario
     - Método Pago
     - Método Envío
     - Estado

     Esto es útil para mostrar tablas resumen sin exponer toda la entidad completa.
    */
    public List<Map<String, Object>> obtenerVentasConDatos() {

        List<Object[]> res = ventaRepository.obtenerVentasResumen();
        List<Map<String, Object>> out = new ArrayList<>();

        for (Object[] r : res) {
            Map<String, Object> m = new HashMap<>();
            m.put("ID", r[0]);
            m.put("Fecha Compra", r[1]);
            m.put("Hora Compra", r[2]);
            m.put("UsuarioID", r[3]);
            m.put("Usuario", r[4]);
            m.put("Método Pago", r[5]);
            m.put("Método Envío", r[6]);
            m.put("Estado", r[7]);
            out.add(m);
        }

        return out;
    }

    /* =========================================================
       NUEVO FLUJO: crear venta a partir de DTO y sesión HttpSession
       ========================================================= */

    /*
     Crear una venta usando los datos que vienen desde el frontend en un DTO (VentaRequestDTO)
     y el usuario que está guardado en la sesión (usuarioId).

     Flujo:
     1) Se obtiene el usuario por su ID (usuarioId) desde la sesión.
     2) Se construye una VentaModel con:
        - usuario
        - método de pago
        - método de envío
        - estado
        - fecha y hora actuales
     3) Se recorre la lista de DetalleVentaRequestDTO para:
        - buscar cada producto por ID
        - crear un DetalleVentaModel con cantidad y precio unitario
        - asociar cada detalle a la venta
     4) Se guarda la venta. Por cascada también se guardan los detalles.
    */
    public VentaResponseDTO crearVentaDesdeRequest(VentaRequestDTO request, Long usuarioId) {

        // Buscar usuario que viene asociado a la sesión
        UsuarioModel usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        // Armar entidad VentaModel base
        VentaModel venta = new VentaModel();
        venta.setUsuarioModel(usuario);
        venta.setMetodoPagoModel(obtenerMetodoPago(request.getMetodoPagoId()));
        venta.setMetodoEnvioModel(obtenerMetodoEnvio(request.getMetodoEnvioId()));
        venta.setEstado(obtenerEstado(request.getEstadoId()));
        venta.setFechaCompra(LocalDate.now());
        venta.setHoraCompra(LocalTime.now());

        List<DetalleVentaModel> detalles = new ArrayList<>();

        // Si el request trae detalles, se procesan uno a uno
        if (request.getDetalles() != null) {
            for (DetalleVentaRequestDTO d : request.getDetalles()) {

                // Buscar el producto por ID, si no existe se lanza error
                ProductoModel producto = productoRepository.findById(d.getProductoId())
                    .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Producto no encontrado"));

                // Construir el detalle de venta
                DetalleVentaModel detalle = new DetalleVentaModel();
                detalle.setVenta(venta);
                detalle.setProducto(producto);
                detalle.setCantidad(d.getCantidad());
                detalle.setPrecioUnitario(d.getPrecioUnitario());
                // El subtotal lo calcula el propio modelo, no se setea aquí

                detalles.add(detalle);
            }
        }

        // Asociar la lista de detalles a la venta
        venta.setDetalles(detalles);

        // Gracias a CascadeType.ALL, al guardar la venta también se guardan los detalles
        VentaModel guardada = ventaRepository.save(venta);
        return toResponseDTO(guardada);
    }

    public PagedResponse<VentaResponseDTO> findMisComprasPaged(Long usuarioId, int page, int size) {

    Pageable pageable = PageRequest.of(page - 1, size, Sort.by("fechaCompra").descending());

    Page<VentaModel> result = ventaRepository.findByUsuarioModel_Id(usuarioId, pageable);

    List<VentaResponseDTO> contenido = result.getContent()
            .stream()
            .map(this::toResponseDTO)
            .toList();

    return new PagedResponse<>(
            contenido,
            page,
            result.getTotalPages(),
            result.getTotalElements()
        );
    }

    /* ================= Helpers internos ================= */

    // Obtiene una venta como entidad por ID, o lanza excepción si no existe
    private VentaModel findEntityById(Long id) {
        return ventaRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Venta no encontrada con ID: " + id));
    }

    // Obtiene un usuario por ID, o lanza excepción si no existe
    private UsuarioModel obtenerUsuario(Long id) {
        return usuarioRepository.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado con ID: " + id));
    }

    // Obtiene un método de pago por ID, o lanza excepción si no existe
    private MetodoPagoModel obtenerMetodoPago(Long id) {
        return metodoPagoRepository.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException("Método de pago no encontrado con ID: " + id));
    }

    // Obtiene un método de envío por ID, o lanza excepción si no existe
    private MetodoEnvioModel obtenerMetodoEnvio(Long id) {
        return metodoEnvioRepository.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException("Método de envío no encontrado con ID: " + id));
    }

    // Obtiene un estado por ID, o lanza excepción si no existe
    private EstadoModel obtenerEstado(Long id) {
        return estadoRepository.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException("Estado no encontrado con ID: " + id));
    }

    // ================= Mapeos entidad → DTO =================

    private VentaResponseDTO toResponseDTO(VentaModel venta) {
        VentaResponseDTO dto = new VentaResponseDTO();

        dto.setId(venta.getId());
        dto.setFechaCompra(venta.getFechaCompra());
        dto.setHoraCompra(venta.getHoraCompra());

        if (venta.getUsuarioModel() != null) {
            dto.setUsuarioId(venta.getUsuarioModel().getId());
            // Si tu UsuarioModel tiene apellido, aquí podrías concatenar.
            dto.setUsuarioNombre(venta.getUsuarioModel().getNombre());
        }

        if (venta.getMetodoPagoModel() != null) {
            dto.setMetodoPagoId(venta.getMetodoPagoModel().getId());
            dto.setMetodoPagoNombre(venta.getMetodoPagoModel().getNombre());
        }

        if (venta.getMetodoEnvioModel() != null) {
            dto.setMetodoEnvioId(venta.getMetodoEnvioModel().getId());
            dto.setMetodoEnvioNombre(venta.getMetodoEnvioModel().getNombre());
        }

        if (venta.getEstado() != null) {
            dto.setEstadoId(venta.getEstado().getId());
            dto.setEstadoNombre(venta.getEstado().getNombre());
        }

        dto.setTotalVenta(venta.getTotalVenta());

        if (venta.getDetalles() != null) {
            List<DetalleVentaResponseDTO> detalleDTOs = venta.getDetalles()
                    .stream()
                    .map(this::toDetalleResponseDTO)
                    .toList();
            dto.setDetalles(detalleDTOs);
        }

        return dto;
    }

    private DetalleVentaResponseDTO toDetalleResponseDTO(DetalleVentaModel detalle) {
        DetalleVentaResponseDTO dto = new DetalleVentaResponseDTO();

        dto.setId(detalle.getId());
        if (detalle.getProducto() != null) {
            dto.setProductoId(detalle.getProducto().getId());
            dto.setProductoNombre(detalle.getProducto().getNombre());
        }
        dto.setCantidad(detalle.getCantidad());
        dto.setPrecioUnitario(detalle.getPrecioUnitario());
        dto.setSubtotal(detalle.getSubtotal());

        return dto;
    }
}