package com.model;

public class ChatbotRequest 
{
    private String userInput;

    // Constructor
    public ChatbotRequest() {}

    public ChatbotRequest(String userInput) 
    {
        this.userInput = userInput;
    }

    // Getter and Setter
    public String getUserInput() 
    {
        return userInput;
    }

    public void setUserInput(String userInput)
    {
        this.userInput = userInput;
    }
}
