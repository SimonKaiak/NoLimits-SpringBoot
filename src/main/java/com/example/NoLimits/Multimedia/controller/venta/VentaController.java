package com.example.NoLimits.Multimedia.controller.venta;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
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

import com.example.NoLimits.Multimedia.dto.pagination.PagedResponse;
import com.example.NoLimits.Multimedia.dto.venta.request.VentaRequestDTO;
import com.example.NoLimits.Multimedia.dto.venta.response.VentaResponseDTO;
import com.example.NoLimits.Multimedia.dto.venta.update.VentaUpdateDTO;
import com.example.NoLimits.Multimedia.service.venta.VentaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

/*
 Controlador encargado de manejar todo lo relacionado con las ventas.

 Aquí se pueden:
 - Listar todas las ventas.
 - Buscar una venta por su ID.
 - Registrar una venta nueva a partir de los datos que llegan del frontend.
 - Actualizar una venta completa (PUT).
 - Actualizar solo algunos campos de una venta (PATCH).
 - Eliminar una venta.
 - Consultar ventas por método de pago.
 - Obtener un resumen de ventas con datos combinados.
*/
@RestController
@RequestMapping("/api/v1/ventas")
@Tag(name = "Venta-Controller", description = "Operaciones relacionadas con las ventas.")
@Validated
@CrossOrigin(
    origins = {
        "http://localhost:5173",
        "https://no-limits-react.vercel.app"
    },
    allowCredentials = "true"
)
public class VentaController {

    // Servicio que contiene la lógica de negocio de las ventas.
    @Autowired
    private VentaService ventaService;

