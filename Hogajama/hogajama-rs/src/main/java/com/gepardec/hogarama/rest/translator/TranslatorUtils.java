package com.gepardec.hogarama.rest.translator;

import com.gepardec.hogarama.domain.entity.Unit;
import com.gepardec.hogarama.domain.unit.UnitDao;
import com.gepardec.hogarama.service.OwnerStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class TranslatorUtils {

    private static final Logger LOG = LoggerFactory.getLogger(TranslatorUtils.class);

    static Unit getUnitByUnitId(Long unitId, UnitDao unitDao, OwnerStore ownerStore) {
        return unitDao.getById(unitId).orElseGet(() -> {
            LOG.warn("No unit with id {} found.", unitId);
            return ownerStore.getOwner().getDefaultUnit();
        });
    }
}
