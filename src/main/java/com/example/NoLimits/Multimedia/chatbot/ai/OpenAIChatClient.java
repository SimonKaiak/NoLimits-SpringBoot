package com.example.NoLimits.Multimedia.chatbot.ai;

import com.example.NoLimits.Multimedia.service.ai.ProductoEmbeddingService;
import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.ChatModel;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionCreateParams;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OpenAIChatClient {

    private static final Logger log = LoggerFactory.getLogger(OpenAIChatClient.class);

    private OpenAIClient client;
    private final ProductoEmbeddingService productoEmbeddingService;

    public OpenAIChatClient(ProductoEmbeddingService productoEmbeddingService) {
        this.productoEmbeddingService = productoEmbeddingService;
    }

    @PostConstruct
    public void init() {
        this.client = OpenAIOkHttpClient.fromEnv();
    }

    public String askNoLimits(String userMessage) {

        // Traer más resultados para cubrir sagas y títulos alternativos
        List<String> resultadosBD = productoEmbeddingService.buscarSimilares(userMessage, 10);

        String contextoBD = resultadosBD.isEmpty()
                ? "SIN_RESULTADOS"
                : String.join("\n---\n", resultadosBD);

        String systemPrompt = """
                Eres el asistente oficial de NoLimits, una plataforma de contenido multimedia.
                NoLimits ofrece información sobre: Películas, Series, Videojuegos, Anime, Música y Libros.
                Responde SIEMPRE en español, de forma clara, amable y formal usando "usted".
                No uses Markdown, ni *, **, #, ##. Responde en texto plano.

                REGLAS ESTRICTAS:
                1. Solo habla de títulos que aparezcan en la "Información disponible". No inventes ni agregues títulos externos.
                2. Si el usuario pregunta por una saga o personaje (ej: "Naruto", "Harry Potter", "Star Wars"), busca TODOS los títulos relacionados en la información disponible y menciónalos todos.
                3. Si no hay ningún resultado relacionado, dile al usuario que no encontraste ese título en NoLimits y sugiérele usar el buscador de la plataforma.
                4. Cuando el usuario pregunte DÓNDE VER o DÓNDE CONSEGUIR un título:
                   - Si la fuente es TMDB o JIKAN o IGDB o RAWG, dile que puede buscarlo en el buscador de NoLimits usando el nombre del título.
                   - Si conoces la plataforma de streaming del título (Netflix, Crunchyroll, Disney+, etc.) menciónala como sugerencia externa.
                   - Nunca inventes links ni precios.
                5. Si el usuario pregunta por un título específico, responde SOLO sobre ese título o saga. No menciones otros títulos no relacionados.
                6. Sé breve y directo. Máximo 4 líneas por título mencionado.
                """;

        try {
            log.info("Llamando a OpenAI con mensaje: {}", userMessage);

            ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
                    .model(ChatModel.GPT_4O_MINI)
                    .addSystemMessage(systemPrompt)
                    .addUserMessage("""
                            Información disponible en NoLimits:
                            %s

                            Pregunta del usuario:
                            %s
                            """.formatted(contextoBD, userMessage))
                    .build();

            ChatCompletion completion = client.chat().completions().create(params);

            String texto = completion.choices().get(0).message().content()
                    .orElse("No pude generar una respuesta en este momento.");

            log.info("Respuesta OpenAI recibida correctamente");
            return limpiarTexto(texto);

        } catch (Exception e) {
            log.error("ERROR EN OPENAI: {} - {}", e.getClass().getName(), e.getMessage());
            return "Lo siento, el asistente no está disponible en este momento. Puede usar el buscador de NoLimits directamente.";
        }
    }

    private String limpiarTexto(String texto) {
        return texto
                .replaceAll("(?m)^\\*\\s*", "")
                .replaceAll("\\*+", "")
                .replaceAll("#+", "")
                .replaceAll("\\n{3,}", "\n\n")
                .trim();
    }
}