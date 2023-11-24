package com.gepardec.hogarama.ai;

import com.gepardec.hogarama.ai.openai.OpenaiFunction;
import com.gepardec.hogarama.ai.openai.OpenaiService;
import com.gepardec.hogarama.domain.unitmanagement.context.DetermineUser;
import com.gepardec.hogarama.domain.unitmanagement.context.UserContext;
import com.gepardec.hogarama.domain.unitmanagement.entity.*;
import com.gepardec.hogarama.domain.unitmanagement.service.ActorService;
import com.gepardec.hogarama.domain.unitmanagement.service.RuleService;
import com.gepardec.hogarama.domain.unitmanagement.service.SensorService;
import com.gepardec.hogarama.domain.unitmanagement.service.UnitService;
import com.gepardec.hogarama.rest.unitmanagement.dto.RuleDto;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

import java.util.*;


@RequestScoped
@DetermineUser
public class ChatService {

    public static final String OPERATION_CHANGE_RULE = "change_rule";
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
        List<OpenaiFunction> functions = createFunctionDefinitions();
        return prepareActions(openaiService.chat(dialog, functions));
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
            rules.append("id\tname\tdescription\tsensorId\tactorId\tunitId\twaterDuration (in seconds)\tlowWater (the sensor value, when the actor should be activated)");
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

    private List<OpenaiFunction> createFunctionDefinitions() {
        List<OpenaiFunction> functions = new ArrayList<>();
        OpenaiFunction function = new OpenaiFunction();
        function.setName(OpenaiFunction.FUNCTION_CHANGE_RULE);
        function.setDescription("Performs action requested by the user if the user wants to modify a single existing rule.");
        function.setParameters(JsonParser.parseJsonToMap(readFile("change_rule.json")));
        functions.add(function);
        return functions;
    }

    private Dialog prepareActions(Dialog dialog) {
        if (dialog.getLastMessage().getAction() != null && OPERATION_CHANGE_RULE.equals(dialog.getLastMessage().getAction().getOperation())) {
            Message msg = dialog.getLastMessage();
            String parametersJson = msg.getContent();

            Map<String, Object> stringObjectMap = JsonParser.parseJsonToMap(parametersJson);

            long ruleId = (Integer) stringObjectMap.get("ruleId");
            String ruleName = (String) stringObjectMap.get("name");
            String messageToUser = (String) stringObjectMap.get("messageToUser");
            String ruleDescription = (String) stringObjectMap.get("description");
            int duration = (Integer) stringObjectMap.get("duration");
            double lowWater = (Double) stringObjectMap.get("lowWater");
            long sensorId = (Integer) stringObjectMap.get("sensorId");
            long actorId = (Integer) stringObjectMap.get("actorId");

            Long unitId;
            try {
                unitId = ruleService.getRuleById(ruleId).getUnit().getId();
            } catch (EntityNotFoundException e) {
                msg.setAction(null);
                msg.setContent(e.getMessage());
                return dialog;
            }

            //extend user message with the key-values, one per line
            messageToUser += String.format("%n%nChanged rule:%nruleId: %s%nname: %s%ndescription: %s%nduration: %s%nlowWater: %s%nsensorId: %s%nactorId: %s%n", ruleId, ruleName, ruleDescription, duration, lowWater, sensorId, actorId);

            Action action = new Action();
            action.setAbortReply("You have aborted the action.");
            action.setConfirmReply("You have confirmed the action.");
            action.setMessageToUser(messageToUser);
            action.setOperation(OPERATION_CHANGE_RULE);
            RuleDto dto = new RuleDto();
            dto.setId(ruleId);
            dto.setName(ruleName);
            dto.setDescription(ruleDescription);
            dto.setActorId(actorId);
            dto.setLowWater(lowWater);
            dto.setSensorId(sensorId);
            dto.setUnitId(unitId);
            dto.setWaterDuration(duration);
            action.setDto(dto);
            msg.setAction(action);
            msg.setContent(null);

            return dialog;
        }

        return dialog;
    }
}
