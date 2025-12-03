package com.example.NoLimits.Multimedia.controllerV2.producto;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.assemblers.producto.ProductoModelAssembler;
import com.example.NoLimits.Multimedia.dto.producto.request.ProductoRequestDTO;
import com.example.NoLimits.Multimedia.dto.producto.response.ProductoResponseDTO;
import com.example.NoLimits.Multimedia.dto.producto.update.ProductoUpdateDTO;
import com.example.NoLimits.Multimedia.service.producto.ProductoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/api/v2/productos", produces = MediaTypes.HAL_JSON_VALUE)
@Tag(
        name = "Producto-Controller-V2",
        description = "Operaciones relacionadas con los productos (HATEOAS) usando DTOs."
)
@Validated
public class ProductoControllerV2 {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private ProductoModelAssembler productoAssembler;

    // ========================= CRUD BÁSICO =========================

    @GetMapping
    @Operation(
            summary = "Obtener todos los productos (HATEOAS)",
            description = "Devuelve una lista de todos los productos con enlaces HATEOAS."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Productos obtenidos exitosamente.",
                    content = @Content(
                            mediaType = "application/hal+json",
                            array = @ArraySchema(schema = @Schema(implementation = ProductoResponseDTO.class))
                    )
            ),
            @ApiResponse(responseCode = "204", description = "No hay productos registrados.")
    })
    public ResponseEntity<CollectionModel<EntityModel<ProductoResponseDTO>>> getAll() {
        List<ProductoResponseDTO> lista = productoService.findAll();

        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
            }

        List<EntityModel<ProductoResponseDTO>> productos = lista.stream()
                .map(productoAssembler::toModel)
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                CollectionModel.of(
                        productos,
                        linkTo(methodOn(ProductoControllerV2.class).getAll()).withSelfRel()
                )
        );
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Obtener un producto por ID (HATEOAS)",
            description = "Devuelve un producto específico con enlaces HATEOAS."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Producto encontrado.",
                    content = @Content(
                            mediaType = "application/hal+json",
                            schema = @Schema(implementation = ProductoResponseDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado.")
    })
    public ResponseEntity<EntityModel<ProductoResponseDTO>> getById(@PathVariable Long id) {
        try {
            ProductoResponseDTO producto = productoService.findById(id);
            return ResponseEntity.ok(productoAssembler.toModel(producto));
        } catch (RecursoNoEncontradoException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(consumes = "application/json", produces = "application/hal+json")
    @Operation(
            summary = "Crear un nuevo producto (HATEOAS)",
            description = "Crea un nuevo producto y devuelve el recurso con enlaces HATEOAS."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Producto creado correctamente.",
                    content = @Content(
                            mediaType = "application/hal+json",
                            schema = @Schema(implementation = ProductoResponseDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Datos inválidos en la solicitud.")
    })
    public ResponseEntity<EntityModel<ProductoResponseDTO>> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "Producto mínimo",
                                            description = "Incluye campos requeridos y FKs válidas.",
                                            value = """
                                                    {
                                                      "nombre": "Control Inalámbrico",
                                                      "precio": 39990,
                                                      "tipoProductoId": 1,
                                                      "estadoId": 1,
                                                      "clasificacionId": 2
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "Película con saga",
                                            description = "Ejemplo de película que pertenece a una saga y define una portada de saga.",
                                            value = """
                                                    {
                                                      "nombre": "Spider-Man 2",
                                                      "precio": 13990,
                                                      "tipoProductoId": 2,
                                                      "clasificacionId": 3,
                                                      "estadoId": 1,
                                                      "saga": "Spiderman",
                                                      "portadaSaga": "/assets/img/sagas/spidermanSaga.webp"
                                                    }
                                                    """
                                    )
                            }
                    )
            )
            @Valid @RequestBody ProductoRequestDTO dto
    ) {
        ProductoResponseDTO nuevo = productoService.save(dto);
        EntityModel<ProductoResponseDTO> entityModel = productoAssembler.toModel(nuevo);

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @PutMapping(value = "/{id}", consumes = "application/json", produces = "application/hal+json")
    @Operation(
            summary = "Actualizar un producto (PUT - HATEOAS)",
            description = "Reemplaza completamente un producto existente por uno nuevo."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Producto actualizado correctamente.",
                    content = @Content(
                            mediaType = "application/hal+json",
                            schema = @Schema(implementation = ProductoResponseDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado.")
    })
    public ResponseEntity<EntityModel<ProductoResponseDTO>> update(
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "PUT seguro (sin id en body)",
                                            description = "Usa el id de la URL. Incluye campos obligatorios.",
                                            value = """
                                                    {
                                                      "nombre": "Control Inalámbrico",
                                                      "precio": 39990,
                                                      "tipoProductoId": 1,
                                                      "clasificacionId": 2,
                                                      "estadoId": 1
                                                    }
                                                    """
                                    )
                            }
                    )
            )
            @Valid @RequestBody ProductoRequestDTO dto
    ) {
        try {
            ProductoResponseDTO actualizado = productoService.update(id, dto);
            return ResponseEntity.ok(productoAssembler.toModel(actualizado));
        } catch (RecursoNoEncontradoException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping(value = "/{id}", consumes = "application/json", produces = "application/hal+json")
    @Operation(
            summary = "Actualizar parcialmente un producto (PATCH - HATEOAS)",
            description = "Modifica campos específicos de un producto existente."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Producto actualizado correctamente.",
                    content = @Content(
                            mediaType = "application/hal+json",
                            schema = @Schema(implementation = ProductoResponseDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado.")
    })
    public ResponseEntity<EntityModel<ProductoResponseDTO>> patch(
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "PATCH ejemplo",
                                            description = "Solo los campos a modificar.",
                                            value = """
                                                    {
                                                      "precio": 34990,
                                                      "estadoId": 2
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "PATCH saga",
                                            description = "Ejemplo de actualización parcial de la saga y portada de saga.",
                                            value = """
                                                    {
                                                      "saga": "El Señor de los Anillos",
                                                      "portadaSaga": "/assets/img/sagas/lotrSaga.webp"
                                                    }
                                                    """
                                    )
                            }
                    )
            )
            @RequestBody ProductoUpdateDTO dto
    ) {
        try {
            ProductoResponseDTO actualizado = productoService.patch(id, dto);
            return ResponseEntity.ok(productoAssembler.toModel(actualizado));
        } catch (RecursoNoEncontradoException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Eliminar un producto",
            description = "Elimina un producto por su ID si existe."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Producto eliminado correctamente."),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado.")
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            productoService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (RecursoNoEncontradoException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ========================= BÚSQUEDAS / FILTROS (HATEOAS) =========================

    @GetMapping("/nombre/{nombre}")
    @Operation(
            summary = "Buscar productos por nombre exacto (HATEOAS)",
            description = "Obtiene productos que coincidan exactamente con el nombre."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Productos encontrados.",
                    content = @Content(
                            mediaType = "application/hal+json",
                            array = @ArraySchema(schema = @Schema(implementation = ProductoResponseDTO.class))
                    )
            ),
            @ApiResponse(responseCode = "204", description = "No se encontraron productos con ese nombre.")
    })
    public ResponseEntity<CollectionModel<EntityModel<ProductoResponseDTO>>> buscarPorNombre(@PathVariable String nombre) {
        List<ProductoResponseDTO> lista = productoService.findByNombre(nombre);

        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<EntityModel<ProductoResponseDTO>> productos = lista.stream()
                .map(productoAssembler::toModel)
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                CollectionModel.of(
                        productos,
                        linkTo(methodOn(ProductoControllerV2.class).buscarPorNombre(nombre)).withSelfRel()
                )
        );
    }

    @GetMapping("/nombre/contiene/{nombre}")
    @Operation(
            summary = "Buscar productos por coincidencia en el nombre (HATEOAS)",
            description = "Obtiene productos cuyo nombre contenga el texto indicado (búsqueda case-insensitive)."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Productos encontrados.",
                    content = @Content(
                            mediaType = "application/hal+json",
                            array = @ArraySchema(schema = @Schema(implementation = ProductoResponseDTO.class))
                    )
            ),
            @ApiResponse(responseCode = "204", description = "No se encontraron productos para ese criterio.")
    })
    public ResponseEntity<CollectionModel<EntityModel<ProductoResponseDTO>>> buscarPorNombreContiene(@PathVariable String nombre) {
        List<ProductoResponseDTO> lista = productoService.findByNombreContainingIgnoreCase(nombre);

        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<EntityModel<ProductoResponseDTO>> productos = lista.stream()
                .map(productoAssembler::toModel)
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                CollectionModel.of(
                        productos,
                        linkTo(methodOn(ProductoControllerV2.class).buscarPorNombreContiene(nombre)).withSelfRel()
                )
        );
    }

    @GetMapping("/tipo/{tipoProductoId}")
    @Operation(
            summary = "Buscar productos por tipo (HATEOAS)",
            description = "Obtiene productos que pertenezcan a un tipo específico."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Productos encontrados.",
                    content = @Content(
                            mediaType = "application/hal+json",
                            array = @ArraySchema(schema = @Schema(implementation = ProductoResponseDTO.class))
                    )
            ),
            @ApiResponse(responseCode = "204", description = "No se encontraron productos para ese tipo.")
    })
    public ResponseEntity<CollectionModel<EntityModel<ProductoResponseDTO>>> buscarPorTipo(@PathVariable Long tipoProductoId) {
        List<ProductoResponseDTO> lista = productoService.findByTipoProducto(tipoProductoId);

        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<EntityModel<ProductoResponseDTO>> productos = lista.stream()
                .map(productoAssembler::toModel)
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                CollectionModel.of(
                        productos,
                        linkTo(methodOn(ProductoControllerV2.class).buscarPorTipo(tipoProductoId)).withSelfRel()
                )
        );
    }

    @GetMapping("/clasificacion/{clasificacionId}")
    @Operation(
            summary = "Buscar productos por clasificación (HATEOAS)",
            description = "Obtiene productos asociados a una clasificación específica."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Productos encontrados.",
                    content = @Content(
                            mediaType = "application/hal+json",
                            array = @ArraySchema(schema = @Schema(implementation = ProductoResponseDTO.class))
                    )
            ),
            @ApiResponse(responseCode = "204", description = "No se encontraron productos para esa clasificación.")
    })
    public ResponseEntity<CollectionModel<EntityModel<ProductoResponseDTO>>> buscarPorClasificacion(@PathVariable Long clasificacionId) {
        List<ProductoResponseDTO> lista = productoService.findByClasificacion(clasificacionId);

        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<EntityModel<ProductoResponseDTO>> productos = lista.stream()
                .map(productoAssembler::toModel)
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                CollectionModel.of(
                        productos,
                        linkTo(methodOn(ProductoControllerV2.class).buscarPorClasificacion(clasificacionId)).withSelfRel()
                )
        );
    }

    @GetMapping("/estado/{estadoId}")
    @Operation(
            summary = "Buscar productos por estado (HATEOAS)",
            description = "Obtiene productos filtrados por estado (ej: Activo, Descontinuado, Agotado)."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Productos encontrados.",
                    content = @Content(
                            mediaType = "application/hal+json",
                            array = @ArraySchema(schema = @Schema(implementation = ProductoResponseDTO.class))
                    )
            ),
            @ApiResponse(responseCode = "204", description = "No se encontraron productos para ese estado.")
    })
    public ResponseEntity<CollectionModel<EntityModel<ProductoResponseDTO>>> buscarPorEstado(@PathVariable Long estadoId) {
        List<ProductoResponseDTO> lista = productoService.findByEstado(estadoId);

        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<EntityModel<ProductoResponseDTO>> productos = lista.stream()
                .map(productoAssembler::toModel)
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                CollectionModel.of(
                        productos,
                        linkTo(methodOn(ProductoControllerV2.class).buscarPorEstado(estadoId)).withSelfRel()
                )
        );
    }

    @GetMapping("/tipo/{tipoProductoId}/estado/{estadoId}")
    @Operation(
            summary = "Buscar productos por tipo y estado (HATEOAS)",
            description = "Obtiene productos que pertenezcan a un tipo específico y se encuentren en determinado estado."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Productos encontrados.",
                    content = @Content(
                            mediaType = "application/hal+json",
                            array = @ArraySchema(schema = @Schema(implementation = ProductoResponseDTO.class))
                    )
            ),
            @ApiResponse(responseCode = "204", description = "No se encontraron productos para esos filtros.")
    })
    public ResponseEntity<CollectionModel<EntityModel<ProductoResponseDTO>>> buscarPorTipoYEstado(
            @PathVariable Long tipoProductoId,
            @PathVariable Long estadoId) {

        List<ProductoResponseDTO> lista = productoService.findByTipoProductoAndEstado(tipoProductoId, estadoId);

        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<EntityModel<ProductoResponseDTO>> productos = lista.stream()
                .map(productoAssembler::toModel)
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                CollectionModel.of(
                        productos,
                        linkTo(methodOn(ProductoControllerV2.class)
                                .buscarPorTipoYEstado(tipoProductoId, estadoId)).withSelfRel()
                )
        );
    }

    // ========================= SAGAS (HATEOAS) =========================

    @GetMapping("/sagas")
    @Operation(
            summary = "Listar nombres de sagas (HATEOAS)",
            description = "Devuelve una lista de nombres de sagas distintas registradas en productos (solo valores no vacíos)."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Listado de sagas obtenido exitosamente.",
                    content = @Content(
                            mediaType = "application/hal+json",
                            array = @ArraySchema(schema = @Schema(implementation = String.class))
                    )
            ),
            @ApiResponse(responseCode = "204", description = "No hay sagas registradas.")
    })
    public ResponseEntity<CollectionModel<String>> listarSagas() {
        List<String> sagas = productoService.obtenerSagasDistinct();
        if (sagas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(
                CollectionModel.of(
                        sagas,
                        linkTo(methodOn(ProductoControllerV2.class).listarSagas()).withSelfRel()
                )
        );
    }

    @GetMapping("/sagas/tipo/{tipoProductoId}")
    @Operation(
            summary = "Listar nombres de sagas por tipo de producto (HATEOAS)",
            description = "Devuelve una lista de nombres de sagas filtradas por el tipo de producto (por ejemplo, solo películas)."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Listado de sagas obtenido exitosamente.",
                    content = @Content(
                            mediaType = "application/hal+json",
                            array = @ArraySchema(schema = @Schema(implementation = String.class))
                    )
            ),
            @ApiResponse(responseCode = "204", description = "No hay sagas para el tipo de producto indicado.")
    })
    public ResponseEntity<CollectionModel<String>> listarSagasPorTipoProducto(@PathVariable Long tipoProductoId) {
        List<String> sagas = productoService.obtenerSagasDistinctPorTipoProducto(tipoProductoId);
        if (sagas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(
                CollectionModel.of(
                        sagas,
                        linkTo(methodOn(ProductoControllerV2.class)
                                .listarSagasPorTipoProducto(tipoProductoId)).withSelfRel()
                )
        );
    }

    @GetMapping("/sagas/{saga}")
    @Operation(
            summary = "Buscar productos por saga (HATEOAS)",
            description = "Obtiene productos que pertenezcan a una saga específica (búsqueda case-insensitive)."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Productos encontrados para la saga indicada.",
                    content = @Content(
                            mediaType = "application/hal+json",
                            array = @ArraySchema(schema = @Schema(implementation = ProductoResponseDTO.class))
                    )
            ),
            @ApiResponse(responseCode = "204", description = "No se encontraron productos para esa saga.")
    })
    public ResponseEntity<CollectionModel<EntityModel<ProductoResponseDTO>>> buscarPorSaga(@PathVariable String saga) {
        List<ProductoResponseDTO> lista = productoService.findBySagaIgnoreCase(saga);

        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<EntityModel<ProductoResponseDTO>> productos = lista.stream()
                .map(productoAssembler::toModel)
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                CollectionModel.of(
                        productos,
                        linkTo(methodOn(ProductoControllerV2.class).buscarPorSaga(saga)).withSelfRel()
                )
        );
    }

    // ========================= RESUMEN =========================

    @GetMapping("/resumen")
    @Operation(
            summary = "Obtener resumen de productos (HATEOAS)",
            description = "Devuelve un resumen liviano de los productos (ID, nombre, precio, tipo, estado, saga y portada de saga)."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Resumen obtenido exitosamente.",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(responseCode = "204", description = "No hay productos para mostrar en el resumen.")
    })
    public ResponseEntity<CollectionModel<Map<String, Object>>> obtenerResumenProductos() {
        List<Map<String, Object>> resumen = productoService.obtenerProductosConDatos();

        if (resumen.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(
                CollectionModel.of(
                        resumen,
                        linkTo(methodOn(ProductoControllerV2.class).obtenerResumenProductos()).withSelfRel()
                )
        );
    }
}