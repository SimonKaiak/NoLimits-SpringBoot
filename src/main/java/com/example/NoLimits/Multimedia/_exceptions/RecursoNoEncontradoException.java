package com.example.NoLimits.Multimedia._exceptions;

public class RecursoNoEncontradoException extends RuntimeException {

    private final String codigoError;

    // Constructor básico con mensaje
    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
        this.codigoError = "RECURSO_NO_ENCONTRADO"; // Un identificador para la excepción
    }

    // Constructor con causa (útil para trazar errores encadenados)
    public RecursoNoEncontradoException(String mensaje, Throwable causa) {
        super(mensaje, causa);
        this.codigoError = "RECURSO_NO_ENCONTRADO";
    }

    // Getter para acceder al código si quieres incluirlo en la respuesta JSON
    public String getCodigoError() {
        return codigoError;
    }
}