package com.main.bitebyte.config;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmbeddingModelConfig {

    private final OpenAiProperties properties;

    public EmbeddingModelConfig(OpenAiProperties properties) {
        this.properties = properties;
        System.out.println("API Key: " + properties.getApiKey()); // Debug log
    }

    @Bean
    public OpenAiApi openAiApi() {
        String apiKey = properties.getApiKey();
        System.out.println("Using API Key: " + apiKey); // Debug log
        return new OpenAiApi(apiKey);
    }

    @Bean
    public EmbeddingModel embeddingModel(OpenAiApi openAiApi) {
        return new OpenAiEmbeddingModel(openAiApi);
    }
}
