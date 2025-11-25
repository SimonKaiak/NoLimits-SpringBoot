package com.example.NoLimits.Multimedia.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/*
 Controlador de salud del sistema.

 Esta clase expone un endpoint muy simple que permite verificar
 si el backend está activo y respondiendo correctamente.

 Es útil para:
 - Monitoreo del servidor (Render, Vercel, etc.).
 - Pruebas rápidas en navegador o Postman.
 - Validar que Spring Boot está levantado sin errores.
*/
@RestController
public class HealthController {

    /*
     Endpoint GET /health

     Retorna un mensaje "OK" con estado HTTP 200.
     Si este endpoint responde, significa que la API está funcionando.
    */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("OK");
    }
}