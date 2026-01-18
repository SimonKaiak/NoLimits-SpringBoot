// Ruta: src/main/java/com/example/NoLimits/Multimedia/controller/producto/ProductoController.java
package com.example.NoLimits.Multimedia.controller.producto;

import com.example.NoLimits.Multimedia.dto.pagination.PagedResponse;
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
import org.springframework.web.bind.annotation.RequestParam;
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
    @Operation(
            summary = "Listar todos los productos.",
            description = "Obtiene una lista de todos los productos registrados."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de productos obtenida exitosamente.",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ProductoResponseDTO.class))
                    )
            ),
            @ApiResponse(responseCode = "204", description = "No hay productos disponibles.")
    })
    public ResponseEntity<List<ProductoResponseDTO>> listarProductos() {
        List<ProductoResponseDTO> productos = productoService.findAll();
        if (productos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/paginacion")
    @Operation(summary = "Listar productos con paginación real")
    public ResponseEntity<PagedResponse<ProductoResponseDTO>> listarProductosPaginado(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        // Opcional: evitar page <= 0 o size <= 0
        if (page < 1) page = 1;
        if (size < 1) size = 5;

        PagedResponse<ProductoResponseDTO> response = productoService.findAllPaged(page, size);
        return ResponseEntity.ok(response);
    }

    // ✅ FIX CLAVE: el id solo acepta números, así no choca con /sagas/... ni /saga/...
    @GetMapping("/{id:\\d+}")
    @Operation(
            summary = "Buscar producto por ID.",
            description = "Obtiene un producto específico por su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Producto encontrado exitosamente.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProductoResponseDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado.")
    })
    public ResponseEntity<ProductoResponseDTO> buscarPorId(@PathVariable Long id) {
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
                            schema = @Schema(implementation = ProductoRequestDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Producto mínimo",
                                            description = "Incluye los campos requeridos y FKs válidas.",
                                            value = """
                                                    {
                                                      "nombre": "Control Inalámbrico",
                                                      "precio": 39990,
                                                      "tipoProductoId": 1,
                                                      "clasificacionId": 2,
                                                      "estadoId": 1
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "Producto con relaciones completas",
                                            description = "Incluye clasificación, N:M y listas de imágenes.",
                                            value = """
                                                    {
                                                      "nombre": "Spider-Man 2 (2004)",
                                                      "precio": 13990,
                                                      "tipoProductoId": 1,
                                                      "clasificacionId": 2,
                                                      "estadoId": 1,
                                                      "plataformasIds": [1, 2],
                                                      "generosIds": [1, 3],
                                                      "empresasIds": [1],
                                                      "desarrolladoresIds": [4],
                                                      "imagenesRutas": [
                                                        "peliculas/spiderman/PSpiderman2.webp",
                                                        "peliculas/spiderman/PSpiderman2-alt.webp"
                                                      ]
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "Película con saga",
                                            description = "Ejemplo de película que pertenece a una saga y define una portada de saga.",
                                            value = """
                                                    {
                                                      "nombre": "Spider-Man 3 (2007)",
                                                      "precio": 14990,
                                                      "tipoProductoId": 1,
                                                      "clasificacionId": 2,
                                                      "estadoId": 1,
                                                      "saga": "Spiderman",
                                                      "portadaSaga": "sagas/SagaSpiderman.webp",
                                                      "imagenesRutas": [
                                                        "peliculas/spiderman/PSpiderman3.webp"
                                                      ]
                                                    }
                                                    """
                                    )
                            }
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Producto creado exitosamente.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProductoResponseDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Datos inválidos para crear el producto.")
    })
    public ResponseEntity<ProductoResponseDTO> crearProducto(@Valid @RequestBody ProductoRequestDTO producto) {
        ProductoResponseDTO nuevoProducto = productoService.save(producto);
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
                            schema = @Schema(implementation = ProductoRequestDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "PUT seguro (sin id en body)",
                                            description = "Usa el id de la URL. Incluye todos los campos obligatorios.",
                                            value = """
                                                    {
                                                      "nombre": "Control Inalámbrico",
                                                      "precio": 39990,
                                                      "tipoProductoId": 1,
                                                      "clasificacionId": 2,
                                                      "estadoId": 1,
                                                      "plataformasIds": [1, 2],
                                                      "generosIds": [1],
                                                      "empresasIds": [1],
                                                      "desarrolladoresIds": [3],
                                                      "imagenesRutas": [
                                                        "accesorios/controles/Control1.webp"
                                                      ]
                                                    }
                                                    """
                                    )
                            }
                    )
            )
    )
    public ResponseEntity<ProductoResponseDTO> actualizarProducto(
            @PathVariable Long id,
            @Valid @RequestBody ProductoRequestDTO productoDetalles) {

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
                            schema = @Schema(implementation = ProductoUpdateDTO.class),
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
                                                      "portadaSaga": "sagas/SagaLOTR.webp"
                                                    }
                                                    """
                                    )
                            }
                    )
            )
    )
    public ResponseEntity<ProductoResponseDTO> editarProducto(
            @PathVariable Long id,
            @RequestBody ProductoUpdateDTO productoDetalles) {

        return ResponseEntity.ok(productoService.patch(id, productoDetalles));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Eliminar un producto.",
            description = "Elimina un producto por su ID. Falla si el producto tiene ventas asociadas."
    )
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        productoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // ========================= BÚSQUEDAS / FILTROS =========================

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<List<ProductoResponseDTO>> buscarPorNombre(@PathVariable String nombre) {
        List<ProductoResponseDTO> productos = productoService.findByNombre(nombre);
        if (productos.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/nombre/contiene/{nombre}")
    public ResponseEntity<List<ProductoResponseDTO>> buscarPorNombreContiene(@PathVariable String nombre) {
        List<ProductoResponseDTO> productos = productoService.findByNombreContainingIgnoreCase(nombre);
        if (productos.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/tipo/{tipoProductoId}")
    public ResponseEntity<List<ProductoResponseDTO>> buscarPorTipo(@PathVariable Long tipoProductoId) {
        List<ProductoResponseDTO> productos = productoService.findByTipoProducto(tipoProductoId);
        if (productos.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/clasificacion/{clasificacionId}")
    public ResponseEntity<List<ProductoResponseDTO>> buscarPorClasificacion(@PathVariable Long clasificacionId) {
        List<ProductoResponseDTO> productos = productoService.findByClasificacion(clasificacionId);
        if (productos.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/estado/{estadoId}")
    public ResponseEntity<List<ProductoResponseDTO>> buscarPorEstado(@PathVariable Long estadoId) {
        List<ProductoResponseDTO> productos = productoService.findByEstado(estadoId);
        if (productos.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/tipo/{tipoProductoId}/estado/{estadoId}")
    public ResponseEntity<List<ProductoResponseDTO>> buscarPorTipoYEstado(
            @PathVariable Long tipoProductoId,
            @PathVariable Long estadoId) {

        List<ProductoResponseDTO> productos = productoService.findByTipoProductoAndEstado(tipoProductoId, estadoId);
        if (productos.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(productos);
    }

    // ========================= SAGAS =========================

    @GetMapping("/sagas")
    public ResponseEntity<List<String>> listarSagas() {
        List<String> sagas = productoService.obtenerSagasDistinct();
        if (sagas.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(sagas);
    }

    @GetMapping("/sagas/resumen")
    public ResponseEntity<List<Map<String, Object>>> listarSagasConPortada() {
        List<Map<String, Object>> sagas = productoService.obtenerSagasConPortada();
        if (sagas.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(sagas);
    }

    @GetMapping("/sagas/tipo/{tipoProductoId}")
    public ResponseEntity<List<String>> listarSagasPorTipoProducto(@PathVariable Long tipoProductoId) {
        List<String> sagas = productoService.obtenerSagasDistinctPorTipoProducto(tipoProductoId);
        if (sagas.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(sagas);
    }

    @GetMapping("/sagas/{saga}")
    public ResponseEntity<List<ProductoResponseDTO>> buscarPorSaga(@PathVariable String saga) {
        List<ProductoResponseDTO> productos = productoService.findBySagaIgnoreCase(saga);
        if (productos.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/saga/{saga}")
    public ResponseEntity<List<ProductoResponseDTO>> buscarPorSagaAlias(@PathVariable String saga) {
        List<ProductoResponseDTO> productos = productoService.findBySagaIgnoreCase(saga);
        if (productos.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(productos);
    }

    // ========================= RESUMEN =========================

    @GetMapping("/resumen")
    public ResponseEntity<List<Map<String, Object>>> obtenerResumenProductos() {
        List<Map<String, Object>> resumen = productoService.obtenerProductosConDatos();
        if (resumen.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(resumen);
    }
}