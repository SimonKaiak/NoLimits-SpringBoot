package com.example.NoLimits.dto.producto.response;

import com.example.NoLimits.Multimedia.dto.producto.response.ProductoResumenDTO;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductoResumenDTOTest {

    @Nested
    @DisplayName("Constructor vacío")
    class ConstructorVacio {

        @Test
        @DisplayName("crea objeto con valores nulos")
        void testConstructorVacio() {

            ProductoResumenDTO dto = new ProductoResumenDTO();

            assertNull(dto.getId());
            assertNull(dto.getNombre());
            assertNull(dto.getPrecio());
            assertNull(dto.getTipoProductoNombre());
            assertNull(dto.getEstadoNombre());
            assertNull(dto.getSaga());
            assertNull(dto.getPortadaSaga());
            assertNull(dto.getImagenPortada());
        }
    }

    @Nested
    @DisplayName("Constructor completo")
    class ConstructorCompleto {

        @Test
        @DisplayName("crea objeto con todos los campos")
        void testConstructorCompleto() {

            ProductoResumenDTO dto = new ProductoResumenDTO(
                    1L,
                    "Spider-Man",
                    12990.0,
                    "PELÍCULA",
                    "Activo",
                    "Spider-Man",
                    "/assets/img/sagas/spidermanSaga.webp",
                    "/assets/img/productos/spiderman.webp"
            );

            assertEquals(1L, dto.getId());
            assertEquals("Spider-Man", dto.getNombre());
            assertEquals(12990.0, dto.getPrecio());
            assertEquals("PELÍCULA", dto.getTipoProductoNombre());
            assertEquals("Activo", dto.getEstadoNombre());
            assertEquals("Spider-Man", dto.getSaga());
            assertEquals("/assets/img/sagas/spidermanSaga.webp", dto.getPortadaSaga());
            assertEquals("/assets/img/productos/spiderman.webp", dto.getImagenPortada());
        }
    }

    @Nested
    @DisplayName("Getters y Setters")
    class GettersSetters {

        @Test
        @DisplayName("asigna y obtiene propiedades correctamente")
        void testGettersSetters() {

            ProductoResumenDTO dto = new ProductoResumenDTO();

            dto.setId(10L);
            dto.setNombre("Minecraft");
            dto.setPrecio(19990.0);
            dto.setTipoProductoNombre("VIDEOJUEGO");
            dto.setEstadoNombre("Activo");
            dto.setSaga("Minecraft");
            dto.setPortadaSaga("/assets/img/sagas/minecraft.webp");
            dto.setImagenPortada("/assets/img/productos/minecraft.webp");

            assertEquals(10L, dto.getId());
            assertEquals("Minecraft", dto.getNombre());
            assertEquals(19990.0, dto.getPrecio());
            assertEquals("VIDEOJUEGO", dto.getTipoProductoNombre());
            assertEquals("Activo", dto.getEstadoNombre());
            assertEquals("Minecraft", dto.getSaga());
            assertEquals("/assets/img/sagas/minecraft.webp", dto.getPortadaSaga());
            assertEquals("/assets/img/productos/minecraft.webp", dto.getImagenPortada());
        }
    }

    @Nested
    @DisplayName("Métodos Lombok")
    class MetodosLombok {

        @Test
        @DisplayName("genera equals y hashCode correctamente")
        void testEqualsYHashCode() {

            ProductoResumenDTO dto1 = new ProductoResumenDTO();
            dto1.setId(1L);
            dto1.setNombre("Spider-Man");

            ProductoResumenDTO dto2 = new ProductoResumenDTO();
            dto2.setId(1L);
            dto2.setNombre("Spider-Man");

            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("genera toString correctamente")
        void testToString() {

            ProductoResumenDTO dto = new ProductoResumenDTO();
            dto.setNombre("Spider-Man");

            String resultado = dto.toString();

            assertNotNull(resultado);
            assertTrue(resultado.contains("Spider-Man"));
        }
    }
}