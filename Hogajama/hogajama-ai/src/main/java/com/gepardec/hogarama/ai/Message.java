package com.gepardec.hogarama.ai;

public class Message {
    private Role role;
    private String content;

    public Message(Role role, String content) {
        this.role = role;
        this.content = content;
    }

    public Message() {
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public static Message createUserMessage(String content) {
        return new Message(Role.USER, content);
    }

    public static Message createAssistantMessage(String content) {
        return new Message(Role.ASSISTANT, content);
    }

    public static Message createSystemMessage(String content) {
        return new Message(Role.SYSTEM, content);
    }

    public static Message createFunctionMessage(String content) {
        return new Message(Role.FUNCTION, content);
    }

    @Override
    public String toString() {
        return "Message{" +
                "role=" + role +
                ", content='" + content + '\'' +
                '}';
    }
}