package com.gepardec.hogarama.domain.unitmanagement.service;

import com.gepardec.hogarama.domain.unitmanagement.context.UnitManagementContext;
import com.gepardec.hogarama.domain.unitmanagement.dao.UnitDAO;
import com.gepardec.hogarama.domain.unitmanagement.entity.Unit;

import javax.inject.Inject;
import java.util.List;

public class UnitService {

    @Inject
    private UnitDAO dao;
    @Inject
    private UnitManagementContext store;

    public List<Unit> getUnitsForOwner() {
        return dao.getUnitsForOwner(store.getOwner().getId());
    }

    public void createUnit(Unit unit) {
        dao.save(unit);
    }
}
