package com.example.NoLimits.Multimedia.controllerV2.usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.assemblers.usuario.UsuarioModelAssembler;
import com.example.NoLimits.Multimedia.model.usuario.UsuarioModel;
import com.example.NoLimits.Multimedia.service.usuario.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.MediaTypes;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/v2/usuarios", produces = MediaTypes.HAL_JSON_VALUE)
@Tag(name = "Usuarios-Controller-V2", description = "Operaciones relacionadas con los usuarios (HATEOAS).")
public class UsuarioControllerV2 {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioModelAssembler assembler;

    // Obtener todos los usuarios
    @Operation(summary = "Listar todos los usuarios (HATEOAS)",
               description = "Obtiene una lista de todos los usuarios registrados con enlaces HATEOAS.")
    @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida exitosamente.",
                 content = @Content(mediaType = "application/hal+json",
                 schema = @Schema(implementation = UsuarioModel.class)))
    @ApiResponse(responseCode = "204", description = "No hay usuarios registrados.")
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<UsuarioModel>>> getAll() {
        List<UsuarioModel> usuarios = usuarioService.findAll();

        if (usuarios.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<EntityModel<UsuarioModel>> usuariosConLinks = usuarios.stream()
                .peek(u -> u.setPassword("********"))
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                CollectionModel.of(usuariosConLinks,
                        linkTo(methodOn(UsuarioControllerV2.class).getAll()).withSelfRel())
        );
    }

    // Obtener usuario por ID
    @Operation(summary = "Buscar usuario por ID (HATEOAS)",
               description = "Obtiene un usuario específico por su ID con enlaces HATEOAS.")
    @ApiResponse(responseCode = "200", description = "Usuario encontrado.",
                 content = @Content(mediaType = "application/hal+json",
                 schema = @Schema(implementation = UsuarioModel.class)))
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado.")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<UsuarioModel>> getById(@PathVariable Long id) {
        UsuarioModel usuario = usuarioService.findById(id); // lanza 404 si no existe
        usuario.setPassword("********");
        return ResponseEntity.ok(assembler.toModel(usuario));
    }

    // Crear usuario
    @PostMapping
    @Operation(
        summary = "Crear un usuario (HATEOAS)",
        description = "Crea un nuevo usuario y devuelve el recurso con enlaces HATEOAS.",
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
            @ApiResponse(responseCode = "201", description = "Usuario creado correctamente.",
                content = @Content(mediaType = "application/hal+json",
                    schema = @Schema(implementation = UsuarioModel.class))),
            @ApiResponse(responseCode = "409", description = "Correo ya registrado"),
            @ApiResponse(responseCode = "400", description = "Error de validación")
        }
    )
    public ResponseEntity<?> create(@RequestBody @jakarta.validation.Valid UsuarioModel usuario) {
        try {
            UsuarioModel nuevo = usuarioService.save(usuario);
            EntityModel<UsuarioModel> entityModel = assembler.toModel(nuevo);
            return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
        } catch (IllegalStateException e) {                 // correo duplicado
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage()); // 409
        } catch (IllegalArgumentException e) {              // validación adicional (p.ej. pass > 10)
            return ResponseEntity.badRequest().body(e.getMessage());                // 400
        }
    }

    // Actualizar usuario (PUT)
    @PutMapping("/{id}")
    @Operation(
        summary = "Actualizar un usuario (HATEOAS)",
        description = "Reemplaza completamente un usuario por su ID y devuelve el recurso con enlaces HATEOAS.",
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
    public ResponseEntity<EntityModel<UsuarioModel>> update(
            @PathVariable Long id,
            @RequestBody @jakarta.validation.Valid UsuarioModel body) {
        var actualizado = usuarioService.update(id, body); // lanza 400/404/409 si corresponde
        return ResponseEntity.ok(assembler.toModel(actualizado));
    }

    // Actualizar parcialmente (PATCH)
    @PatchMapping("/{id}")
    @Operation(
        summary = "Actualizar usuario parcialmente (HATEOAS)",
        description = "Modifica campos específicos de un usuario existente y devuelve el recurso con enlaces HATEOAS.",
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
            @ApiResponse(responseCode = "200", description = "Usuario actualizado correctamente.",
                content = @Content(mediaType = "application/hal+json",
                    schema = @Schema(implementation = UsuarioModel.class))),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
            @ApiResponse(responseCode = "409", description = "Correo ya registrado"),
            @ApiResponse(responseCode = "400", description = "Error de validación")
        }
    )
    public ResponseEntity<?> patch(@PathVariable Long id, @RequestBody UsuarioModel parcial) {
        try {
            UsuarioModel actualizado = usuarioService.patch(id, parcial);
            return ResponseEntity.ok(assembler.toModel(actualizado));
        } catch (RecursoNoEncontradoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()); // 404
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());  // 409
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());                  // 400
        }
    }

    // Eliminar usuario
    @Operation(summary = "Eliminar un usuario (HATEOAS)",
               description = "Elimina un usuario existente por su ID.")
    @ApiResponse(responseCode = "204", description = "Usuario eliminado correctamente.")
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            usuarioService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (RecursoNoEncontradoException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }
}