package com.gepardec.hogarama.domain.owner;

public interface OwnerService {

    boolean isRegistered(String ssoUserId);

    void register(String ssoUserId);
}
