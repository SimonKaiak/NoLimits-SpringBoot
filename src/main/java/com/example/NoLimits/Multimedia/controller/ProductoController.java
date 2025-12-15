package com.example.NoLimits.Multimedia.controller;

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

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

@RestController
@RequestMapping("/api/v1/productos")
@Tag(name = "Producto-Controller", description = "Operaciones relacionadas con los productos.")
@Validated
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    // ========================= CRUD BÁSICO =========================

    @GetMapping
    @Operation(summary = "Listar todos los productos.",
            description = "Obtiene una lista de todos los productos registrados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de productos obtenida exitosamente.",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ProductoModel.class)))),
            @ApiResponse(responseCode = "204", description = "No hay productos disponibles.")
    })
    public ResponseEntity<List<ProductoModel>> listarProductos() {
        List<ProductoModel> productos = productoService.findAll();
        if (productos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar producto por ID.",
            description = "Obtiene un producto específico por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto encontrado exitosamente.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductoModel.class))),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado.")
    })
    public ResponseEntity<ProductoModel> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.findById(id));
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    @Operation(
            summary = "Crear un nuevo producto.",
            description = "Registra un nuevo producto en el sistema.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "Producto mínimo",
                                            description = "Incluye los campos requeridos y FKs válidas.",
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
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Producto creado exitosamente.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductoModel.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos para crear el producto.")
    })
    public ResponseEntity<ProductoModel> crearProducto(@Valid @RequestBody ProductoModel producto) {
        ProductoModel nuevoProducto = productoService.save(producto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoProducto);
    }

    @PutMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
    @Operation(
            summary = "Actualizar un producto.",
            description = "Actualiza un producto existente (reemplaza todos sus campos).",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "PUT seguro (sin id en body)",
                                            description = "Usa el id de la URL. Incluye todos los campos obligatorios.",
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
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto actualizado exitosamente.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductoModel.class))),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado.")
    })
    public ResponseEntity<ProductoModel> actualizarProducto(
            @PathVariable Long id,
            @Valid @RequestBody ProductoModel productoDetalles) {

        return ResponseEntity.ok(productoService.update(id, productoDetalles));
    }

    @PatchMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
    @Operation(
            summary = "Editar parcialmente un producto.",
            description = "Actualiza parcialmente un producto (solo los campos enviados).",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
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
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto actualizado parcialmente.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductoModel.class))),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado.")
    })
    public ResponseEntity<ProductoModel> editarProducto(
            @PathVariable Long id,
            @RequestBody ProductoModel productoDetalles) {

        return ResponseEntity.ok(productoService.patch(id, productoDetalles));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un producto.",
            description = "Elimina un producto por su ID. Falla si el producto tiene ventas asociadas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Producto eliminado exitosamente."),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado."),
            @ApiResponse(responseCode = "409", description = "No se puede eliminar: el producto tiene movimientos en ventas.")
    })
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        productoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // ========================= BÚSQUEDAS / FILTROS =========================

    @GetMapping("/nombre/{nombre}")
    @Operation(summary = "Buscar productos por nombre exacto.",
            description = "Obtiene una lista de productos que coincidan exactamente con el nombre.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Productos encontrados exitosamente.",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ProductoModel.class)))),
            @ApiResponse(responseCode = "204", description = "No se encontraron productos con ese nombre.")
    })
    public ResponseEntity<List<ProductoModel>> buscarPorNombre(@PathVariable String nombre) {
        List<ProductoModel> productos = productoService.findByNombre(nombre);
        if (productos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/nombre/contiene/{nombre}")
    @Operation(summary = "Buscar productos por coincidencia en el nombre.",
            description = "Obtiene productos cuyo nombre contenga el texto indicado (búsqueda case-insensitive).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Productos encontrados exitosamente.",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ProductoModel.class)))),
            @ApiResponse(responseCode = "204", description = "No se encontraron productos para ese criterio.")
    })
    public ResponseEntity<List<ProductoModel>> buscarPorNombreContiene(@PathVariable String nombre) {
        List<ProductoModel> productos = productoService.findByNombreContainingIgnoreCase(nombre);
        if (productos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/tipo/{tipoProductoId}")
    @Operation(summary = "Buscar productos por tipo.",
            description = "Obtiene una lista de productos que pertenezcan a un tipo específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Productos encontrados exitosamente.",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ProductoModel.class)))),
            @ApiResponse(responseCode = "204", description = "No se encontraron productos para ese tipo.")
    })
    public ResponseEntity<List<ProductoModel>> buscarPorTipo(@PathVariable Long tipoProductoId) {
        List<ProductoModel> productos = productoService.findByTipoProducto(tipoProductoId);
        if (productos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/clasificacion/{clasificacionId}")
    @Operation(summary = "Buscar productos por clasificación.",
            description = "Obtiene productos asociados a una clasificación específica.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Productos encontrados exitosamente.",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ProductoModel.class)))),
            @ApiResponse(responseCode = "204", description = "No se encontraron productos para esa clasificación.")
    })
    public ResponseEntity<List<ProductoModel>> buscarPorClasificacion(@PathVariable Long clasificacionId) {
        List<ProductoModel> productos = productoService.findByClasificacion(clasificacionId);
        if (productos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/estado/{estadoId}")
    @Operation(summary = "Buscar productos por estado.",
            description = "Obtiene productos filtrados por estado (ej: Activo, Descontinuado, Agotado).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Productos encontrados exitosamente.",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ProductoModel.class)))),
            @ApiResponse(responseCode = "204", description = "No se encontraron productos para ese estado.")
    })
    public ResponseEntity<List<ProductoModel>> buscarPorEstado(@PathVariable Long estadoId) {
        List<ProductoModel> productos = productoService.findByEstado(estadoId);
        if (productos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/tipo/{tipoProductoId}/estado/{estadoId}")
    @Operation(summary = "Buscar productos por tipo y estado.",
            description = "Obtiene productos que pertenezcan a un tipo específico y se encuentren en determinado estado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Productos encontrados exitosamente.",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ProductoModel.class)))),
            @ApiResponse(responseCode = "204", description = "No se encontraron productos para esos filtros.")
    })
    public ResponseEntity<List<ProductoModel>> buscarPorTipoYEstado(
            @PathVariable Long tipoProductoId,
            @PathVariable Long estadoId) {

        List<ProductoModel> productos = productoService.findByTipoProductoAndEstado(tipoProductoId, estadoId);
        if (productos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(productos);
    }

    // ========================= RESUMEN =========================

    @GetMapping("/resumen")
    @Operation(summary = "Obtener resumen de productos.",
            description = "Devuelve un resumen liviano de los productos (ID, nombre, precio, tipo y estado).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resumen obtenido exitosamente.",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "204", description = "No hay productos para mostrar en el resumen.")
    })
    public ResponseEntity<List<Map<String, Object>>> obtenerResumenProductos() {
        List<Map<String, Object>> resumen = productoService.obtenerProductosConDatos();
        if (resumen.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(resumen);
    }
}