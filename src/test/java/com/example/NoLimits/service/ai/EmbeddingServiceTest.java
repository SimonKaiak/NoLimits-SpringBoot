package com.example.NoLimits.service.ai;

import com.example.NoLimits.Multimedia.service.ai.EmbeddingService;
import com.openai.client.OpenAIClient;
import com.openai.models.embeddings.CreateEmbeddingResponse;
import com.openai.models.embeddings.Embedding;
import com.openai.models.embeddings.EmbeddingCreateParams;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EmbeddingServiceTest {

    private EmbeddingService embeddingService;
    private OpenAIClient openAIClientMock;

    @BeforeEach
    void setUp() {
        embeddingService = new EmbeddingService();
        openAIClientMock = mock(OpenAIClient.class);
        ReflectionTestUtils.setField(embeddingService, "client", openAIClientMock);
    }

    @Nested
    @DisplayName("generarEmbedding")
    class GenerarEmbedding {

        @Test
        @DisplayName("retorna lista de floats cuando OpenAI responde correctamente")
        void retornaListaDeFloats() {
            var embeddingApi = mock(com.openai.services.blocking.EmbeddingService.class);
            var responseMock = mock(CreateEmbeddingResponse.class);
            var embeddingMock = mock(Embedding.class);

            when(openAIClientMock.embeddings()).thenReturn(embeddingApi);
            when(embeddingApi.create(any(EmbeddingCreateParams.class))).thenReturn(responseMock);
            when(responseMock.data()).thenReturn(List.of(embeddingMock));
            when(embeddingMock.embedding()).thenReturn(List.of(0.1f, 0.2f, 0.3f));

            List<Float> resultado = embeddingService.generarEmbedding("texto de prueba");

            assertThat(resultado).isNotEmpty();
            assertThat(resultado).containsExactly(0.1f, 0.2f, 0.3f);
        }

        @Test
        @DisplayName("llama a la API con los parámetros correctos")
        void llamaApiConParametrosCorrectos() {
            var embeddingApi = mock(com.openai.services.blocking.EmbeddingService.class);
            var responseMock = mock(CreateEmbeddingResponse.class);
            var embeddingMock = mock(Embedding.class);

            when(openAIClientMock.embeddings()).thenReturn(embeddingApi);
            when(embeddingApi.create(any(EmbeddingCreateParams.class))).thenReturn(responseMock);
            when(responseMock.data()).thenReturn(List.of(embeddingMock));
            when(embeddingMock.embedding()).thenReturn(List.of(0.5f));

            embeddingService.generarEmbedding("hola mundo");

            verify(embeddingApi).create(any(EmbeddingCreateParams.class));
        }
    }

    @Nested
    @DisplayName("generarEmbedding — branches adicionales")
    class GenerarEmbeddingBranches {

        @Test
        @DisplayName("lanza excepción cuando OpenAI falla → se propaga")
        void lanzaExcepcionCuandoOpenAIFalla() {
            var embeddingApi = mock(com.openai.services.blocking.EmbeddingService.class);

            when(openAIClientMock.embeddings()).thenReturn(embeddingApi);
            when(embeddingApi.create(any(EmbeddingCreateParams.class)))
                    .thenThrow(new RuntimeException("timeout de OpenAI"));

            assertThrows(RuntimeException.class,
                    () -> embeddingService.generarEmbedding("texto de prueba"));
        }

        @Test
        @DisplayName("retorna primer embedding cuando hay múltiples en la respuesta")
        void retornaPrimerEmbeddingCuandoHayMultiples() {
            var embeddingApi  = mock(com.openai.services.blocking.EmbeddingService.class);
            var responseMock  = mock(CreateEmbeddingResponse.class);
            var embedding1    = mock(Embedding.class);
            var embedding2    = mock(Embedding.class);

            when(openAIClientMock.embeddings()).thenReturn(embeddingApi);
            when(embeddingApi.create(any(EmbeddingCreateParams.class))).thenReturn(responseMock);
            when(responseMock.data()).thenReturn(List.of(embedding1, embedding2));
            when(embedding1.embedding()).thenReturn(List.of(1.0f, 2.0f));
            when(embedding2.embedding()).thenReturn(List.of(9.0f, 8.0f));

            List<Float> resultado = embeddingService.generarEmbedding("hola");

            assertThat(resultado).containsExactly(1.0f, 2.0f);
        }

        @Test
        @DisplayName("pasa el texto exacto al builder de parámetros")
        void pasaTextoExactoAlBuilder() {
            var embeddingApi = mock(com.openai.services.blocking.EmbeddingService.class);
            var responseMock = mock(CreateEmbeddingResponse.class);
            var embeddingMock = mock(Embedding.class);

            when(openAIClientMock.embeddings()).thenReturn(embeddingApi);
            when(embeddingApi.create(any(EmbeddingCreateParams.class))).thenReturn(responseMock);
            when(responseMock.data()).thenReturn(List.of(embeddingMock));
            when(embeddingMock.embedding()).thenReturn(List.of(0.5f));

            embeddingService.generarEmbedding("texto específico largo de prueba");

            verify(embeddingApi).create(any(EmbeddingCreateParams.class));
        }
    }
}