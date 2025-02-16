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

    /**
     * Processes a text-based input and provides grammatical feedback.
     */
    public String getFeedback(String inputText) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost request = new HttpPost(openAIConfig.getApiUrl());
            request.setHeader("Authorization", "Bearer " + openAIConfig.getApiKey());
            request.setHeader("Content-Type", "application/json; charset=UTF-8");

            // Create JSON payload
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

            String jsonPayload = objectMapper.writeValueAsString(payload);
            request.setEntity(new StringEntity(jsonPayload, ContentType.APPLICATION_JSON.withCharset("UTF-8")));

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                JsonNode jsonResponse = objectMapper.readTree(response.getEntity().getContent());
                return jsonResponse.get("choices").get(0).get("message").get("content").asText();
            }
        } catch (Exception e) {
            LOGGER.error("Error fetching feedback from OpenAI API.", e);
            return "Error fetching feedback. Please try again.";
        }
    }

    /**
     * Processes an image, extracts handwritten text, and provides feedback.
     */
    public String getFeedbackFromImage(File imageFile) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost request = new HttpPost(openAIConfig.getApiUrl());
            request.setHeader("Authorization", "Bearer " + openAIConfig.getApiKey());
            request.setHeader("Content-Type", "application/json");  // Ensure JSON content type is set

            // Convert image to base64
            String base64Image = encodeImageToBase64(imageFile);

            // Create JSON payload
            ObjectNode payload = objectMapper.createObjectNode();
            payload.put("model", "gpt-4o"); // Ensure correct model that supports vision

            ArrayNode messages = objectMapper.createArrayNode();
            messages.add(objectMapper.createObjectNode()
                    .put("role", "system")
                    .put("content", "You are a B1 German tutor. Extract handwritten text from the image and provide grammatical feedback."));

            // Attach image to the user message
            ObjectNode userMessage = objectMapper.createObjectNode();
            userMessage.put("role", "user");

            ArrayNode contentArray = objectMapper.createArrayNode();
            contentArray.add(objectMapper.createObjectNode().put("type", "text").put("text", "Extract text and provide grammatical feedback."));

            ObjectNode imageObject = objectMapper.createObjectNode();
            imageObject.put("url", "data:image/jpeg;base64," + base64Image);

            ObjectNode imageContent = objectMapper.createObjectNode();
            imageContent.put("type", "image_url");
            imageContent.set("image_url", imageObject);  // Image URL should be an object

            contentArray.add(imageContent);
            userMessage.set("content", contentArray);

            messages.add(userMessage);
            payload.set("messages", messages);

            // Convert payload to JSON string
            String jsonPayload = objectMapper.writeValueAsString(payload);
            request.setEntity(new StringEntity(jsonPayload, ContentType.APPLICATION_JSON.withCharset("UTF-8")));

            System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(payload));

            // Execute request
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                JsonNode jsonResponse = objectMapper.readTree(response.getEntity().getContent());
                return jsonResponse.get("choices").get(0).get("message").get("content").asText();
            }
        } catch (Exception e) {
            LOGGER.error("Error processing image through OpenAI API.", e);
            return "Error processing image. Please try again.";
        }
    }


    /**
     * Converts an image file to a Base64 string.
     */
    private String encodeImageToBase64(File imageFile) throws Exception {
        byte[] fileContent = Files.readAllBytes(imageFile.toPath());
        return Base64.getEncoder().encodeToString(fileContent);
    }
}
