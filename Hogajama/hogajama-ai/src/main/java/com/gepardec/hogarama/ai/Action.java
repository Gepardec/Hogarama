package com.gepardec.hogarama.ai;

public class Action {
    private String text;
    private String confirmReply;
    private String abortReply;
    private String operation;
    private Object dto;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getConfirmReply() {
        return confirmReply;
    }

    public void setConfirmReply(String confirmReply) {
        this.confirmReply = confirmReply;
    }

    public String getAbortReply() {
        return abortReply;
    }

    public void setAbortReply(String abortReply) {
        this.abortReply = abortReply;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public Object getDto() {
        return dto;
    }

    public void setDto(Object dto) {
        this.dto = dto;
    }
}
