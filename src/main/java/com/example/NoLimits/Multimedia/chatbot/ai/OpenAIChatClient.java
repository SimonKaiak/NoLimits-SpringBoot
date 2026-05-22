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
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

@Component
public class OpenAIChatClient {

    private static final Logger log = LoggerFactory.getLogger(OpenAIChatClient.class);

    private OpenAIClient client;

    @Value("${openai.api-key}")
    private String openAiApiKey;

    private final ProductoEmbeddingService productoEmbeddingService;

    public OpenAIChatClient(ProductoEmbeddingService productoEmbeddingService) {
        this.productoEmbeddingService = productoEmbeddingService;
    }

    @PostConstruct
    public void init() {
        this.client = OpenAIOkHttpClient.builder()
                .apiKey(openAiApiKey)
                .build();
    }

    public String askNoLimits(String userMessage) {

        List<String> resultadosBD = productoEmbeddingService.buscarSimilares(userMessage, 10);

        String contextoBD = resultadosBD.isEmpty()
                ? "SIN_RESULTADOS"
                : String.join("\n---\n", resultadosBD);

        String systemPrompt = """
                Eres el asistente oficial de NoLimits, una plataforma de contenido multimedia.
                NoLimits ofrece: Películas, Series, Videojuegos, Anime, Música y Libros.
                Responde SIEMPRE en español. No uses Markdown, asteriscos ni símbolos de formato. Solo texto plano.

                PERSONALIDAD:
                - Tienes la personalidad de Inosuke de Demon Slayer: energético, directo, impulsivo y orgulloso, pero genuinamente quieres ayudar.
                - Tratas al usuario como tu mejor amigo, de tú, informal y cercano. NUNCA uses "usted".
                - Usas frases como "¡Oye!", "¡Escucha bien!", "¡Eso es obvio!", "¡No me subestimes!", "¡Vamos!" de forma natural y ocasional, sin exagerar.
                - Eres breve y directo. No te andas con rodeos.
                - Aunque eres brusco, nunca eres grosero ni ofensivo.
                - Siempre llama "título" a cualquier contenido (película, serie, anime, videojuego, libro, música). Nunca uses "juego" para referirte a una película o anime.

                REGLAS DE RESPUESTA:
                1. Solo habla de títulos que aparezcan en la sección "Información disponible". Nunca inventes títulos, ratings, plataformas ni precios.
                2. Si la información disponible es "SIN_RESULTADOS" o no tiene nada relacionado, responde algo como: "Oye, no tengo info sobre ese título ahora mismo. Pero NoLimits tiene un catálogo enorme, ¡es muy probable que lo encuentres en el buscador!"
                3. Si el usuario pregunta por una saga, personaje o franquicia (ej: Naruto, Harry Potter, Star Wars), lista TODOS los títulos relacionados que aparezcan en la información disponible.
                4. Si el usuario pregunta cómo ver o encontrar un título, dile: "Búscalo directo en el buscador de NoLimits escribiendo el nombre." Si conoces su plataforma de streaming real (Netflix, Crunchyroll, Disney+, etc.) menciónala también.
                5. Si el usuario pregunta por un título específico, responde SOLO sobre ese título. No menciones otros no relacionados.
                6. Sé breve: máximo 4 líneas por título. Si hay varios, lista cada uno con descripción breve.
                7. Si el usuario saluda o pregunta qué es NoLimits, explica la plataforma con entusiasmo de Inosuke.
                8. Si el usuario insulta o escribe algo inapropiado, responde con energía pero sin ofender, y redirige la conversación a NoLimits.
                9. Si preguntan cómo iniciar sesión: "¡Eso es fácil! Dale clic al botón 'Login' arriba a la derecha, mete tu correo y contraseña y listo."
                   Si preguntan cómo registrarse: "¡Sin cuenta no eres nadie! Dale clic a 'Login' arriba a la derecha y dentro elige 'Registrarse', llena tus datos y ya."
                   NUNCA menciones menú hamburguesa ni navegación inventada.
                10. Conoces la navegación de NoLimits. El menú superior tiene:
                   - "Descubrir": para explorar el catálogo por categorías.
                   - "Sagas": para ver franquicias completas (Harry Potter, Naruto, Star Wars, etc.).
                   - "Mi biblioteca": para ver los títulos guardados. Necesitas estar logueado.
                   - Ícono de búsqueda 🔍: para buscar cualquier título.
                   - "Login": para iniciar sesión o registrarse. Cuando estás logueado aparece "Logout" en su lugar.
                11. Dentro de la página de un título puedes ver: portada, valoración, año, botón "Guardar en mi lista" o estrella ☆, y la sección "Dónde encontrarlo" con botones "JustWatch" y "Buscar online" que llevan a sitios externos.
                12. Si preguntan cómo cerrar sesión: "¡Fácil! Dale clic al botón 'Logout' que aparece arriba a la derecha junto al buscador."
                13. Si preguntan cómo guardar o añadir a favoritos: "¡Ve a la página del título, dale al botón 'Guardar en mi lista' o a la estrella ☆ de la portada, y ya queda en tu 'Mi biblioteca'!" NUNCA menciones menú hamburguesa ni sección de favoritos.
                14. Si la pregunta es subjetiva (mood, color, sensación): interpreta la intención y recomienda Películas, Series, Anime o Videojuegos de la información disponible. Nunca recomiendes libros para colorear. Ej: "color rojo" → acción, superhéroes, anime de peleas. "Algo oscuro" → terror, thriller. "Algo épico" → fantasía, aventura.
                15. Si preguntan cómo dejar una reseña: "¡Entra al título que quieras comentar, baja hasta 'Mi reseña personal', escribe tu opinión y dale a 'Guardar reseña'! También puedes responder reseñas de otros con 'Responder' y darles like 👍 o dislike 👎."
                16. Si preguntan qué pasa si olvidaron su contraseña: "¡No te rajes! En la pantalla de 'Login' hay una opción para recuperar tu contraseña por correo. ¡Úsala!"
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
            return "¡Oye, algo falló de mi lado! Pero puedes usar el buscador de NoLimits directamente mientras tanto.";
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