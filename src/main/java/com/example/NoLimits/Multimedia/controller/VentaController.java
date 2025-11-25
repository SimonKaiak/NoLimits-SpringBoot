// Ruta: src/main/java/com/example/NoLimits/Multimedia/controller/VentaController.java
package com.example.NoLimits.Multimedia.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.RestController;

import com.example.NoLimits.Multimedia.dto.VentaRequest;
import com.example.NoLimits.Multimedia.model.VentaModel;
import com.example.NoLimits.Multimedia.service.VentaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

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
            array = @ArraySchema(schema = @Schema(implementation = VentaModel.class)))),
        @ApiResponse(responseCode = "204", description = "Sin contenido")
    })
    public ResponseEntity<List<VentaModel>> listarVentas() {
        List<VentaModel> ventas = ventaService.findAll();
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
            content = @Content(schema = @Schema(implementation = VentaModel.class))),
        @ApiResponse(responseCode = "404", description = "No encontrada")
    })
    public ResponseEntity<VentaModel> buscarVentaPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ventaService.findById(id));
    }

    /*
     REGISTRAR VENTA REAL

     Este endpoint se usa cuando el usuario finaliza la compra desde el frontend.
     - Recibe un VentaRequest con los datos de la venta y sus detalles (productos del carrito).
     - Recupera el usuario autenticado desde la sesión (usuarioId).
     - Llama al servicio para crear la venta completa en la base de datos.

     Si el usuario no está autenticado (no hay usuarioId en sesión),
     se lanza un error 401 (no autorizado).
    */
    @PostMapping("/registrar")
    public ResponseEntity<VentaModel> registrarVenta(
            @RequestBody VentaRequest request,
            jakarta.servlet.http.HttpSession session) {

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
        return ResponseEntity.ok(
            ventaService.crearVentaDesdeRequest(request, usuarioId)
        );
    }

    /*
     Actualiza una venta usando PUT.

     Se espera que el cuerpo contenga todos los datos necesarios de la venta
     y se reemplace el registro completo en la base de datos.
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
                      "usuarioModel": { "id": 1 },
                      "metodoPagoModel": { "id": 2 },
                      "metodoEnvioModel": { "id": 1 },
                      "estado": { "id": 2 }
                    }
                    """
                )
            )
        )
    )
    public ResponseEntity<VentaModel> actualizarVenta(
            @PathVariable Long id,
            @Valid @RequestBody VentaModel venta) {

        return ResponseEntity.ok(ventaService.update(id, venta));
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
                      "estado": { "id": 3 }
                    }
                    """
                )
            )
        )
    )
    public ResponseEntity<VentaModel> editarVenta(
            @PathVariable Long id,
            @RequestBody VentaModel venta) {

        return ResponseEntity.ok(ventaService.patch(id, venta));
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
    public ResponseEntity<List<VentaModel>> buscarVentasPorMetodoPago(@PathVariable Long metodoPagoId) {
        List<VentaModel> ventas = ventaService.findByMetodoPago(metodoPagoId);
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