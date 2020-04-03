package com.gepardec.hogarama.domain.unitmanagement.entity;

import com.gepardec.hogarama.domain.exception.TechnicalException;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class OwnerTest {

    @Test
    public void addUnit() {
        Owner owner = new Owner();
        Unit unit = new Unit();

        owner.addUnit(unit);

        assertThat(owner.getUnitList()).containsExactly(unit);
    }

    @Test(expected = NullPointerException.class)
    public void addUnit_NullInput_ExceptionExpected() {
        Owner owner = new Owner();

        owner.addUnit(null);
    }

    @Test(expected = TechnicalException.class)
    public void getDefaultUnit_EmptyUnitList() {
        Owner owner = new Owner();
        owner.getDefaultUnit();
    }

    @Test(expected = TechnicalException.class)
    public void getDefaultUnit_NoDefaultUnitPresent() {
        Owner owner = new Owner();
        owner.addUnit(newNonDefaultUnit());
        owner.getDefaultUnit();
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
