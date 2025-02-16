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

import java.io.File;
import java.nio.file.Files;
import java.util.Base64;

@Service
public class ChatGPTService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChatGPTService.class);

    private final OpenAIConfig openAIConfig;
    private final ObjectMapper objectMapper;

    public ChatGPTService(OpenAIConfig openAIConfig, ObjectMapper objectMapper) {
        this.openAIConfig = openAIConfig;
        this.objectMapper = objectMapper;
    }

    private String executeRequest(ObjectNode payload) throws Exception {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost request = new HttpPost(openAIConfig.getApiUrl());
            request.setHeader("Authorization", "Bearer " + openAIConfig.getApiKey());
            request.setHeader("Content-Type", "application/json; charset=UTF-8");

            String jsonPayload = objectMapper.writeValueAsString(payload);
            request.setEntity(new StringEntity(jsonPayload, ContentType.APPLICATION_JSON.withCharset("UTF-8")));

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                JsonNode jsonResponse = objectMapper.readTree(response.getEntity().getContent());
                return jsonResponse.get("choices").get(0).get("message").get("content").asText().trim();
            }
        }
    }

    private ObjectNode createMessage(String role, String content) {
        ObjectNode message = objectMapper.createObjectNode();
        message.put("role", role);
        message.put("content", content);
        return message;
    }

    public boolean validateSubmission(String submissionText, String topic) {
        try {
            ObjectNode payload = objectMapper.createObjectNode();
            payload.put("model", "gpt-4o");

            ArrayNode messages = objectMapper.createArrayNode();
            messages.add(createMessage("system", "You are an evaluator. Given a topic and an essay, determine if the essay is related to the topic. If it is, answer only with 'RELATED'. If it is not, answer only with 'NOT RELATED'. Do not include any additional text."));
            messages.add(createMessage("user", "Topic: " + topic + "\nEssay: " + submissionText));
            payload.set("messages", messages);

            String result = executeRequest(payload);
            return result.equalsIgnoreCase("RELATED");
        } catch (Exception e) {
            LOGGER.error("Error during submission validation", e);
            // In case of error, assume submission is valid (or handle as you see fit)
            return true;
        }
    }

    /**
     * Extracts text from an image file using OCR.
     */
    public String extractTextFromImage(File imageFile) {
        try {
            String base64Image = encodeImageToBase64(imageFile);

            ObjectNode payload = objectMapper.createObjectNode();
            payload.put("model", "gpt-4o");

            ArrayNode messages = objectMapper.createArrayNode();
            messages.add(createMessage("system", "You are an OCR tool. Extract only the handwritten text from the image. Provide only the extracted text without any additional commentary."));

            // Build user message with an image attachment.
            ObjectNode userMessage = objectMapper.createObjectNode();
            userMessage.put("role", "user");
            ArrayNode contentArray = objectMapper.createArrayNode();
            ObjectNode imageObject = objectMapper.createObjectNode();
            imageObject.put("url", "data:image/jpeg;base64," + base64Image);
            ObjectNode imageContent = objectMapper.createObjectNode();
            imageContent.put("type", "image_url");
            imageContent.set("image_url", imageObject);
            contentArray.add(imageContent);
            userMessage.set("content", contentArray);
            messages.add(userMessage);

            payload.set("messages", messages);

            return executeRequest(payload);
        } catch (Exception e) {
            LOGGER.error("Error extracting text from image through OpenAI API.", e);
            return "";
        }
    }

    /**
     * Provides grammatical feedback for a text-based input.
     */
    public String getFeedback(String inputText) {
        try {
            ObjectNode payload = objectMapper.createObjectNode();
            payload.put("model", "gpt-4o");

            ArrayNode messages = objectMapper.createArrayNode();
            messages.add(createMessage("system", "You are a B1 German tutor. Provide grammatical feedback."));
            messages.add(createMessage("user", inputText));
            payload.set("messages", messages);

            return executeRequest(payload);
        } catch (Exception e) {
            LOGGER.error("Error fetching feedback from OpenAI API.", e);
            return "Error fetching feedback. Please try again.";
        }
    }

    private String encodeImageToBase64(File imageFile) throws Exception {
        byte[] fileContent = Files.readAllBytes(imageFile.toPath());
        return Base64.getEncoder().encodeToString(fileContent);
    }
}
