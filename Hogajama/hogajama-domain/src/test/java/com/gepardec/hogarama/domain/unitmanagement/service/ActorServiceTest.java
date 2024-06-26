package com.gepardec.hogarama.domain.unitmanagement.service;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.gepardec.hogarama.domain.exception.TechnicalException;
import com.gepardec.hogarama.domain.unitmanagement.context.UserContext;
import com.gepardec.hogarama.domain.unitmanagement.dao.ActorDAO;
import com.gepardec.hogarama.domain.unitmanagement.entity.Actor;
import com.gepardec.hogarama.domain.unitmanagement.entity.User;
import com.gepardec.hogarama.domain.unitmanagement.entity.Unit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.enterprise.event.Event;
import java.util.Collections;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ActorServiceTest {

    private static final Long ACTOR_ID = 3537L;

    @Mock
    private ActorDAO dao;
    @Mock
    private UserContext userContext;
    @InjectMocks
    private ActorService service;
    @Mock
    Event<Actor> actorChanged;

    private Unit unit;
    private User user;

    @BeforeEach
    public void setUp() {
        user = newUser();
        Mockito.lenient().when(userContext.getUser()).thenReturn(user);
    }

    @Test
    public void createActor_OK() {
        Actor actor = newActor();

        service.createActor(actor);

        Mockito.verify(dao).save(actor);
        Mockito.verifyNoMoreInteractions(dao);
    }

    @Test
    public void createActor_UnitDoesntBelongToUser() {
        assertThrows(TechnicalException.class, () -> {
            Actor actor = newActorWithNotBelongingUnit();

            service.createActor(actor);
        });
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
    public void updateActor_UnitDoesntBelongToUser() {
        assertThrows(TechnicalException.class, () -> {
            Actor actor = newActorWithNotBelongingUnit();

            service.updateActor(actor);
        });
    }

    @Test
    public void getAllActorsForUser() {
        service.getAllActorsForUser();

        Mockito.verify(dao).getAllActorsForUser(user);
    }

    private User newUser() {
        User user = new User();
        user.setId(-1L);
        this.unit = new Unit();
        unit.setId(1337L);
        unit.setUser(user);
        user.setUnitList(Collections.singletonList(unit));
        return user;
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
