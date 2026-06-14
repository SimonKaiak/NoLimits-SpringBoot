package com.example.NoLimits.dto.pagination;

import com.example.NoLimits.Multimedia.dto.pagination.PagedResponse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PagedResponseTest {

    @Nested
    @DisplayName("Constructores")
    class Constructores {

        @Test
        @DisplayName("constructor vacío")
        void testConstructorVacio() {

            PagedResponse<String> dto =
                    new PagedResponse<>();

            assertNull(dto.getContenido());
            assertEquals(0, dto.getPagina());
            assertEquals(0, dto.getTotalPaginas());
            assertEquals(0, dto.getTotalElementos());
        }

        @Test
        @DisplayName("constructor con argumentos")
        void testConstructorCompleto() {

            List<String> contenido =
                    List.of("A", "B");

            PagedResponse<String> dto =
                    new PagedResponse<>(
                            contenido,
                            1,
                            5,
                            20L
                    );

            assertEquals(contenido, dto.getContenido());
            assertEquals(1, dto.getPagina());
            assertEquals(5, dto.getTotalPaginas());
            assertEquals(20L, dto.getTotalElementos());
        }
    }

    @Nested
    @DisplayName("Getters y Setters")
    class GettersSetters {

        @Test
        @DisplayName("asigna y obtiene propiedades")
        void testGettersSetters() {

            PagedResponse<String> dto =
                    new PagedResponse<>();

            List<String> contenido =
                    List.of("Uno", "Dos");

            dto.setContenido(contenido);
            dto.setPagina(2);
            dto.setTotalPaginas(10);
            dto.setTotalElementos(100L);

            assertEquals(contenido, dto.getContenido());
            assertEquals(2, dto.getPagina());
            assertEquals(10, dto.getTotalPaginas());
            assertEquals(100L, dto.getTotalElementos());
        }
    }

    @Nested
    @DisplayName("Métodos Lombok")
    class MetodosLombok {

        @Test
        @DisplayName("equals y hashCode")
        void testEqualsYHashCode() {

            PagedResponse<String> dto1 =
                    new PagedResponse<>(
                            List.of("A"),
                            1,
                            5,
                            10L
                    );

            PagedResponse<String> dto2 =
                    new PagedResponse<>(
                            List.of("A"),
                            1,
                            5,
                            10L
                    );

            assertEquals(dto1, dto2);
            assertEquals(
                    dto1.hashCode(),
                    dto2.hashCode()
            );
        }

        @Test
        @DisplayName("toString")
        void testToString() {

            PagedResponse<String> dto =
                    new PagedResponse<>(
                            List.of("A"),
                            1,
                            5,
                            10L
                    );

            String resultado =
                    dto.toString();

            assertNotNull(resultado);
            assertTrue(resultado.contains("pagina=1"));
        }

        @Test
        @DisplayName("equals misma instancia")
        void testEqualsMismaInstancia() {

            PagedResponse<String> dto =
                    new PagedResponse<>();

            assertEquals(dto, dto);
        }

        @Test
        @DisplayName("equals con null")
        void testEqualsNull() {

            PagedResponse<String> dto =
                    new PagedResponse<>();

            assertNotEquals(dto, null);
        }

        @Test
        @DisplayName("equals con otro tipo")
        void testEqualsOtroTipo() {

            PagedResponse<String> dto =
                    new PagedResponse<>();

            assertNotEquals(dto, "texto");
        }

        @Test
        @DisplayName("equals pagina distinta")
        void testEqualsPaginaDistinta() {

            PagedResponse<String> dto1 =
                    new PagedResponse<>();
            dto1.setPagina(1);

            PagedResponse<String> dto2 =
                    new PagedResponse<>();
            dto2.setPagina(2);

            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando el contenido es distinto")
        void testEqualsContenidoDistinto() {

            // Arrange
            PagedResponse<String> dto1 =
                    new PagedResponse<>();
            dto1.setContenido(List.of("A"));

            PagedResponse<String> dto2 =
                    new PagedResponse<>();
            dto2.setContenido(List.of("B"));

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando totalPaginas es distinto")
        void testEqualsTotalPaginasDistinto() {

            // Arrange
            PagedResponse<String> dto1 =
                    new PagedResponse<>();
            dto1.setTotalPaginas(5);

            PagedResponse<String> dto2 =
                    new PagedResponse<>();
            dto2.setTotalPaginas(10);

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("equals retorna false cuando totalElementos es distinto")
        void testEqualsTotalElementosDistinto() {

            // Arrange
            PagedResponse<String> dto1 =
                    new PagedResponse<>();
            dto1.setTotalElementos(10L);

            PagedResponse<String> dto2 =
                    new PagedResponse<>();
            dto2.setTotalElementos(20L);

            // Act + Assert
            assertNotEquals(dto1, dto2);
        }
    }
}
