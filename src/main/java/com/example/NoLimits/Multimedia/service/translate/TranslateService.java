package com.example.NoLimits.Multimedia.service.translate;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.ChatModel;
import com.openai.models.chat.completions.ChatCompletionCreateParams;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TranslateService {

    private static final Logger log = LoggerFactory.getLogger(TranslateService.class);

    @Value("${openai.api-key}")
    private String openAiApiKey;

    private OpenAIClient client;

    @PostConstruct
    public void init() {
        this.client = OpenAIOkHttpClient.builder()
                .apiKey(openAiApiKey)
                .build();
    }

    public String translateToSpanish(String text) {
        if (text == null || text.isBlank()) return text;

        try {
            var params = ChatCompletionCreateParams.builder()
                    .model(ChatModel.GPT_4O_MINI)
                    .addSystemMessage(
                        "Eres un traductor profesional. " +
                        "Traduce el siguiente texto al español de forma natural y fluida. " +
                        "Responde ÚNICAMENTE con la traducción, sin explicaciones ni texto adicional."
                    )
                    .addUserMessage(text)
                    .build();

            var completion = client.chat().completions().create(params);
            return completion.choices().get(0).message().content()
                    .orElse(text);

        } catch (Exception e) {
            log.error("Error traduciendo texto: {}", e.getMessage());
            return text; // fallback: devuelve el original
        }
    }
}