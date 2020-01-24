package com.gepardec.hogarama.service.dao;

import com.gepardec.hogarama.domain.entity.Unit;
import com.gepardec.hogarama.domain.entity.QUnit;
import com.gepardec.hogarama.domain.entity.Sensor;
import com.querydsl.jpa.impl.JPAQuery;

import javax.enterprise.context.Dependent;
import java.util.List;

@Dependent
public class UnitDao extends BaseDao<Unit> {

    @Override
    public Class<Unit> getEntityClass() {
        return Unit.class;
    }

    public List<Unit> getUnitsForOwner(Long ownerId) {
        JPAQuery<Sensor> query = new JPAQuery<>(entityManager);
        QUnit unit = QUnit.unit;
        return query.select(unit).from(unit).where(unit.unit.owner.id.eq(ownerId)).fetch();
    }
}
