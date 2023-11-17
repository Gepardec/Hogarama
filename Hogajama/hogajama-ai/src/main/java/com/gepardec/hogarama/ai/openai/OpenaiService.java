package com.gepardec.hogarama.ai.openai;

import com.azure.ai.openai.OpenAIClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.ai.openai.models.*;
import com.azure.core.credential.KeyCredential;
import com.gepardec.hogarama.ai.*;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.bean.RequestScoped;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RequestScoped
public class OpenaiService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OpenaiService.class);

    private static final String OPEN_AI_API_KEY = Optional.ofNullable(System.getenv("OPENAI_API_KEY")).orElse(System.getProperty("openai.api.key"));
    private static final String MODEL_ID = "gpt-4-1106-preview";

    public Dialog chat(Dialog dialog, List<Function> functions) {
        OpenAIClient client = new OpenAIClientBuilder()
                .credential(new KeyCredential(OPEN_AI_API_KEY))
                .buildClient();

        ChatCompletionsOptions chatCompletionsOptions = new ChatCompletionsOptions(dialog.toOpenaiMessages());
        chatCompletionsOptions.setTemperature(0.1);
        if (functions != null) {
            List<FunctionDefinition> functionDefinitions = functions.stream().map(function -> {
                FunctionDefinition functionDefinition = new FunctionDefinition(function.getName());
                functionDefinition.setDescription(function.getDescription());
                functionDefinition.setParameters(function.getParameters());
                return functionDefinition;
            }).collect(Collectors.toList());
            chatCompletionsOptions.setFunctions(functionDefinitions);
        }

        try {
            LOGGER.debug("Sending request to OpenAI : " + dialog);
            long ts = System.currentTimeMillis();
            ChatCompletions chatCompletions = client.getChatCompletions(MODEL_ID, chatCompletionsOptions);
            ts = System.currentTimeMillis() - ts;
            LOGGER.debug(String.format("Model ID=%s is created at %s.", chatCompletions.getId(), chatCompletions.getCreatedAt()));
            if (chatCompletions.getChoices().isEmpty()) {
                dialog.addMessage(Message.createAssistantMessage("Something went wrong. OpenAI has returned empty result"));
                return dialog;
            }
            return processChatCompletions(dialog, chatCompletions, ts);
        } catch (Exception e) {
            String id = UUID.randomUUID().toString();
            LOGGER.error("Error at communication with openai " + id + ": " + ExceptionUtils.getStackTrace(e));
            dialog.addMessage(Message.createAssistantMessage("Something went wrong. Ask your admin, what was the problem. This is the reference id: " + id));
            return dialog;
        }
    }

    private static Dialog processChatCompletions(Dialog dialog, ChatCompletions chatCompletions, long ts) {
        ChatChoice choice = chatCompletions.getChoices().get(0);
        if (CompletionsFinishReason.FUNCTION_CALL.equals(choice.getFinishReason())) {
            String name = choice.getMessage().getFunctionCall().getName();
            String args = choice.getMessage().getFunctionCall().getArguments();
            LOGGER.debug("Function call: " + name + " with args: " + args);
            dialog.addMessage(Message.createAssistantMessage(args));
            Message msg = dialog.getLastMessage();
            Action action = new Action();
            action.setOperation(name);
            msg.setAction(action);
        } else {
            ChatMessage message = choice.getMessage();
            LOGGER.debug("Message:" + message.getContent());
            dialog.addMessage(new Message(Role.byValue(message.getRole().toString().toLowerCase()), message.getContent()));
        }

        CompletionsUsage usage = chatCompletions.getUsage();
        LOGGER.debug(String.format("Usage: number of prompt token is %d, "
                        + "number of completion token is %d, and number of total tokens in request and response is %d.%n",
                usage.getPromptTokens(), usage.getCompletionTokens(), usage.getTotalTokens()));
        LOGGER.debug(String.format("Required time: %d ms", ts));

        return dialog;
    }

}
