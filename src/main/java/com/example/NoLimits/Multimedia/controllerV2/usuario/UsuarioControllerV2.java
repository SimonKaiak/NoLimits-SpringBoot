package com.example.NoLimits.Multimedia.controllerV2.usuario;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.NoLimits.Multimedia.assemblers.usuario.UsuarioModelAssembler;
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

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/api/v2/usuarios", produces = MediaTypes.HAL_JSON_VALUE)
@Tag(name = "Usuarios-Controller-V2", description = "Operaciones relacionadas con los usuarios (HATEOAS + DTOs).")
public class UsuarioControllerV2 {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioModelAssembler assembler;

    /* ===================== LISTAR ===================== */

    @GetMapping
    @Operation(summary = "Listar todos los usuarios (DTO + HATEOAS)")
    @ApiResponse(
        responseCode = "200",
        description = "Lista de usuarios obtenida exitosamente.",
        content = @Content(
            mediaType = "application/hal+json",
            schema = @Schema(implementation = UsuarioResponseDTO.class)
        )
    )
    @ApiResponse(responseCode = "204", description = "No hay usuarios registrados.")
    public ResponseEntity<CollectionModel<EntityModel<UsuarioResponseDTO>>> getAll() {

        List<UsuarioResponseDTO> lista = usuarioService.findAll();

        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<EntityModel<UsuarioResponseDTO>> body =
                lista.stream().map(assembler::toModel).collect(Collectors.toList());

        return ResponseEntity.ok(
                CollectionModel.of(
                        body,
                        linkTo(methodOn(UsuarioControllerV2.class).getAll()).withSelfRel()
                )
        );
    }

    /* ===================== OBTENER POR ID ===================== */

    @GetMapping("/{id}")
    @Operation(summary = "Obtener usuario por ID (DTO + HATEOAS)")
    @ApiResponse(
        responseCode = "200",
        description = "Usuario encontrado.",
        content = @Content(
            mediaType = "application/hal+json",
            schema = @Schema(implementation = UsuarioResponseDTO.class)
        )
    )
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado.")
    public ResponseEntity<EntityModel<UsuarioResponseDTO>> getById(@PathVariable Long id) {
        UsuarioResponseDTO dto = usuarioService.findById(id);
        return ResponseEntity.ok(assembler.toModel(dto));
    }

    /* ===================== CREAR ===================== */

    @PostMapping
    @Operation(
        summary = "Crear usuario (DTO + HATEOAS)",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = UsuarioRequestDTO.class),
                examples = @ExampleObject(
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
                description = "Usuario creado correctamente.",
                content = @Content(
                    mediaType = "application/hal+json",
                    schema = @Schema(implementation = UsuarioResponseDTO.class)
                )
            ),
            @ApiResponse(responseCode = "409", description = "Correo ya registrado"),
            @ApiResponse(responseCode = "400", description = "Error de validación")
        }
    )
    public ResponseEntity<EntityModel<UsuarioResponseDTO>> create(@RequestBody UsuarioRequestDTO body) {
        UsuarioResponseDTO creado = usuarioService.save(body);
        EntityModel<UsuarioResponseDTO> entity = assembler.toModel(creado);

        return ResponseEntity
                .created(entity.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entity);
    }

    /* ===================== PUT ===================== */

    @PutMapping("/{id}")
    @Operation(
        summary = "Actualizar usuario (PUT + DTO + HATEOAS)",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = @Content(schema = @Schema(implementation = UsuarioUpdateDTO.class))
        )
    )
    @ApiResponse(
        responseCode = "200",
        description = "Usuario actualizado correctamente.",
        content = @Content(
            mediaType = "application/hal+json",
            schema = @Schema(implementation = UsuarioResponseDTO.class)
        )
    )
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    @ApiResponse(responseCode = "409", description = "Correo ya registrado")
    @ApiResponse(responseCode = "400", description = "Error de validación")
    public ResponseEntity<EntityModel<UsuarioResponseDTO>> update(
            @PathVariable Long id,
            @RequestBody UsuarioUpdateDTO body) {

        UsuarioResponseDTO actualizado = usuarioService.update(id, body);
        return ResponseEntity.ok(assembler.toModel(actualizado));
    }

    /* ===================== PATCH ===================== */

    @PatchMapping("/{id}")
    @Operation(
        summary = "Actualizar usuario parcialmente (PATCH + DTO + HATEOAS)",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = @Content(schema = @Schema(implementation = UsuarioUpdateDTO.class))
        )
    )
    @ApiResponse(
        responseCode = "200",
        description = "Usuario actualizado parcialmente.",
        content = @Content(
            mediaType = "application/hal+json",
            schema = @Schema(implementation = UsuarioResponseDTO.class)
        )
    )
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    @ApiResponse(responseCode = "409", description = "Correo ya registrado")
    @ApiResponse(responseCode = "400", description = "Error de validación")
    public ResponseEntity<EntityModel<UsuarioResponseDTO>> patch(
            @PathVariable Long id,
            @RequestBody UsuarioUpdateDTO body) {

        UsuarioResponseDTO actualizado = usuarioService.patch(id, body);
        return ResponseEntity.ok(assembler.toModel(actualizado));
    }

    /* ===================== DELETE ===================== */

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un usuario (DTO + HATEOAS)")
    @ApiResponse(responseCode = "204", description = "Usuario eliminado correctamente.")
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado.")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        usuarioService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}