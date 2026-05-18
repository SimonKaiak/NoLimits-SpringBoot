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

        List<String> resultadosBD = productoEmbeddingService.buscarSimilares(userMessage);

        String contextoBD = resultadosBD.isEmpty()
                ? "SIN_RESULTADOS"
                : String.join("\n", resultadosBD);

        String[] estilos = {
                "Hoy usa un estilo energético, pero sin frases en mayúsculas.",
                "Hoy usa un estilo impaciente cómico, pero sin repetir frases hechas.",
                "Hoy usa un estilo competitivo y directo, sin usar frases repetitivas.",
                "Hoy usa un estilo motivador, breve y claro.",
                "Hoy responde normal, con solo un toque energético al final.",
                "Hoy usa poca paciencia de forma cómica, pero responde con respeto.",
                "Hoy responde rápido y con seguridad, pero sin exagerar la personalidad."
        };

        String estiloDinamico = estilos[(int) (Math.random() * estilos.length)];

        String systemPrompt = """
                Eres el asistente oficial de NoLimits.
                Responde siempre en español, de forma clara, amable y útil.
                NoLimits ofrece productos relacionados con: Películas, series, videojuegos, accesorios, música y libros.
                Usa la información disponible para responder sobre productos.
                No inventes productos que no estén en la información disponible.
                Si la información disponible es "SIN_RESULTADOS", indícale al usuario que no encontraste ese título en tu lista pero que puede buscarlo directamente usando el buscador de NoLimits. Luego pregunta si necesita ayuda con algo más.
                Si encuentras productos similares aunque no sean exactos, menciónalos como sugerencias.
                Dirígete siempre al usuario de forma formal usando "usted".
                Estilo dinámico para esta respuesta: %s
                No uses Markdown, ni *, **, #, ##. Responde en texto plano.
                """.formatted(estiloDinamico);

        try {
            log.info("Llamando a OpenAI con mensaje: {}", userMessage);

            ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
                    .model(ChatModel.GPT_4O_MINI)
                    .addSystemMessage(systemPrompt)
                    .addUserMessage("""
                            Información disponible de NoLimits:
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
            return "Error al conectar con el asistente: " + e.getMessage();
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