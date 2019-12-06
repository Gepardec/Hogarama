package com.gepardec.hogarama.domain.owner;

import com.gepardec.hogarama.domain.entity.Owner;
import org.apache.commons.beanutils.BeanUtils;
import org.assertj.core.api.Assertions;
import org.dcm4che.test.em.EntityManipulator;
import org.dcm4che.test.remote.WarpGate;
import org.dcm4che.test.remote.WarpUnit;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import static org.assertj.core.api.Assertions.assertThat;

public class OwnerServiceIT {

    @Inject
    private OwnerService service;
    private WarpGate gate;

    @Before
    public void setUp() throws Exception {
        gate = WarpUnit.builder()
                .primaryClass(OwnerServiceIT.class)
                .url("http://localhost:8080/hogajama-rs/rest/")
                .createGate();
    }

    @Test
    public void getRegisteredOwner() {
    }

    @Test
    public void register() {
    }

    @Test
    public void testOwnerExistsNot() {
        Owner owner = gate.warp(() -> service.getRegisteredOwner("I_DONT_EXIST").orElse(null));
        Assert.assertNull(owner);
    }

    @Test
    public void testOwnerFound() throws IOException {
        String ssoUserId = "HELLO_WORLD";
        try (EntityManipulator manipulator = new EntityManipulator()) {
            Owner owner = new Owner();
            owner.setSsoUserId(ssoUserId);
            manipulator.insertEntity(owner);
            Owner result = gate.warp(() -> {
                Owner ownerEntity = service.getRegisteredOwner(ssoUserId).orElse(null);
                ownerEntity.setUnitList(null);
                Owner ownerDTO = new Owner();
                copyProperties(ownerEntity, ownerDTO);
                return ownerDTO;
            });
            assertThat(result)
                    .isNotNull()
                    .extracting(Owner::getSsoUserId).isEqualTo(ssoUserId);
        }

    }

    private void copyProperties(Owner ownerEntity, Owner ownerDTO)  {
        try {
            BeanUtils.copyProperties(ownerDTO, ownerEntity);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
