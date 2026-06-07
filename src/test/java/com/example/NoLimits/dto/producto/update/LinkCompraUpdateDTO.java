package com.example.NoLimits.dto.producto.update;

import com.example.NoLimits.Multimedia.dto.producto.update.LinkCompraUpdateDTO;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LinkCompraUpdateDTOTest {

    @Nested
    @DisplayName("Constructor vacío")
    class ConstructorVacio {

        @Test
        @DisplayName("crea objeto con valores nulos")
        void testConstructorVacio() {

            LinkCompraUpdateDTO dto = new LinkCompraUpdateDTO();

            assertNull(dto.getPlataformaId());
            assertNull(dto.getUrl());
            assertNull(dto.getLabel());
            assertNull(dto.getAppId());
            assertNull(dto.getPrecioActual());
        }
    }

    @Nested
    @DisplayName("Constructor completo")
    class ConstructorCompleto {

        @Test
        @DisplayName("crea objeto con todos los campos")
        void testConstructorCompleto() {

            LinkCompraUpdateDTO dto = new LinkCompraUpdateDTO(
                    1L,
                    "https://store.steampowered.com/app/test",
                    "Steam",
                    "12345",
                    19990.0
            );

            assertEquals(1L, dto.getPlataformaId());
            assertEquals("https://store.steampowered.com/app/test", dto.getUrl());
            assertEquals("Steam", dto.getLabel());
            assertEquals("12345", dto.getAppId());
            assertEquals(19990.0, dto.getPrecioActual());
        }
    }

    @Nested
    @DisplayName("Getters y Setters")
    class GettersSetters {

        @Test
        @DisplayName("asigna y obtiene propiedades correctamente")
        void testGettersSetters() {

            LinkCompraUpdateDTO dto = new LinkCompraUpdateDTO();

            dto.setPlataformaId(2L);
            dto.setUrl("https://playstation.com/game");
            dto.setLabel("PlayStation");
            dto.setAppId("PS001");
            dto.setPrecioActual(29990.0);

            assertEquals(2L, dto.getPlataformaId());
            assertEquals("https://playstation.com/game", dto.getUrl());
            assertEquals("PlayStation", dto.getLabel());
            assertEquals("PS001", dto.getAppId());
            assertEquals(29990.0, dto.getPrecioActual());
        }
    }

    @Nested
    @DisplayName("Métodos heredados de Object")
    class MetodosObject {

        @Test
        @DisplayName("toString retorna un valor válido")
        void testToString() {

            LinkCompraUpdateDTO dto = new LinkCompraUpdateDTO();
            dto.setLabel("Steam");

            String resultado = dto.toString();

            assertNotNull(resultado);
        }
    }
}