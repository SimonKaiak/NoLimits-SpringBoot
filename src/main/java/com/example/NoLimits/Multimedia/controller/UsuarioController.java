package com.example.NoLimits.Multimedia.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin; // NUEVO: para permitir peticiones desde React.

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.model.UsuarioModel;
import com.example.NoLimits.Multimedia.service.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;

/**
 * Controlador REST para operaciones relacionadas con usuarios.
 * 
 * Incluye:
 * - CRUD de usuarios.
 * - Búsquedas por nombre/correo.
 * - Consultar compras asociadas.
 * - Endpoints de perfil basados en sesión (/me).
 */
@CrossOrigin(
    origins = {
        "http://localhost:5173",
    },
    allowCredentials = "true"
)
@RestController
@RequestMapping("/api/v1/usuarios")
@Tag(name = "Usuarios-Controller", description = "Operaciones relacionadas con los usuarios.")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // Listar todos los usuarios
    @GetMapping
    @Operation(summary = "Listar todos los usuarios", description = "Obtiene una lista de todos los usuarios registrados")
    public ResponseEntity<List<UsuarioModel>> listarUsuarios() {
        List<UsuarioModel> usuarios = usuarioService.findAll();
        return usuarios.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(usuarios);
    }

    // Buscar usuario por ID
    @GetMapping("/{id}")
    @Operation(summary = "Buscar usuario por ID", description = "Obtiene un usuario específico por su ID.")
    public ResponseEntity<UsuarioModel> buscarUsuarioPorId(@PathVariable long id) {
        try {
            return ResponseEntity.ok(usuarioService.findById(id));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // 🔹 NUEVO: Listar compras del usuario (ventas asociadas)
    @GetMapping("/{id}/compras")
    @Operation(
        summary = "Listar compras del usuario",
        description = "Obtiene el listado de compras (ventas) asociadas a un usuario."
    )
    public ResponseEntity<?> detalleComprasUsuario(@PathVariable long id) {

        Map<String, Object> data = usuarioService.obtenerDetalleUsuario(id);

        return ResponseEntity.ok(data);
    }

    // Crear usuario
    @PostMapping
    @Operation(
        summary = "Crear usuario",
        description = "Registra un nuevo usuario.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = UsuarioModel.class),
                examples = @ExampleObject(
                    name = "Nuevo usuario",
                    value = """
                    {
                      "nombre": "Lucas",
                      "apellidos": "Fernández",
                      "correo": "lucas@example.com",
                      "telefono": 912345678,
                      "password": "clave123"
                    }
                    """
                )
            )
        ),
        responses = {
            @ApiResponse(responseCode = "201", description = "Usuario creado",
                content = @Content(schema = @Schema(implementation = UsuarioModel.class))),
            @ApiResponse(responseCode = "409", description = "Correo ya registrado"),
            @ApiResponse(responseCode = "400", description = "Error de validación")
        }
    )
    public ResponseEntity<UsuarioModel> crearUsuario(@RequestBody UsuarioModel usuario) {
        UsuarioModel nuevoUsuario = usuarioService.save(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuario);
    }

    // Actualizar usuario completo (PUT)
    @PutMapping("/{id}")
    @Operation(
        summary = "Actualizar usuario",
        description = "Reemplaza completamente un usuario por su ID.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = UsuarioModel.class),
                examples = @ExampleObject(
                    name = "Usuario PUT completo",
                    value = """
                    {
                      "nombre": "Damon",
                      "apellidos": "Medhurst",
                      "correo": "damon@example.com",
                      "telefono": 912345678,
                      "password": "clave123"
                    }
                    """
                )
            )
        )
    )
    public ResponseEntity<?> actualizarUsuario(
            @PathVariable long id,
            @RequestBody @jakarta.validation.Valid UsuarioModel usuario) {
        try {
            return ResponseEntity.ok(usuarioService.update(id, usuario));
        } catch (RecursoNoEncontradoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Actualizar usuario parcialmente (PATCH)
    @PatchMapping("/{id}")
    @Operation(
        summary = "Editar parcialmente un usuario",
        description = "Actualiza algunos campos de un usuario existente.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = UsuarioModel.class),
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
            @ApiResponse(responseCode = "200", description = "Usuario actualizado",
                content = @Content(schema = @Schema(implementation = UsuarioModel.class))),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
            @ApiResponse(responseCode = "409", description = "Correo ya registrado"),
            @ApiResponse(responseCode = "400", description = "Error de validación")
        }
    )
    public ResponseEntity<UsuarioModel> editarUsuarioParcial(
            @PathVariable long id,
            @RequestBody UsuarioModel usuario) {
        UsuarioModel usuarioActualizado = usuarioService.patch(id, usuario);
        return usuarioActualizado == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(usuarioActualizado);
    }

    // Eliminar usuario por ID
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar usuario", description = "Elimina un usuario por su ID.")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable long id) {
        usuarioService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Buscar usuarios por nombre
    @GetMapping("/nombre/{nombre}")
    @Operation(summary = "Buscar usuarios por nombre", description = "Obtiene una lista de usuarios que coinciden con el nombre.")
    public ResponseEntity<List<UsuarioModel>> buscarUsuarioPorNombre(@PathVariable String nombre) {
        List<UsuarioModel> usuarios = usuarioService.findByNombre(nombre);
        return usuarios.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(usuarios);
    }

    // Buscar usuario único por correo
    @GetMapping("/correo/{correo}")
    @Operation(summary = "Buscar usuario por correo", description = "Obtiene un usuario que coincide con el correo.")
    public ResponseEntity<UsuarioModel> buscarUsuarioPorCorreo(@PathVariable String correo) {
        try {
            return ResponseEntity.ok(usuarioService.findByCorreo(correo));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Perfil del usuario actualmente autenticado.
     * Usa la sesión HTTP (atributo "usuarioId") para saber quién es "me".
     */
    @GetMapping("/me")
    @Operation(
        summary = "Obtener mi perfil",
        description = "Devuelve los datos del usuario asociado a la sesión actual."
    )
    public ResponseEntity<UsuarioModel> obtenerMiPerfil(HttpSession session) {
        Long usuarioId = (Long) session.getAttribute("usuarioId");

        if (usuarioId == null) {
            // No hay usuario asociado a la sesión
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        UsuarioModel usuario = usuarioService.findById(usuarioId);
        return ResponseEntity.ok(usuario);
    }

    /**
     * Actualizar parcialmente el usuario de la sesión actual.
     * Similar a PATCH /{id}, pero usa el ID almacenado en sesión.
     */
    @PatchMapping("/me")
    @Operation(
        summary = "Actualizar mi perfil",
        description = "Actualiza algunos campos del usuario asociado a la sesión actual."
    )
    public ResponseEntity<UsuarioModel> actualizarMiPerfilSesion(
            @RequestBody UsuarioModel cambios,
            HttpSession session) {

        Long usuarioId = (Long) session.getAttribute("usuarioId");

        if (usuarioId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        UsuarioModel actualizado = usuarioService.patch(usuarioId, cambios);
        return ResponseEntity.ok(actualizado);
    }
}