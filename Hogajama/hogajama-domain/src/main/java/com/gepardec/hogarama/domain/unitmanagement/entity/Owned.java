package com.gepardec.hogarama.domain.unitmanagement.entity;

import com.gepardec.hogarama.domain.exception.TechnicalException;

import java.util.stream.Collectors;

public interface Owned {

    Unit getUnit();

    default void verifyIsOwned(User user) {
        if (!user.equals(getUnit().getUser())) {
            throw new TechnicalException("Unit '"+getUnit()+"' doesn't belong to user '"+user.getId()+"'");
        }
    }

}
