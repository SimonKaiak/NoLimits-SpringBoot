package com.example.NoLimits.Multimedia.chatbot.ai;

import com.example.NoLimits.Multimedia.service.ai.ProductoEmbeddingService;
import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.ChatModel;
import com.openai.models.responses.Response;
import com.openai.models.responses.ResponseCreateParams;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OpenAIChatClient {

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

        String systemPrompt = """
                Eres el asistente oficial de NoLimits.

                Tu tarea es orientar al usuario dentro de la plataforma.
                Responde siempre en español, de forma clara, amable y útil.
                Si corresponde, explica paso a paso.

                Contexto real de NoLimits:
                - El usuario entra primero a la página principal.
                - No es obligatorio iniciar sesión para explorar productos.
                - Para iniciar sesión o registrarse, debe usar el menú hamburguesa de la parte superior izquierda.
                - Desde ese mismo menú puede acceder a iniciar sesión, registrarse y favoritos.
                - Para ver favoritos, primero debe haber iniciado sesión.
                - Existe una opción para dejar de ver favoritos.
                - El usuario puede explorar sagas destacadas o usar el buscador.
                - Desde un producto puede entrar a "Ver Plataformas".
                - Después puede usar "Ver Precios".
                - Al hacer clic sobre un precio, el usuario es redirigido a una plataforma externa.
                - Si preguntan por soporte, el correo es NoLimits@gmail.com.

                NoLimits solo ofrece productos relacionados con:
                Películas, videojuegos y accesorios.

                Usa la información real encontrada en la base de datos de NoLimits para responder sobre productos.
                Responde solo usando la información entregada en la sección de base de datos.
                No inventes productos, precios, plataformas ni datos que no estén en la información entregada.

                Si la información encontrada es "SIN_RESULTADOS", responde únicamente:
                "Ese producto no está disponible en NoLimits."

                Si el usuario pregunta por un producto que no pertenece a películas, videojuegos o accesorios, responde únicamente:
                "Ese producto no está disponible en NoLimits."

                Reglas:
                - No inventes funciones que la plataforma no tenga.
                - Si no sabes algo, dilo con honestidad.
                - Mantén las respuestas cortas o medianas.
                - No uses Markdown.
                - No uses símbolos como *, **, #, ##, ### ni guiones tipo lista.
                - Responde en texto plano.
                - Si das pasos, usa este formato:
                1) Primer paso
                2) Segundo paso
                3) Tercer paso
                - Usa saltos de línea para separar ideas.
                - No escribas todo en un solo párrafo.
                - Cada idea importante debe ir en una nueva línea.
                - Cuando muestres precios:
                   - Usa los valores exactos entregados en la base de datos.
                   - Siempre muestra el precio en pesos chilenos (CLP).
                   - Formato obligatorio: $19.990 CLP.
                - Dirígete siempre al usuario de forma formal, usando "usted".
                - Mantén un tono respetuoso, amable y profesional.
                - No uses lenguaje informal ni cercano (evita "tú", "te", "puedes", "quieres").
                - Prefiere expresiones como:
                   "usted puede",
                   "le recomendamos",
                   "debe dirigirse",
                   "puede acceder",
                   "debe seleccionar".
                """;

        ResponseCreateParams params = ResponseCreateParams.builder()
                .model(ChatModel.GPT_5_2)
                .input("""
                        %s

                        Información real encontrada en la base de datos de NoLimits:
                        %s

                        Pregunta del usuario:
                        %s
                        """.formatted(systemPrompt, contextoBD, userMessage))
                .build();

        Response response = client.responses().create(params);

        String texto = response.output().stream()
                .flatMap(item -> item.message().stream())
                .flatMap(message -> message.content().stream())
                .flatMap(content -> content.outputText().stream())
                .map(outputText -> outputText.text())
                .findFirst()
                .orElse("No pude generar una respuesta en este momento.");

        return limpiarTexto(texto);
    }

    // Limpieza extra por si la IA se pasa de lista
    private String limpiarTexto(String texto) {
        return texto
                // elimina listas tipo "* texto"
                .replaceAll("(?m)^\\*\\s*", "")
                
                // elimina markdown restante
                .replaceAll("\\*+", "")   // otros *
                .replaceAll("#+", "")    // ### títulos
                
                // limpia espacios
                .replaceAll("\\n{3,}", "\n\n")
                
                .trim();
        }
}