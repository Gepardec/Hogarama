package com.gepardec.hogarama.domain.unitmanagement.entity;

import com.gepardec.hogarama.domain.exception.TechnicalException;

import java.util.stream.Collectors;

public interface Owned {

    public Unit getUnit();

    public default void verifyIsOwned(Owner owner) {
        if (!owner.getUnitList().stream().map(Unit::getId).collect(Collectors.toSet()).contains(getUnit().getId())) {
            throw new TechnicalException("Unit doesn't belong to owner");
        }
    }


}
