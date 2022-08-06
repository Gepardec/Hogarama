package com.gepardec.hogarama.domain.unitmanagement.service;

import com.gepardec.hogarama.domain.unitmanagement.context.UserContext;
import com.gepardec.hogarama.domain.unitmanagement.dao.UnitDAO;
import com.gepardec.hogarama.domain.unitmanagement.entity.Unit;

import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.ws.rs.NotFoundException;
import java.util.List;

public class UnitService {

    @Inject
    private UnitDAO dao;
    @Inject
    private UserContext userContext;
    @Inject
    Event<Unit> unitChanged;

    public List<Unit> getUnitsForUser() {
        return dao.getUnitsForUser(userContext.getUser().getId());
    }

    public void createUnit(Unit unit) {
        dao.save(unit);
    }

    public void updateUnit(Unit unit) {
        unitChanged.fire(unit);
        dao.update(unit);
    }

    public void deleteUnit(Long unitId) {
        Unit unit = this.dao.getById(unitId)
                .orElseThrow(() -> new NotFoundException(String.format("Unit with id [%d] not found", unitId)));

        unitChanged.fire(unit);
        dao.delete(unit);
    }
}
