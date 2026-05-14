package com.example.NoLimits.Multimedia.controller.igdb;

import com.example.NoLimits.Multimedia.service.igdb.IgdbTokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Proxy hacia la API de IGDB.
 * El frontend llama a /api/igdb/** y este controlador reenvía
 * la petición a api.igdb.com añadiendo automáticamente:
 *
 *  - Client-ID de Twitch
 *  - Access Token OAuth generado dinámicamente
 *
 * El token se renueva automáticamente cuando expira,
 * evitando que la integración con IGDB deje de funcionar.
 *
 * Variables de entorno necesarias en Render:
 *   IGDB_CLIENT_ID       → Client-ID de Twitch Developer
 *   IGDB_CLIENT_SECRET   → Client Secret de Twitch Developer
 */
@RestController
@RequestMapping("/api/igdb")
public class IgdbProxyController {

    private static final Logger log =
            LoggerFactory.getLogger(IgdbProxyController.class);

    private static final String IGDB_BASE_URL = "https://api.igdb.com/v4";

    @Value("${igdb.client-id}")
    private String clientId;

    /**
     * Servicio encargado de:
     *  - generar tokens OAuth
     *  - almacenarlos temporalmente en memoria
     *  - renovarlos automáticamente al expirar
     */
    private final IgdbTokenService igdbTokenService;

    private final RestTemplate restTemplate = new RestTemplate();

    public IgdbProxyController(IgdbTokenService igdbTokenService) {
        this.igdbTokenService = igdbTokenService;
    }

    /**
     * GET /api/igdb/games
     *
     * Devuelve una lista de juegos destacados.
     *
     * Ejemplo desde frontend:
     *
     * fetch(`${VITE_API_BASE_URL}/api/igdb/games`)
     */
    @GetMapping("/games")
    public ResponseEntity<String> getGames() {

        String body =
                "fields name,cover.url,genres.name,rating,first_release_date,summary; " +
                "where rating > 75 & cover != null; " +
                "sort rating desc; " +
                "limit 20;";

        return forwardToIgdb("/games", body);
    }

    /**
     * POST /api/igdb/games
     *
     * Permite enviar queries Apicalypse personalizadas.
     */
    @PostMapping("/games")
    public ResponseEntity<String> queryGames(
            @RequestBody(required = false) String apicalypseQuery
    ) {

        if (apicalypseQuery == null || apicalypseQuery.isBlank()) {

            apicalypseQuery =
                    "fields name,cover.url,genres.name,rating,first_release_date,summary; " +
                    "where rating > 75 & cover != null; " +
                    "sort rating desc; " +
                    "limit 20;";
        }

        return forwardToIgdb("/games", apicalypseQuery);
    }

    /**
     * GET /api/igdb/games/search?q=zelda
     *
     * Realiza búsqueda de juegos por nombre.
     */
    @GetMapping("/games/search")
    public ResponseEntity<String> searchGames(@RequestParam String q) {

        String body =
                "search \"" + q + "\"; " +
                "fields name,cover.url,genres.name,rating,first_release_date,summary; " +
                "where cover != null; " +
                "limit 10;";

        return forwardToIgdb("/games", body);
    }

    // ──────────────────────────────────────────────
    // Helper privado
    // ──────────────────────────────────────────────

    /**
     * Reenvía una consulta hacia la API oficial de IGDB.
     *
     * Aquí se obtiene automáticamente el token OAuth válido
     * desde IgdbTokenService.
     */
    private ResponseEntity<String> forwardToIgdb(
            String endpoint,
            String apicalypseBody
    ) {

        try {

            // Obtiene token válido (o genera uno nuevo automáticamente)
            String token = igdbTokenService.getAccessToken();

            HttpHeaders headers = new HttpHeaders();

            headers.set("Client-ID", clientId);

            // Authorization: Bearer TOKEN
            headers.setBearerAuth(token);

            headers.setContentType(MediaType.TEXT_PLAIN);

            HttpEntity<String> entity =
                    new HttpEntity<>(apicalypseBody, headers);

            return restTemplate.exchange(
                    IGDB_BASE_URL + endpoint,
                    HttpMethod.POST,
                    entity,
                    String.class
            );

        } catch (Exception e) {

            log.error("Error consultando IGDB: {}", e.getMessage());

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("""
                            {
                            "error": "No se pudo consultar IGDB",
                            "detalle": "Servicio temporalmente no disponible"
                            }
                            """);
        }
    }
}