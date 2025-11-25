// Ruta: src/main/java/com/example/NoLimits/Multimedia/dto/VentaRequest.java
package com.example.NoLimits.Multimedia.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/*
 Esta clase es un DTO que se utiliza para recibir desde el frontend
 toda la información necesaria para registrar una venta completa.

 Su objetivo es agrupar los datos principales de la venta junto con
 la lista de productos que el usuario está comprando.

 No representa una tabla en la base de datos, solo sirve para transportar
 la información de forma ordenada entre el frontend y el backend.
*/
@Data
@Schema(description = "DTO para registrar una venta con sus detalles desde el frontend.")
public class VentaRequest {

    /*
     ID del usuario que está realizando la compra.
     Este valor permite asociar la venta con la persona correcta.
    */
    @Schema(
        description = "ID del usuario que realiza la compra",
        example = "1",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Long usuarioId;

    /*
     ID del método de pago seleccionado por el usuario.
     Por ejemplo: tarjeta, onepay u otro medio disponible.
    */
    @Schema(
        description = "ID del método de pago utilizado",
        example = "1",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Long metodoPagoId;

    /*
     ID del método de envío elegido para la entrega del pedido.
     Puede ser retiro en tienda o despacho a domicilio.
    */
    @Schema(
        description = "ID del método de envío",
        example = "1",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Long metodoEnvioId;

    /*
     ID del estado inicial de la venta.
     Normalmente se usa un estado como PENDIENTE al momento de crearla.
    */
    @Schema(
        description = "ID del estado inicial de la venta",
        example = "1",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Long estadoId;

    /*
     Lista de productos que el usuario está comprando.
     Cada elemento representa un detalle de la venta con su producto,
     cantidad y precio unitario.
    */
    @Schema(
        description = "Lista de detalles (productos del carrito)",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private List<DetalleVentaRequest> detalles;
}