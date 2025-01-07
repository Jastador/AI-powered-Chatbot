package com.chatbot;

import com.model.ChatbotRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chatbot")  //URL for the Controller
public class ChatbotController 
{
    private final ChatbotService chatbotService;

    // Dependency injection to automatically provide the needed objects
    @Autowired
    public ChatbotController(ChatbotService chatbotService) 
    {
        this.chatbotService = chatbotService;
    }

    // POST request to handle user queries
    @PostMapping("/ask")
    public ResponseEntity<String> askQuestion(@RequestBody ChatbotRequest request)
    {
        String userInput = request.getUserInput(); 

        if (userInput == null || userInput.trim().isEmpty())
        {
            return ResponseEntity.badRequest().body("That's a question? My brain is doing cartwheels trying to process it!");
        }

        try 
        {
            
            // Service tries to process user's input here
            String response = chatbotService.getResponse(userInput);
            if (response == null || response.isEmpty()) 
            {
                return ResponseEntity.ok("My brain must be on airplane mode. Could you rephrase that?");
            }

            // Returns the response as JSON
            return ResponseEntity.ok(response);
            
        } 
        
        catch (Exception e)      
        {
            // Handle any unexpected errors
            return ResponseEntity.status(500).body("Error 2025! Request too mysterious for me to handle. Please resend: " + e.getMessage());
        }
    }
}
