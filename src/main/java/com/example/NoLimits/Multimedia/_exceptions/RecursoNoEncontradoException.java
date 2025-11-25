package com.example.NoLimits.Multimedia._exceptions;

/**
 * Excepción personalizada utilizada cuando un recurso solicitado
 * no existe en la base de datos o no puede ser encontrado.
 *
 * Esta excepción permite estandarizar los errores 404 dentro del sistema
 * y facilita el control de errores en los controllers mediante @ExceptionHandler
 * o @ControllerAdvice.
 *
 * Ejemplo de uso:
 * throw new RecursoNoEncontradoException("Producto no encontrado con ID: 5");
 */
public class RecursoNoEncontradoException extends RuntimeException {

    /**
     * Código identificador del error.
     * Puede ser utilizado para respuestas JSON estructuradas
     * o para manejo centralizado de errores.
     */
    private final String codigoError;

    /**
     * Constructor que recibe un mensaje descriptivo del error.
     *
     * @param mensaje descripción del problema ocurrido
     */
    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
        this.codigoError = "RECURSO_NO_ENCONTRADO";
    }

    /**
     * Constructor que permite encadenar excepciones.
     * Útil cuando el error proviene de otra excepción subyacente.
     *
     * @param mensaje descripción del problema
     * @param causa excepción original que provocó el fallo
     */
    public RecursoNoEncontradoException(String mensaje, Throwable causa) {
        super(mensaje, causa);
        this.codigoError = "RECURSO_NO_ENCONTRADO";
    }

    /**
     * Obtiene el código interno del error.
     * Puede incluirse en la respuesta HTTP para mayor trazabilidad.
     *
     * @return código identificador del error
     */
    public String getCodigoError() {
        return codigoError;
    }
}