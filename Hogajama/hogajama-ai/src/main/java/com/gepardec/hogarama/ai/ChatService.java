package com.gepardec.hogarama.ai;

import com.gepardec.hogarama.ai.openai.OpenaiService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

import java.util.Objects;
import java.util.Scanner;


@RequestScoped
public class ChatService {

    @Inject
    private OpenaiService openaiService;

    public Dialog chat(Message... messages) {
        String systemMessage = readFile("system_prompt.txt");
        Dialog dialog = new Dialog(systemMessage);
        dialog.addMessages(messages);
        return openaiService.chat(dialog);
    }

    public static String readFile(String filename) {
        StringBuilder content = new StringBuilder();
        try (Scanner scanner = new Scanner(Objects.requireNonNull(ChatService.class.getResourceAsStream("/" + filename)))) {
            while (scanner.hasNextLine()) {
                content.append(scanner.nextLine()).append(System.lineSeparator());
            }
        }
        return content.toString();
    }

}
