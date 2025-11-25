// Ruta: src/main/java/com/example/NoLimits/Multimedia/service/VentaService.java
package com.example.NoLimits.Multimedia.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.dto.DetalleVentaRequest;
import com.example.NoLimits.Multimedia.dto.VentaRequest;
import com.example.NoLimits.Multimedia.model.DetalleVentaModel;
import com.example.NoLimits.Multimedia.model.EstadoModel;
import com.example.NoLimits.Multimedia.model.MetodoEnvioModel;
import com.example.NoLimits.Multimedia.model.MetodoPagoModel;
import com.example.NoLimits.Multimedia.model.ProductoModel;
import com.example.NoLimits.Multimedia.model.UsuarioModel;
import com.example.NoLimits.Multimedia.model.VentaModel;
import com.example.NoLimits.Multimedia.repository.EstadoRepository;
import com.example.NoLimits.Multimedia.repository.MetodoEnvioRepository;
import com.example.NoLimits.Multimedia.repository.MetodoPagoRepository;
import com.example.NoLimits.Multimedia.repository.ProductoRepository;
import com.example.NoLimits.Multimedia.repository.UsuarioRepository;
import com.example.NoLimits.Multimedia.repository.VentaRepository;

import jakarta.transaction.Transactional;

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

    // Listar todas las ventas
    public List<VentaModel> findAll() {
        return ventaRepository.findAll();
    }

    // Buscar una venta por ID. Si no existe, lanzo excepción de recurso no encontrado.
    public VentaModel findById(Long id) {
        return ventaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Venta no encontrada con ID: " + id));
    }

    // Buscar ventas filtrando por el ID del método de pago
    public List<VentaModel> findByMetodoPago(Long metodoPagoId) {
        return ventaRepository.findByMetodoPagoModel_Id(metodoPagoId);
    }

    /*
     Guardar una venta recibiendo la entidad completa.

     Aquí se valida que vengan los IDs de usuario, método de pago, método de envío y estado.
     Luego se cargan las entidades completas desde los repositorios para asegurar que existen.
    */
    public VentaModel save(VentaModel venta) {

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

        return ventaRepository.save(venta);
    }

    // Eliminar una venta por ID, validando primero que exista
    public void deleteById(Long id) {
        if (!ventaRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("Venta no encontrada con ID: " + id);
        }
        ventaRepository.deleteById(id);
    }

    // Buscar ventas por fecha de compra
    public List<VentaModel> findByFechaCompra(LocalDate fechaCompra) {
        return ventaRepository.findByFechaCompra(fechaCompra);
    }

    // Buscar ventas por hora de compra
    public List<VentaModel> findByHoraCompra(LocalTime horaCompra) {
        return ventaRepository.findByHoraCompra(horaCompra);
    }

    // Buscar ventas filtrando por usuario y método de pago
    public List<VentaModel> findByUsuarioYMetodoPago(Long usuarioId, Long metodoPagoId) {
        return ventaRepository.findByUsuarioModel_IdAndMetodoPagoModel_Id(usuarioId, metodoPagoId);
    }

    /*
     Actualización completa (PUT).

     Reemplaza los datos principales de la venta:
     - Fecha y hora.
     - Usuario, método de pago, método de envío y estado (si vienen con ID).
    */
    public VentaModel update(Long id, VentaModel d) {

        var v = findById(id);
        v.setFechaCompra(d.getFechaCompra());
        v.setHoraCompra(d.getHoraCompra());

        if (d.getUsuarioModel() != null && d.getUsuarioModel().getId() != null)
            v.setUsuarioModel(obtenerUsuario(d.getUsuarioModel().getId()));

        if (d.getMetodoPagoModel() != null && d.getMetodoPagoModel().getId() != null)
            v.setMetodoPagoModel(obtenerMetodoPago(d.getMetodoPagoModel().getId()));

        if (d.getMetodoEnvioModel() != null && d.getMetodoEnvioModel().getId() != null)
            v.setMetodoEnvioModel(obtenerMetodoEnvio(d.getMetodoEnvioModel().getId()));

        if (d.getEstado() != null && d.getEstado().getId() != null)
            v.setEstado(obtenerEstado(d.getEstado().getId()));

        return ventaRepository.save(v);
    }

    /*
     Actualización parcial (PATCH).

     Solo se modifican los campos que vienen no nulos:
     - Fecha, hora.
     - Usuario, método de pago, método de envío y estado, si vienen con ID.
    */
    public VentaModel patch(Long id, VentaModel d) {

        var v = findById(id);

        if (d.getFechaCompra() != null) v.setFechaCompra(d.getFechaCompra());
        if (d.getHoraCompra() != null)  v.setHoraCompra(d.getHoraCompra());

        if (d.getUsuarioModel() != null && d.getUsuarioModel().getId() != null)
            v.setUsuarioModel(obtenerUsuario(d.getUsuarioModel().getId()));

        if (d.getMetodoPagoModel() != null && d.getMetodoPagoModel().getId() != null)
            v.setMetodoPagoModel(obtenerMetodoPago(d.getMetodoPagoModel().getId()));

        if (d.getMetodoEnvioModel() != null && d.getMetodoEnvioModel().getId() != null)
            v.setMetodoEnvioModel(obtenerMetodoEnvio(d.getMetodoEnvioModel().getId()));

        if (d.getEstado() != null && d.getEstado().getId() != null)
            v.setEstado(obtenerEstado(d.getEstado().getId()));

        return ventaRepository.save(v);
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
     Crear una venta usando los datos que vienen desde el frontend en un DTO (VentaRequest)
     y el usuario que está guardado en la sesión.

     Flujo:
     1) Se obtiene el usuario por su ID (usuarioId) desde la sesión.
     2) Se construye una VentaModel con:
        - usuario
        - método de pago
        - método de envío
        - estado
        - fecha y hora actuales
     3) Se recorre la lista de DetalleVentaRequest para:
        - buscar cada producto por ID
        - crear un DetalleVentaModel con cantidad y precio unitario
        - asociar cada detalle a la venta
     4) Se guarda la venta. Por cascada también se guardan los detalles.
    */
    public VentaModel crearVentaDesdeRequest(VentaRequest request, Long usuarioId) {

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
            for (DetalleVentaRequest d : request.getDetalles()) {

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
        return ventaRepository.save(venta);
    }

    /* ================= Helpers internos ================= */

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
}