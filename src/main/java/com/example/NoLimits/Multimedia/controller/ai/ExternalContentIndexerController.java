package com.example.NoLimits.Multimedia.controller.ai;

import com.example.NoLimits.Multimedia.service.ai.ExternalContentIndexerService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/embeddings")
public class ExternalContentIndexerController {

    private final ExternalContentIndexerService indexerService;

    public ExternalContentIndexerController(ExternalContentIndexerService indexerService) {
        this.indexerService = indexerService;
    }

    @PostMapping("/indexar-externas")
    public Map<String, Integer> indexarExternas() {
        return indexerService.indexarTodo();
    }

    @PostMapping("/indexar-tmdb-peliculas")
    public String indexarPeliculas() {
        int total = indexerService.indexarPeliculasTMDB();
        return "Películas TMDB indexadas: " + total;
    }

    @PostMapping("/indexar-tmdb-series")
    public String indexarSeries() {
        int total = indexerService.indexarSeriesTMDB();
        return "Series TMDB indexadas: " + total;
    }

    @PostMapping("/indexar-rawg")
    public String indexarRawg() {
        int total = indexerService.indexarJuegosRAWG();
        return "Juegos RAWG indexados: " + total;
    }

    @PostMapping("/indexar-igdb")
    public String indexarIgdb() {
        int total = indexerService.indexarJuegosIGDB();
        return "Juegos IGDB indexados: " + total;
    }

    @PostMapping("/indexar-jikan")
    public String indexarJikan() {
        int total = indexerService.indexarAnimeJikan();
        return "Animes Jikan indexados: " + total;
    }

    @PostMapping("/indexar-libros")
    public String indexarLibros() {
        int total = indexerService.indexarLibrosOpenLibrary();
        return "Libros OpenLibrary indexados: " + total;
    }
}