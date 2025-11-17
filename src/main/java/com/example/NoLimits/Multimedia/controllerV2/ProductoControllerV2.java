package com.example.NoLimits.Multimedia.controllerV2;

import com.example.NoLimits.Multimedia._exceptions.RecursoNoEncontradoException;
import com.example.NoLimits.Multimedia.assemblers.ProductoModelAssembler;
import com.example.NoLimits.Multimedia.model.ProductoModel;
import com.example.NoLimits.Multimedia.service.ProductoService;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/api/v2/productos", produces = MediaTypes.HAL_JSON_VALUE)
@Tag(
        name = "Producto-Controller-V2",
        description = "Operaciones relacionadas con los productos (HATEOAS)."
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
                            array = @ArraySchema(schema = @Schema(implementation = ProductoModel.class))
                    )
            ),
            @ApiResponse(responseCode = "204", description = "No hay productos registrados.")
    })
    public ResponseEntity<CollectionModel<EntityModel<ProductoModel>>> getAll() {
        List<EntityModel<ProductoModel>> productos = productoService.findAll().stream()
                .map(productoAssembler::toModel)
                .collect(Collectors.toList());

        if (productos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

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
                            schema = @Schema(implementation = ProductoModel.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado.")
    })
    public ResponseEntity<EntityModel<ProductoModel>> getById(@PathVariable Long id) {
        try {
            ProductoModel producto = productoService.findById(id);
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
                            schema = @Schema(implementation = ProductoModel.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Datos inválidos en la solicitud.")
    })
    public ResponseEntity<EntityModel<ProductoModel>> create(
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
                                                      "tipoProducto": { "id": 1 },
                                                      "estado": { "id": 1 }
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "Producto con clasificación",
                                            description = "Incluye clasificación opcional.",
                                            value = """
                                                    {
                                                      "nombre": "Spider-Man (2002)",
                                                      "precio": 12990,
                                                      "tipoProducto": { "id": 2 },
                                                      "clasificacion": { "id": 3 },
                                                      "estado": { "id": 1 }
                                                    }
                                                    """
                                    )
                            }
                    )
            )
            @Valid @RequestBody ProductoModel producto
    ) {
        ProductoModel nuevo = productoService.save(producto);
        EntityModel<ProductoModel> entityModel = productoAssembler.toModel(nuevo);

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
                            schema = @Schema(implementation = ProductoModel.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado.")
    })
    public ResponseEntity<EntityModel<ProductoModel>> update(
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
                                                      "tipoProducto": { "id": 1 },
                                                      "clasificacion": { "id": 2 },
                                                      "estado": { "id": 1 }
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "PUT con id (coincide con path)",
                                            description = "Incluye el id en el body y debe coincidir con el {id} de la URL.",
                                            value = """
                                                    {
                                                      "id": 1,
                                                      "nombre": "Control Inalámbrico",
                                                      "precio": 39990,
                                                      "tipoProducto": { "id": 1 },
                                                      "clasificacion": { "id": 2 },
                                                      "estado": { "id": 1 }
                                                    }
                                                    """
                                    )
                            }
                    )
            )
            @Valid @RequestBody ProductoModel detalles
    ) {
        try {
            ProductoModel actualizado = productoService.update(id, detalles);
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
                            schema = @Schema(implementation = ProductoModel.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado.")
    })
    public ResponseEntity<EntityModel<ProductoModel>> patch(
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
                                                      "estado": { "id": 2 }
                                                    }
                                                    """
                                    )
                            }
                    )
            )
            @RequestBody ProductoModel detalles
    ) {
        try {
            ProductoModel actualizado = productoService.patch(id, detalles);
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
                            array = @ArraySchema(schema = @Schema(implementation = ProductoModel.class))
                    )
            ),
            @ApiResponse(responseCode = "204", description = "No se encontraron productos con ese nombre.")
    })
    public ResponseEntity<CollectionModel<EntityModel<ProductoModel>>> buscarPorNombre(@PathVariable String nombre) {
        List<ProductoModel> lista = productoService.findByNombre(nombre);

        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<EntityModel<ProductoModel>> productos = lista.stream()
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
                            array = @ArraySchema(schema = @Schema(implementation = ProductoModel.class))
                    )
            ),
            @ApiResponse(responseCode = "204", description = "No se encontraron productos para ese criterio.")
    })
    public ResponseEntity<CollectionModel<EntityModel<ProductoModel>>> buscarPorNombreContiene(@PathVariable String nombre) {
        List<ProductoModel> lista = productoService.findByNombreContainingIgnoreCase(nombre);

        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<EntityModel<ProductoModel>> productos = lista.stream()
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
                            array = @ArraySchema(schema = @Schema(implementation = ProductoModel.class))
                    )
            ),
            @ApiResponse(responseCode = "204", description = "No se encontraron productos para ese tipo.")
    })
    public ResponseEntity<CollectionModel<EntityModel<ProductoModel>>> buscarPorTipo(@PathVariable Long tipoProductoId) {
        List<ProductoModel> lista = productoService.findByTipoProducto(tipoProductoId);

        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<EntityModel<ProductoModel>> productos = lista.stream()
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
                            array = @ArraySchema(schema = @Schema(implementation = ProductoModel.class))
                    )
            ),
            @ApiResponse(responseCode = "204", description = "No se encontraron productos para esa clasificación.")
    })
    public ResponseEntity<CollectionModel<EntityModel<ProductoModel>>> buscarPorClasificacion(@PathVariable Long clasificacionId) {
        List<ProductoModel> lista = productoService.findByClasificacion(clasificacionId);

        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<EntityModel<ProductoModel>> productos = lista.stream()
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
                            array = @ArraySchema(schema = @Schema(implementation = ProductoModel.class))
                    )
            ),
            @ApiResponse(responseCode = "204", description = "No se encontraron productos para ese estado.")
    })
    public ResponseEntity<CollectionModel<EntityModel<ProductoModel>>> buscarPorEstado(@PathVariable Long estadoId) {
        List<ProductoModel> lista = productoService.findByEstado(estadoId);

        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<EntityModel<ProductoModel>> productos = lista.stream()
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
                            array = @ArraySchema(schema = @Schema(implementation = ProductoModel.class))
                    )
            ),
            @ApiResponse(responseCode = "204", description = "No se encontraron productos para esos filtros.")
    })
    public ResponseEntity<CollectionModel<EntityModel<ProductoModel>>> buscarPorTipoYEstado(
            @PathVariable Long tipoProductoId,
            @PathVariable Long estadoId) {

        List<ProductoModel> lista = productoService.findByTipoProductoAndEstado(tipoProductoId, estadoId);

        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<EntityModel<ProductoModel>> productos = lista.stream()
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

    // ========================= RESUMEN =========================

    @GetMapping("/resumen")
    @Operation(
            summary = "Obtener resumen de productos (HATEOAS)",
            description = "Devuelve un resumen liviano de los productos (ID, nombre, precio, tipo y estado)."
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