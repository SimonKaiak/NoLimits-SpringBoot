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

        @GetMapping("/{id}")
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
                                                description = "Incluye clasificación y relaciones N:M con catálogos.",
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
                                                        "desarrolladoresIds": [4]
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
                                                        "portadaSaga": "/assets/img/sagas/spidermanSaga.webp"
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
                                                        "estadoId": 1
                                                        }
                                                        """
                                        )
                                }
                        )
                )
        )
        @ApiResponses(value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Producto actualizado exitosamente.",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = ProductoResponseDTO.class)
                        )
                ),
                @ApiResponse(responseCode = "404", description = "Producto no encontrado.")
        })
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
                                                        "portadaSaga": "/assets/img/sagas/lotrSaga.webp"
                                                        }
                                                        """
                                        )
                                }
                        )
                )
        )
        @ApiResponses(value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Producto actualizado parcialmente.",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = ProductoResponseDTO.class)
                        )
                ),
                @ApiResponse(responseCode = "404", description = "Producto no encontrado.")
        })
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
        @Operation(
                summary = "Buscar productos por nombre exacto.",
                description = "Obtiene una lista de productos que coincidan exactamente con el nombre."
        )
        @ApiResponses(value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Productos encontrados exitosamente.",
                        content = @Content(
                                mediaType = "application/json",
                                array = @ArraySchema(schema = @Schema(implementation = ProductoResponseDTO.class))
                        )
                ),
                @ApiResponse(responseCode = "204", description = "No se encontraron productos con ese nombre.")
        })
        public ResponseEntity<List<ProductoResponseDTO>> buscarPorNombre(@PathVariable String nombre) {
        List<ProductoResponseDTO> productos = productoService.findByNombre(nombre);
        if (productos.isEmpty()) {
                return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(productos);
        }

        @GetMapping("/nombre/contiene/{nombre}")
        @Operation(
                summary = "Buscar productos por coincidencia en el nombre.",
                description = "Obtiene productos cuyo nombre contenga el texto indicado (búsqueda case-insensitive)."
        )
        @ApiResponses(value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Productos encontrados exitosamente.",
                        content = @Content(
                                mediaType = "application/json",
                                array = @ArraySchema(schema = @Schema(implementation = ProductoResponseDTO.class))
                        )
                ),
                @ApiResponse(responseCode = "204", description = "No se encontraron productos para ese criterio.")
        })
        public ResponseEntity<List<ProductoResponseDTO>> buscarPorNombreContiene(@PathVariable String nombre) {
        List<ProductoResponseDTO> productos = productoService.findByNombreContainingIgnoreCase(nombre);
        if (productos.isEmpty()) {
                return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(productos);
        }

        @GetMapping("/tipo/{tipoProductoId}")
        @Operation(
                summary = "Buscar productos por tipo.",
                description = "Obtiene una lista de productos que pertenezcan a un tipo específico."
        )
        @ApiResponses(value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Productos encontrados exitosamente.",
                        content = @Content(
                                mediaType = "application/json",
                                array = @ArraySchema(schema = @Schema(implementation = ProductoResponseDTO.class))
                        )
                ),
                @ApiResponse(responseCode = "204", description = "No se encontraron productos para ese tipo.")
        })
        public ResponseEntity<List<ProductoResponseDTO>> buscarPorTipo(@PathVariable Long tipoProductoId) {
        List<ProductoResponseDTO> productos = productoService.findByTipoProducto(tipoProductoId);
        if (productos.isEmpty()) {
                return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(productos);
        }

        @GetMapping("/clasificacion/{clasificacionId}")
        @Operation(
                summary = "Buscar productos por clasificación.",
                description = "Obtiene productos asociados a una clasificación específica."
        )
        @ApiResponses(value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Productos encontrados exitosamente.",
                        content = @Content(
                                mediaType = "application/json",
                                array = @ArraySchema(schema = @Schema(implementation = ProductoResponseDTO.class))
                        )
                ),
                @ApiResponse(responseCode = "204", description = "No se encontraron productos para esa clasificación.")
        })
        public ResponseEntity<List<ProductoResponseDTO>> buscarPorClasificacion(@PathVariable Long clasificacionId) {
        List<ProductoResponseDTO> productos = productoService.findByClasificacion(clasificacionId);
        if (productos.isEmpty()) {
                return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(productos);
        }

        @GetMapping("/estado/{estadoId}")
        @Operation(
                summary = "Buscar productos por estado.",
                description = "Obtiene productos filtrados por estado (ej: Activo, Descontinuado, Agotado)."
        )
        @ApiResponses(value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Productos encontrados exitosamente.",
                        content = @Content(
                                mediaType = "application/json",
                                array = @ArraySchema(schema = @Schema(implementation = ProductoResponseDTO.class))
                        )
                ),
                @ApiResponse(responseCode = "204", description = "No se encontraron productos para ese estado.")
        })
        public ResponseEntity<List<ProductoResponseDTO>> buscarPorEstado(@PathVariable Long estadoId) {
        List<ProductoResponseDTO> productos = productoService.findByEstado(estadoId);
        if (productos.isEmpty()) {
                return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(productos);
        }

        @GetMapping("/tipo/{tipoProductoId}/estado/{estadoId}")
        @Operation(
                summary = "Buscar productos por tipo y estado.",
                description = "Obtiene productos que pertenezcan a un tipo específico y se encuentren en determinado estado."
        )
        @ApiResponses(value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Productos encontrados exitosamente.",
                        content = @Content(
                                mediaType = "application/json",
                                array = @ArraySchema(schema = @Schema(implementation = ProductoResponseDTO.class))
                        )
                ),
                @ApiResponse(responseCode = "204", description = "No se encontraron productos para esos filtros.")
        })
        public ResponseEntity<List<ProductoResponseDTO>> buscarPorTipoYEstado(
                @PathVariable Long tipoProductoId,
                @PathVariable Long estadoId) {

        List<ProductoResponseDTO> productos = productoService.findByTipoProductoAndEstado(tipoProductoId, estadoId);
        if (productos.isEmpty()) {
                return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(productos);
        }

        // ========================= SAGAS =========================

        @GetMapping("/sagas")
        @Operation(
                summary = "Listar nombres de sagas.",
                description = "Devuelve una lista de nombres de sagas distintas registradas en productos (solo valores no vacíos)."
        )
        @ApiResponses(value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Listado de sagas obtenido exitosamente.",
                        content = @Content(
                                mediaType = "application/json",
                                array = @ArraySchema(schema = @Schema(implementation = String.class))
                        )
                ),
                @ApiResponse(responseCode = "204", description = "No hay sagas registradas.")
        })
        public ResponseEntity<List<String>> listarSagas() {
        List<String> sagas = productoService.obtenerSagasDistinct();
        if (sagas.isEmpty()) {
                return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(sagas);
        }

        @GetMapping("/sagas/tipo/{tipoProductoId}")
        @Operation(
                summary = "Listar nombres de sagas por tipo de producto.",
                description = "Devuelve una lista de nombres de sagas filtradas por el tipo de producto (por ejemplo, solo películas)."
        )
        @ApiResponses(value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Listado de sagas obtenido exitosamente.",
                        content = @Content(
                                mediaType = "application/json",
                                array = @ArraySchema(schema = @Schema(implementation = String.class))
                        )
                ),
                @ApiResponse(responseCode = "204", description = "No hay sagas para el tipo de producto indicado.")
        })
        public ResponseEntity<List<String>> listarSagasPorTipoProducto(@PathVariable Long tipoProductoId) {
        List<String> sagas = productoService.obtenerSagasDistinctPorTipoProducto(tipoProductoId);
        if (sagas.isEmpty()) {
                return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(sagas);
        }

        @GetMapping("/sagas/{saga}")
        @Operation(
                summary = "Buscar productos por saga.",
                description = "Obtiene productos que pertenezcan a una saga específica (búsqueda case-insensitive)."
        )
        @ApiResponses(value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Productos encontrados para la saga indicada.",
                        content = @Content(
                                mediaType = "application/json",
                                array = @ArraySchema(schema = @Schema(implementation = ProductoResponseDTO.class))
                        )
                ),
                @ApiResponse(responseCode = "204", description = "No se encontraron productos para esa saga.")
        })
        public ResponseEntity<List<ProductoResponseDTO>> buscarPorSaga(@PathVariable String saga) {
        List<ProductoResponseDTO> productos = productoService.findBySagaIgnoreCase(saga);
        if (productos.isEmpty()) {
                return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(productos);
        }

        // ========================= RESUMEN =========================

        @GetMapping("/resumen")
        @Operation(
                summary = "Obtener resumen de productos.",
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
        public ResponseEntity<List<Map<String, Object>>> obtenerResumenProductos() {
        List<Map<String, Object>> resumen = productoService.obtenerProductosConDatos();
        if (resumen.isEmpty()) {
                return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(resumen);
        }

        @GetMapping("/paginacion")
        @Operation(summary = "Listar productos con paginación real")
        public ResponseEntity<PagedResponse<ProductoResponseDTO>> listarProductosPaginado(
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "5") int size
        ) {
        PagedResponse<ProductoResponseDTO> response = productoService.findAllPaged(page, size);
        return ResponseEntity.ok(response);
        }

}