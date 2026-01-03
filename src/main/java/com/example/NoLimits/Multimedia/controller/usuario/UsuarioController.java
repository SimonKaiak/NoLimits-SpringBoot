// Ruta: backend/src/main/java/com/example/NoLimits/Multimedia/controller/usuario/UsuarioController.java
package com.example.NoLimits.Multimedia.controller.usuario;

import java.util.List;
import java.util.Map;
import org.springframework.security.core.Authentication;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.NoLimits.Multimedia.dto.pagination.PagedResponse;
import com.example.NoLimits.Multimedia.dto.usuario.request.UsuarioRequestDTO;
import com.example.NoLimits.Multimedia.dto.usuario.response.UsuarioResponseDTO;
import com.example.NoLimits.Multimedia.dto.usuario.update.UsuarioUpdateDTO;
import com.example.NoLimits.Multimedia.service.usuario.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * Controlador REST para operaciones relacionadas con usuarios.
 *
 * Aquí centralizo:
 * - CRUD de usuarios.
 * - Búsquedas por nombre y por correo.
 * - Consulta de compras asociadas a un usuario.
 * - Endpoints de perfil basados en sesión (/me).
 */
@RestController
@RequestMapping("/api/v1/usuarios")
@Tag(name = "Usuarios-Controller", description = "Operaciones relacionadas con los usuarios.")
public class UsuarioController {

    // Servicio donde está la lógica de negocio de los usuarios.
    @Autowired
    private UsuarioService usuarioService;

    /**
     * Listar todos los usuarios.
     *
     * Si hay usuarios registrados, se devuelven con código 200.
     * Si la lista está vacía, se responde con 204 (sin contenido).
     */
    @GetMapping
    @SecurityRequirement(name = "bearerAuth")
    @Operation(
        summary = "Listar todos los usuarios",
        description = "Obtiene una lista de todos los usuarios registrados."
    )
    public ResponseEntity<List<UsuarioResponseDTO>> listarUsuarios() {
        List<UsuarioResponseDTO> usuarios = usuarioService.findAll();
        return usuarios.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(usuarios);
    }

