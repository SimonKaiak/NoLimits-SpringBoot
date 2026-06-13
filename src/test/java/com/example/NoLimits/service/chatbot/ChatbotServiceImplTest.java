package com.example.NoLimits.service.chatbot;

import com.example.NoLimits.Multimedia.chatbot.ai.OpenAIChatClient;
import com.example.NoLimits.Multimedia.chatbot.dto.ChatResponse;
import com.example.NoLimits.Multimedia.chatbot.service.ChatbotServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ChatbotServiceImpl Tests")
class ChatbotServiceImplTest {


@Mock
private OpenAIChatClient openAIClient;

private ChatbotServiceImpl service;

@BeforeEach
void setUp() {
    service = new ChatbotServiceImpl(openAIClient);
}

@Nested
@DisplayName("getWelcomeMessage")
class GetWelcomeMessageTests {

    @Test
    @DisplayName("Retorna mensaje de bienvenida")
    void retornaMensajeBienvenida() {

        // Arrange

        // Act
        ChatResponse response =
                service.getWelcomeMessage();

        // Assert
        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals("system", response.getSource()),
                () -> assertEquals("/principal", response.getRoute()),
                () -> assertFalse(response.isExternalWarning()),
                () -> assertFalse(response.getActions().isEmpty()),
                () -> assertTrue(
                        response.getReply().contains("NoLimits")
                )
        );
    }
}

@Nested
@DisplayName("processMessage")
class ProcessMessageTests {

    @Test
    @DisplayName("Retorna ayuda para iniciar sesión")
    void retornaAyudaLogin() {

        // Arrange
        String message = "login";

        // Act
        ChatResponse response =
                service.processMessage(message);

        // Assert
        assertAll(
                () -> assertEquals("rule", response.getSource()),
                () -> assertTrue(
                        response.getReply()
                                .toLowerCase()
                                .contains("iniciar sesión")
                )
        );
    }

    @Test
    @DisplayName("Retorna ayuda para registrarse")
    void retornaAyudaRegistro() {

        // Arrange
        String message = "crear cuenta";

        // Act
        ChatResponse response =
                service.processMessage(message);

        // Assert
        assertEquals("rule", response.getSource());
    }

    @Test
    @DisplayName("Retorna ayuda para cerrar sesión")
    void retornaAyudaLogout() {

        // Arrange
        String message = "logout";

        // Act
        ChatResponse response =
                service.processMessage(message);

        // Assert
        assertEquals("rule", response.getSource());
    }

    @Test
    @DisplayName("Retorna ayuda para favoritos")
    void retornaAyudaFavoritos() {

        // Arrange
        String message = "guardar en mi biblioteca";

        // Act
        ChatResponse response =
                service.processMessage(message);

        // Assert
        assertEquals("rule", response.getSource());
    }

    @Test
    @DisplayName("Retorna ayuda para recuperar contraseña")
    void retornaAyudaContrasena() {

        // Arrange
        String message = "olvide mi contraseña";

        // Act
        ChatResponse response =
                service.processMessage(message);

        // Assert
        assertEquals("rule", response.getSource());
    }

    @Test
    @DisplayName("Retorna ayuda para encontrar contenido")
    void retornaAyudaDondeVer() {

        // Arrange
        String message = "donde ver";

        // Act
        ChatResponse response =
                service.processMessage(message);

        // Assert
        assertEquals("rule", response.getSource());
    }

    @Test
    @DisplayName("Retorna información general de NoLimits")
    void retornaInformacionNoLimits() {

        // Arrange
        String message = "que es nolimits";

        // Act
        ChatResponse response =
                service.processMessage(message);

        // Assert
        assertEquals("rule", response.getSource());
    }

    @Test
    @DisplayName("Retorna ayuda para reseñas")
    void retornaAyudaResenas() {

        // Arrange
        String message = "como dejo una resena";

        // Act
        ChatResponse response =
                service.processMessage(message);

        // Assert
        assertEquals("rule", response.getSource());
    }

    @Test
    @DisplayName("Reconoce mayúsculas y acentos")
    void reconoceTextoNormalizado() {

        // Arrange
        String message = "¿CÓMO INICIAR SESIÓN?";

        // Act
        ChatResponse response =
                service.processMessage(message);

        // Assert
        assertEquals("rule", response.getSource());
    }

    @Test
    @DisplayName("Usa OpenAI cuando no encuentra coincidencias")
    void usaOpenAI() {

        // Arrange
        when(openAIClient.askNoLimits("pregunta rara"))
                .thenReturn("respuesta ia");

        // Act
        ChatResponse response =
                service.processMessage("pregunta rara");

        // Assert
        assertAll(
                () -> assertEquals("ai", response.getSource()),
                () -> assertEquals(
                        "respuesta ia",
                        response.getReply()
                )
        );

        verify(openAIClient)
                .askNoLimits("pregunta rara");
    }
}


}
