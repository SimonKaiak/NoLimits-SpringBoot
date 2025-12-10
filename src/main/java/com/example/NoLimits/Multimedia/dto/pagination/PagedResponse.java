package com.example.NoLimits.Multimedia.dto.pagination;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase genérica para representar una respuesta paginada del backend.
 *
 * Esta clase se usa cuando queremos devolver datos que están divididos
 * por páginas (paginación). Es decir, cuando el usuario solicita:
 *
 *   GET /tipos-producto?page=1&size=5
 *
 * Entonces el backend debe responder no solo los datos, sino información
 * adicional como:
 *  - qué página se está enviando
 *  - cuántas páginas existen en total
 *  - cuántos elementos existen en total
 *
 * Esta clase NO sabe qué tipo de dato contiene la lista, porque es genérica <T>.
 * Así puede usarse para:
 *  - Tipos de producto
 *  - Usuarios
 *  - Productos
 *  - Ventas
 *  - etc.
 *
 * Cada campo se explica más abajo.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PagedResponse<T> {
    private List<T> contenido;
    private int pagina;
    private int totalPaginas;
    private long totalElementos;
}