package com.gepardec.hogarama.service;

import com.gepardec.hogarama.domain.entity.Owner;
import com.gepardec.hogarama.domain.entity.Unit;
import org.dcm4che.test.em.EntityManipulator;
import org.dcm4che.test.support.WarpUnitTest;
import org.dcm4che.test.support.WarpUnitTestConfig;
import org.junit.Assert;
import org.junit.Test;

import javax.inject.Inject;
import java.io.Serializable;

import static org.assertj.core.api.Assertions.assertThat;


@WarpUnitTestConfig(baseRestUrl = "http://localhost:8080/hogajama-rs/rest/")
public class OwnerServiceIT extends WarpUnitTest implements Serializable {

    @Inject
    private OwnerService service;

    @Test
    public void testRegister() {
        String ssoUserId = "hello registered owner";
        Owner registeredOwner = warp(() -> service.register(ssoUserId));
        cleanUp(() -> {
            getEntityManipulator().removeEntityWithoutRestoring(registeredOwner.getDefaultUnit());
            registeredOwner.setUnitList(null);
            getEntityManipulator().removeEntityWithoutRestoring(registeredOwner);
        });
        assertThat(registeredOwner)
                .isNotNull()
                .extracting(Owner::getSsoUserId).isEqualTo(ssoUserId);
        assertThat(registeredOwner.getUnitList()).hasSize(1);
        assertThat(registeredOwner.getUnitList().get(0))
                .extracting(Unit::getDefaultUnit).isEqualTo(true);

    }


    @Test
    public void testGetRegisteredOwner_OwnerExistsNot() {
        Owner owner = warp(() -> service.getRegisteredOwner("I_DONT_EXIST").orElse(null));
        Assert.assertNull(owner);
    }

    @Test
    public void testGetRegisteredOwner_OwnerExists() throws Exception {
        String ssoUserId = "HELLO_WORLD";
        try (EntityManipulator manipulator = getEntityManipulator()) {
            Owner owner = new Owner();
            owner.setSsoUserId(ssoUserId);
            manipulator.insertEntity(owner);
            Owner result = warp(() -> service.getRegisteredOwner("HELLO_WORLD").orElse(null));
            assertThat(result)
                    .isNotNull()
                    .extracting(Owner::getSsoUserId).isEqualTo(ssoUserId);
        }
    }

}
