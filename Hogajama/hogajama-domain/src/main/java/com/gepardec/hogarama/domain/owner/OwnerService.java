package com.gepardec.hogarama.domain.owner;

import com.gepardec.hogarama.domain.entity.Owner;

import java.util.Optional;

public interface OwnerService {

    Optional<Owner> getRegisteredOwner(String ssoUserId);

    Owner register(String ssoUserId);
}
