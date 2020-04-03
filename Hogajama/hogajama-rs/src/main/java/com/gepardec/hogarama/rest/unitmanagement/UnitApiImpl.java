package com.gepardec.hogarama.rest.unitmanagement;

import com.gepardec.hogarama.domain.unitmanagement.entity.Unit;
import com.gepardec.hogarama.domain.unitmanagement.service.UnitService;
import com.gepardec.hogarama.rest.unitmanagement.dto.UnitDto;
import com.gepardec.hogarama.rest.unitmanagement.interceptor.DetermineOwner;
import com.gepardec.hogarama.rest.unitmanagement.translator.UnitDtoTranslator;
import org.apache.http.HttpStatus;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.List;

@DetermineOwner
public class UnitApiImpl implements UnitApi {

    @Inject
    private UnitService service;
    @Inject
    private UnitDtoTranslator translator;

    @Override
    public Response getForOwner(SecurityContext securityContext) {
        List<UnitDto> dtoList = translator.toDtoList(service.getUnitsForOwner());
        return new BaseResponse<>(dtoList, HttpStatus.SC_OK).createRestResponse();
    }

    @Override
    @Transactional
    public Response create(SecurityContext securityContext, UnitDto unitDto) {
        Unit unit = translator.fromDto(unitDto);
        service.createUnit(unit);

        return new BaseResponse<>(HttpStatus.SC_CREATED).createRestResponse();
    }
}