    /**
     * Listar usuarios con paginación real.
     */
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/paginado")
    @Operation(summary = "Listar usuarios con paginación real")
    public ResponseEntity<PagedResponse<UsuarioResponseDTO>> listarUsuariosPaginado(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "4") int size
    ) {
        PagedResponse<UsuarioResponseDTO> response = usuarioService.findAllPaged(page, size);
        return ResponseEntity.ok(response);
    }

    /**
     * Buscar un usuario por su ID.
     *
     * Si el usuario existe, se devuelve con código 200.
     * Si no se encuentra, el service lanza 404.
     */
    @GetMapping("/{id}")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(
        summary = "Buscar usuario por ID",
        description = "Obtiene un usuario específico por su ID."
    )
    public ResponseEntity<UsuarioResponseDTO> buscarUsuarioPorId(@PathVariable long id) {
        UsuarioResponseDTO usuario = usuarioService.findById(id);
        return ResponseEntity.ok(usuario);
    }

    /**
     * Listar compras asociadas a un usuario.
     *
     * Este endpoint devuelve un mapa con la información del usuario
     * (UsuarioResponseDTO) y el detalle de sus compras (ventas) asociadas.
     */
    @GetMapping("/{id}/compras")
    @Operation(
        summary = "Listar compras del usuario",
        description = "Obtiene el listado de compras (ventas) asociadas a un usuario."
    )
    public ResponseEntity<?> detalleComprasUsuario(@PathVariable long id) {
        Map<String, Object> data = usuarioService.obtenerDetalleUsuario(id);
        return ResponseEntity.ok(data);
    }

    /**
     * Crear un nuevo usuario.
     *
     * Recibe un UsuarioRequestDTO en el cuerpo del request y lo guarda en la base de datos.
     * Si todo sale bien, responde con 201 (creado) y el usuario resultante (UsuarioResponseDTO).
     */
    @PostMapping
    @Operation(
        summary = "Crear usuario",
        description = "Registra un nuevo usuario.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = UsuarioRequestDTO.class),
                examples = @ExampleObject(
                    name = "Nuevo usuario",
                    value = """
                    {
                      "nombre": "Lucas",
                      "apellidos": "Fernández",
                      "correo": "lucas@example.com",
                      "telefono": 912345678,
                      "password": "clave123",
                      "rolId": 1
                    }
                    """
                )
            )
        ),
        responses = {
            @ApiResponse(
                responseCode = "201",
                description = "Usuario creado",
                content = @Content(schema = @Schema(implementation = UsuarioResponseDTO.class))
            ),
            @ApiResponse(responseCode = "409", description = "Correo ya registrado"),
            @ApiResponse(responseCode = "400", description = "Error de validación")
        }
    )
    public ResponseEntity<UsuarioResponseDTO> crearUsuario(
            @Valid @RequestBody UsuarioRequestDTO usuario) {

        UsuarioResponseDTO nuevoUsuario = usuarioService.save(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuario);
    }

    /**
     * Actualizar completamente un usuario (PUT).
     *
     * Este método reemplaza los datos del usuario con el ID indicado
     * por los datos enviados en el cuerpo del request.
     */
    @PutMapping("/{id}")
    @Operation(
        summary = "Actualizar usuario",
        description = "Reemplaza completamente un usuario por su ID.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = UsuarioUpdateDTO.class),
                examples = @ExampleObject(
                    name = "Usuario PUT completo",
                    value = """
                    {
                      "nombre": "Damon",
                      "apellidos": "Medhurst",
                      "correo": "damon@example.com",
                      "telefono": 912345678,
                      "password": "clave123",
                      "rolId": 1
                    }
                    """
                )
            )
        )
    )
    public ResponseEntity<UsuarioResponseDTO> actualizarUsuario(
            @PathVariable long id,
            @RequestBody @Valid UsuarioUpdateDTO usuario) {

        UsuarioResponseDTO actualizado = usuarioService.update(id, usuario);
        return ResponseEntity.ok(actualizado);
    }

    /**
     * Actualizar parcialmente un usuario (PATCH).
     *
     * Aquí se pueden enviar solo los campos que se quieran modificar.
     * El servicio se encarga de aplicar los cambios sobre el usuario existente.
     */
    @PatchMapping("/{id}")
    @Operation(
        summary = "Editar parcialmente un usuario",
        description = "Actualiza algunos campos de un usuario existente.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = UsuarioUpdateDTO.class),
                examples = @ExampleObject(
                    name = "Usuario PATCH parcial",
                    value = """
                    {
                      "telefono": 987654321,
                      "password": "nuevaClave"
                    }
                    """
                )
            )
        ),
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Usuario actualizado",
                content = @Content(schema = @Schema(implementation = UsuarioResponseDTO.class))
            ),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
            @ApiResponse(responseCode = "409", description = "Correo ya registrado"),
            @ApiResponse(responseCode = "400", description = "Error de validación")
        }
    )
    public ResponseEntity<UsuarioResponseDTO> editarUsuarioParcial(
            @PathVariable long id,
            @RequestBody UsuarioUpdateDTO usuario) {

        UsuarioResponseDTO usuarioActualizado = usuarioService.patch(id, usuario);
        return ResponseEntity.ok(usuarioActualizado);
    }

    /**
     * Eliminar un usuario por su ID.
     *
     * Si la operación tiene éxito, responde con 204 (sin contenido).
     * Si el usuario no existe o tiene compras, el service lanza la excepción correspondiente.
     */
    @DeleteMapping("/{id}")
    @Operation(
        summary = "Eliminar usuario",
        description = "Elimina un usuario por su ID."
    )
    public ResponseEntity<Void> eliminarUsuario(@PathVariable long id) {
        usuarioService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Buscar usuarios por nombre.
     *
     * Devuelve una lista de usuarios cuyo nombre coincide con el valor enviado.
     * Si no hay coincidencias, responde con 204.
     */
    @GetMapping("/nombre/{nombre}")
    @Operation(
        summary = "Buscar usuarios por nombre",
        description = "Obtiene una lista de usuarios que coinciden con el nombre."
    )
    public ResponseEntity<List<UsuarioResponseDTO>> buscarUsuarioPorNombre(@PathVariable String nombre) {
        List<UsuarioResponseDTO> usuarios = usuarioService.findByNombre(nombre);
        return usuarios.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(usuarios);
    }

    /**
     * Buscar un usuario por correo.
     *
     * Este endpoint asume que el correo es único.
     * Si se encuentra, se devuelve el usuario con código 200.
     * Si no, el service lanza 404.
     */
    @GetMapping("/correo/{correo}")
    @Operation(
        summary = "Buscar usuario por correo",
        description = "Obtiene un usuario que coincide con el correo."
    )
    public ResponseEntity<UsuarioResponseDTO> buscarUsuarioPorCorreo(@PathVariable String correo) {
        UsuarioResponseDTO usuario = usuarioService.findByCorreo(correo);
        return ResponseEntity.ok(usuario);
    }

    /**
     * Obtener el perfil del usuario actualmente autenticado.
     *
     * Usa la sesión HTTP (atributo "usuarioId") para saber quién es "me".
     * Si no hay usuario en sesión, responde con 401 (no autorizado).
     */
    @GetMapping("/me")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<UsuarioResponseDTO> obtenerMiPerfil() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String correo = (String) auth.getPrincipal(); // lo pones tú en JwtFilter
        UsuarioResponseDTO usuario = usuarioService.findByCorreo(correo);
        return ResponseEntity.ok(usuario);
    }

    /**
     * Actualizar parcialmente el usuario de la sesión actual.
     *
     * Similar a PATCH /{id}, pero aquí el ID se saca directamente
     * de la sesión (usuario actualmente autenticado).
     */
    @PatchMapping("/me")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(
        summary = "Actualizar mi perfil",
        description = "Actualiza algunos campos del usuario autenticado por JWT."
    )
    public ResponseEntity<UsuarioResponseDTO> actualizarMiPerfil(
            @RequestBody UsuarioUpdateDTO cambios) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String correo = (String) auth.getPrincipal();
        UsuarioResponseDTO usuario = usuarioService.findByCorreo(correo);

        UsuarioResponseDTO actualizado = usuarioService.patch(usuario.getId(), cambios);
        return ResponseEntity.ok(actualizado);
    }
}