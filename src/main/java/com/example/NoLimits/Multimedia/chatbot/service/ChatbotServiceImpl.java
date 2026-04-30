package com.example.NoLimits.Multimedia.chatbot.service;

import com.example.NoLimits.Multimedia.chatbot.ai.OpenAIChatClient;
import com.example.NoLimits.Multimedia.chatbot.dto.ChatResponse;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.List;

@Service
public class ChatbotServiceImpl implements ChatbotService {

    private final OpenAIChatClient openAIClient;

    public ChatbotServiceImpl(OpenAIChatClient openAIClient) {
        this.openAIClient = openAIClient;
    }

    @Override
    public ChatResponse getWelcomeMessage() {
        return new ChatResponse(
                "¡Hola! 👋 Soy el asistente de NoLimits! Puedo orientarle dentro de la plataforma. ¿Busca algo en especifico?",
                List.of(
                        "¿Qué es NoLimits?",
                        "¿Cómo Crear Una Cuenta?",
                        "¿Cómo Iniciar Sesión?",
                        "¿Cómo ver un producto?",
                        "¿Cómo ver mis favoritos?"
                ),
                "/principal",
                false,
                "system"
        );
    }

    @Override
    public ChatResponse processMessage(String message) {
        String text = normalize(message);

        if (containsAny(text, "iniciar sesion", "login", "acceder", "entrar a mi cuenta")) {
            return new ChatResponse(
                    "Para iniciar sesión en NoLimits:\n"
                            + "1) Ingrese a la página principal.\n"
                            + "2) Abra el menú hamburguesa.\n"
                            + "3) Seleccione 'Iniciar Sesión'.\n"
                            + "4) Ingrese su correo y contraseña.",
                    List.of("Olvidé mi contraseña", "¿Cómo Crear Una Cuenta?", "¿Cómo ver mis favoritos?"),
                    "/login",
                    false,
                    "rule"
            );
        }

        if (containsAny(text, "registrarse", "registro", "crear cuenta", "como crear una cuenta")) {
            return new ChatResponse(
                    "Para crear una cuenta:\n"
                            + "1) Ingrese a principal.\n"
                            + "2) Abra el menú hamburguesa.\n"
                            + "3) Seleccione 'Registrarse'.\n"
                            + "4) Complete el formulario y envíelo.",
                    List.of("¿Cómo Iniciar Sesión?", "Soporte"),
                    "/registro",
                    false,
                    "rule"
            );
        }

        if (containsAny(text, "favorito", "favoritos", "mis favoritos")) {
            return new ChatResponse(
                    "Para ver sus favoritos:\n"
                            + "1) Primero debe haber iniciado sesión.\n"
                            + "2) Luego abra el menú hamburguesa.\n"
                            + "3) Seleccione 'Favoritos'.",
                    List.of("¿Cómo dejar de ver mis favoritos?", "¿Cómo Iniciar Sesión?"),
                    "/favoritos",
                    false,
                    "rule"
            );
        }

        if (containsAny(text, "plataforma", "ver precios", "precio", "link", "enlace")) {
            return new ChatResponse(
                    "Para ir a una plataforma desde un producto:\n"
                            + "1) Entre a principal.\n"
                            + "2) Busque o seleccione un producto.\n"
                            + "3) Entre a 'Ver Plataformas'.\n"
                            + "4) Luego use 'Ver Precios'.\n"
                            + "5) Al hacer clic en el precio, será redirigido a una plataforma externa.",
                    List.of("¿Cómo ver un producto?", "Soporte"),
                    "/principal",
                    true,
                    "rule"
            );
        }

        String aiReply = openAIClient.askNoLimits(message);

        return new ChatResponse(
                aiReply,
                List.of("¿Cómo ver un producto?", "¿Cómo Iniciar Sesión?", "Soporte"),
                "/principal",
                false,
                "ai"
        );
    }

    private String normalize(String text) {
        String normalized = Normalizer.normalize(text, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
        return normalized.toLowerCase().trim().replaceAll("\\s+", " ");
    }

    private boolean containsAny(String text, String... values) {
        for (String value : values) {
            if (text.contains(value)) {
                return true;
            }
        }
        return false;
    }
}