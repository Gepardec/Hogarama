package com.gepardec.hogarama.domain.unitmanagement.entity;

import com.gepardec.hogarama.domain.exception.TechnicalException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class OwnerTest {

    @Test
    public void addUnit() {
        Owner owner = new Owner();
        Unit unit = new Unit();

        owner.addUnit(unit);

        assertThat(owner.getUnitList()).containsExactly(unit);
    }

    @Test
    public void addUnit_NullInput_ExceptionExpected() {
        Owner owner = new Owner();
        Assertions.assertThrows(NullPointerException.class, () -> owner.addUnit(null));

    }

    @Test
    public void getDefaultUnit_EmptyUnitList() {
        Owner owner = new Owner();
        Assertions.assertThrows(NullPointerException.class, owner::getDefaultUnit);

    }

    @Test
    public void getDefaultUnit_NoDefaultUnitPresent() {
        Owner owner = new Owner();
        owner.addUnit(newNonDefaultUnit());
        Assertions.assertThrows(TechnicalException.class, owner::getDefaultUnit);
    }

    @Test
    public void getDefaultUnit() {
        Owner owner = new Owner();

        // add default unit
        Unit defaultUnit = Unit.createDefault(owner);
        owner.addUnit(defaultUnit);
        // add non default unit
        owner.addUnit(newNonDefaultUnit());

        Unit result = owner.getDefaultUnit();

        assertThat(result).isEqualTo(defaultUnit);
    }

    private Unit newNonDefaultUnit() {
        Unit unit = new Unit();
        unit.setDefaultUnit(false);
        return unit;
    }
}
