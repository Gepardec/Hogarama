package com.gepardec.hogarama.service;

import com.gepardec.hogarama.domain.entity.*;
import org.dcm4che.test.em.EntityManipulator;
import org.dcm4che.test.support.BeforeWarp;
import org.dcm4che.test.support.WarpUnitTest;
import org.dcm4che.test.support.WarpUnitTestConfig;
import org.junit.Test;

import javax.inject.Inject;
import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

@WarpUnitTestConfig(baseRestUrl = "http://localhost:8080/hogajama-rs/rest/")
public class ActorServiceIT extends WarpUnitTest {

    public static final String TEST_OWNER = "TEST_OWNER";

    @Inject
    private OwnerService ownerService;
    @Inject
    private ActorService service;
    @Inject
    private OwnerStore store;

    @Test
    public void createActor() throws IOException {
        warpMeta.setExecuteBeforeWarp(true);
        warpMeta.setExecuteInTransaction(true);

        try (EntityManipulator manipulator = getEntityManipulator()) {
            Owner owner = createTestOwner(TEST_OWNER);

            Actor actor = new Actor();
            actor.setName("MyActor1");
            actor.setDeviceId("MyActorDeviceId");
            actor.setUnit(owner.getDefaultUnit());

            warp(() -> service.createActor(actor));
            Actor result = manipulator.loadEntityByQuery("select a from Actor as a where a.name = 'MyActor1'", Actor.class);

            assertThat(result.getDeviceId()).isEqualTo("MyActorDeviceId");
            getEntityManipulator().removeEntityWithoutRestoringById(result.getId(), Actor.class);
        }
    }

    @Test
    public void createActorForDefaultUnit() throws IOException {
        warpMeta.setExecuteBeforeWarp(true);
        warpMeta.setExecuteInTransaction(true);

        try (EntityManipulator manipulator = getEntityManipulator()) {
            createTestOwner(TEST_OWNER);

            Actor actor = new Actor();
            actor.setName("MyActor1");
            actor.setDeviceId("MyActorDeviceId");

            warp(() -> service.createActorForDefaultUnit(actor));
            Actor result = manipulator.loadEntityByQuery("select a from Actor as a where a.name = 'MyActor1'", Actor.class);

            assertThat(result.getDeviceId()).isEqualTo("MyActorDeviceId");
            assertTrue(result.getUnit().isDefaultUnit());
            getEntityManipulator().removeEntityWithoutRestoringById(result.getId(), Actor.class);
        }
    }

    @Test
    public void getActorsForOwner() throws IOException {
        warpMeta.setExecuteBeforeWarp(true);
        warpMeta.setExecuteInTransaction(true);

        try (EntityManipulator manipulator = getEntityManipulator()) {
            Owner owner = createTestOwner(TEST_OWNER);

            Actor actor = new Actor();
            actor.setName("MyActor1");
            actor.setDeviceId("MyActorDeviceId");
            actor.setUnit(owner.getDefaultUnit());
            manipulator.insertEntity(actor);

            List<Actor> actorList = warp(() -> service.getActorsForOwner());
            assertThat(actorList).hasSize(1);
            assertThat(actorList.get(0)).extracting(Actor::getName).isEqualTo("MyActor1");
            assertThat(actorList.get(0)).extracting(Actor::getDeviceId).isEqualTo("MyActorDeviceId");
        }
    }

    // TODO create & move to common test module
    @BeforeWarp
    private void fillOwnerStore(){
        store.setOwner(ownerService.getRegisteredOwner(TEST_OWNER).orElse(null));
    }

    private Owner createTestOwner(String ssoUserId) {
        Owner registeredOwner = warp(() -> {
            Owner owner = ownerService.register(ssoUserId);
            return owner;
        });
        cleanUp(() -> {
            getEntityManipulator().removeEntityWithoutRestoring(registeredOwner.getDefaultUnit());
            registeredOwner.setUnitList(null);
            getEntityManipulator().removeEntityWithoutRestoring(registeredOwner);
        });
        return registeredOwner;
    }
}
