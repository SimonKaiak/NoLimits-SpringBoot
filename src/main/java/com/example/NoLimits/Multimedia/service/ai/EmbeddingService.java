package com.example.NoLimits.Multimedia.service.ai;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.embeddings.EmbeddingCreateParams;
import com.openai.models.embeddings.CreateEmbeddingResponse;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmbeddingService {

    private OpenAIClient client;

    @PostConstruct
    public void init() {
        this.client = OpenAIOkHttpClient.fromEnv();
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