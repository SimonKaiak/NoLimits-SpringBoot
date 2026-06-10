package com.example.NoLimits.chatbot.ai;

import com.example.NoLimits.Multimedia.chatbot.ai.OpenAIChatClient;
import com.example.NoLimits.Multimedia.service.ai.ProductoEmbeddingService;
import com.openai.client.OpenAIClient;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionMessage;
import org.springframework.test.util.ReflectionTestUtils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class OpenAIChatClientTest {

    @Nested
    @DisplayName("Constructor")
    class ConstructorTests {

        @Test
        @DisplayName("Debe crear cliente")
        void debeCrearCliente() {

            // Arrange
            ProductoEmbeddingService embeddingService =
                    mock(ProductoEmbeddingService.class);

            // Act
            OpenAIChatClient client =
                    new OpenAIChatClient(embeddingService);

            // Assert
            assertNotNull(client);
        }
    }

    @Nested
    @DisplayName("Init")
    class InitTests {

        @Test
        @DisplayName("Debe inicializar OpenAIClient")
        void debeInicializarCliente() throws Exception {

            // Arrange
            ProductoEmbeddingService embeddingService =
                    mock(ProductoEmbeddingService.class);

            OpenAIChatClient client =
                    new OpenAIChatClient(embeddingService);

            Field apiKey =
                    OpenAIChatClient.class.getDeclaredField("openAiApiKey");

            apiKey.setAccessible(true);
            apiKey.set(client, "fake-key");

            // Act + Assert
            assertDoesNotThrow(client::init);
        }
    }

        @Nested
    @DisplayName("limpiarTexto")
    class LimpiarTextoTests {

        @Test
        @DisplayName("Elimina markdown")
        void eliminaMarkdown() throws Exception {

            // Arrange
            ProductoEmbeddingService embeddingService =
                    mock(ProductoEmbeddingService.class);

            OpenAIChatClient client =
                    new OpenAIChatClient(embeddingService);

            Method limpiar =
                    OpenAIChatClient.class.getDeclaredMethod(
                            "limpiarTexto",
                            String.class
                    );

            limpiar.setAccessible(true);

            String texto = """
                    * Hola
                    ## Titulo

                    ****Texto****

                    """;

            // Act
            String resultado =
                    (String) limpiar.invoke(client, texto);

            // Assert
            assertFalse(resultado.contains("*"));
            assertFalse(resultado.contains("#"));
        }
    }

        @Nested
    @DisplayName("askNoLimits")
    class AskNoLimitsTests {

        @Test
        @DisplayName("Retorna fallback cuando OpenAI falla")
        void retornaFallbackCuandoOpenAIFalla() throws Exception {

                // Arrange
                ProductoEmbeddingService embeddingService =
                        mock(ProductoEmbeddingService.class);

                OpenAIChatClient client =
                        new OpenAIChatClient(embeddingService);

                when(embeddingService.buscarSimilares(anyString(), eq(10)))
                        .thenReturn(List.of());

                // Inyectamos null en el cliente OpenAI
                Field field = OpenAIChatClient.class.getDeclaredField("client");
                field.setAccessible(true);
                field.set(client, null);

                // Act
                String respuesta = client.askNoLimits("Naruto");

                // Assert
                assertTrue(respuesta.contains("algo falló"));
        }

        @Test
        @DisplayName("Cubre rama con resultados en base de datos")
        void cubreRamaConResultadosBD() {

                ProductoEmbeddingService embeddingService =
                        mock(ProductoEmbeddingService.class);

                OpenAIChatClient client =
                        new OpenAIChatClient(embeddingService);

                when(embeddingService.buscarSimilares(anyString(), eq(10)))
                        .thenReturn(List.of("Naruto", "Dragon Ball"));

                String respuesta = client.askNoLimits("anime");

                assertTrue(respuesta.contains("algo falló"));
        }

        @Test
        @DisplayName("Retorna respuesta exitosa de OpenAI")
        void retornaRespuestaExitosaOpenAI() {

                // Arrange
                ProductoEmbeddingService embeddingService =
                        mock(ProductoEmbeddingService.class);

                OpenAIChatClient client =
                        new OpenAIChatClient(embeddingService);

                OpenAIClient clientMock =
                        mock(OpenAIClient.class);

                ReflectionTestUtils.setField(
                        client,
                        "client",
                        clientMock
                );

                when(embeddingService.buscarSimilares(anyString(), eq(10)))
                        .thenReturn(List.of("Naruto Shippuden"));

                var chatServiceMock =
                        mock(com.openai.services.blocking.ChatService.class);

                var completionServiceMock =
                        mock(com.openai.services.blocking.chat.ChatCompletionService.class);

                var completion =
                        mock(ChatCompletion.class);

                var choice =
                        mock(ChatCompletion.Choice.class);

                var message =
                        mock(ChatCompletionMessage.class);

                when(message.content())
                        .thenReturn(Optional.of("**Naruto** es un anime."));

                when(choice.message())
                        .thenReturn(message);

                when(completion.choices())
                        .thenReturn(List.of(choice));
                
                when(completionServiceMock.create(
                        any(com.openai.models.chat.completions.ChatCompletionCreateParams.class)
                ))
                .thenReturn(completion);

                when(chatServiceMock.completions())
                        .thenReturn(completionServiceMock);

                when(clientMock.chat())
                        .thenReturn(chatServiceMock);

                // Act
                String respuesta =
                        client.askNoLimits("Naruto");

                // Assert
                assertEquals(
                        "Naruto es un anime.",
                        respuesta
                );
        }
        
    }
}
