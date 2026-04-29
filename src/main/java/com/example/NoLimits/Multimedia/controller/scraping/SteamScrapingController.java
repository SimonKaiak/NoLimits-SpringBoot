package com.example.NoLimits.Multimedia.controller.scraping;

import com.example.NoLimits.Multimedia.service.scraping.ScrapingClientService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Controlador REST encargado de exponer el endpoint para obtener precios
 * de videojuegos desde Steam a través del microservicio de scraping.
 * 
 * Este controlador actúa como intermediario entre el frontend (o cliente)
 * y el servicio ScrapingClientService, el cual se comunica con el backend
 * Node.js encargado del scraping.
 * 
 * Forma parte de la arquitectura de microservicios de NoLimits, permitiendo
 * centralizar el acceso a datos externos desde el backend principal.
 */
@RestController
@RequestMapping("/api/scraping/steam")
public class SteamScrapingController {

    /**
     * Servicio que realiza la comunicación con el microservicio de scraping.
     */
    private final ScrapingClientService scrapingClientService;

    /**
     * Constructor con inyección de dependencias.
     * 
     * @param scrapingClientService Servicio encargado de consumir el scraping
     */
    public SteamScrapingController(ScrapingClientService scrapingClientService) {
        this.scrapingClientService = scrapingClientService;
    }

    /**
     * Endpoint GET para obtener el precio de un videojuego desde Steam.
     *
     * Ejemplo de uso:
     * GET http://localhost:8080/api/scraping/steam?appId=730
     *
     * @param appId ID del videojuego en Steam
     * @return Un Map con los datos obtenidos desde el microservicio:
     *         - nombre
     *         - precio
     *         - precioFormato
     *         - moneda
     *         - urlPlataforma
     *         - plataforma
     *         - fechaUltimaActualizacion
     *
     * Flujo:
     * 1. Recibe la solicitud HTTP con el appId
     * 2. Llama al servicio ScrapingClientService
     * 3. El servicio consulta el microservicio Node.js
     * 4. Retorna los datos al cliente en formato JSON
     */
    @GetMapping
    public Map<String, Object> obtenerPrecioSteam(@RequestParam String appId) {

        // Llama al servicio que consume el microservicio Node.js
        return scrapingClientService.obtenerPrecioSteam(appId);
    }
}