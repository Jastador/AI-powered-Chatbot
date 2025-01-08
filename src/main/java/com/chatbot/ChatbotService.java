package com.chatbot;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class ChatbotService {

    private static final String API_URL = "https://api.openai.com/v1/chat/completions";
    private static final String API_KEY = System.getenv("OPENAI_API_KEY");

    public String getResponse(String userInput) throws IOException {
        // Log the user input
        System.out.println("User Input: " + userInput);

        // Step 1: Prepare the JSON payload for the request
        JSONObject jsonPayload = new JSONObject();
        jsonPayload.put("model", "gpt-3.5-turbo");

        // Use a system message to guide the chatbot to respond casually
        JSONArray messages = new JSONArray();
        messages.put(new JSONObject().put("role", "system").put("content", "You are a friendly, casual chatbot."));
        messages.put(new JSONObject().put("role", "user").put("content", userInput));

        jsonPayload.put("messages", messages);
        jsonPayload.put("max_tokens", 50);
        jsonPayload.put("temperature", 0.7);

        // Step 2: Create the request and send it
        String jsonBody = jsonPayload.toString();
        URL url = new URL(API_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", "Bearer " + API_KEY);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonBody.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        // Step 3: Get the response code and read the response
        int responseCode = connection.getResponseCode();
        System.out.println("Response Code: " + responseCode);

        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            String apiResponse = response.toString();

            // Step 4: Log the raw API response
            System.out.println("Raw API Response: " + apiResponse);

            // Step 5: Check if the response is successful and clean it
            if (responseCode == HttpURLConnection.HTTP_OK) {
                String cleanedMessage = cleanResponse(apiResponse);
                System.out.println("Cleaned API Response: " + cleanedMessage);
                return cleanedMessage;
            } else {
                System.out.println("Error: Received non-OK response code");
                return "Oops! Something went wrong. Please try again later.";
            }
        }
    }

    private String cleanResponse(String response) {
        try {
            // Parse the response as a JSON object
            JSONObject jsonResponse = new JSONObject(response);

            // Extract the message content
            String message = jsonResponse.getJSONArray("choices")
                                         .getJSONObject(0)  // Get the first choice
                                         .getJSONObject("message")  // Get the message object
                                         .getString("content")  // Get the content
                                         .trim();  // Remove any surrounding whitespace

            // Check if the response contains the word "Error"
            if (message.toLowerCase().contains("error")) {
                return "Oops! Something went wrong. Please try again later."; // Custom fallback message
            }

            // Log the cleaned message
            System.out.println("Cleaned Message: " + message);

            // Return the cleaned message
            return message;
        } catch (Exception e) {
            // Log the error and return a fallback message if parsing fails
            System.out.println("Error parsing response: " + e.getMessage());
            return "Oops! Something went wrong. Please try again later.";
        }
    }
}
