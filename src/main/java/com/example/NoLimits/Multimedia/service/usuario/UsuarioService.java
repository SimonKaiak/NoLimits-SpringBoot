// Ruta: src/main/java/com/example/NoLimits/Multimedia/service/UsuarioService.java
package com.example.NoLimits.Multimedia.service.usuario;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.NoLimits.Multimedia.model.usuario.RolModel;
import com.example.NoLimits.Multimedia.model.usuario.UsuarioModel;
import com.example.NoLimits.Multimedia.model.venta.VentaModel;
import com.example.NoLimits.Multimedia.repository.venta.VentaRepository;
import com.example.NoLimits.Multimedia.repository.usuario.UsuarioRepository;

import jakarta.transaction.Transactional;

/*
 Servicio encargado de la lógica de usuarios.

 Desde aquí se maneja:
 - CRUD de usuarios.
 - Búsquedas por nombre/correo.
 - Validaciones de negocio (correo único, longitud de contraseña, etc.).
 - Resumen de usuarios.
 - Cálculo y detalle de compras asociadas a un usuario.
*/
@Service
@Transactional
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private VentaRepository ventaRepository;

    /* ================= CRUD BÁSICO ================= */

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

    /*
     Guardar un nuevo usuario.

     Validaciones:
     - Contraseña máximo 10 caracteres.
     - Correo obligatorio.
     - Correo no duplicado (se normaliza en minúsculas).
    */
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

    /*
     Eliminar un usuario por ID.

     Regla de negocio:
     - Si el usuario tiene compras asociadas, se bloquea la eliminación.
    */
    public void deleteById(Long id) {
        // Verificar existencia
        usuarioRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Usuario no encontrado con ID: " + id));

        // Validar que no tenga ventas
        boolean tieneVentas = !ventaRepository.findByUsuarioModel_Id(id).isEmpty();
        if (tieneVentas) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "No se puede eliminar el usuario porque tiene compras asociadas."
            );
        }

        usuarioRepository.deleteById(id);
    }

    /*
     Obtener detalle + total de compras del usuario.

     Devuelve un mapa con:
     - "usuario": UsuarioModel
     - "compras": List<VentaModel>
     - "totalCompras": cantidad de ventas
    */
    public Map<String, Object> obtenerDetalleUsuario(long usuarioId) {
        UsuarioModel usuario = findById(usuarioId);

        List<VentaModel> compras = ventaRepository.findByUsuarioModel_Id(usuarioId);
        long totalCompras = ventaRepository.countByUsuarioModel_Id(usuarioId);

        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("usuario", usuario);
        respuesta.put("compras", compras);
        respuesta.put("totalCompras", totalCompras);

        return respuesta;
    }

    /* ================= BÚSQUEDAS ================= */

    // Buscar por nombre (parcial, sin distinguir mayúsculas/minúsculas)
    public List<UsuarioModel> findByNombre(String nombreUsuario) {
        if (nombreUsuario == null) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "El nombre de búsqueda no puede ser nulo");
        }

        String filtro = nombreUsuario.trim();
        if (filtro.isEmpty()) {
            // Si viene vacío, devolvemos todos
            return usuarioRepository.findAll();
        }

        return usuarioRepository.findByNombreContainingIgnoreCase(filtro);
    }

    // Buscar por correo (404 si no existe)
    public UsuarioModel findByCorreo(String correoUsuario) {
        String correo = (correoUsuario == null)
                ? null
                : correoUsuario.trim().toLowerCase();

        return usuarioRepository.findByCorreo(correo)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Usuario no encontrado con correo: " + correoUsuario));
    }

    /* ================= Actualización (PUT) ================= */

    /*
     Actualizar un usuario (PUT estricto).

     Reglas:
     - Todos los campos deben venir informados: nombre, apellidos, correo, teléfono, password.
     - El correo se normaliza en minúsculas y debe seguir siendo único.
     - La contraseña máximo 10 caracteres.
    */
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

        // Si el correo cambia, validar que no exista en otro usuario
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

    /* ================= Actualización parcial (PATCH) ================= */

    /*
     Actualización parcial del usuario.

     Solo se modifican los campos que vienen no nulos / no vacíos:
     - nombre
     - apellidos
     - correo (validando duplicados)
     - teléfono
     - password (máx. 10 caracteres)
     - rol (usando solo el ID)
    */
    public UsuarioModel patch(long id, UsuarioModel d) {
        UsuarioModel u = findById(id);

        // Nombre
        if (d.getNombre() != null) {
            String v = d.getNombre().trim();
            if (!v.isEmpty()) {
                u.setNombre(v);
            }
        }

        // Apellidos
        if (d.getApellidos() != null) {
            String v = d.getApellidos().trim();
            if (!v.isEmpty()) {
                u.setApellidos(v);
            }
        }

        // Correo (normalizado y sin duplicar)
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

        // Teléfono
        if (d.getTelefono() != null) {
            u.setTelefono(d.getTelefono());
        }

        // Password (con máximo de caracteres)
        if (d.getPassword() != null) {
            if (d.getPassword().length() > 10) {
                throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "La contraseña debe tener máximo 10 caracteres");
            }
            u.setPassword(d.getPassword());
        }

        // Rol (solo se toma el ID y se crea una instancia ligera)
        if (d.getRol() != null && d.getRol().getId() != null) {
            RolModel nuevoRol = new RolModel();
            nuevoRol.setId(d.getRol().getId());
            u.setRol(nuevoRol);
        }

        return usuarioRepository.save(u);
    }

    /* ================= Resumen para reportes / admin ================= */

    /*
     Obtener resumen de usuarios en formato de lista de mapas.

     Cada registro incluye:
     - ID
     - Nombre
     - Apellidos
     - Correo
     - Teléfono
    */
    public List<Map<String, Object>> obtenerUsuariosConDatos() {
        List<Object[]> resultados = usuarioRepository.obtenerUsuariosResumen();
        List<Map<String, Object>> lista = new ArrayList<>();

        for (Object[] fila : resultados) {
            Map<String, Object> datos = new HashMap<>();
            datos.put("ID",        fila[0]);
            datos.put("Nombre",    fila[1]);
            datos.put("Apellidos", fila[2]);
            datos.put("Correo",    fila[3]);
            datos.put("Teléfono",  fila[4]);
            lista.add(datos);
        }
        return lista;
    }

    /* ================= Login simple (sin HttpSession) ================= */

    /*
     Login a nivel de servicio (no crea sesión, solo valida credenciales).

     Se usa:
     - findByCorreo para buscar el usuario.
     - Comparación directa de password (si luego usas hashing, se reemplaza aquí).
    */
    public UsuarioModel login(String correo, String password) {
        UsuarioModel usuario = findByCorreo(correo);

        // Aquí podrías aplicar hashing en vez de comparar texto plano
        if (!usuario.getPassword().equals(password)) {
            throw new ResponseStatusException(
                HttpStatus.UNAUTHORIZED,
                "Credenciales inválidas"
            );
        }

        return usuario;
    }
}