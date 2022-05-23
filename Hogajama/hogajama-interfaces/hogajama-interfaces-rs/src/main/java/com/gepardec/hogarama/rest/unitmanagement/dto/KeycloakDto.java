package com.gepardec.hogarama.rest.unitmanagement.dto;

public class KeycloakDto {

    private String authServerUrl;
    private String realm;
    private String clientId;
    private String credentialsSecret;

    public String getAuthServerUrl() {
        return authServerUrl;
    }

    public void setAuthServerUrl(String authServerUrl) {
        this.authServerUrl = authServerUrl;
    }

    public String getRealm() {
        return realm;
    }

    public void setRealm(String realm) {
        this.realm = realm;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getCredentialsSecret() {
        return credentialsSecret;
    }

    public void setCredentialsSecret(String credentialsSecret) {
        this.credentialsSecret = credentialsSecret;
    }
}
