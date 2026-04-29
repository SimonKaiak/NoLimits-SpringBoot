package com.example.NoLimits.Multimedia.service.scraping;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * Servicio encargado de consumir el microservicio de scraping desarrollado en Node.js.
 * 
 * Este servicio actúa como cliente HTTP que se comunica con el backend externo
 * (nolimits-scraping-service) para obtener información actualizada de precios
 * desde plataformas como Steam.
 * 
 * Forma parte de la arquitectura de microservicios de NoLimits, permitiendo
 * desacoplar la lógica de negocio (Spring Boot) de la obtención de datos externos (Node.js).
 */
@Service
public class ScrapingClientService {

    /**
     * Cliente HTTP utilizado para realizar peticiones al microservicio de scraping.
     * 
     * RestTemplate permite consumir endpoints REST de forma sencilla desde Spring Boot.
     */
    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * Obtiene el precio de un videojuego desde Steam a través del microservicio de scraping.
     *
     * @param appId ID del videojuego en Steam (ej: 730 para Counter-Strike)
     * @return Un Map con los datos obtenidos desde el scraping, incluyendo:
     *         - nombre
     *         - precio
     *         - precioFormato
     *         - moneda
     *         - urlPlataforma
     *         - plataforma
     *         - fechaUltimaActualizacion
     *
     * Flujo:
     * 1. Construye la URL del microservicio Node.js
     * 2. Realiza una petición HTTP GET al endpoint /api/precios
     * 3. Recibe la respuesta en formato JSON
     * 4. La convierte automáticamente a Map<String, Object>
     */
    public Map<String, Object> obtenerPrecioSteam(String appId) {

        // URL del microservicio de scraping corriendo en Node.js 
        String url = "https://nolimits-scraping-service.onrender.com/api/precios?appId=" + appId;

        // Realiza la petición GET y retorna la respuesta convertida en Map
        return restTemplate.getForObject(url, Map.class);
    }
}