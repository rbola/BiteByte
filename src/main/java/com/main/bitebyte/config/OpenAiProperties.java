package com.main.bitebyte.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "spring.ai.openai")
public class OpenAiProperties {
    private String apiKey;
    private String model;
    private Urls urls;

    public static class Urls {
        private String base;
        private String chatCompletion;

        public String getBase() {
            return base;
        }

        public void setBase(String base) {
            this.base = base;
        }

        public String getChatCompletion() {
            return chatCompletion;
        }

        public void setChatCompletion(String chatCompletion) {
            this.chatCompletion = chatCompletion;
        }
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Urls getUrls() {
        return urls;
    }

    public void setUrls(Urls urls) {
        this.urls = urls;
    }

    public String getEndpoint() {
        return urls.getBase() + urls.getChatCompletion();
    }
}
