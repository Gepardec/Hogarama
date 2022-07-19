package com.gepardec.hogarama.domain.unitmanagement.entity;

import com.gepardec.hogarama.domain.exception.TechnicalException;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UserTest {

    @Test
    public void addUnit() {
        User user = new User();
        Unit unit = new Unit();

        user.addUnit(unit);

        assertThat(user.getUnitList()).containsExactly(unit);
    }

    @Test(expected = NullPointerException.class)
    public void addUnit_NullInput_ExceptionExpected() {
        User user = new User();

        user.addUnit(null);
    }

    @Test(expected = NullPointerException.class)
    public void getDefaultUnit_EmptyUnitList() {
        User user = new User();
        user.getDefaultUnit();
    }

    @Test(expected = TechnicalException.class)
    public void getDefaultUnit_NoDefaultUnitPresent() {
        User user = new User();
        user.addUnit(newNonDefaultUnit());
        user.getDefaultUnit();
    }

    @Test
    public void getDefaultUnit() {
        User user = new User();

        // add default unit
        Unit defaultUnit = Unit.createDefault(user);
        user.addUnit(defaultUnit);
        // add non default unit
        user.addUnit(newNonDefaultUnit());

        Unit result = user.getDefaultUnit();

        assertThat(result).isEqualTo(defaultUnit);
    }

    private Unit newNonDefaultUnit() {
        Unit unit = new Unit();
        unit.setDefaultUnit(false);
        return unit;
    }
}
