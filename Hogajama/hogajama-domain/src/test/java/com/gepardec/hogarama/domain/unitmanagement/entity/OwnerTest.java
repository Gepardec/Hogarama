package com.gepardec.hogarama.domain.unitmanagement.entity;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.ListAssert;
import org.junit.Test;

import static org.junit.Assert.*;

public class OwnerTest {

    @Test
    public void addToUnitList() {
        Owner owner = new Owner();
        Unit unit = new Unit();

        owner.addToUnitList(unit);

        Assertions.assertThat(owner.getUnitList()).containsExactly(unit);
    }

    @Test(expected = NullPointerException.class)
    public void addToUnitList_NullInput_ExceptionExpected() {
        Owner owner = new Owner();

        owner.addToUnitList(null);
    }
}
