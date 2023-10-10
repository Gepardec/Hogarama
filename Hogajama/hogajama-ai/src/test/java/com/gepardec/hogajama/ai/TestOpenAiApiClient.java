package com.gepardec.hogajama.ai;

import com.azure.ai.openai.OpenAIClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.ai.openai.models.*;
import com.azure.core.credential.KeyCredential;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TestOpenAiApiClient {

    String openAiApiKey = "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";
    String modelId = "gpt-3.5-turbo";

    OpenAIClient client = new OpenAIClientBuilder()
                .credential(new KeyCredential(openAiApiKey))
                .buildClient();

    @Test
    public void helloWorldTest(){
        String systemMessage = "You are the default chatbot that you are.";
        String userPrompt = "Can you say \"Hello World!\"";

        List<ChatMessage> chatMessages = new ArrayList<>();
        chatMessages.add(new ChatMessage(ChatRole.SYSTEM, systemMessage));
        chatMessages.add(new ChatMessage(ChatRole.USER, userPrompt));

        ChatCompletions chatCompletions = client.getChatCompletions(modelId, new ChatCompletionsOptions(chatMessages));


        System.out.printf("Model ID=%s is created at %s.%n", chatCompletions.getId(), chatCompletions.getCreatedAt());
        for (ChatChoice choice : chatCompletions.getChoices()) {
            ChatMessage message = choice.getMessage();
            System.out.printf("Index: %d, Chat Role: %s.%n", choice.getIndex(), message.getRole());
            System.out.println("Message:");
            System.out.println(message.getContent());
        }

        System.out.println();
        CompletionsUsage usage = chatCompletions.getUsage();
        System.out.printf("Usage: number of prompt token is %d, "
                        + "number of completion token is %d, and number of total tokens in request and response is %d.%n",
                usage.getPromptTokens(), usage.getCompletionTokens(), usage.getTotalTokens());
    }

    @Test
    public void systemPromptTest(){
        String systemMessage = "You are a chatbot integrated with an automatic watering system.\n" +
                "\n" +
                "- User Context:\n" +
                "    The logged-in user is Erhard\n" +
                "\n" +
                "- User's Sensors (Moisture measurement for plants):\n" +
                "id;name;deviceId;unitId;sensorTypeId\n" +
                "1;Pflanze Wien;verspielterGepard;5;6\n" +
                "2;Kleiner Ahorn;s_gb01;6;6\n" +
                "3;Langer Ahorn;s_gb02;6;6\n" +
                "4;Slim Ash;s_gb03;6;6\n" +
                "5;Sequoia;s_gb04;6;6\n" +
                "\n" +
                "- User's Actors (Water pumps controlled by rules):\n" +
                "id;name;deviceId;unitId;queueName\n" +
                "1;Pumpe Büro Wien;GruenerGepard;5;\n" +
                "2;Pumpe Gruenbach;a_gb01;6;\n" +
                "\n" +
                "- User's Watering Rules (Watering logic configurations):\n" +
                "id;name;description;sensorId;actorId;unitId;waterDuration (in ms);lowWater\n" +
                "1;Wien;Gepardec Büro Wien;1;1;5;20;0.6\n" +
                "2;Gruenbach;Bewässerung Grünbach;3;2;6;600;0.55\n" +
                "\n" +
                "- UnitIds (unitId has nothing to do with the \"id\")\n" +
                "unitId 5 = Wien\n" +
                "unitId 6 = Grünbach\n" +
                "\n" +
                "**Instructions:**\n" +
                "1. When answering, always refer to items by their name\n" +
                "2. never show the id of a sensor, actor or rule and always refuse changes to the \"id\"\n" +
                "3. If the values are given in ms, always ensure you convert the provided value from millisecons (1/1000 seconds) to seconds by dividing it by 1000. For instance, a waterDuration of 600 milliseconds in the database should be presented as 0.6 seconds in your responses.\n" +
                "4. Erhard is only interested in units located in Grünbach. Don't show him sensors, actors or rules in Wien unless you are directly asked about it.\n" +
                "5. Always before making a change on the data, show how the whole rule would look and ask for confirmation to make the change. If you are asked to make other changes you have to confirm them too.\n" +
                "6. Output all Data in a List in the form of <descirption>: <value>\n" +
                "\n" +
                "Address the user's questions based on the given context.";

        String userPrompt = "list my rules";

        List<ChatMessage> chatMessages = new ArrayList<>();
        chatMessages.add(new ChatMessage(ChatRole.SYSTEM, systemMessage));
        chatMessages.add(new ChatMessage(ChatRole.USER, userPrompt));

        ChatCompletions chatCompletions = client.getChatCompletions(modelId, new ChatCompletionsOptions(chatMessages));


        System.out.printf("Model ID=%s is created at %s.%n", chatCompletions.getId(), chatCompletions.getCreatedAt());
        for (ChatChoice choice : chatCompletions.getChoices()) {
            ChatMessage message = choice.getMessage();
            System.out.printf("Index: %d, Chat Role: %s.%n", choice.getIndex(), message.getRole());
            System.out.println("Message:");
            System.out.println(message.getContent());
        }

        System.out.println();
        CompletionsUsage usage = chatCompletions.getUsage();
        System.out.printf("Usage: number of prompt token is %d, "
                        + "number of completion token is %d, and number of total tokens in request and response is %d.%n",
                usage.getPromptTokens(), usage.getCompletionTokens(), usage.getTotalTokens());
    }
}
