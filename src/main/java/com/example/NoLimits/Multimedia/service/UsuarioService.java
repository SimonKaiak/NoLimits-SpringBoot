package com.example.NoLimits.Multimedia.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.NoLimits.Multimedia.model.UsuarioModel;
import com.example.NoLimits.Multimedia.repository.UsuarioRepository;
import com.example.NoLimits.Multimedia.repository.VentaRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private VentaRepository ventaRepository;

    // Obtener todos los usuarios
    public List<UsuarioModel> findAll() {
        return usuarioRepository.findAll();
    }

    // Obtener un usuario por ID (404 si no existe)
    public UsuarioModel findById(long id) {
        return usuarioRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Usuario no encontrado con ID: " + id));
    }

    // Guardar un nuevo usuario
    public UsuarioModel save(UsuarioModel usuario) {
        if (usuario.getPassword() != null && usuario.getPassword().length() > 10) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "La contraseña debe tener máximo 10 caracteres");
        }
        if (usuario.getCorreo() == null || usuario.getCorreo().trim().isEmpty()) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "El correo es obligatorio");
        }

        String correo = usuario.getCorreo().trim().toLowerCase();

        if (usuarioRepository.existsByCorreo(correo)) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT, "Correo ya registrado por otro usuario");
        }

        usuario.setCorreo(correo);
        return usuarioRepository.save(usuario);
    }

    // Eliminar un usuario (bloquear si tiene ventas asociadas)
    public void deleteById(Long id) {
        usuarioRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Usuario no encontrado con ID: " + id));

        if (ventaRepository.existsById(id)) {
            // ojo: existsById(id de venta) no sirve; mejor:
        }
        boolean tieneVentas = !ventaRepository.findByUsuarioModel_Id(id).isEmpty();
        if (tieneVentas) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "No se puede eliminar el usuario porque tiene ventas asociadas."
            );
        }

        usuarioRepository.deleteById(id);
    }

    // Buscar por nombre
    public List<UsuarioModel> findByNombre(String nombreUsuario) {
        return usuarioRepository.findByNombre(nombreUsuario);
    }

    // Buscar por correo (404 si no existe)
    public UsuarioModel findByCorreo(String correoUsuario) {
        String correo = (correoUsuario == null) ? null : correoUsuario.trim().toLowerCase();
        return usuarioRepository.findByCorreo(correo)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Usuario no encontrado con correo: " + correoUsuario));
    }

    // Actualizar un usuario (PUT estricto)
    public UsuarioModel update(long id, UsuarioModel d) {
        UsuarioModel u = findById(id);

        if (d.getNombre() == null || d.getApellidos() == null ||
            d.getCorreo() == null || d.getTelefono() == null ||
            d.getPassword() == null) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "PUT requiere todos los campos: nombre, apellidos, correo, telefono, password");
        }

        String nuevoCorreo = d.getCorreo().trim().toLowerCase();
        if (!nuevoCorreo.equalsIgnoreCase(u.getCorreo()) &&
            usuarioRepository.existsByCorreo(nuevoCorreo)) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT, "Correo ya registrado por otro usuario");
        }

        if (d.getPassword() != null && d.getPassword().length() > 10) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "La contraseña debe tener máximo 10 caracteres");
        }

        u.setNombre(d.getNombre());
        u.setApellidos(d.getApellidos());
        u.setCorreo(nuevoCorreo);
        u.setTelefono(d.getTelefono());
        u.setPassword(d.getPassword());

        return usuarioRepository.save(u);
    }

    // Actualización parcial (PATCH)
    public UsuarioModel patch(long id, UsuarioModel d) {
        UsuarioModel u = findById(id);

        if (d.getNombre() != null) {
            String v = d.getNombre().trim();
            if (!v.isEmpty()) u.setNombre(v);
        }

        if (d.getApellidos() != null) {
            String v = d.getApellidos().trim();
            if (!v.isEmpty()) u.setApellidos(v);
        }

        if (d.getCorreo() != null) {
            String nuevo = d.getCorreo().trim().toLowerCase();
            if (!nuevo.isEmpty() && !nuevo.equalsIgnoreCase(u.getCorreo())) {
                if (usuarioRepository.existsByCorreo(nuevo)) {
                    throw new ResponseStatusException(
                        HttpStatus.CONFLICT, "Correo ya registrado por otro usuario");
                }
                u.setCorreo(nuevo);
            }
        }

        if (d.getTelefono() != null) {
            u.setTelefono(d.getTelefono());
        }

        if (d.getPassword() != null) {
            if (d.getPassword().length() > 10) {
                throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "La contraseña debe tener máximo 10 caracteres");
            }
            u.setPassword(d.getPassword());
        }

        return usuarioRepository.save(u);
    }

    // Obtener resumen de usuarios
    public List<Map<String, Object>> obtenerUsuariosConDatos() {
        List<Object[]> resultados = usuarioRepository.obtenerUsuariosResumen();
        List<Map<String, Object>> lista = new ArrayList<>();

        for (Object[] fila : resultados) {
            Map<String, Object> datos = new HashMap<>();
            datos.put("ID",       fila[0]);
            datos.put("Nombre",   fila[1]);
            datos.put("Apellidos",fila[2]);
            datos.put("Correo",   fila[3]);
            datos.put("Teléfono", fila[4]);
            lista.add(datos);
        }
        return lista;
    }
}