package com.gepardec.hogarama.domain.security;

public class AccessTokenDTO {

    private String accessToken;
    private int validityDuration;

    public int getValidityDuration() {

        return validityDuration;
    }

    public void setValidityDuration(int validityTime) {

        this.validityDuration = validityTime;
    }

    public String getAccessToken() {

        return accessToken;
    }

    public void setAccessToken(String accessToken) {

        this.accessToken = accessToken;
    }
}
