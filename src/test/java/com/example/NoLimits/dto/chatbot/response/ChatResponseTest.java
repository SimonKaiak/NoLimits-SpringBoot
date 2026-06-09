package com.example.NoLimits.dto.chatbot.response;

import com.example.NoLimits.Multimedia.chatbot.dto.ChatResponse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ChatResponse")
class ChatResponseTest {

    @Nested
    @DisplayName("describe: constructor vacío")
    class ConstructorVacio {

        @Test
        @DisplayName("it: debería crear una instancia con campos por defecto")
        void deberiaCrearInstanciaConCamposPorDefecto() {
            ChatResponse response = new ChatResponse();

            assertNotNull(response);
            assertNull(response.getReply());
            assertNull(response.getActions());
            assertNull(response.getRoute());
            assertFalse(response.isExternalWarning());
            assertNull(response.getSource());
        }
    }

    @Nested
    @DisplayName("describe: constructor con argumentos")
    class ConstructorConArgumentos {

        @Test
        @DisplayName("it: debería crear una instancia con todos los campos")
        void deberiaCrearInstanciaConTodosLosCampos() {
            List<String> actions = List.of("buscar", "mostrar");

            ChatResponse response = new ChatResponse(
                    "Respuesta del bot",
                    actions,
                    "/productos",
                    true,
                    "external"
            );

            assertEquals("Respuesta del bot", response.getReply());
            assertEquals(actions, response.getActions());
            assertEquals("/productos", response.getRoute());
            assertTrue(response.isExternalWarning());
            assertEquals("external", response.getSource());
        }
    }

    @Nested
    @DisplayName("describe: getters y setters")
    class GettersSetters {

        @Test
        @DisplayName("it: debería asignar y obtener todos los campos correctamente")
        void deberiaAsignarYObtenerTodosLosCamposCorrectamente() {
            ChatResponse response = new ChatResponse();
            List<String> actions = List.of("ir_detalle");

            response.setReply("Listo");
            response.setActions(actions);
            response.setRoute("/detalle/1");
            response.setExternalWarning(false);
            response.setSource("local");

            assertEquals("Listo", response.getReply());
            assertEquals(actions, response.getActions());
            assertEquals("/detalle/1", response.getRoute());
            assertFalse(response.isExternalWarning());
            assertEquals("local", response.getSource());
        }

        @Test
        @DisplayName("it: debería permitir externalWarning en true")
        void deberiaPermitirExternalWarningEnTrue() {
            ChatResponse response = new ChatResponse();

            response.setExternalWarning(true);

            assertTrue(response.isExternalWarning());
        }

        @Test
        @DisplayName("it: debería permitir externalWarning en false")
        void deberiaPermitirExternalWarningEnFalse() {
            ChatResponse response = new ChatResponse();

            response.setExternalWarning(false);

            assertFalse(response.isExternalWarning());
        }
    }
}