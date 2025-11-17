package com.example.NoLimits.Multimedia._exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import java.time.OffsetDateTime;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // @Valid fallido -> 400 con listado de errores
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

    // Respeta exactamente el status de ResponseStatusException (409/404/400/etc.)
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

    // 404 de tu excepción custom
    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<Map<String,Object>> handleNotFound(RecursoNoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
            "timestamp", OffsetDateTime.now().toString(),
            "status", 404,
            "error", "Not Found",
            "message", ex.getMessage()
        ));
    }

    // 409 conflictos de negocio (p.ej. correo duplicado si aún lanzas IllegalStateException)
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String,Object>> handleConflict(IllegalStateException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
            "timestamp", OffsetDateTime.now().toString(),
            "status", 409,
            "error", "Conflict",
            "message", ex.getMessage()
        ));
    }

    // 400 validaciones simples (p.ej. password > 10 si lanzas IllegalArgumentException)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String,Object>> handleBadRequest(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
            "timestamp", OffsetDateTime.now().toString(),
            "status", 400,
            "error", "Bad Request",
            "message", ex.getMessage()
        ));
    }

    // 500 resto (¡NO atrapes RuntimeException a 400!)
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