package com.gepardec.hogarama.ai.openai;

public class OpenaiFunction {
    public static final String FUNCTION_CHANGE_RULE = "change_rule";
    private String name;
    private String description;
    private Object parameters;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Object getParameters() {
        return parameters;
    }

    public void setParameters(Object parameters) {
        this.parameters = parameters;
    }
}
