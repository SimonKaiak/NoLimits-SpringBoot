package com.example.NoLimits.Multimedia.chatbot.ai;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.ChatModel;
import com.openai.models.responses.Response;
import com.openai.models.responses.ResponseCreateParams;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class OpenAIChatClient {

    private OpenAIClient client;

    @PostConstruct
    public void init() {
        this.client = OpenAIOkHttpClient.fromEnv();
    }

    public String askNoLimits(String userMessage) {
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
                
                Reglas:
                - No inventes funciones que la plataforma no tenga.
                - Si no sabes algo, dilo con honestidad.
                - Mantén las respuestas cortas o medianas.
                """;

        ResponseCreateParams params = ResponseCreateParams.builder()
                .model(ChatModel.GPT_5_2)
                .input("""
                        %s
                        
                        Pregunta del usuario:
                        %s
                        """.formatted(systemPrompt, userMessage))
                .build();

        Response response = client.responses().create(params);

        String texto = response.output().stream()
                .flatMap(item -> item.message().stream())
                .flatMap(message -> message.content().stream())
                .flatMap(content -> content.outputText().stream())
                .map(outputText -> outputText.text())
                .findFirst()
                .orElse("No pude generar una respuesta en este momento.");

        return texto;
    }
}

// Test