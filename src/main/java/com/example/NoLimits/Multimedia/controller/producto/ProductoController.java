// ARCHIVO MODIFICADO
// Ruta: src/main/java/com/example/NoLimits/Multimedia/controller/producto/ProductoController.java
//
// CAMBIOS:
//   1. listarProductos()        → retorna List<ProductoResumenDTO> (antes ProductoResponseDTO completo)
//   2. listarProductosPaginado()→ retorna PagedResponse<ProductoResumenDTO> (antes ProductoResponseDTO)
//   3. buscarPorSaga()          → acepta ?page y ?size, retorna paginado
//   4. buscarPorSagaAlias()     → igual
//   5. buscarPorTipo()          → acepta ?page y ?size, retorna paginado
//   6. buscarPorEstado()        → acepta ?page y ?size, retorna paginado
//   7. buscarPorNombreContiene()→ acepta ?page y ?size, retorna paginado
//   8. buscarPorId()            → SIN CAMBIOS (retorna ProductoResponseDTO completo ✅)

package com.example.NoLimits.Multimedia.controller.producto;

import com.example.NoLimits.Multimedia.dto.pagination.PagedResponse;
import com.example.NoLimits.Multimedia.dto.producto.request.ProductoRequestDTO;
import com.example.NoLimits.Multimedia.dto.producto.response.ProductoResumenDTO;
import com.example.NoLimits.Multimedia.dto.producto.response.ProductoResponseDTO;
import com.example.NoLimits.Multimedia.dto.producto.update.ProductoUpdateDTO;
import com.example.NoLimits.Multimedia.service.producto.ProductoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/productos")
@Tag(name = "Producto-Controller", description = "Operaciones relacionadas con los productos.")
@Validated
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    // ========================= LISTADO GENERAL =========================

    /**
     * GET /api/v1/productos
     *
     * Lista todos los productos en formato liviano (resumen).
     * ✅ Una sola query liviana, sin relaciones pesadas.
     * Para ver el detalle completo de un producto, usar GET /productos/{id}
     */
    @GetMapping
    @Operation(
        summary = "Listar todos los productos (resumen liviano).",
        description = "Devuelve id, nombre, precio, tipo, estado, saga y portada. Sin relaciones N:M. Usar /productos/{id} para el detalle completo."
    )
    public ResponseEntity<List<ProductoResumenDTO>> listarProductos() {
        List<ProductoResumenDTO> productos = productoService.findAll();
        if (productos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(productos);
    }

    /**
     * GET /api/v1/productos/paginacion?page=1&size=20
     *
     * ✅ Usar SIEMPRE este endpoint en el frontend para listar el catálogo.
     * Solo trae UNA página a la vez. El frontend muestra la página y puede
     * pedir la siguiente con page=2, page=3, etc.
     *
     * size recomendado: 12 a 24 productos por página.
     */
    @GetMapping("/paginacion")
    @Operation(
        summary = "Listar productos paginados (resumen liviano).",
        description = "Devuelve una página de productos. Usar page=1&size=20. No acumular todas las páginas en el frontend."
    )
    public ResponseEntity<PagedResponse<ProductoResumenDTO>> listarProductosPaginado(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        if (page < 1) page = 1;
        if (size < 1 || size > 50) size = 20; // máximo 50 por página
        PagedResponse<ProductoResumenDTO> response = productoService.findAllPaged(page, size);
        return ResponseEntity.ok(response);
    }

    // ========================= DETALLE COMPLETO =========================

    /**
     * GET /api/v1/productos/{id}
     *
     * Retorna el producto COMPLETO con todas sus relaciones.
     * ✅ Úsalo SOLO cuando el usuario entra al detalle de un producto.
     * ❌ Nunca llames este endpoint en un loop o para construir un listado.
     */
    @GetMapping("/{id:\\d+}")
    @Operation(
        summary = "Buscar producto por ID (detalle completo).",
        description = "Retorna el producto con todas sus relaciones. Solo para vista de detalle."
    )
    public ResponseEntity<ProductoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.findById(id));
    }

    // ========================= CRUD =========================

    @PostMapping(consumes = "application/json", produces = "application/json")
    @Operation(summary = "Crear un nuevo producto.")
    public ResponseEntity<ProductoResponseDTO> crearProducto(@Valid @RequestBody ProductoRequestDTO producto) {
        ProductoResponseDTO nuevoProducto = productoService.save(producto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoProducto);
    }

    @PutMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
    @Operation(summary = "Actualizar un producto completo.")
    public ResponseEntity<ProductoResponseDTO> actualizarProducto(
            @PathVariable Long id,
            @Valid @RequestBody ProductoRequestDTO productoDetalles) {
        return ResponseEntity.ok(productoService.update(id, productoDetalles));
    }

    @PatchMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
    @Operation(summary = "Editar parcialmente un producto.")
    public ResponseEntity<ProductoResponseDTO> editarProducto(
            @PathVariable Long id,
            @Valid @RequestBody ProductoUpdateDTO productoDetalles) {
        return ResponseEntity.ok(productoService.patch(id, productoDetalles));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un producto por ID.")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        productoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // ========================= PRECIO STEAM =========================

    @PatchMapping("/{id}/actualizar-precio-steam")
    public ResponseEntity<ProductoResponseDTO> actualizarPrecioDesdeSteam(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.actualizarPrecioDesdeSteam(id));
    }

    // ========================= BÚSQUEDA POR NOMBRE =========================

    /**
     * GET /api/v1/productos/buscar?nombre=spider&page=1&size=20
     *
     * Búsqueda por nombre paginada. El filtrado ocurre en el backend.
     * ✅ Reemplaza buscar en el frontend con .filter() sobre todos los productos.
     */
    @GetMapping("/buscar")
    @Operation(
        summary = "Buscar productos por nombre (paginado).",
        description = "Filtra en el backend. Usar ?nombre=texto&page=1&size=20"
    )
    public ResponseEntity<PagedResponse<ProductoResumenDTO>> buscarPorNombreContiene(
            @RequestParam String nombre,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        if (page < 1) page = 1;
        if (size < 1 || size > 50) size = 20;
        PagedResponse<ProductoResumenDTO> resultado = productoService.findByNombreContainingIgnoreCase(nombre, page, size);
        if (resultado.getContenido().isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(resultado);
    }

    // ========================= FILTROS PAGINADOS =========================

    /**
     * GET /api/v1/productos/tipo/{tipoProductoId}?page=1&size=20
     *
     * ✅ Filtra por tipo en el backend, paginado.
     */
    @GetMapping("/tipo/{tipoProductoId}")
    @Operation(
        summary = "Buscar productos por tipo (paginado).",
        description = "Filtra en el backend. Usar ?page=1&size=20"
    )
    public ResponseEntity<PagedResponse<ProductoResumenDTO>> buscarPorTipo(
            @PathVariable Long tipoProductoId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        if (page < 1) page = 1;
        if (size < 1 || size > 50) size = 20;
        PagedResponse<ProductoResumenDTO> resultado = productoService.findByTipoProducto(tipoProductoId, page, size);
        if (resultado.getContenido().isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(resultado);
    }

    /**
     * GET /api/v1/productos/estado/{estadoId}?page=1&size=20
     *
     * ✅ Filtra por estado en el backend, paginado.
     */
    @GetMapping("/estado/{estadoId}")
    @Operation(
        summary = "Buscar productos por estado (paginado).",
        description = "Filtra en el backend. Usar ?page=1&size=20"
    )
    public ResponseEntity<PagedResponse<ProductoResumenDTO>> buscarPorEstado(
            @PathVariable Long estadoId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        if (page < 1) page = 1;
        if (size < 1 || size > 50) size = 20;
        PagedResponse<ProductoResumenDTO> resultado = productoService.findByEstado(estadoId, page, size);
        if (resultado.getContenido().isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(resultado);
    }

    // ========================= SAGAS =========================

    @GetMapping("/sagas")
    @Operation(summary = "Listar todas las sagas distintas.")
    public ResponseEntity<List<String>> listarSagas() {
        List<String> sagas = productoService.obtenerSagasDistinct();
        if (sagas.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(sagas);
    }

    @GetMapping("/sagas/resumen")
    @Operation(summary = "Listar sagas con su portada.")
    public ResponseEntity<List<Map<String, Object>>> listarSagasConPortada() {
        List<Map<String, Object>> sagas = productoService.obtenerSagasConPortada();
        if (sagas.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(sagas);
    }

    @GetMapping("/sagas/tipo/{tipoProductoId}")
    @Operation(summary = "Listar sagas filtradas por tipo de producto.")
    public ResponseEntity<List<String>> listarSagasPorTipoProducto(@PathVariable Long tipoProductoId) {
        List<String> sagas = productoService.obtenerSagasDistinctPorTipoProducto(tipoProductoId);
        if (sagas.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(sagas);
    }

    /**
     * GET /api/v1/productos/sagas/{saga}?page=1&size=20
     *
     * ✅ Filtra productos por saga en el backend, paginado.
     */
    @GetMapping("/sagas/{saga}")
    @Operation(
        summary = "Buscar productos por saga (paginado).",
        description = "Filtra en el backend. Usar ?page=1&size=20"
    )
    public ResponseEntity<PagedResponse<ProductoResumenDTO>> buscarPorSaga(
            @PathVariable String saga,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        if (page < 1) page = 1;
        if (size < 1 || size > 50) size = 20;
        PagedResponse<ProductoResumenDTO> resultado = productoService.findBySagaIgnoreCase(saga, page, size);
        if (resultado.getContenido().isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(resultado);
    }

    // Alias por compatibilidad con frontend que usaba /saga/{saga}
    @GetMapping("/saga/{saga}")
    @Operation(summary = "Alias de /sagas/{saga} (paginado).")
    public ResponseEntity<PagedResponse<ProductoResumenDTO>> buscarPorSagaAlias(
            @PathVariable String saga,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return buscarPorSaga(saga, page, size);
    }

    // ========================= RESUMEN ADMIN =========================

    @GetMapping("/resumen")
    @Operation(
        summary = "Resumen de productos para admin.",
        description = "Lista liviana con datos básicos. Sin paginación. Solo para admin/debugging."
    )
    public ResponseEntity<List<Map<String, Object>>> obtenerResumenProductos() {
        List<Map<String, Object>> resumen = productoService.obtenerProductosConDatos();
        if (resumen.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(resumen);
    }
}