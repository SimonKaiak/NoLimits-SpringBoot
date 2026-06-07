package com.example.NoLimits.dto.producto.update;

import com.example.NoLimits.Multimedia.dto.producto.update.ImagenesUpdateDTO;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ImagenesUpdateDTOTest {

    @Nested
    @DisplayName("Getters y Setters")
    class GettersSetters {

        @Test
        @DisplayName("asigna y obtiene propiedades correctamente")
        void testGettersSetters() {

            ImagenesUpdateDTO dto = new ImagenesUpdateDTO();

            dto.setRuta("/assets/img/Peliculas/nueva-imagen.webp");
            dto.setAltText("Portada alternativa del producto");
            dto.setProductoId(10L);

            assertEquals("/assets/img/Peliculas/nueva-imagen.webp", dto.getRuta());
            assertEquals("Portada alternativa del producto", dto.getAltText());
            assertEquals(10L, dto.getProductoId());
        }
    }

    @Nested
    @DisplayName("Valores por defecto")
    class ValoresPorDefecto {

        @Test
        @DisplayName("inicia con campos nulos")
        void testValoresPorDefecto() {

            ImagenesUpdateDTO dto = new ImagenesUpdateDTO();

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

            ImagenesUpdateDTO dto1 = new ImagenesUpdateDTO();
            dto1.setRuta("/assets/img/Peliculas/test.webp");
            dto1.setAltText("Imagen");
            dto1.setProductoId(10L);

            ImagenesUpdateDTO dto2 = new ImagenesUpdateDTO();
            dto2.setRuta("/assets/img/Peliculas/test.webp");
            dto2.setAltText("Imagen");
            dto2.setProductoId(10L);

            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("genera toString correctamente")
        void testToString() {

            ImagenesUpdateDTO dto = new ImagenesUpdateDTO();
            dto.setRuta("/assets/img/Peliculas/test.webp");

            String resultado = dto.toString();

            assertNotNull(resultado);
            assertTrue(resultado.contains("test.webp"));
        }
    }
}