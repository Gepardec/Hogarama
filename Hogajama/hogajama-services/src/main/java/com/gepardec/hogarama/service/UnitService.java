package com.gepardec.hogarama.service;

import com.gepardec.hogarama.domain.entity.Unit;
import com.gepardec.hogarama.domain.exception.TechnicalException;
import com.gepardec.hogarama.service.dao.UnitDao;

import javax.inject.Inject;
import javax.ws.rs.NotFoundException;
import java.util.List;

public class UnitService {

    @Inject
    private UnitDao dao;
    @Inject
    private OwnerStore store;


    public List<Unit> getUnitsForOwner() {
        return dao.getUnitsForOwner(store.getOwner().getId());
    }

    public void createUnit(Unit unit) {
        dao.save(unit);
    }

    public List<Unit> getAllUnits() {
        return dao.findAll();
    }

    private boolean unitExists(Unit unit) {
        return dao.getById(unit.getId()).isPresent();
    }

    public void updateUnit(Unit unit) {
        if (unitExists(unit)) {
            dao.update(unit);
        } else {
            throw new TechnicalException("Unit doesn't belong to owner");
        }
    }

    public void deleteUnit(Long idNum) {
        Unit ac = this.dao.getById(idNum)
                .orElseThrow(() -> new NotFoundException(String.format("Unit with id [%d] not found", idNum)));

        this.dao.delete(ac);
    }
}
