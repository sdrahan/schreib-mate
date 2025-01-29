package com.serhiidrahan.daily_sochinenie_de.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.serhiidrahan.daily_sochinenie_de.config.OpenAIConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ChatGPTService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChatGPTService.class);

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
            request.setHeader("Content-Type", "application/json; charset=UTF-8");

            // Use Jackson ObjectMapper to construct JSON payload
            ObjectNode payload = objectMapper.createObjectNode();
            payload.put("model", "gpt-4o");

            ArrayNode messages = objectMapper.createArrayNode();
            messages.add(objectMapper.createObjectNode()
                    .put("role", "system")
                    .put("content", "You are a B1 German tutor. Provide grammatical feedback."));
            messages.add(objectMapper.createObjectNode()
                    .put("role", "user")
                    .put("content", inputText));

            payload.set("messages", messages);

            // Convert payload to JSON string
            String jsonPayload = objectMapper.writeValueAsString(payload);
            request.setEntity(new StringEntity(jsonPayload, ContentType.APPLICATION_JSON.withCharset("UTF-8")));

            // Execute request
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                JsonNode jsonResponse = objectMapper.readTree(response.getEntity().getContent());
                return jsonResponse.get("choices").get(0).get("message").get("content").asText();
            }
        } catch (Exception e) {
            LOGGER.error("Error fetching feedback from OpenAI API.", e);
            return "Error fetching feedback. Please try again.";
        }
    }

}

