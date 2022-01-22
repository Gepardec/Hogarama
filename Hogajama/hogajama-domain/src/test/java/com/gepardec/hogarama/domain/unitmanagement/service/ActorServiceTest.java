package com.gepardec.hogarama.domain.unitmanagement.service;

import com.gepardec.hogarama.domain.exception.TechnicalException;
import com.gepardec.hogarama.domain.unitmanagement.context.UserContext;
import com.gepardec.hogarama.domain.unitmanagement.dao.ActorDAO;
import com.gepardec.hogarama.domain.unitmanagement.entity.Actor;
import com.gepardec.hogarama.domain.unitmanagement.entity.Owner;
import com.gepardec.hogarama.domain.unitmanagement.entity.Unit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.enterprise.event.Event;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;


@ExtendWith(MockitoExtension.class)
public class ActorServiceTest {

    private static final Long ACTOR_ID = 3537L;

    @Mock
    private ActorDAO dao;
    @Mock
    private UserContext userContext;
    @InjectMocks
    private ActorService service;
    @SuppressWarnings("unused") @Mock
    Event<Actor>   actorChanged;

    private Unit unit;
    private Owner owner;

    @BeforeEach
    public void setUp() {
        owner = newOwner();
        Mockito.lenient().when(userContext.getOwner()).thenReturn(owner);
    }

    @Test
    public void createActor_OK() {
        Actor actor = newActor();

        service.createActor(actor);

        Mockito.verify(dao).save(actor);
        Mockito.verifyNoMoreInteractions(dao);
    }

    @Test
    public void createActor_UnitDoesntBelongToOwner() {
        Actor actor = newActorWithNotBelongingUnit();

        assertThrows(TechnicalException.class, () -> service.createActor(actor));
    }

    @Test
    public void deleteActor() {
        Actor actor = newActor();
        Mockito.when(dao.getById(ACTOR_ID)).thenReturn(Optional.of(actor));

        service.deleteActor(ACTOR_ID);

        Mockito.verify(dao).delete(actor);
    }

    @Test
    public void updateActor_OK() {
        Actor actor = newActor();

        service.updateActor(actor);

        Mockito.verify(dao).update(actor);
        Mockito.verifyNoMoreInteractions(dao);
    }

    @Test
    public void updateActor_UnitDoesntBelongToOwner() {
        Actor actor = newActorWithNotBelongingUnit();

        assertThrows(TechnicalException.class, () -> service.updateActor(actor));
    }

    @Test
    public void getAllActorsForOwner() {
        service.getAllActorsForOwner();

        Mockito.verify(dao).getAllActorsForOwner(owner);
    }

    private Owner newOwner() {
        Owner owner = new Owner();
        this.unit = new Unit();
        unit.setId(1337L);
        owner.setUnitList(Collections.singletonList(unit));
        return owner;
    }

    private Actor newActor() {
        Actor actor = new Actor();
        actor.setId(ACTOR_ID);
        actor.setUnit(unit);
        return actor;
    }

    private Actor newActorWithNotBelongingUnit() {
        Actor actor = new Actor();
        Unit unit = new Unit();
        unit.setId(-1L);
        actor.setUnit(unit);
        return actor;
    }
}
