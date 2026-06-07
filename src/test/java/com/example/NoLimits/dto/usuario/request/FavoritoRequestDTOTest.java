package com.example.NoLimits.dto.usuario.request;

import com.example.NoLimits.Multimedia.dto.usuario.request.FavoritoRequestDTO;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FavoritoRequestDTOTest {

    @Nested
    @DisplayName("Getters y Setters")
    class GettersSetters {

        @Test
        @DisplayName("asigna y obtiene propiedades correctamente")
        void testGettersSetters() {

            FavoritoRequestDTO dto = new FavoritoRequestDTO();

            dto.setObraId("123");
            dto.setTitulo("Spider-Man");
            dto.setTipo("PELICULA");
            dto.setPoster("/img/spiderman.webp");
            dto.setSource("TMDB");

            assertEquals("123", dto.getObraId());
            assertEquals("Spider-Man", dto.getTitulo());
            assertEquals("PELICULA", dto.getTipo());
            assertEquals("/img/spiderman.webp", dto.getPoster());
            assertEquals("TMDB", dto.getSource());
        }
    }

    @Nested
    @DisplayName("Valores por defecto")
    class ValoresPorDefecto {

        @Test
        @DisplayName("inicia con campos nulos")
        void testValoresPorDefecto() {

            FavoritoRequestDTO dto = new FavoritoRequestDTO();

            assertNull(dto.getObraId());
            assertNull(dto.getTitulo());
            assertNull(dto.getTipo());
            assertNull(dto.getPoster());
            assertNull(dto.getSource());
        }
    }
}