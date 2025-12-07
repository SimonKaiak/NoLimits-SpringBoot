package com.example.NoLimits.Multimedia.service.usuario;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.NoLimits.Multimedia.dto.pagination.PagedResponse;
import com.example.NoLimits.Multimedia.dto.ubicacion.request.DireccionRequestDTO;
import com.example.NoLimits.Multimedia.dto.usuario.request.UsuarioRequestDTO;
import com.example.NoLimits.Multimedia.dto.usuario.response.UsuarioResponseDTO;
import com.example.NoLimits.Multimedia.dto.usuario.update.UsuarioUpdateDTO;
import com.example.NoLimits.Multimedia.model.ubicacion.ComunaModel;
import com.example.NoLimits.Multimedia.model.ubicacion.DireccionModel;
import com.example.NoLimits.Multimedia.model.usuario.RolModel;
import com.example.NoLimits.Multimedia.model.usuario.UsuarioModel;
import com.example.NoLimits.Multimedia.model.venta.VentaModel;
import com.example.NoLimits.Multimedia.repository.ubicacion.ComunaRepository;
import com.example.NoLimits.Multimedia.repository.ubicacion.DireccionRepository;
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

    @Autowired
    private DireccionRepository direccionRepository;

    @Autowired
    private ComunaRepository comunaRepository;

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

    // ===================== VALIDACIONES BÁSICAS =====================
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


    // ===================== CREAR USUARIO =====================
    UsuarioModel usuario = new UsuarioModel();
    usuario.setNombre(dto.getNombre());
    usuario.setApellidos(dto.getApellidos());
    usuario.setCorreo(correo);
    usuario.setTelefono(dto.getTelefono());
    usuario.setPassword(dto.getPassword());

    // Rol
    if (dto.getRolId() != null) {
        RolModel rol = new RolModel();
        rol.setId(dto.getRolId());
        usuario.setRol(rol);
    }

    // Guardar usuario sin dirección primero (para obtener ID)
    UsuarioModel usuarioGuardado = usuarioRepository.save(usuario);


    // ===================== CREAR DIRECCIÓN =====================
    DireccionRequestDTO d = dto.getDireccion();

    if (d != null) {

        // 1. Buscar comuna
        ComunaModel comuna = comunaRepository.findById(d.getComunaId())
                .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Comuna no encontrada"));

        // 2. Crear objeto dirección
        DireccionModel direccion = new DireccionModel();
        direccion.setCalle(d.getCalle());
        direccion.setNumero(d.getNumero());
        direccion.setComplemento(d.getComplemento());
        direccion.setCodigoPostal(d.getCodigoPostal());
        direccion.setActivo(d.getActivo() == null ? true : d.getActivo());
        direccion.setComuna(comuna);

        // 3. Asociar usuario ↔ dirección
        direccion.setUsuarioModel(usuarioGuardado);

        // 4. Guardar dirección
        direccionRepository.save(direccion);

        // 5. Relacionar dirección en el modelo del usuario
        usuarioGuardado.setDireccion(direccion);
    }


    // ===================== RESPUESTA =====================
    return toResponseDTO(usuarioGuardado);
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
        if (d.getNombre() != null && !d.getNombre().trim().isEmpty()) {
            u.setNombre(d.getNombre().trim());
        }

        // Apellidos
        if (d.getApellidos() != null && !d.getApellidos().trim().isEmpty()) {
            u.setApellidos(d.getApellidos().trim());
        }

        // Correo
        if (d.getCorreo() != null && !d.getCorreo().trim().isEmpty()) {
            String nuevo = d.getCorreo().trim().toLowerCase();

            if (!nuevo.equalsIgnoreCase(u.getCorreo()) &&
                usuarioRepository.existsByCorreo(nuevo)) {

                throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Correo ya registrado por otro usuario");
            }
            u.setCorreo(nuevo);
        }

        // Teléfono
        if (d.getTelefono() != null) {
            u.setTelefono(d.getTelefono());
        }

        // Password
        if (d.getPassword() != null && !d.getPassword().isEmpty()) {
            if (d.getPassword().length() > 10) {
                throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "La contraseña debe tener máximo 10 caracteres");
            }
            u.setPassword(d.getPassword());
        }

        // Rol
        if (d.getRolId() != null) {
            RolModel nuevoRol = new RolModel();
            nuevoRol.setId(d.getRolId());
            u.setRol(nuevoRol);
        }

        // ================================================
        // ACTUALIZAR DIRECCIÓN (nuevo)
        // ================================================
        if (d.getDireccion() != null) {

            DireccionRequestDTO dir = d.getDireccion();

            // Buscar comuna
            ComunaModel comuna = comunaRepository.findById(dir.getComunaId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Comuna no encontrada"));

            DireccionModel direccion;

            // Si el usuario YA tiene dirección → se actualiza
            if (u.getDireccion() != null) {
                direccion = u.getDireccion();
            } else {
                // Si no tiene dirección → se crea una nueva
                direccion = new DireccionModel();
                direccion.setUsuarioModel(u);
            }

            direccion.setCalle(dir.getCalle());
            direccion.setNumero(dir.getNumero());
            direccion.setComplemento(dir.getComplemento());
            direccion.setCodigoPostal(dir.getCodigoPostal());
            direccion.setActivo(dir.getActivo() == null ? true : dir.getActivo());
            direccion.setComuna(comuna);

            // Guardar dirección
            direccionRepository.save(direccion);

            // Relacionar en usuario
            u.setDireccion(direccion);
        }

        // Guardar usuario
        UsuarioModel guardado = usuarioRepository.save(u);
        return toResponseDTO(guardado);
    }

    public PagedResponse<UsuarioResponseDTO> findAllPaged(int page, int size) {

    Pageable pageable = PageRequest.of(page - 1, size, Sort.by("id").ascending());

    Page<UsuarioModel> result = usuarioRepository.findAll(pageable);

    List<UsuarioResponseDTO> contenido = result.getContent()
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