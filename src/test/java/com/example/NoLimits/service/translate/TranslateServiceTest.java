package com.example.NoLimits.service.translate;

import com.example.NoLimits.Multimedia.service.translate.TranslateService;
import com.openai.client.OpenAIClient;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("TranslateService")
class TranslateServiceTest {

    private TranslateService service;
    private OpenAIClient clientMock;

    @BeforeEach
    void setUp() {
        service    = new TranslateService();
        clientMock = mock(OpenAIClient.class);
        ReflectionTestUtils.setField(service, "client", clientMock);
    }

    @Nested
    @DisplayName("entrada vacía o nula — sin llamar a OpenAI")
    class EntradaVacia {

        @Test
        @DisplayName("retorna null cuando el texto es null")
        void retornaNullCuandoTextoEsNull() {
            assertNull(service.translateToSpanish(null));
            verifyNoInteractions(clientMock);
        }

        @Test
        @DisplayName("retorna el mismo texto cuando está vacío")
        void retornaMismoTextoCuandoEstaVacio() {
            assertEquals("", service.translateToSpanish(""));
            verifyNoInteractions(clientMock);
        }

        @Test
        @DisplayName("retorna el mismo texto cuando contiene solo espacios")
        void retornaMismoTextoCuandoSoloTieneEspacios() {
            assertEquals("   ", service.translateToSpanish("   "));
            verifyNoInteractions(clientMock);
        }
    }

    @Nested
    @DisplayName("llamada a OpenAI con mock del cliente")
    class LlamadaOpenAI {

        @Test
        @DisplayName("texto real → retorna la traducción que devuelve el mock")
        void textoReal_retornaTraduccion() {
            var chatServiceMock       = mock(com.openai.services.blocking.ChatService.class);
            var completionServiceMock = mock(com.openai.services.blocking.chat.ChatCompletionService.class);
            var completion            = mock(ChatCompletion.class);
            var choice                = mock(ChatCompletion.Choice.class);
            var message               = mock(ChatCompletionMessage.class);

            when(message.content()).thenReturn(Optional.of("Hola mundo"));
            when(choice.message()).thenReturn(message);
            when(completion.choices()).thenReturn(List.of(choice));
            when(completionServiceMock.create(any(com.openai.models.chat.completions.ChatCompletionCreateParams.class))).thenReturn(completion);
            when(chatServiceMock.completions()).thenReturn(completionServiceMock);
            when(clientMock.chat()).thenReturn(chatServiceMock);

            assertEquals("Hola mundo", service.translateToSpanish("Hello world"));
        }

        @Test
        @DisplayName("OpenAI lanza excepción → retorna el texto original (fallback)")
        void openAILanzaExcepcion_retornaTextoOriginal() {
            var chatServiceMock = mock(com.openai.services.blocking.ChatService.class);
            when(clientMock.chat()).thenReturn(chatServiceMock);
            when(chatServiceMock.completions()).thenThrow(new RuntimeException("timeout"));

            assertEquals("Hello world", service.translateToSpanish("Hello world"));
        }

        @Test
        @DisplayName("content vacío → retorna texto original")
        void contentVacio_retornaTextoOriginal() {
            var chatServiceMock       = mock(com.openai.services.blocking.ChatService.class);
            var completionServiceMock = mock(com.openai.services.blocking.chat.ChatCompletionService.class);
            var completion            = mock(ChatCompletion.class);
            var choice                = mock(ChatCompletion.Choice.class);
            var message               = mock(ChatCompletionMessage.class);

            when(message.content()).thenReturn(Optional.empty());
            when(choice.message()).thenReturn(message);
            when(completion.choices()).thenReturn(List.of(choice));
            when(completionServiceMock.create(any(com.openai.models.chat.completions.ChatCompletionCreateParams.class))).thenReturn(completion);
            when(chatServiceMock.completions()).thenReturn(completionServiceMock);
            when(clientMock.chat()).thenReturn(chatServiceMock);

            assertEquals("Hello world", service.translateToSpanish("Hello world"));
        }
    }
}