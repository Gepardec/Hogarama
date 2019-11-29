package com.gepardec.hogarama.service.dao;

import com.gepardec.hogarama.domain.entity.Unit;
import com.gepardec.hogarama.domain.unit.UnitDao;

import javax.enterprise.context.Dependent;

@Dependent
public class UnitDaoImpl extends BaseDao<Unit> implements UnitDao {

    @Override
    public Class<Unit> getEntityClass() {
        return Unit.class;
    }
}
