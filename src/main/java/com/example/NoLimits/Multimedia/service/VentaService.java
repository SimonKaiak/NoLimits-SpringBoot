package com.example.NoLimits.Multimedia.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.model.EstadoModel;
import com.example.NoLimits.Multimedia.model.MetodoEnvioModel;
import com.example.NoLimits.Multimedia.model.MetodoPagoModel;
import com.example.NoLimits.Multimedia.model.UsuarioModel;
import com.example.NoLimits.Multimedia.model.VentaModel;
import com.example.NoLimits.Multimedia.repository.EstadoRepository;
import com.example.NoLimits.Multimedia.repository.MetodoEnvioRepository;
import com.example.NoLimits.Multimedia.repository.MetodoPagoRepository;
import com.example.NoLimits.Multimedia.repository.UsuarioRepository;
import com.example.NoLimits.Multimedia.repository.VentaRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class VentaService {

    @Autowired private VentaRepository ventaRepository;
    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private MetodoPagoRepository metodoPagoRepository;
    @Autowired private MetodoEnvioRepository metodoEnvioRepository;
    @Autowired private EstadoRepository estadoRepository;

    public List<VentaModel> findAll() { 
        return ventaRepository.findAll(); 
    }

    public VentaModel findById(Long id) {
        return ventaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Venta no encontrada con ID: " + id));
    }

    public List<VentaModel> findByMetodoPago(Long metodoPagoId) {
        return ventaRepository.findByMetodoPagoModel_Id(metodoPagoId);
    }

    public VentaModel save(VentaModel venta) {
        if (venta.getUsuarioModel()==null || venta.getUsuarioModel().getId()==null)
            throw new RecursoNoEncontradoException("Debe proporcionar un ID de usuario válido.");
        if (venta.getMetodoPagoModel()==null || venta.getMetodoPagoModel().getId()==null)
            throw new RecursoNoEncontradoException("Debe proporcionar un ID de método de pago válido.");
        if (venta.getMetodoEnvioModel()==null || venta.getMetodoEnvioModel().getId()==null)
            throw new RecursoNoEncontradoException("Debe proporcionar un ID de método de envío válido.");
        if (venta.getEstado()==null || venta.getEstado().getId()==null)
            throw new RecursoNoEncontradoException("Debe proporcionar un ID de estado válido.");

        venta.setUsuarioModel(obtenerUsuario(venta.getUsuarioModel().getId()));
        venta.setMetodoPagoModel(obtenerMetodoPago(venta.getMetodoPagoModel().getId()));
        venta.setMetodoEnvioModel(obtenerMetodoEnvio(venta.getMetodoEnvioModel().getId()));
        venta.setEstado(obtenerEstado(venta.getEstado().getId()));

        if (venta.getFechaCompra()==null) venta.setFechaCompra(LocalDate.now());
        if (venta.getHoraCompra()==null)  venta.setHoraCompra(LocalTime.now());

        return ventaRepository.save(venta);
    }

    public void deleteById(Long id) {
        if (!ventaRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("Venta no encontrada con ID: " + id);
        }
        ventaRepository.deleteById(id);
    }

    public List<VentaModel> findByFechaCompra(LocalDate fechaCompra) { 
        return ventaRepository.findByFechaCompra(fechaCompra); 
    }

    public List<VentaModel> findByHoraCompra(LocalTime horaCompra) { 
        return ventaRepository.findByHoraCompra(horaCompra); 
    }

    public List<VentaModel> findByUsuarioYMetodoPago(Long usuarioId, Long metodoPagoId) {
        return ventaRepository.findByUsuarioModel_IdAndMetodoPagoModel_Id(usuarioId, metodoPagoId);
    }

    public VentaModel update(Long id, VentaModel d) {
        var v = findById(id);
        v.setFechaCompra(d.getFechaCompra());
        v.setHoraCompra(d.getHoraCompra());

        if (d.getUsuarioModel()!=null && d.getUsuarioModel().getId()!=null)
            v.setUsuarioModel(obtenerUsuario(d.getUsuarioModel().getId()));

        if (d.getMetodoPagoModel()!=null && d.getMetodoPagoModel().getId()!=null)
            v.setMetodoPagoModel(obtenerMetodoPago(d.getMetodoPagoModel().getId()));

        if (d.getMetodoEnvioModel()!=null && d.getMetodoEnvioModel().getId()!=null)
            v.setMetodoEnvioModel(obtenerMetodoEnvio(d.getMetodoEnvioModel().getId()));

        if (d.getEstado()!=null && d.getEstado().getId()!=null)
            v.setEstado(obtenerEstado(d.getEstado().getId()));

        return ventaRepository.save(v);
    }

    public VentaModel patch(Long id, VentaModel d) {
        var v = findById(id);

        if (d.getFechaCompra()!=null) v.setFechaCompra(d.getFechaCompra());
        if (d.getHoraCompra()!=null)  v.setHoraCompra(d.getHoraCompra());

        if (d.getUsuarioModel()!=null && d.getUsuarioModel().getId()!=null)
            v.setUsuarioModel(obtenerUsuario(d.getUsuarioModel().getId()));

        if (d.getMetodoPagoModel()!=null && d.getMetodoPagoModel().getId()!=null)
            v.setMetodoPagoModel(obtenerMetodoPago(d.getMetodoPagoModel().getId()));

        if (d.getMetodoEnvioModel()!=null && d.getMetodoEnvioModel().getId()!=null)
            v.setMetodoEnvioModel(obtenerMetodoEnvio(d.getMetodoEnvioModel().getId()));

        if (d.getEstado()!=null && d.getEstado().getId()!=null)
            v.setEstado(obtenerEstado(d.getEstado().getId()));

        return ventaRepository.save(v);
    }


    public List<Map<String, Object>> obtenerVentasConDatos() {
        List<Object[]> res = ventaRepository.obtenerVentasResumen();
        List<Map<String, Object>> out = new ArrayList<>();

        for (Object[] r : res) {
            Map<String,Object> m = new HashMap<>();
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

    private UsuarioModel obtenerUsuario(Long id) {
        return usuarioRepository.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado con ID: " + id));
    }

    private MetodoPagoModel obtenerMetodoPago(Long id) {
        return metodoPagoRepository.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException("Método de pago no encontrado con ID: " + id));
    }

    private MetodoEnvioModel obtenerMetodoEnvio(Long id) {
        return metodoEnvioRepository.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException("Método de envío no encontrado con ID: " + id));
    }

    private EstadoModel obtenerEstado(Long id) {
        return estadoRepository.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException("Estado no encontrado con ID: " + id));
    }
}