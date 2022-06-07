package com.gepardec.hogarama.rest.unitmanagement.dto;

public class KeycloakDto {

    private String authServerUrl;
    private String realm;
    private String clientIdFrontend;

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

    public String getClientIdFrontend() {
        return clientIdFrontend;
    }

    public void setClientIdFrontend(String clientIdFrontend) {
        this.clientIdFrontend = clientIdFrontend;
    }
}
