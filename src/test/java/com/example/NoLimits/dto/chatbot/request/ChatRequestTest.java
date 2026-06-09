package com.example.NoLimits.dto.chatbot.request;

import com.example.NoLimits.Multimedia.chatbot.dto.ChatRequest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ChatRequest")
class ChatRequestTest {

    @Nested
    @DisplayName("describe: constructor vacío")
    class ConstructorVacio {

        @Test
        @DisplayName("it: debería crear una instancia con message nulo")
        void deberiaCrearInstanciaConMessageNulo() {
            ChatRequest request = new ChatRequest();

            assertNotNull(request);
            assertNull(request.getMessage());
        }
    }

    @Nested
    @DisplayName("describe: constructor con argumentos")
    class ConstructorConArgumentos {

        @Test
        @DisplayName("it: debería crear una instancia con message")
        void deberiaCrearInstanciaConMessage() {
            ChatRequest request = new ChatRequest("Hola bot");

            assertEquals("Hola bot", request.getMessage());
        }
    }

    @Nested
    @DisplayName("describe: getters y setters")
    class GettersSetters {

        @Test
        @DisplayName("it: debería asignar y obtener message correctamente")
        void deberiaAsignarYObtenerMessageCorrectamente() {
            ChatRequest request = new ChatRequest();

            request.setMessage("Buscar productos");

            assertEquals("Buscar productos", request.getMessage());
        }
    }
}