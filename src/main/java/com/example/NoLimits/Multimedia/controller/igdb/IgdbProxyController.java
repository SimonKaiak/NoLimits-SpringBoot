package com.example.NoLimits.Multimedia.controller.igdb;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

/**
 * Proxy hacia la API de IGDB.
 * El frontend llama a /api/igdb/** y este controlador reenvía
 * la petición a api.igdb.com añadiendo las credenciales de Twitch.
 *
 * Variables de entorno necesarias en Render:
 *   IGDB_CLIENT_ID     → Client-ID de tu app en dev.twitch.tv
 *   IGDB_ACCESS_TOKEN  → Token obtenido de https://id.twitch.tv/oauth2/token
 */
@RestController
@RequestMapping("/api/igdb")
public class IgdbProxyController {

    private static final String IGDB_BASE_URL = "https://api.igdb.com/v4";

    @Value("${igdb.client-id}")
    private String clientId;

    @Value("${igdb.access-token}")
    private String accessToken;

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * GET /api/igdb/games
     * Reenvía el cuerpo (query Apicalypse) a IGDB y devuelve la respuesta JSON.
     *
     * Ejemplo de llamada desde el frontend:
     *   fetch(`${import.meta.env.VITE_API_BASE_URL}/api/igdb/games`, {
     *     method: 'GET'
     *   })
     */
    @GetMapping("/games")
    public ResponseEntity<String> getGames() {
        String body = "fields name,cover.url,genres.name,rating,first_release_date,summary; " +
                      "where rating > 75 & cover != null; " +
                      "sort rating desc; " +
                      "limit 20;";
        return forwardToIgdb("/games", body);
    }

    /**
     * POST /api/igdb/games
     * Permite al frontend enviar su propia query Apicalypse.
     */
    @PostMapping("/games")
    public ResponseEntity<String> queryGames(@RequestBody(required = false) String apicalypseQuery) {
        if (apicalypseQuery == null || apicalypseQuery.isBlank()) {
            apicalypseQuery = "fields name,cover.url,genres.name,rating,first_release_date,summary; " +
                              "where rating > 75 & cover != null; " +
                              "sort rating desc; limit 20;";
        }
        return forwardToIgdb("/games", apicalypseQuery);
    }

    /**
     * POST /api/igdb/games/search?q=zelda
     * Búsqueda por nombre.
     */
    @GetMapping("/games/search")
    public ResponseEntity<String> searchGames(@RequestParam String q) {
        String body = "search \"" + q + "\"; " +
                      "fields name,cover.url,genres.name,rating,first_release_date,summary; " +
                      "where cover != null; limit 10;";
        return forwardToIgdb("/games", body);
    }

    // ──────────────────────────────────────────────
    // Helper privado
    // ──────────────────────────────────────────────

    private ResponseEntity<String> forwardToIgdb(String endpoint, String apicalypseBody) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Client-ID", clientId);
        headers.set("Authorization", "Bearer " + accessToken);
        headers.setContentType(MediaType.TEXT_PLAIN);

        HttpEntity<String> entity = new HttpEntity<>(apicalypseBody, headers);

        return restTemplate.exchange(
                IGDB_BASE_URL + endpoint,
                HttpMethod.POST,   // IGDB siempre usa POST aunque sea una consulta
                entity,
                String.class
        );
    }
}