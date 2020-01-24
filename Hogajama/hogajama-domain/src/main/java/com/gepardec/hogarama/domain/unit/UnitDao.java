package com.gepardec.hogarama.domain.unit;

import com.gepardec.hogarama.domain.GenericDao;
import com.gepardec.hogarama.domain.entity.Unit;

import java.util.List;

public interface UnitDao extends GenericDao<Unit> {
    public List<Unit> getUnitsForOwner(Long ownerId);
}
