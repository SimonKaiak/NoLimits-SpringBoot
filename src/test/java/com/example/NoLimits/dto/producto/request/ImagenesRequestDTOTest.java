package com.example.NoLimits.dto.producto.request;

import com.example.NoLimits.Multimedia.dto.producto.request.ImagenesRequestDTO;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ImagenesRequestDTOTest {

    @Nested
    @DisplayName("Getters y Setters")
    class GettersSetters {

        @Test
        @DisplayName("asigna y obtiene propiedades correctamente")
        void testGettersSetters() {

            ImagenesRequestDTO dto = new ImagenesRequestDTO();

            dto.setRuta("/assets/img/Peliculas/spiderman.webp");
            dto.setAltText("Spider-Man posando");
            dto.setProductoId(10L);

            assertEquals("/assets/img/Peliculas/spiderman.webp", dto.getRuta());
            assertEquals("Spider-Man posando", dto.getAltText());
            assertEquals(10L, dto.getProductoId());
        }
    }

    @Nested
    @DisplayName("Valores por defecto")
    class ValoresPorDefecto {

        @Test
        @DisplayName("inicia con campos nulos")
        void testValoresPorDefecto() {

            ImagenesRequestDTO dto = new ImagenesRequestDTO();

            assertNull(dto.getRuta());
            assertNull(dto.getAltText());
            assertNull(dto.getProductoId());
        }
    }

    @Nested
    @DisplayName("Métodos Lombok")
    class MetodosLombok {

        @Test
        @DisplayName("genera equals y hashCode correctamente")
        void testEqualsYHashCode() {

            ImagenesRequestDTO dto1 = new ImagenesRequestDTO();
            dto1.setRuta("/assets/img/Peliculas/spiderman.webp");
            dto1.setAltText("Spider-Man");
            dto1.setProductoId(10L);

            ImagenesRequestDTO dto2 = new ImagenesRequestDTO();
            dto2.setRuta("/assets/img/Peliculas/spiderman.webp");
            dto2.setAltText("Spider-Man");
            dto2.setProductoId(10L);

            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("genera toString correctamente")
        void testToString() {

            ImagenesRequestDTO dto = new ImagenesRequestDTO();
            dto.setRuta("/assets/img/Peliculas/spiderman.webp");

            String resultado = dto.toString();

            assertNotNull(resultado);
            assertTrue(resultado.contains("/assets/img/Peliculas/spiderman.webp"));
        }
    }
}