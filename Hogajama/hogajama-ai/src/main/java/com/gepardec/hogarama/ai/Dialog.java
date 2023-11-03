package com.gepardec.hogarama.ai;

import com.azure.ai.openai.models.ChatMessage;
import com.azure.ai.openai.models.ChatRole;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Dialog {
    private final List<Message> messages;
    private Message systemMessage;

    public Dialog(String systemMessage) {
        this.systemMessage = Message.createSystemMessage(systemMessage);
        this.messages = new ArrayList<>();
    }

    public void addMessage(Message message) {
        if (message.getRole() == Role.SYSTEM) {
            systemMessage = message;  // Update the system message if the role is SYSTEM
        }
        messages.add(message);
    }

    public void addMessages(Message... messages) {
        for (Message message : messages) {
            addMessage(message);  // Delegate to addMessage to handle each message individually
        }
    }

    public Message getLastMessage() {
        if (messages.isEmpty()) {
            throw new IllegalStateException("No messages available.");
        }
        return messages.get(messages.size() - 1);
    }

    public List<Message> getMessages() {
        return messages;
    }

    public Message getSystemMessage() {
        return systemMessage;
    }

    @Override
    public String toString() {
        return "Dialog{" +
                "messages=" + messages +
                ", systemMessage=" + systemMessage +
                '}';
    }

    public List<ChatMessage> toOpenaiMessages() {
        List<ChatMessage> result = new ArrayList<>();
        result.add(new ChatMessage(ChatRole.SYSTEM, systemMessage.getContent()));
        result.addAll(messages.stream().map(message -> new ChatMessage(ChatRole.fromString(message.getRole().getValue()), message.getContent())).collect(Collectors.toList()));
        return result;
    }
}
