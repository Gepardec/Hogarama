package com.gepardec.hogarama.domain.unitmanagement.entity;

import com.gepardec.hogarama.domain.exception.TechnicalException;

import java.util.stream.Collectors;

public interface Owned {

    Unit getUnit();

    default void verifyIsOwned(User user) {
        if (!user.getUnitList().stream().map(Unit::getId).collect(Collectors.toSet()).contains(getUnit().getId())) {
            throw new TechnicalException("Unit doesn't belong to user");
        }
    }


}
