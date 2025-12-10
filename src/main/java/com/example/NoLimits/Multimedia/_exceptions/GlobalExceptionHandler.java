package com.example.NoLimits.Multimedia._exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import java.time.OffsetDateTime;
import java.util.Map;

/**
 * Manejador global de excepciones para toda la API NoLimits.
 *
 * Esta clase centraliza el control de errores y transforma las excepciones
 * lanzadas por el sistema en respuestas HTTP estructuradas y claras.
 *
 * Evita que los errores se devuelvan como mensajes genéricos y mejora
 * la experiencia tanto para desarrolladores como para consumidores de la API.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Manejo de errores de validación provocados por anotaciones @Valid.
     * Se ejecuta cuando los DTO no cumplen las reglas de validación.
     *
     * Retorna código 400 con listado detallado de los campos inválidos.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,Object>> handleValidation(MethodArgumentNotValidException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
            "timestamp", OffsetDateTime.now().toString(),
            "status", 400,
            "error", "Error de validación",
            "messages", ex.getBindingResult().getFieldErrors()
                    .stream()
                    .map(err -> err.getField() + ": " + err.getDefaultMessage())
                    .toList()
        ));
    }

    /**
     * Maneja excepciones del tipo ResponseStatusException.
     * Respeta exactamente el código HTTP definido en la excepción.
     *
     * Útil para errores personalizados lanzados directamente desde servicios o controllers.
     */
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String,Object>> handleResponseStatus(ResponseStatusException ex) {
        var status = ex.getStatusCode();
        return ResponseEntity.status(status).body(Map.of(
            "timestamp", OffsetDateTime.now().toString(),
            "status", status.value(),
            "error", status.toString(),
            "message", ex.getReason()
        ));
    }

    /**
     * Manejo específico para recursos no encontrados.
     * Asociado a la excepción personalizada RecursoNoEncontradoException.
     *
     * Retorna código 404 cuando un recurso no existe.
     */
    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<Map<String,Object>> handleNotFound(RecursoNoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
            "timestamp", OffsetDateTime.now().toString(),
            "status", 404,
            "error", "Not Found",
            "message", ex.getMessage()
        ));
    }

    /**
     * Maneja conflictos de lógica de negocio.
     * Ejemplo: intento de registrar un correo ya existente.
     *
     * Retorna código 409 Conflict.
     */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String,Object>> handleConflict(IllegalStateException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
            "timestamp", OffsetDateTime.now().toString(),
            "status", 409,
            "error", "Conflict",
            "message", ex.getMessage()
        ));
    }

    /**
     * Maneja errores por argumentos inválidos simples.
     * Ejemplo: valores incorrectos enviados manualmente.
     *
     * Retorna código 400 Bad Request.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String,Object>> handleBadRequest(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
            "timestamp", OffsetDateTime.now().toString(),
            "status", 400,
            "error", "Bad Request",
            "message", ex.getMessage()
        ));
    }

    /**
     * Captura cualquier otra excepción no controlada.
     * Previene caídas del backend y devuelve error genérico 500.
     *
     * Importante: aquí NO se deben mapear RuntimeException a 400,
     * ya que ocultaría fallos reales del sistema.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String,Object>> handleAll(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
            "timestamp", OffsetDateTime.now().toString(),
            "status", 500,
            "error", "Internal Server Error",
            "message", ex.getMessage()
        ));
    }
}