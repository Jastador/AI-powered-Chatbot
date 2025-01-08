package com.chatbot;

import com.model.ChatbotRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chatbot")  // URL for the Controller
public class ChatbotController {

    private final ChatbotService chatbotService;

    // Dependency injection to automatically provide the needed objects
    @Autowired
    public ChatbotController(ChatbotService chatbotService) {
        this.chatbotService = chatbotService;
    }

    // POST request to handle user queries
    @PostMapping("/ask")
    public ResponseEntity<String> askQuestion(@RequestBody ChatbotRequest request) {
        // Extract user input from the request body
        String userInput = request.getUserInput();

        // If the user input is empty, return a bad request response
        if (userInput == null || userInput.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("That's a question? My brain is doing cartwheels trying to process it!");
        }

        try {
            // Service processes the user's input and gets a response
            String response = chatbotService.getResponse(userInput);
            if (response == null || response.isEmpty()) {
                return ResponseEntity.ok("My brain must be on airplane mode. Could you rephrase that?");
            }

            // Return the chatbot response as JSON
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // Handle any unexpected errors
            return ResponseEntity.status(500).body("Error 2025! Request too mysterious for me to handle. Please resend: " + e.getMessage());
        }
    }
}
