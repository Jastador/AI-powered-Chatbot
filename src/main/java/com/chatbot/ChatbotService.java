package com.chatbot;

import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Service
public class ChatbotService {

    private static final String API_URL = "https://api.openai.com/v1/chat/completions";
    private static final String API_KEY = System.getenv("OPENAI_API_KEY");
    {
        System.out.println("API Key: " + API_KEY); // Debugging line
    }

    private final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build();

    public String getResponse(String userInput) throws IOException {
        // Log the user input
        System.out.println("User Input: " + userInput);

        // Prepare the JSON payload
        JSONObject jsonPayload = new JSONObject();
        jsonPayload.put("model", "gpt-3.5-turbo");
        jsonPayload.put("messages", new JSONArray().put(new JSONObject().put("role", "user").put("content", userInput)));
        jsonPayload.put("max_tokens", 50);
        jsonPayload.put("temperature", 0.7);

        // Build the request body
        RequestBody body = RequestBody.create(
                MediaType.parse("application/json"),
                jsonPayload.toString()
        );

        // Build the request
        Request request = new Request.Builder()
                .url(API_URL)
                .addHeader("Authorization", "Bearer " + API_KEY)
                .post(body)
                .build();

        // Send the request to get the response
        try (Response response = client.newCall(request).execute()) {
            // Log response code
            System.out.println("Response Code: " + response.code());

            // Log the exact response body for debugging
            if (response.body() != null) {
                String responseBody = response.body().string();
                System.out.println("Response Body: " + responseBody);

                if (!response.isSuccessful()) {
                    return "Error: " + response.code() + " - " + responseBody; // Return detailed error
                }

                // Clean up the response text to extract the generated text
                return cleanResponse(responseBody);
            } else {
                System.out.println("Response Body: null");
                return "Error: Response body is null";
            }
        } catch (IOException e) {
            // Handle any IO exceptions
            System.out.println("Error during API call: " + e.getMessage());
            throw new IOException("Error during API call: " + e.getMessage(), e);
        }
    }

    private String cleanResponse(String response) {
        try {
            // Parse the response as a JSON object
            JSONObject jsonResponse = new JSONObject(response);

            // Extract the generated text from the "choices" array and "message" object
            return jsonResponse.getJSONArray("choices")
                               .getJSONObject(0)
                               .getJSONObject("message")
                               .getString("content")
                               .trim();
        } catch (Exception e) {
            // Handle errors if parsing fails
            System.out.println("Error parsing response: " + e.getMessage());
            return "Error parsing response.";
        }
    }
}
