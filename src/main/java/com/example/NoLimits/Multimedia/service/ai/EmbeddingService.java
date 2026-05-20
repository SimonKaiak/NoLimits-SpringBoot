package com.example.NoLimits.Multimedia.service.ai;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.embeddings.EmbeddingCreateParams;
import com.openai.models.embeddings.CreateEmbeddingResponse;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmbeddingService {

    private OpenAIClient client;

    @Value("${openai.api-key}")
    private String openAiApiKey;

    @PostConstruct
    public void init() {
        this.client = OpenAIOkHttpClient.builder()
                .apiKey(openAiApiKey)
                .build();
    }

    public List<Float> generarEmbedding(String texto) {

        EmbeddingCreateParams params = EmbeddingCreateParams.builder()
                .model("text-embedding-3-small")
                .input(texto)
                .build();

        CreateEmbeddingResponse response = client.embeddings().create(params);

        return response.data().get(0).embedding();
    }
}