    /*
     Lista todas las ventas registradas en el sistema.

     Si hay ventas, se devuelven con código 200.
     Si no hay ninguna, se responde con 204 (sin contenido).
    */
    @GetMapping
    @Operation(summary = "Listar todas las ventas", description = "Obtiene todas las ventas registradas")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "OK",
            content = @Content(mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = VentaResponseDTO.class)))),
        @ApiResponse(responseCode = "204", description = "Sin contenido")
    })
    public ResponseEntity<List<VentaResponseDTO>> listarVentas() {
        List<VentaResponseDTO> ventas = ventaService.findAll();
        return ventas.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(ventas);
    }

    /*
     Busca una venta específica usando su ID.

     Si el servicio no encuentra la venta, se encargará de lanzar la excepción correspondiente.
    */

    @GetMapping("/{id}")
    @Operation(summary = "Buscar venta por ID", description = "Obtiene una venta por su ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "OK",
            content = @Content(schema = @Schema(implementation = VentaResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "No encontrada")
    })
    public ResponseEntity<VentaResponseDTO> buscarVentaPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ventaService.findById(id));
    }

    // === PAGINADO ===
    @GetMapping("/mis-compras/paginado")
    @Operation(summary = "Listar mis compras con paginación real")
    public ResponseEntity<PagedResponse<VentaResponseDTO>> misComprasPaginado(
            HttpSession session,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size
    ) {

        Long usuarioId = (Long) session.getAttribute("usuarioId");
        if (usuarioId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        PagedResponse<VentaResponseDTO> response =
                ventaService.findMisComprasPaged(usuarioId, page, size);

        return ResponseEntity.ok(response);
    }

    /*
     REGISTRAR VENTA REAL

     Este endpoint se usa cuando el usuario finaliza la compra desde el frontend.
     - Recibe un VentaRequestDTO con los datos de la venta y sus detalles (productos del carrito).
     - Recupera el usuario autenticado desde la sesión (usuarioId).
     - Llama al servicio para crear la venta completa en la base de datos.

     Si el usuario no está autenticado (no hay usuarioId en sesión),
     se lanza un error 401 (no autorizado).
    */
    @PostMapping("/registrar")
    @Operation(summary = "Registrar una venta real desde el frontend")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Venta registrada correctamente",
            content = @Content(schema = @Schema(implementation = VentaResponseDTO.class))),
        @ApiResponse(responseCode = "401", description = "Usuario no autenticado")
    })
    public ResponseEntity<VentaResponseDTO> registrarVenta(
            @RequestBody VentaRequestDTO request,
            HttpSession session) {

        // Obtenemos el ID del usuario desde la sesión creada en el login.
        Long usuarioId = (Long) session.getAttribute("usuarioId");

        // Si no hay usuario en sesión, no se permite registrar la venta.
        if (usuarioId == null) {
            throw new org.springframework.web.server.ResponseStatusException(
                org.springframework.http.HttpStatus.UNAUTHORIZED,
                "Usuario no autenticado"
            );
        }

        // Se delega al servicio la creación de la venta a partir del DTO y el usuario autenticado.
        VentaResponseDTO creada = ventaService.crearVentaDesdeRequest(request, usuarioId);
        return ResponseEntity.ok(creada);
    }

    /*
     Actualiza una venta usando PUT.

     Se espera que el cuerpo contenga los datos necesarios de la venta
     (fecha, hora y FKs por ID) y se reemplace el registro en la base de datos.
    */
    @PutMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
    @Operation(
        summary = "Actualizar venta (PUT)",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = @Content(mediaType = "application/json",
                examples = @ExampleObject(
                    name = "PUT con FKs por id",
                    value = """
                    {
                      "fechaCompra": "2025-08-08",
                      "horaCompra": "12:00:00",
                      "metodoPagoId": 2,
                      "metodoEnvioId": 1,
                      "estadoId": 2
                    }
                    """
                )
            )
        )
    )
    public ResponseEntity<VentaResponseDTO> actualizarVenta(
            @PathVariable Long id,
            @Valid @RequestBody VentaUpdateDTO body) {

        return ResponseEntity.ok(ventaService.update(id, body));
    }

    /*
     Actualiza parcialmente una venta usando PATCH.

     Aquí no es obligatorio enviar todos los campos.
     El servicio se encarga de aplicar solo los cambios indicados en el body.
    */
    @PatchMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
    @Operation(
        summary = "Editar parcialmente una venta",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = @Content(mediaType = "application/json",
                examples = @ExampleObject(
                    name = "PATCH ejemplo",
                    value = """
                    {
                      "fechaCompra": "2025-08-09",
                      "horaCompra": "18:30:00",
                      "estadoId": 3
                    }
                    """
                )
            )
        )
    )
    public ResponseEntity<VentaResponseDTO> editarVenta(
            @PathVariable Long id,
            @RequestBody VentaUpdateDTO body) {

        return ResponseEntity.ok(ventaService.patch(id, body));
    }

    /*
     Elimina una venta por su ID.

     Si la venta no existe, el servicio es el encargado de lanzar la excepción adecuada.
    */
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar venta", description = "Elimina una venta por su ID")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Eliminada"),
        @ApiResponse(responseCode = "404", description = "No encontrada")
    })
    public ResponseEntity<Void> eliminarVenta(@PathVariable Long id) {
        ventaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /*
     Busca todas las ventas realizadas con un método de pago específico.

     Se recibe el ID del método de pago por path.
     Si no hay resultados, se devuelve 204 (sin contenido).
    */
    @GetMapping("/metodopago/{metodoPagoId}")
    @Operation(summary = "Buscar ventas por método de pago")
    public ResponseEntity<List<VentaResponseDTO>> buscarVentasPorMetodoPago(@PathVariable Long metodoPagoId) {
        List<VentaResponseDTO> ventas = ventaService.findByMetodoPago(metodoPagoId);
        return ventas.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(ventas);
    }

    /*
     Devuelve un resumen de las ventas.

     Este resumen suele incluir datos combinados, como:
     - Información del usuario.
     - Método de pago.
     - Estado.
     - Otros datos útiles para reportes.

     Si no hay ventas, se responde con 204.
    */
    @GetMapping("/resumen")
    @Operation(summary = "Resumen de ventas")
    public ResponseEntity<List<Map<String, Object>>> resumenVentas() {
        var resumen = ventaService.obtenerVentasConDatos();
        return resumen.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(resumen);
    }
}