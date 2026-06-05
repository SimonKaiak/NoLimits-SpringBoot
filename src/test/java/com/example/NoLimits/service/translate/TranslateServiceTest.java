package com.example.NoLimits.service.translate;

import com.example.NoLimits.Multimedia.service.translate.TranslateService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class TranslateServiceTest {

    private final TranslateService service = new TranslateService();

    @Nested
    @DisplayName("Unitario - translateToSpanish")
    class TranslateToSpanish {

        @Test
        @DisplayName("retorna null cuando el texto es null")
        void retornaNullCuandoTextoEsNull() {
            assertNull(service.translateToSpanish(null));
        }

        @Test
        @DisplayName("retorna el mismo texto cuando está vacío")
        void retornaMismoTextoCuandoEstaVacio() {
            assertEquals("", service.translateToSpanish(""));
        }

        @Test
        @DisplayName("retorna el mismo texto cuando contiene solo espacios")
        void retornaMismoTextoCuandoSoloTieneEspacios() {
            assertEquals("   ", service.translateToSpanish("   "));
        }
    }
}