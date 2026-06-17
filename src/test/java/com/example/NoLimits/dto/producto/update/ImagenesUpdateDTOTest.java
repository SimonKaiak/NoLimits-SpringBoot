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
        @DisplayName("equals retorna true para la misma instancia")
        void equalsMismaInstancia() {

            ImagenesUpdateDTO dto = new ImagenesUpdateDTO();

            assertEquals(dto, dto);
        }

        @Test
        @DisplayName("equals retorna false con null")
        void equalsConNull() {

            ImagenesUpdateDTO dto = new ImagenesUpdateDTO();

            assertNotEquals(dto, null);
        }

        @Test
        @DisplayName("equals retorna false con otra clase")
        void equalsConOtraClase() {

            ImagenesUpdateDTO dto = new ImagenesUpdateDTO();

            assertNotEquals(dto, "texto");
        }

        @Test
        @DisplayName("objetos vacíos son iguales")
        void objetosVaciosSonIguales() {

            ImagenesUpdateDTO dto1 = new ImagenesUpdateDTO();
            ImagenesUpdateDTO dto2 = new ImagenesUpdateDTO();

            assertEquals(dto1, dto2);
        }

        @Test
        @DisplayName("objetos vacíos tienen mismo hashCode")
        void objetosVaciosMismoHashCode() {

            ImagenesUpdateDTO dto1 = new ImagenesUpdateDTO();
            ImagenesUpdateDTO dto2 = new ImagenesUpdateDTO();

            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("equals detecta diferencia en ruta")
        void equalsRutaDistinta() {

            ImagenesUpdateDTO dto1 = new ImagenesUpdateDTO();
            dto1.setRuta("img1.webp");

            ImagenesUpdateDTO dto2 = new ImagenesUpdateDTO();
            dto2.setRuta("img2.webp");

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals detecta diferencia en altText")
        void equalsAltTextDistinto() {

            ImagenesUpdateDTO dto1 = new ImagenesUpdateDTO();
            dto1.setAltText("Imagen 1");

            ImagenesUpdateDTO dto2 = new ImagenesUpdateDTO();
            dto2.setAltText("Imagen 2");

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals detecta diferencia en productoId")
        void equalsProductoIdDistinto() {

            ImagenesUpdateDTO dto1 = new ImagenesUpdateDTO();
            dto1.setProductoId(1L);

            ImagenesUpdateDTO dto2 = new ImagenesUpdateDTO();
            dto2.setProductoId(2L);

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("null versus valor en ruta")
        void nullVsValorRuta() {

            ImagenesUpdateDTO dto1 = new ImagenesUpdateDTO();

            ImagenesUpdateDTO dto2 = new ImagenesUpdateDTO();
            dto2.setRuta("imagen.webp");

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("null versus valor en altText")
        void nullVsValorAltText() {

            ImagenesUpdateDTO dto1 = new ImagenesUpdateDTO();

            ImagenesUpdateDTO dto2 = new ImagenesUpdateDTO();
            dto2.setAltText("Portada");

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("null versus valor en productoId")
        void nullVsValorProductoId() {

            ImagenesUpdateDTO dto1 = new ImagenesUpdateDTO();

            ImagenesUpdateDTO dto2 = new ImagenesUpdateDTO();
            dto2.setProductoId(10L);

            assertNotEquals(dto1, dto2);
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