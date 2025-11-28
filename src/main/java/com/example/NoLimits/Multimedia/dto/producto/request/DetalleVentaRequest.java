// Ruta: src/main/java/com/example/NoLimits/Multimedia/dto/DetalleVentaRequest.java
package com.example.NoLimits.Multimedia.dto.producto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/*
 Esta clase es un DTO (Data Transfer Object).
 Su función es recibir desde el frontend la información básica de cada producto
 que se está comprando, para poder registrarlo dentro de una venta.

 No representa una tabla de la base de datos, solo sirve como un contenedor
 sencillo de datos que viajan entre el frontend y el backend.
*/
@Data
@Schema(description = "Detalle de producto enviado desde el frontend para crear una venta.")
public class DetalleVentaRequest {

    /*
     ID del producto que se está vendiendo.
     Este valor permite al backend identificar exactamente qué producto
     se está incluyendo en la venta.
    */
    @Schema(
        description = "ID del producto vendido",
        example = "10",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Long productoId;

    /*
     Cantidad de unidades que el usuario compró de este producto.
     Por ejemplo, si compra 2 controles iguales, aquí llegará el valor 2.
    */
    @Schema(
        description = "Cantidad vendida",
        example = "2",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Integer cantidad;

    /*
     Precio unitario del producto al momento de la compra.
     Este valor se guarda para mantener el historial exacto del precio,
     incluso si el producto cambia de valor más adelante.
    */
    @Schema(
        description = "Precio unitario al momento de la compra",
        example = "12990",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Float precioUnitario;
}