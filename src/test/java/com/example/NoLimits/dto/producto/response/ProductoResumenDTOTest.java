package com.example.NoLimits.dto.producto.response;

import com.example.NoLimits.Multimedia.dto.producto.response.ProductoResumenDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductoResumenDTOTest {

    private ProductoResumenDTO crearDTO() {
        ProductoResumenDTO dto = new ProductoResumenDTO();
        dto.setId(1L);
        dto.setNombre("Spider-Man");
        dto.setPrecio(12990.0);
        dto.setTipoProductoNombre("PELÍCULA");
        dto.setEstadoNombre("Activo");
        dto.setSaga("Spider-Man");
        dto.setPortadaSaga("/assets/img/sagas/spiderman.webp");
        dto.setImagenPortada("/assets/img/peliculas/spiderman1.webp");
        return dto;
    }

    @Nested
    @DisplayName("Getters y Setters")
    class GettersSetters {

        @Test
        @DisplayName("asigna y obtiene todos los campos correctamente")
        void testGettersSetters() {
            ProductoResumenDTO dto = crearDTO();
            assertEquals(1L, dto.getId());
            assertEquals("Spider-Man", dto.getNombre());
            assertEquals(12990.0, dto.getPrecio());
            assertEquals("PELÍCULA", dto.getTipoProductoNombre());
            assertEquals("Activo", dto.getEstadoNombre());
            assertEquals("Spider-Man", dto.getSaga());
            assertEquals("/assets/img/sagas/spiderman.webp", dto.getPortadaSaga());
            assertEquals("/assets/img/peliculas/spiderman1.webp", dto.getImagenPortada());
        }

        @Test
        @DisplayName("objeto vacío tiene todos los campos null")
        void testDefaultNulls() {
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

        @Test
        @DisplayName("saga puede ser null")
        void testSagaNull() {
            ProductoResumenDTO dto = crearDTO();
            dto.setSaga(null);
            assertNull(dto.getSaga());
        }

        @Test
        @DisplayName("portadaSaga puede ser null")
        void testPortadaSagaNull() {
            ProductoResumenDTO dto = crearDTO();
            dto.setPortadaSaga(null);
            assertNull(dto.getPortadaSaga());
        }

        @Test
        @DisplayName("imagenPortada puede ser null")
        void testImagenPortadaNull() {
            ProductoResumenDTO dto = crearDTO();
            dto.setImagenPortada(null);
            assertNull(dto.getImagenPortada());
        }
    }

    @Nested
    @DisplayName("Constructor AllArgs")
    class ConstructorAllArgs {

        @Test
        @DisplayName("constructor con todos los parámetros funciona")
        void testAllArgsConstructor() {
            ProductoResumenDTO dto = new ProductoResumenDTO(
                    1L, "Spider-Man", 12990.0, "PELÍCULA", "Activo",
                    "Spider-Man", "/sagas/spiderman.webp", "/img/spiderman.webp"
            );
            assertEquals(1L, dto.getId());
            assertEquals("Spider-Man", dto.getNombre());
            assertEquals(12990.0, dto.getPrecio());
            assertEquals("PELÍCULA", dto.getTipoProductoNombre());
            assertEquals("Activo", dto.getEstadoNombre());
            assertEquals("Spider-Man", dto.getSaga());
            assertEquals("/sagas/spiderman.webp", dto.getPortadaSaga());
            assertEquals("/img/spiderman.webp", dto.getImagenPortada());
        }

        @Test
        @DisplayName("constructor con saga y portada null")
        void testAllArgsConstructorConNulls() {
            ProductoResumenDTO dto = new ProductoResumenDTO(
                    2L, "Batman", 9990.0, "PELÍCULA", "Activo",
                    null, null, null
            );
            assertEquals(2L, dto.getId());
            assertNull(dto.getSaga());
            assertNull(dto.getPortadaSaga());
            assertNull(dto.getImagenPortada());
        }
    }

    @Nested
    @DisplayName("Equals y HashCode")
    class EqualsHashCode {

        @Test
        @DisplayName("objetos con mismos datos son iguales")
        void testEquals() {
            assertEquals(crearDTO(), crearDTO());
            assertEquals(crearDTO().hashCode(), crearDTO().hashCode());
        }

        @Test
        @DisplayName("misma instancia es igual a sí misma")
        void testSameInstance() {
            ProductoResumenDTO dto = crearDTO();
            assertEquals(dto, dto);
        }

        @Test
        @DisplayName("comparación con null retorna false")
        void testEqualsNull() {
            assertNotEquals(null, crearDTO());
        }

        @Test
        @DisplayName("comparación con otra clase retorna false")
        void testEqualsOtraClase() {
            assertNotEquals("texto", crearDTO());
        }

        @Test
        @DisplayName("objetos vacíos son iguales entre sí")
        void testVaciosIguales() {
            assertEquals(new ProductoResumenDTO(), new ProductoResumenDTO());
            assertEquals(new ProductoResumenDTO().hashCode(), new ProductoResumenDTO().hashCode());
        }

        @Test
        @DisplayName("diferente por id")
        void testNotEqualsId() {
            ProductoResumenDTO d1 = crearDTO(), d2 = crearDTO();
            d2.setId(99L);
            assertNotEquals(d1, d2);
        }

        @Test
        @DisplayName("diferente por nombre")
        void testNotEqualsNombre() {
            ProductoResumenDTO d1 = crearDTO(), d2 = crearDTO();
            d2.setNombre("Batman");
            assertNotEquals(d1, d2);
        }

        @Test
        @DisplayName("diferente por precio")
        void testNotEqualsPrecio() {
            ProductoResumenDTO d1 = crearDTO(), d2 = crearDTO();
            d2.setPrecio(999.0);
            assertNotEquals(d1, d2);
        }

        @Test
        @DisplayName("diferente por tipoProductoNombre")
        void testNotEqualsTipo() {
            ProductoResumenDTO d1 = crearDTO(), d2 = crearDTO();
            d2.setTipoProductoNombre("VIDEOJUEGO");
            assertNotEquals(d1, d2);
        }

        @Test
        @DisplayName("diferente por estadoNombre")
        void testNotEqualsEstado() {
            ProductoResumenDTO d1 = crearDTO(), d2 = crearDTO();
            d2.setEstadoNombre("Inactivo");
            assertNotEquals(d1, d2);
        }

        @Test
        @DisplayName("diferente por saga")
        void testNotEqualsSaga() {
            ProductoResumenDTO d1 = crearDTO(), d2 = crearDTO();
            d2.setSaga("Batman");
            assertNotEquals(d1, d2);
        }

        @Test
        @DisplayName("diferente por imagenPortada")
        void testNotEqualsImagenPortada() {
            ProductoResumenDTO d1 = crearDTO(), d2 = crearDTO();
            d2.setImagenPortada("/otro.webp");
            assertNotEquals(d1, d2);
        }

        @Test
        @DisplayName("null vs valor — id")
        void testNullVsValorId() {
            ProductoResumenDTO d1 = new ProductoResumenDTO();
            ProductoResumenDTO d2 = new ProductoResumenDTO(); d2.setId(1L);
            assertNotEquals(d1, d2);
        }

        @Test
        @DisplayName("null vs valor — nombre")
        void testNullVsValorNombre() {
            ProductoResumenDTO d1 = new ProductoResumenDTO();
            ProductoResumenDTO d2 = new ProductoResumenDTO(); d2.setNombre("Spider-Man");
            assertNotEquals(d1, d2);
        }

        @Test
        @DisplayName("null vs valor — precio")
        void testNullVsValorPrecio() {
            ProductoResumenDTO d1 = new ProductoResumenDTO();
            ProductoResumenDTO d2 = new ProductoResumenDTO(); d2.setPrecio(12990.0);
            assertNotEquals(d1, d2);
        }

        @Test
        @DisplayName("null vs valor — saga")
        void testNullVsValorSaga() {
            ProductoResumenDTO d1 = new ProductoResumenDTO();
            ProductoResumenDTO d2 = new ProductoResumenDTO(); d2.setSaga("Spider-Man");
            assertNotEquals(d1, d2);
        }

        @Test
        @DisplayName("null vs valor — estadoNombre")
        void testNullVsValorEstado() {
            ProductoResumenDTO d1 = new ProductoResumenDTO();
            ProductoResumenDTO d2 = new ProductoResumenDTO(); d2.setEstadoNombre("Activo");
            assertNotEquals(d1, d2);
        }

        @Test
        @DisplayName("diferente por portadaSaga")
        void testNotEqualsPortadaSaga() {
            ProductoResumenDTO d1 = crearDTO();
            ProductoResumenDTO d2 = crearDTO();

            d2.setPortadaSaga("/otra-portada.webp");

            assertNotEquals(d1, d2);
        }

        @Test
        @DisplayName("null vs valor — tipoProductoNombre")
        void testNullVsValorTipoProductoNombre() {
            ProductoResumenDTO d1 = new ProductoResumenDTO();

            ProductoResumenDTO d2 = new ProductoResumenDTO();
            d2.setTipoProductoNombre("PELÍCULA");

            assertNotEquals(d1, d2);
        }

        @Test
        @DisplayName("null vs valor — portadaSaga")
        void testNullVsValorPortadaSaga() {
            ProductoResumenDTO d1 = new ProductoResumenDTO();

            ProductoResumenDTO d2 = new ProductoResumenDTO();
            d2.setPortadaSaga("/assets/img/saga.webp");

            assertNotEquals(d1, d2);
        }

        @Test
        @DisplayName("null vs valor — imagenPortada")
        void testNullVsValorImagenPortada() {
            ProductoResumenDTO d1 = new ProductoResumenDTO();

            ProductoResumenDTO d2 = new ProductoResumenDTO();
            d2.setImagenPortada("/assets/img/imagen.webp");

            assertNotEquals(d1, d2);
        }

        @Test
        @DisplayName("hashCode puede ejecutarse sobre objeto vacío")
        void testHashCodeObjetoVacio() {
            ProductoResumenDTO dto = new ProductoResumenDTO();

            assertDoesNotThrow(dto::hashCode);
        }
    }

    @Nested
    @DisplayName("ToString")
    class ToStringTest {

        @Test
        @DisplayName("no es null")
        void testNotNull() {
            assertNotNull(crearDTO().toString());
        }

        @Test
        @DisplayName("contiene nombre y id")
        void testContenido() {
            String s = crearDTO().toString();
            assertTrue(s.contains("Spider-Man"));
            assertTrue(s.contains("1"));
        }

        @Test
        @DisplayName("objeto vacío genera toString sin errores")
        void testVacio() {
            assertNotNull(new ProductoResumenDTO().toString());
        }
    }
}