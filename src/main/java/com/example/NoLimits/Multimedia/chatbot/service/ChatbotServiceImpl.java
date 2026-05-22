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
                "¡Oye! 👊 Soy el asistente de NoLimits. ¡No me subestimes, sé de todo lo que hay aquí! ¿Qué andas buscando?",
                List.of(
                        "¿Qué es NoLimits?",
                        "¿Cómo crear una cuenta?",
                        "¿Cómo iniciar sesión?",
                        "¿Cómo ver un título?",
                        "¿Cómo guardar en Mi biblioteca?"
                ),
                "/principal",
                false,
                "system"
        );
    }

    @Override
    public ChatResponse processMessage(String message) {
        String text = normalize(message);

        // Iniciar sesión
        if (containsAny(text, "iniciar sesion", "login", "acceder", "entrar a mi cuenta")) {
            return new ChatResponse(
                    "Para iniciar sesión en NoLimits, haga clic en el botón 'Login' que aparece en la parte superior derecha e ingrese su correo y contraseña.",
                    List.of("¿Olvidé mi contraseña?", "¿Cómo crear una cuenta?", "¿Cómo guardar en Mi biblioteca?"),
                    "/principal",
                    false,
                    "rule"
            );
        }

        // Registrarse
        if (containsAny(text, "registrarse", "registro", "crear cuenta", "como crear una cuenta", "nueva cuenta")) {
            return new ChatResponse(
                    "Para registrarse, haga clic en el botón 'Login' en la parte superior derecha. Dentro de esa pantalla, seleccione la opción 'Registrarse', complete sus datos y envíe el formulario.",
                    List.of("¿Cómo iniciar sesión?", "¿Cómo guardar en Mi biblioteca?"),
                    "/principal",
                    false,
                    "rule"
            );
        }

        // Cerrar sesión
        if (containsAny(text, "cerrar sesion", "logout", "salir de mi cuenta", "desconectarme")) {
            return new ChatResponse(
                    "Para cerrar sesión, haga clic en el botón 'Logout' que aparece en la parte superior derecha de la plataforma, junto al ícono de búsqueda.",
                    List.of("¿Cómo iniciar sesión?", "¿Cómo guardar en Mi biblioteca?"),
                    "/principal",
                    false,
                    "rule"
            );
        }

        // Favoritos / guardar / biblioteca
        if (containsAny(text, "favorito", "favoritos", "mis favoritos", "guardar", "mi lista", "biblioteca", "mi biblioteca")) {
            return new ChatResponse(
                    "¡Ve a la página del título, dale al botón 'Guardar en mi lista' o a la estrella ☆ de la portada, y ya queda en tu 'Mi biblioteca'!",
                    List.of("¿Cómo iniciar sesión?", "¿Cómo ver un título?"),
                    "/principal", false, "rule"
            );
        }

        // Olvidé mi contraseña
        if (containsAny(text, "olvide mi contrasena", "olvide contrasena", "recuperar contrasena", "contrasena olvidada", "forgot password")) {
            return new ChatResponse(
                    "¡No te rajes! En la pantalla de 'Login' hay una opción para recuperar tu contraseña por correo. ¡Úsala!",
                    List.of("¿Cómo iniciar sesión?", "¿Cómo crear una cuenta?"),
                    "/principal", false, "rule"
            );
        }

        // Dónde ver / plataformas de streaming
        if (containsAny(text, "donde ver", "como ver", "justwatch", "buscar online", "plataforma de streaming", "donde encuentro")) {
            return new ChatResponse(
                    "Dentro de la página de cada título encontrará la sección 'Dónde encontrarlo' con los botones 'JustWatch' y 'Buscar online' que le redirigen a sitios externos para ver o conseguir el contenido.",
                    List.of("¿Cómo ver un título?", "¿Cómo guardar en Mi biblioteca?"),
                    "/principal",
                    false,
                    "rule"
            );
        }

        // Navegación general
        if (containsAny(text, "que es nolimits", "como funciona", "que puedo hacer", "para que sirve")) {
            return new ChatResponse(
                    "NoLimits es una plataforma multimedia donde puede explorar Películas, Series, Anime, Videojuegos, Música y Libros. Use 'Descubrir' para explorar el catálogo, 'Sagas' para ver franquicias completas, o el buscador 🔍 para encontrar un título específico.",
                    List.of("¿Cómo iniciar sesión?", "¿Cómo guardar en Mi biblioteca?", "¿Cómo ver un título?"),
                    "/principal",
                    false,
                    "rule"
            );
        }

        // Reseñas
        if (containsAny(text, "resena", "resenas", "opinion", "comentario", "como comento", "como dejo una resena", "guardar resena")) {
            return new ChatResponse(
                    "¡Entra al título, baja hasta 'Mi reseña personal', escribe tu opinión y dale a 'Guardar reseña'! También puedes responder reseñas de otros con 'Responder' y darles like 👍 o dislike 👎.",
                    List.of("¿Cómo guardar en Mi biblioteca?", "¿Cómo iniciar sesión?"),
                    "/principal", false, "rule"
            );
        }
        
        // Todo lo demás → OpenAI
        String aiReply = openAIClient.askNoLimits(message);

        return new ChatResponse(
                aiReply,
                List.of("¿Cómo ver un título?", "¿Cómo iniciar sesión?", "¿Cómo guardar en Mi biblioteca?"),
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