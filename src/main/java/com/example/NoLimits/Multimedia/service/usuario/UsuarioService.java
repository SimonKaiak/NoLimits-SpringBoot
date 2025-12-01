package com.example.NoLimits.Multimedia.service.usuario;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.NoLimits.Multimedia.dto.usuario.request.UsuarioRequestDTO;
import com.example.NoLimits.Multimedia.dto.usuario.response.UsuarioResponseDTO;
import com.example.NoLimits.Multimedia.dto.usuario.update.UsuarioUpdateDTO;
import com.example.NoLimits.Multimedia.model.usuario.RolModel;
import com.example.NoLimits.Multimedia.model.usuario.UsuarioModel;
import com.example.NoLimits.Multimedia.model.venta.VentaModel;
import com.example.NoLimits.Multimedia.repository.usuario.UsuarioRepository;
import com.example.NoLimits.Multimedia.repository.venta.VentaRepository;

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
    public List<UsuarioResponseDTO> findAll() {
        List<UsuarioModel> usuarios = usuarioRepository.findAll();
        List<UsuarioResponseDTO> respuesta = new ArrayList<>();

        for (UsuarioModel u : usuarios) {
            respuesta.add(toResponseDTO(u));
        }
        return respuesta;
    }

    // Obtener un usuario por ID (404 si no existe)
    public UsuarioResponseDTO findById(long id) {
        UsuarioModel usuario = getUsuarioOrThrow(id);
        return toResponseDTO(usuario);
    }

    /*
     Guardar un nuevo usuario.

     Validaciones:
     - Contraseña máximo 10 caracteres.
     - Correo obligatorio.
     - Correo no duplicado (se normaliza en minúsculas).
    */
    public UsuarioResponseDTO save(UsuarioRequestDTO dto) {
        if (dto.getPassword() != null && dto.getPassword().length() > 10) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "La contraseña debe tener máximo 10 caracteres");
        }

        if (dto.getCorreo() == null || dto.getCorreo().trim().isEmpty()) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "El correo es obligatorio");
        }

        String correo = dto.getCorreo().trim().toLowerCase();

        if (usuarioRepository.existsByCorreo(correo)) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT, "Correo ya registrado por otro usuario");
        }

        UsuarioModel usuario = new UsuarioModel();
        usuario.setNombre(dto.getNombre());
        usuario.setApellidos(dto.getApellidos());
        usuario.setCorreo(correo);
        usuario.setTelefono(dto.getTelefono());
        usuario.setPassword(dto.getPassword());

        if (dto.getRolId() != null) {
            RolModel rol = new RolModel();
            rol.setId(dto.getRolId());
            usuario.setRol(rol);
        }

        UsuarioModel guardado = usuarioRepository.save(usuario);
        return toResponseDTO(guardado);
    }

    /*
     Eliminar un usuario por ID.

     Regla de negocio:
     - Si el usuario tiene compras asociadas, se bloquea la eliminación.
    */
    public void deleteById(Long id) {
        // Verificar existencia
        getUsuarioOrThrow(id);

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
     - "usuario": UsuarioResponseDTO
     - "compras": List<VentaModel>
     - "totalCompras": cantidad de ventas
    */
    public Map<String, Object> obtenerDetalleUsuario(long usuarioId) {
        UsuarioModel usuario = getUsuarioOrThrow(usuarioId);

        List<VentaModel> compras = ventaRepository.findByUsuarioModel_Id(usuarioId);
        long totalCompras = ventaRepository.countByUsuarioModel_Id(usuarioId);

        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("usuario", toResponseDTO(usuario));
        respuesta.put("compras", compras);
        respuesta.put("totalCompras", totalCompras);

        return respuesta;
    }

    /* ================= BÚSQUEDAS ================= */

    // Buscar por nombre (parcial, sin distinguir mayúsculas/minúsculas)
    public List<UsuarioResponseDTO> findByNombre(String nombreUsuario) {
        if (nombreUsuario == null) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "El nombre de búsqueda no puede ser nulo");
        }

        String filtro = nombreUsuario.trim();
        List<UsuarioModel> encontrados;

        if (filtro.isEmpty()) {
            // Si viene vacío, devolvemos todos
            encontrados = usuarioRepository.findAll();
        } else {
            encontrados = usuarioRepository.findByNombreContainingIgnoreCase(filtro);
        }

        List<UsuarioResponseDTO> respuesta = new ArrayList<>();
        for (UsuarioModel u : encontrados) {
            respuesta.add(toResponseDTO(u));
        }
        return respuesta;
    }

    // Buscar por correo (404 si no existe)
    public UsuarioResponseDTO findByCorreo(String correoUsuario) {
        UsuarioModel usuario = getUsuarioByCorreoOrThrow(correoUsuario);
        return toResponseDTO(usuario);
    }

    /* ================= Actualización (PUT) ================= */

    /*
     Actualizar un usuario (PUT estricto).

     Reglas:
     - Todos los campos deben venir informados: nombre, apellidos, correo, teléfono, password, rolId.
     - El correo se normaliza en minúsculas y debe seguir siendo único.
     - La contraseña máximo 10 caracteres.
    */
    public UsuarioResponseDTO update(long id, UsuarioUpdateDTO d) {
        UsuarioModel u = getUsuarioOrThrow(id);

        if (d.getNombre() == null || d.getApellidos() == null ||
            d.getCorreo() == null || d.getTelefono() == null ||
            d.getPassword() == null || d.getRolId() == null) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "PUT requiere todos los campos: nombre, apellidos, correo, telefono, password, rolId");
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

        if (d.getRolId() != null) {
            RolModel nuevoRol = new RolModel();
            nuevoRol.setId(d.getRolId());
            u.setRol(nuevoRol);
        }

        UsuarioModel guardado = usuarioRepository.save(u);
        return toResponseDTO(guardado);
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
     - rolId
    */
    public UsuarioResponseDTO patch(long id, UsuarioUpdateDTO d) {
        UsuarioModel u = getUsuarioOrThrow(id);

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

        // Rol (usando rolId)
        if (d.getRolId() != null) {
            RolModel nuevoRol = new RolModel();
            nuevoRol.setId(d.getRolId());
            u.setRol(nuevoRol);
        }

        UsuarioModel guardado = usuarioRepository.save(u);
        return toResponseDTO(guardado);
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
    public UsuarioResponseDTO login(String correo, String password) {
        UsuarioModel usuario = getUsuarioByCorreoOrThrow(correo);

        // Aquí podrías aplicar hashing en vez de comparar texto plano
        if (!usuario.getPassword().equals(password)) {
            throw new ResponseStatusException(
                HttpStatus.UNAUTHORIZED,
                "Credenciales inválidas"
            );
        }

        return toResponseDTO(usuario);
    }

    /* ================= MÉTODOS PRIVADOS ================= */

    private UsuarioModel getUsuarioOrThrow(long id) {
        return usuarioRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Usuario no encontrado con ID: " + id));
    }

    private UsuarioModel getUsuarioByCorreoOrThrow(String correoUsuario) {
        String correo = (correoUsuario == null)
                ? null
                : correoUsuario.trim().toLowerCase();

        return usuarioRepository.findByCorreo(correo)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Usuario no encontrado con correo: " + correoUsuario));
    }

    private UsuarioResponseDTO toResponseDTO(UsuarioModel u) {
        if (u == null) {
            return null;
        }

        UsuarioResponseDTO dto = new UsuarioResponseDTO();
        dto.setId(u.getId());
        dto.setNombre(u.getNombre());
        dto.setApellidos(u.getApellidos());

        if (u.getNombre() != null && u.getApellidos() != null) {
            dto.setNombreCompleto(u.getNombre() + " " + u.getApellidos());
        }

        dto.setCorreo(u.getCorreo());
        dto.setTelefono(u.getTelefono());

        if (u.getRol() != null) {
            dto.setRolId(u.getRol().getId());
            dto.setRolNombre(u.getRol().getNombre());
        }

        if (u.getDireccion() != null) {
            dto.setDireccionId(u.getDireccion().getId());

            if (u.getDireccion().getComuna() != null) {
                dto.setComunaId(u.getDireccion().getComuna().getId());
                dto.setComunaNombre(u.getDireccion().getComuna().getNombre());

                if (u.getDireccion().getComuna().getRegion() != null) {
                    dto.setRegionId(u.getDireccion().getComuna().getRegion().getId());
                    dto.setRegionNombre(u.getDireccion().getComuna().getRegion().getNombre());
                }
            }
        }

        return dto;
    }
}