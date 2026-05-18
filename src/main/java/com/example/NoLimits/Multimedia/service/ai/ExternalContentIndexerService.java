package com.example.NoLimits.Multimedia.service.ai;

import com.example.NoLimits.Multimedia.service.igdb.IgdbTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class ExternalContentIndexerService {

    private static final Logger log = LoggerFactory.getLogger(ExternalContentIndexerService.class);

    private final JdbcTemplate jdbcTemplate;
    private final EmbeddingService embeddingService;
    private final IgdbTokenService igdbTokenService;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${tmdb.token}")
    private String tmdbToken;

    @Value("${igdb.client-id}")
    private String igdbClientId;

    @Value("${rawg.key}")
    private String rawgKey;

    public ExternalContentIndexerService(
            JdbcTemplate jdbcTemplate,
            EmbeddingService embeddingService,
            IgdbTokenService igdbTokenService) {
        this.jdbcTemplate = jdbcTemplate;
        this.embeddingService = embeddingService;
        this.igdbTokenService = igdbTokenService;
    }

    @SuppressWarnings("unchecked")
    public int indexarPeliculasTMDB() {
        int contador = 0;
        try {
            for (int page = 1; page <= 5; page++) {
                String url = "https://api.themoviedb.org/3/movie/popular?api_key=" + tmdbToken + "&language=es-CL&page=" + page;
                ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
                if (response.getBody() == null) continue;
                List<Map<String, Object>> results = (List<Map<String, Object>>) response.getBody().get("results");
                if (results == null) continue;
                for (Map<String, Object> movie : results) {
                    try {
                        String titulo = (String) movie.getOrDefault("title", "Sin título");
                        String descripcion = (String) movie.getOrDefault("overview", "");
                        Object rating = movie.getOrDefault("vote_average", 0);
                        Object año = movie.getOrDefault("release_date", "");
                        String contenido = """
                                Nombre: %s
                                Tipo: Película
                                Descripción: %s
                                Rating: %s
                                Año: %s
                                Fuente: TMDB
                                """.formatted(titulo, descripcion, rating, año);
                        guardarEmbeddingExterno(titulo, contenido, "TMDB");
                        contador++;
                    } catch (Exception e) {
                        log.warn("Error indexando película TMDB: {}", e.getMessage());
                    }
                }
            }
        } catch (Exception e) {
            log.error("Error al indexar películas TMDB: {}", e.getMessage());
        }
        return contador;
    }

    @SuppressWarnings("unchecked")
    public int indexarSeriesTMDB() {
        int contador = 0;
        try {
            for (int page = 1; page <= 3; page++) {
                String url = "https://api.themoviedb.org/3/tv/popular?api_key=" + tmdbToken + "&language=es-CL&page=" + page;
                ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
                if (response.getBody() == null) continue;
                List<Map<String, Object>> results = (List<Map<String, Object>>) response.getBody().get("results");
                if (results == null) continue;
                for (Map<String, Object> serie : results) {
                    try {
                        String titulo = (String) serie.getOrDefault("name", "Sin título");
                        String descripcion = (String) serie.getOrDefault("overview", "");
                        Object rating = serie.getOrDefault("vote_average", 0);
                        Object año = serie.getOrDefault("first_air_date", "");
                        String contenido = """
                                Nombre: %s
                                Tipo: Serie
                                Descripción: %s
                                Rating: %s
                                Año: %s
                                Fuente: TMDB
                                """.formatted(titulo, descripcion, rating, año);
                        guardarEmbeddingExterno(titulo, contenido, "TMDB");
                        contador++;
                    } catch (Exception e) {
                        log.warn("Error indexando serie TMDB: {}", e.getMessage());
                    }
                }
            }
        } catch (Exception e) {
            log.error("Error al indexar series TMDB: {}", e.getMessage());
        }
        return contador;
    }

    @SuppressWarnings("unchecked")
    public int indexarJuegosRAWG() {
        int contador = 0;
        try {
            for (int page = 1; page <= 5; page++) {
                String url = "https://api.rawg.io/api/games?key=" + rawgKey + "&page=" + page + "&page_size=20&ordering=-rating";
                ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
                if (response.getBody() == null) continue;
                List<Map<String, Object>> results = (List<Map<String, Object>>) response.getBody().get("results");
                if (results == null) continue;
                for (Map<String, Object> game : results) {
                    try {
                        String titulo = (String) game.getOrDefault("name", "Sin título");
                        Object rating = game.getOrDefault("rating", 0);
                        Object año = game.getOrDefault("released", "");
                        List<Map<String, Object>> genresList = (List<Map<String, Object>>) game.getOrDefault("genres", List.of());
                        String generos = genresList.stream().map(g -> (String) g.getOrDefault("name", "")).filter(s -> !s.isEmpty()).reduce((a, b) -> a + ", " + b).orElse("");
                        List<Map<String, Object>> platformsList = (List<Map<String, Object>>) game.getOrDefault("platforms", List.of());
                        String plataformas = platformsList.stream().map(p -> { Map<String, Object> platform = (Map<String, Object>) p.get("platform"); return platform != null ? (String) platform.getOrDefault("name", "") : ""; }).filter(s -> !s.isEmpty()).reduce((a, b) -> a + ", " + b).orElse("");
                        String contenido = """
                                Nombre: %s
                                Tipo: Videojuego
                                Rating: %s
                                Año: %s
                                Géneros: %s
                                Plataformas: %s
                                Fuente: RAWG
                                """.formatted(titulo, rating, año, generos, plataformas);
                        guardarEmbeddingExterno(titulo, contenido, "RAWG");
                        contador++;
                    } catch (Exception e) {
                        log.warn("Error indexando juego RAWG: {}", e.getMessage());
                    }
                }
            }
        } catch (Exception e) {
            log.error("Error al indexar juegos RAWG: {}", e.getMessage());
        }
        return contador;
    }

    @SuppressWarnings("unchecked")
    public int indexarJuegosIGDB() {
        int contador = 0;
        try {
            String token = igdbTokenService.getAccessToken();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Client-ID", igdbClientId);
            headers.setBearerAuth(token);
            headers.setContentType(MediaType.TEXT_PLAIN);
            String body = "fields name,summary,genres.name,platforms.name,rating,first_release_date; where rating > 70 & rating_count > 50; sort rating desc; limit 100;";
            HttpEntity<String> entity = new HttpEntity<>(body, headers);
            ResponseEntity<List> response = restTemplate.exchange("https://api.igdb.com/v4/games", HttpMethod.POST, entity, List.class);
            if (response.getBody() == null) return 0;
            for (Object item : response.getBody()) {
                try {
                    Map<String, Object> game = (Map<String, Object>) item;
                    String titulo = (String) game.getOrDefault("name", "Sin título");
                    String resumen = (String) game.getOrDefault("summary", "");
                    Object rating = game.getOrDefault("rating", 0);
                    List<Map<String, Object>> genresList = (List<Map<String, Object>>) game.getOrDefault("genres", List.of());
                    String generos = genresList.stream().map(g -> (String) g.getOrDefault("name", "")).filter(s -> !s.isEmpty()).reduce((a, b) -> a + ", " + b).orElse("");
                    List<Map<String, Object>> platformsList = (List<Map<String, Object>>) game.getOrDefault("platforms", List.of());
                    String plataformas = platformsList.stream().map(p -> (String) p.getOrDefault("name", "")).filter(s -> !s.isEmpty()).reduce((a, b) -> a + ", " + b).orElse("");
                    String contenido = """
                            Nombre: %s
                            Tipo: Videojuego
                            Descripción: %s
                            Rating: %s
                            Géneros: %s
                            Plataformas: %s
                            Fuente: IGDB
                            """.formatted(titulo, resumen, rating, generos, plataformas);
                    guardarEmbeddingExterno(titulo, contenido, "IGDB");
                    contador++;
                } catch (Exception e) {
                    log.warn("Error indexando juego IGDB: {}", e.getMessage());
                }
            }
        } catch (Exception e) {
            log.error("Error al indexar juegos IGDB: {}", e.getMessage());
        }
        return contador;
    }

    @SuppressWarnings("unchecked")
    public int indexarAnimeJikan() {
        int contador = 0;
        try {
            for (int page = 1; page <= 5; page++) {
                String url = "https://api.jikan.moe/v4/top/anime?page=" + page + "&limit=20";
                ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
                if (response.getBody() == null) continue;
                List<Map<String, Object>> results = (List<Map<String, Object>>) response.getBody().get("data");
                if (results == null) continue;
                for (Map<String, Object> anime : results) {
                    try {
                        String titulo = (String) anime.getOrDefault("title", "Sin título");
                        String tituloEn = (String) anime.getOrDefault("title_english", "");
                        String synopsis = (String) anime.getOrDefault("synopsis", "");
                        Object rating = anime.getOrDefault("score", 0);
                        Object año = anime.getOrDefault("year", "");
                        List<Map<String, Object>> genresList = (List<Map<String, Object>>) anime.getOrDefault("genres", List.of());
                        String generos = genresList.stream().map(g -> (String) g.getOrDefault("name", "")).filter(s -> !s.isEmpty()).reduce((a, b) -> a + ", " + b).orElse("");
                        String contenido = """
                                Nombre: %s
                                Nombre en inglés: %s
                                Tipo: Anime
                                Descripción: %s
                                Rating: %s
                                Año: %s
                                Géneros: %s
                                Fuente: JIKAN
                                """.formatted(titulo, tituloEn, synopsis, rating, año, generos);
                        guardarEmbeddingExterno(titulo, contenido, "JIKAN");
                        contador++;
                        Thread.sleep(400);
                    } catch (Exception e) {
                        log.warn("Error indexando anime Jikan: {}", e.getMessage());
                    }
                }
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            log.error("Error al indexar anime Jikan: {}", e.getMessage());
        }
        return contador;
    }

    @SuppressWarnings("unchecked")
    public int indexarLibrosOpenLibrary() {
        int contador = 0;
        String[] temas = {"fantasy", "science_fiction", "romance", "thriller", "horror", "adventure", "mystery"};
        try {
            for (String tema : temas) {
                String url = "https://openlibrary.org/subjects/" + tema + ".json?limit=20";
                ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
                if (response.getBody() == null) continue;
                List<Map<String, Object>> works = (List<Map<String, Object>>) response.getBody().get("works");
                if (works == null) continue;
                for (Map<String, Object> book : works) {
                    try {
                        String titulo = (String) book.getOrDefault("title", "Sin título");
                        List<Map<String, Object>> authorsList = (List<Map<String, Object>>) book.getOrDefault("authors", List.of());
                        String autores = authorsList.stream()
                                .map(a -> (String) a.getOrDefault("name", ""))
                                .filter(s -> !s.isEmpty())
                                .reduce((a, b) -> a + ", " + b)
                                .orElse("");
                        Object año = book.getOrDefault("first_publish_year", "");
                        String contenido = """
                                Nombre: %s
                                Tipo: Libro
                                Autor: %s
                                Año: %s
                                Género: %s
                                Fuente: OPENLIBRARY
                                """.formatted(titulo, autores, año, tema);
                        guardarEmbeddingExterno(titulo, contenido, "OPENLIBRARY");
                        contador++;
                    } catch (Exception e) {
                        log.warn("Error indexando libro OpenLibrary: {}", e.getMessage());
                    }
                }
                Thread.sleep(500);
            }
        } catch (Exception e) {
            log.error("Error al indexar libros OpenLibrary: {}", e.getMessage());
        }
        return contador;
    }
    
    @SuppressWarnings("unchecked")
    public int indexarLibrosBusqueda(String query) {
        int contador = 0;
        try {
            String url = "https://openlibrary.org/search.json?q=" + query.replace(" ", "+") + "&limit=20";
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
            if (response.getBody() == null) return 0;
            List<Map<String, Object>> docs = (List<Map<String, Object>>) response.getBody().get("docs");
            if (docs == null) return 0;
            for (Map<String, Object> book : docs) {
                try {
                    String titulo = (String) book.getOrDefault("title", "Sin título");
                    List<String> authorsList = (List<String>) book.getOrDefault("author_name", List.of());
                    String autores = authorsList.stream().reduce((a, b) -> a + ", " + b).orElse("");
                    Object año = book.getOrDefault("first_publish_year", "");
                    List<String> subjectsList = (List<String>) book.getOrDefault("subject", List.of());
                    String generos = subjectsList.stream().limit(3).reduce((a, b) -> a + ", " + b).orElse(query);
                    String contenido = """
                            Nombre: %s
                            Tipo: Libro
                            Autor: %s
                            Año: %s
                            Géneros: %s
                            Fuente: OPENLIBRARY
                            """.formatted(titulo, autores, año, generos);
                    guardarEmbeddingExterno(titulo, contenido, "OPENLIBRARY");
                    contador++;
                } catch (Exception e) {
                    log.warn("Error indexando libro búsqueda: {}", e.getMessage());
                }
            }
        } catch (Exception e) {
            log.error("Error al indexar libros por búsqueda: {}", e.getMessage());
        }
        return contador;
    }

    private void guardarEmbeddingExterno(String titulo, String contenido, String fuente) {
        List<Float> embedding = embeddingService.generarEmbedding(contenido);
        String vector = embedding.toString();
        String sql = """
                INSERT INTO producto_embeddings (producto_id, titulo, contenido, embedding, fuente)
                VALUES (NULL, ?, ?, ?::vector, ?)
                ON CONFLICT DO NOTHING
                """;
        jdbcTemplate.update(sql, titulo, contenido, vector, fuente);
    }

    public Map<String, Integer> indexarTodo() {
        int peliculas = indexarPeliculasTMDB();
        int series = indexarSeriesTMDB();
        int juegosRawg = indexarJuegosRAWG();
        int juegosIgdb = indexarJuegosIGDB();
        int anime = indexarAnimeJikan();
        int libros = indexarLibrosOpenLibrary();
        return Map.of(
                "peliculas_tmdb", peliculas,
                "series_tmdb", series,
                "juegos_rawg", juegosRawg,
                "juegos_igdb", juegosIgdb,
                "anime_jikan", anime,
                "libros_openlibrary", libros,
                "total", peliculas + series + juegosRawg + juegosIgdb + anime + libros
        );
    }
}