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
    }

    @Nested
    @DisplayName("Cobertura adicional Lombok")
     class CoberturaAdicional {

        @Test
        @DisplayName("equals retorna true para misma instancia")
        void equalsMismaInstancia() {

            ImagenesResponseDTO dto = new ImagenesResponseDTO();

            assertEquals(dto, dto);
        }

        @Test
        @DisplayName("equals retorna false con null")
        void equalsConNull() {

            ImagenesResponseDTO dto = new ImagenesResponseDTO();

            assertNotEquals(null, dto);
        }

        @Test
        @DisplayName("equals retorna false con otra clase")
        void equalsConOtraClase() {

            ImagenesResponseDTO dto = new ImagenesResponseDTO();

            assertNotEquals("texto", dto);
        }

        @Test
        @DisplayName("equals retorna false cuando cambia id")
        void equalsDistintoId() {

            ImagenesResponseDTO dto1 = new ImagenesResponseDTO();
            dto1.setId(1L);

            ImagenesResponseDTO dto2 = new ImagenesResponseDTO();
            dto2.setId(2L);

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando cambia ruta")
        void equalsDistintaRuta() {

            ImagenesResponseDTO dto1 = new ImagenesResponseDTO();
            dto1.setRuta("ruta1");

            ImagenesResponseDTO dto2 = new ImagenesResponseDTO();
            dto2.setRuta("ruta2");

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando cambia altText")
        void equalsDistintoAltText() {

            ImagenesResponseDTO dto1 = new ImagenesResponseDTO();
            dto1.setAltText("imagen uno");

            ImagenesResponseDTO dto2 = new ImagenesResponseDTO();
            dto2.setAltText("imagen dos");

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando cambia productoId")
        void equalsDistintoProductoId() {

            ImagenesResponseDTO dto1 = new ImagenesResponseDTO();
            dto1.setProductoId(10L);

            ImagenesResponseDTO dto2 = new ImagenesResponseDTO();
            dto2.setProductoId(20L);

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("objetos vacíos son iguales")
        void objetosVaciosSonIguales() {

            ImagenesResponseDTO dto1 = new ImagenesResponseDTO();
            ImagenesResponseDTO dto2 = new ImagenesResponseDTO();

            assertEquals(dto1, dto2);
        }

        @Test
        @DisplayName("objetos vacíos generan mismo hash")
        void objetosVaciosMismoHash() {

            ImagenesResponseDTO dto1 = new ImagenesResponseDTO();
            ImagenesResponseDTO dto2 = new ImagenesResponseDTO();

            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("hashCode de objeto vacío")
        void hashCodeObjetoVacio() {

            ImagenesResponseDTO dto = new ImagenesResponseDTO();

            assertNotEquals(0, dto.hashCode());
        }

        @Test
        @DisplayName("null vs valor en id")
        void nullVsValorId() {

            // Arrange
            ImagenesResponseDTO dto1 = new ImagenesResponseDTO();

            ImagenesResponseDTO dto2 = new ImagenesResponseDTO();
            dto2.setId(1L);

            // Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("valor vs null en id")
        void valorVsNullId() {

            // Arrange
            ImagenesResponseDTO dto1 = new ImagenesResponseDTO();
            dto1.setId(1L);

            ImagenesResponseDTO dto2 = new ImagenesResponseDTO();

            // Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("null vs valor en ruta")
        void nullVsValorRuta() {

            // Arrange
            ImagenesResponseDTO dto1 = new ImagenesResponseDTO();

            ImagenesResponseDTO dto2 = new ImagenesResponseDTO();
            dto2.setRuta("ruta");

            // Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("valor vs null en ruta")
        void valorVsNullRuta() {

            // Arrange
            ImagenesResponseDTO dto1 = new ImagenesResponseDTO();
            dto1.setRuta("ruta");

            ImagenesResponseDTO dto2 = new ImagenesResponseDTO();

            // Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("null vs valor en altText")
        void nullVsValorAltText() {

            // Arrange
            ImagenesResponseDTO dto1 = new ImagenesResponseDTO();

            ImagenesResponseDTO dto2 = new ImagenesResponseDTO();
            dto2.setAltText("alt");

            // Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("valor vs null en altText")
        void valorVsNullAltText() {

            // Arrange
            ImagenesResponseDTO dto1 = new ImagenesResponseDTO();
            dto1.setAltText("alt");

            ImagenesResponseDTO dto2 = new ImagenesResponseDTO();

            // Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("null vs valor en productoId")
        void nullVsValorProductoId() {

            // Arrange
            ImagenesResponseDTO dto1 = new ImagenesResponseDTO();

            ImagenesResponseDTO dto2 = new ImagenesResponseDTO();
            dto2.setProductoId(10L);

            // Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("valor vs null en productoId")
        void valorVsNullProductoId() {

            // Arrange
            ImagenesResponseDTO dto1 = new ImagenesResponseDTO();
            dto1.setProductoId(10L);

            ImagenesResponseDTO dto2 = new ImagenesResponseDTO();

            // Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("objetos con todos los campos iguales")
        void objetosCompletamenteIguales() {

            // Arrange
            ImagenesResponseDTO dto1 = new ImagenesResponseDTO();
            dto1.setId(1L);
            dto1.setRuta("ruta");
            dto1.setAltText("alt");
            dto1.setProductoId(10L);

            ImagenesResponseDTO dto2 = new ImagenesResponseDTO();
            dto2.setId(1L);
            dto2.setRuta("ruta");
            dto2.setAltText("alt");
            dto2.setProductoId(10L);

            // Assert
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
