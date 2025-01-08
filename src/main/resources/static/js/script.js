function sendMessage() {
    const userInputField = document.getElementById('userInput');
    const messagesContainer = document.getElementById('messages');
    const userInput = userInputField.value.trim();

    if (!userInput) {
        return;
    }

    // Display the user message
    displayMessage(userInput, 'user');

    // Display typing indicator for bot
    if (!document.querySelector('.typing-indicator')) {
        const typingIndicator = document.createElement('div');
        typingIndicator.classList.add('message', 'bot-message', 'typing-indicator');
        typingIndicator.innerText = "Bot is typing...";
        messagesContainer.appendChild(typingIndicator);
    }

    // Send request to Spring Boot API
    fetch('/chatbot/ask', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ userInput }),
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Failed to fetch bot response');
        }
        return response.text(); // Adjust to response.json() if necessary
    })
    .then(data => {
        const typingIndicator = document.querySelector('.typing-indicator');
        if (typingIndicator) {
            messagesContainer.removeChild(typingIndicator);
        }
        displayMessage(data, 'bot');
    })
    .catch(error => {
        console.error('Error:', error);
        displayMessage("Error communicating with the chatbot.", 'bot');
    });

    // Clear input field and focus back to it
    userInputField.value = '';
    userInputField.focus();
}

function displayMessage(message, sender) {
    const messagesContainer = document.getElementById('messages');
    const messageDiv = document.createElement('div');
    messageDiv.classList.add('message', sender === 'user' ? 'user-message' : 'bot-message');
    messageDiv.innerText = sender === 'user' ? `You: ${message}` : `Bot: ${message}`;

    messagesContainer.appendChild(messageDiv);

    // Scroll to the bottom
    messagesContainer.scrollTop = messagesContainer.scrollHeight;
}

function handleKeyPress(event) {
    if (event.key === 'Enter') {
        sendMessage();
    }
}
