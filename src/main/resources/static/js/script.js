function sendMessage() {
    const userInput = document.getElementById('userInput').value;

    if (!userInput.trim()) {
        return;
    }

    // Display the user message
    displayMessage(userInput, 'user');

    // Send request to Spring Boot API
    fetch('http://localhost:8080/chatbot/ask', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ userInput: userInput }),
    })
    .then(response => response.text())  // Handle response as text
    .then(data => {
        // Display the bot response directly as it's plain text
        displayMessage(data, 'bot');
    })
    .catch(error => {
        console.error('Error:', error);
        displayMessage("Error communicating with the chatbot.", 'bot');
    });

    // Clear input field
    document.getElementById('userInput').value = '';
}

function displayMessage(message, sender) {
    const messageDiv = document.createElement('div');
    messageDiv.classList.add('message');
    messageDiv.classList.add(sender === 'user' ? 'user-message' : 'bot-message');
    messageDiv.innerText = sender === 'user' ? `You: ${message}` : `Bot: ${message}`;

    document.getElementById('messages').appendChild(messageDiv);

    // Scroll to the bottom
    document.getElementById('messages').scrollTop = document.getElementById('messages').scrollHeight;
}

function handleKeyPress(event) {
    // Check if the Enter key was pressed (keyCode 13)
    if (event.key === 'Enter') {
        sendMessage();
    }
}
