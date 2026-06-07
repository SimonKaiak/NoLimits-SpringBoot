package com.example.NoLimits.dto.producto.response;

import com.example.NoLimits.Multimedia.dto.producto.response.ImagenesResponseDTO;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ImagenesResponseDTOTest {

    @Nested
    @DisplayName("Getters y Setters")
    class GettersSetters {

        @Test
        @DisplayName("asigna y obtiene propiedades correctamente")
        void testGettersSetters() {

            ImagenesResponseDTO dto = new ImagenesResponseDTO();

            dto.setId(1L);
            dto.setRuta("/assets/img/Peliculas/spiderman.webp");
            dto.setAltText("Spider-Man posando");
            dto.setProductoId(10L);

            assertEquals(1L, dto.getId());
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

            ImagenesResponseDTO dto = new ImagenesResponseDTO();

            assertNull(dto.getId());
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

            ImagenesResponseDTO dto1 = new ImagenesResponseDTO();
            dto1.setId(1L);
            dto1.setRuta("/assets/img/Peliculas/spiderman.webp");

            ImagenesResponseDTO dto2 = new ImagenesResponseDTO();
            dto2.setId(1L);
            dto2.setRuta("/assets/img/Peliculas/spiderman.webp");

            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("genera toString correctamente")
        void testToString() {

            ImagenesResponseDTO dto = new ImagenesResponseDTO();
            dto.setRuta("/assets/img/Peliculas/spiderman.webp");

            String resultado = dto.toString();

            assertNotNull(resultado);
            assertTrue(resultado.contains("spiderman.webp"));
        }
    }
}