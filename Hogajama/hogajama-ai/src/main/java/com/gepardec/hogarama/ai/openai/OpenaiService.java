package com.gepardec.hogarama.ai.openai;

import com.azure.ai.openai.OpenAIClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.ai.openai.models.*;
import com.azure.core.credential.KeyCredential;
import com.gepardec.hogarama.ai.Dialog;
import com.gepardec.hogarama.ai.Message;
import com.gepardec.hogarama.ai.Role;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.bean.RequestScoped;
import java.util.Optional;
import java.util.UUID;

@RequestScoped
public class OpenaiService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OpenaiService.class);

    private static final String OPEN_AI_API_KEY = Optional.ofNullable(System.getenv("OPENAI_API_KEY")).orElse(System.getProperty("openai.api.key"));
    private static final String MODEL_ID = "gpt-3.5-turbo";

    public Dialog chat(Dialog dialog) {
        OpenAIClient client = new OpenAIClientBuilder()
                .credential(new KeyCredential(OPEN_AI_API_KEY))
                .buildClient();

        ChatCompletionsOptions chatCompletionsOptions = new ChatCompletionsOptions(dialog.toOpenaiMessages());

        try {
            LOGGER.debug("Sending request to OpenAI");
            long ts = System.currentTimeMillis();
            ChatCompletions chatCompletions = client.getChatCompletions(MODEL_ID, chatCompletionsOptions);
            ts = System.currentTimeMillis() - ts;
            LOGGER.debug(String.format("Model ID=%s is created at %s.%n", chatCompletions.getId(), chatCompletions.getCreatedAt()));
            if (chatCompletions.getChoices().isEmpty()) {
                dialog.addMessage(Message.createAssistantMessage("Something went wrong. OpenAI has returned empty result"));
                return dialog;
            }
            ChatChoice choice = chatCompletions.getChoices().get(0);
            ChatMessage message = choice.getMessage();
            LOGGER.debug(String.format("Index: %d, Chat Role: %s.%n", choice.getIndex(), message.getRole()));
            LOGGER.debug("Message:" + message.getContent());

            CompletionsUsage usage = chatCompletions.getUsage();
            LOGGER.debug(String.format("Usage: number of prompt token is %d, "
                            + "number of completion token is %d, and number of total tokens in request and response is %d.%n",
                    usage.getPromptTokens(), usage.getCompletionTokens(), usage.getTotalTokens()));
            LOGGER.debug(String.format("Required time: %d ms", ts));
            dialog.addMessage(new Message(Role.byValue(message.getRole().toString().toLowerCase()), message.getContent()));
            return dialog;
        } catch (Exception e) {
            String id = UUID.randomUUID().toString();
            LOGGER.error("Error at communication with openai " + id + ": " + ExceptionUtils.getStackTrace(e));
            dialog.addMessage(Message.createAssistantMessage("Something went wrong. Ask your admin, what was the problem. This is the reference id: " + id));
            return dialog;
        }

    }

}
