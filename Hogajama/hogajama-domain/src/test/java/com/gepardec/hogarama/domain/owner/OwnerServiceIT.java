package com.gepardec.hogarama.domain.owner;

import com.gepardec.hogarama.domain.entity.Owner;
import org.apache.commons.beanutils.BeanUtils;
import org.assertj.core.api.Assertions;
import org.dcm4che.test.em.EntityManipulator;
import org.dcm4che.test.remote.WarpGate;
import org.dcm4che.test.remote.WarpUnit;
import org.dcm4che.test.support.WarpUnitTest;
import org.dcm4che.test.support.WarpUnitTestConfig;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;
import javax.transaction.*;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;

@WarpUnitTestConfig(baseRestUrl = "http://localhost:8080/hogajama-rs/rest/")
public class OwnerServiceIT extends WarpUnitTest implements Serializable{

    @Inject
    private OwnerService service;

    @Test
    public void getRegisteredOwner() {
    }

    @Test
    public void register() {
    }

    @Test
    public void testOwnerExistsNot() {
        Owner owner = warp(() -> service.getRegisteredOwner("I_DONT_EXIST").orElse(null));
        Assert.assertNull(owner);
    }

    @Test
    public void testOwnerFound() throws Exception {
        String ssoUserId = "HELLO_WORLD";
        try (EntityManipulator manipulator = getEntityManipulator()) {
            Owner owner = new Owner();
            owner.setSsoUserId(ssoUserId);
            manipulator.insertEntity(owner);
            Owner result = warp( () -> service.getRegisteredOwner("HELLO_WORLD").orElse(null));
            assertThat(result)
                    .isNotNull()
                    .extracting(Owner::getSsoUserId).isEqualTo(ssoUserId);
        }
    }

    private Owner findOwner(String ssoUserId){
        return service.getRegisteredOwner(ssoUserId).orElse(null);
    }

    private void copyProperties(Owner ownerEntity, Owner ownerDTO)  {
        try {
            BeanUtils.copyProperties(ownerDTO, ownerEntity);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
