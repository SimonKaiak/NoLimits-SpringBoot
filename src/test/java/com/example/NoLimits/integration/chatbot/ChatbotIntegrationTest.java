package com.example.NoLimits.integration.chatbot;

import com.example.NoLimits.Multimedia.chatbot.ai.OpenAIChatClient;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:nolimits_chatbot_integration_test;MODE=PostgreSQL;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.sql.init.mode=never",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect",
        "spring.jpa.properties.hibernate.id.new_generator_mappings=false"
})
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@DisplayName("T-Integración · Chatbot")
class ChatbotIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OpenAIChatClient openAIChatClient;

    @Nested
    @DisplayName("describe: integración Controller → Service")
    class IntegracionChatbot {

        @Test
        @DisplayName("it: retorna mensaje de bienvenida")
        void welcome_retornaMensajeBienvenida() throws Exception {
            mockMvc.perform(get("/api/chatbot/welcome"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.reply").exists())
                    .andExpect(jsonPath("$.source").value("system"))
                    .andExpect(jsonPath("$.route").value("/principal"));
        }

        @Test
        @DisplayName("it: responde por regla interna al consultar login")
        void chat_login_respondePorReglaInterna() throws Exception {
            String body = """
                    {
                      "message": "como iniciar sesion"
                    }
                    """;

            mockMvc.perform(post("/api/chatbot/chat")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.reply").value("Para iniciar sesión en NoLimits, haga clic en el botón 'Login' que aparece en la parte superior derecha e ingrese su correo y contraseña."))
                    .andExpect(jsonPath("$.source").value("rule"))
                    .andExpect(jsonPath("$.route").value("/principal"));
        }

        @Test
        @DisplayName("it: usa cliente IA cuando no coincide ninguna regla")
        void chat_sinRegla_usaClienteIA() throws Exception {
            when(openAIChatClient.askNoLimits("recomiendame algo raro"))
                    .thenReturn("Respuesta simulada desde IA");

            String body = """
                    {
                      "message": "recomiendame algo raro"
                    }
                    """;

            mockMvc.perform(post("/api/chatbot/chat")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.reply").value("Respuesta simulada desde IA"))
                    .andExpect(jsonPath("$.source").value("ai"))
                    .andExpect(jsonPath("$.route").value("/principal"));
        }
    }
}