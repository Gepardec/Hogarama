package com.gepardec.hogarama.ai;

import com.gepardec.hogarama.ai.openai.OpenaiService;
import com.gepardec.hogarama.domain.unitmanagement.context.DetermineUser;
import com.gepardec.hogarama.domain.unitmanagement.context.UserContext;
import com.gepardec.hogarama.domain.unitmanagement.entity.Actor;
import com.gepardec.hogarama.domain.unitmanagement.entity.LowWaterWateringRule;
import com.gepardec.hogarama.domain.unitmanagement.entity.Sensor;
import com.gepardec.hogarama.domain.unitmanagement.entity.Unit;
import com.gepardec.hogarama.domain.unitmanagement.service.ActorService;
import com.gepardec.hogarama.domain.unitmanagement.service.RuleService;
import com.gepardec.hogarama.domain.unitmanagement.service.SensorService;
import com.gepardec.hogarama.domain.unitmanagement.service.UnitService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Objects;
import java.util.Scanner;


@RequestScoped
@DetermineUser
public class ChatService {

    @Inject
    private OpenaiService openaiService;

    @Inject
    private UserContext userContext;

    @Inject
    private SensorService sensorService;

    @Inject
    private ActorService actorService;

    @Inject
    private UnitService unitService;

    @Inject
    private RuleService ruleService;

    public Dialog chat(Message... messages) {
        String systemMessage = buildSystemMessage();
        Dialog dialog = new Dialog(systemMessage);
        dialog.addMessages(messages);
        return openaiService.chat(dialog);
    }

    private String buildSystemMessage() {
        String text = readFile("system_prompt.txt");
        text = text.
                replace("${userName}", buildUsernameString()).
                replace("${sensorData}", buildSensorsString()).
                replace("${ruleData}", buildRulesString()).
                replace("${unitData}", buildUnitsString()).
                replace("${actorData}", buildActorsString());
        return text;
    }

    private String buildRulesString() {
        StringBuilder rules = new StringBuilder();
        List<LowWaterWateringRule> allRulesForUser = ruleService.getAllRulesForUser();
        if (allRulesForUser.isEmpty()) {
            rules.append("No rules found for this user.");
        } else {
            rules.append("id\tname\tdescription\tsensorId\tactorId\tunitId\twaterDuration (in ms)\tlowWater (the sensor value, when the actor should be activated)");
            for (LowWaterWateringRule lowWaterWateringRule : allRulesForUser) {
                rules.append(String.format("%n%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s", lowWaterWateringRule.getId(), lowWaterWateringRule.getName(), lowWaterWateringRule.getDescription(), lowWaterWateringRule.getSensor().getId(), lowWaterWateringRule.getActor().getId(), lowWaterWateringRule.getUnit().getId(), lowWaterWateringRule.getWaterDuration(), lowWaterWateringRule.getLowWater()));
            }
        }
        return rules.toString();
    }

    private String buildUsernameString() {
        return userContext.getUserProfile().getName();
    }

    private String buildSensorsString() {
        StringBuilder sensors = new StringBuilder();
        List<Sensor> allSensorsForUser = sensorService.getAllSensorsForUser();
        if (allSensorsForUser.isEmpty()) {
            sensors.append("No sensors found for this user.");
        } else {
            sensors.append("id\tname\tdeviceId\tunitId\tsensorTypeId");
            for (Sensor sensor : allSensorsForUser) {
                sensors.append(String.format("%n%s\t%s\t%s\t%s\t%s", sensor.getId(), sensor.getName(), sensor.getDeviceId(), sensor.getUnit().getId(), sensor.getSensorType().getId()));
            }
        }
        return sensors.toString();
    }

    private String buildActorsString() {
        StringBuilder actors = new StringBuilder();
        List<Actor> allActorsForUser = actorService.getAllActorsForUser();
        if (allActorsForUser.isEmpty()) {
            actors.append("No actors found for this user.");
        } else {
            actors.append("id\tname\tdeviceId\tunitId\tqueueName");
            for (Actor actor : allActorsForUser) {
                actors.append(String.format("%n%s\t%s\t%s\t%s\t%s", actor.getId(), actor.getName(), actor.getDeviceId(), actor.getUnit().getId(), actor.getQueueName()));
            }
        }
        return actors.toString();
    }

    private String buildUnitsString() {
        StringBuilder units = new StringBuilder();
        List<Unit> unitList = unitService.getUnitsForUser();
        if (unitList.isEmpty()) {
            units.append("No units found for this user.");
        } else {
            units.append("unitId\tunitName\tunitDescription");
            for (Unit unit : unitList) {
                units.append(String.format("%n%s\t%s\t%s", unit.getId(), unit.getName(), unit.getDescription()));
            }
        }
        return units.toString();
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
