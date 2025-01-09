package com.serhiidrahan.daily_sochinenie_de.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.serhiidrahan.daily_sochinenie_de.config.OpenAIConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Service;

@Service
public class ChatGPTService {

    private final OpenAIConfig openAIConfig;
    private final ObjectMapper objectMapper;

    public ChatGPTService(OpenAIConfig openAIConfig, ObjectMapper objectMapper) {
        this.openAIConfig = openAIConfig;
        this.objectMapper = objectMapper;
    }

    public String getFeedback(String inputText) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost request = new HttpPost(openAIConfig.getApiUrl());
            request.setHeader("Authorization", "Bearer " + openAIConfig.getApiKey());
            request.setHeader("Content-Type", "application/json");

            // Prepare payload
            String payload = String.format("""
                {
                    "model": "gpt-4o",
                    "messages": [
                        {"role": "system", "content": "You are a B1 German tutor. Provide grammatical feedback."},
                        {"role": "user", "content": "%s"}
                    ]
                }
            """, inputText);
            request.setEntity(new StringEntity(payload));

            // Execute request
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                JsonNode jsonResponse = objectMapper.readTree(response.getEntity().getContent());
                return jsonResponse.get("choices").get(0).get("message").get("content").asText();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error fetching feedback. Please try again.";
        }
    }
}

