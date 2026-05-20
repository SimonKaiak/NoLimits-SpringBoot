package com.example.NoLimits.Multimedia.controller.GoogleBooks;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/books")
public class GoogleBooksProxyController {

    @Value("${google.books.key}")
    private String booksKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String BOOKS_BASE = "https://www.googleapis.com/books/v1";

    @GetMapping("/**")
    public ResponseEntity<String> proxy(HttpServletRequest request) {
        String path = request.getRequestURI().replace("/api/books", "");
        String queryString = request.getQueryString();

        String url = BOOKS_BASE + path
                   + (queryString != null ? "?" + queryString : "")
                   + (queryString != null ? "&key=" : "?key=") + booksKey;

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        return ResponseEntity.ok(response.getBody());
    }
}