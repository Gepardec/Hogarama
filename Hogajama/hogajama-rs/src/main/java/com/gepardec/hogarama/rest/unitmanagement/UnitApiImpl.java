package com.gepardec.hogarama.rest.unitmanagement;

import com.gepardec.hogarama.domain.unitmanagement.entity.Unit;
import com.gepardec.hogarama.domain.unitmanagement.service.UnitService;
import com.gepardec.hogarama.rest.unitmanagement.dto.UnitDto;
import com.gepardec.hogarama.rest.unitmanagement.interceptor.DetermineOwner;
import com.gepardec.hogarama.rest.unitmanagement.translator.UnitDtoTranslator;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.List;

@DetermineOwner
public class UnitApiImpl implements UnitApi {

    private static final Logger LOG = LoggerFactory.getLogger(UnitApiImpl.class);

    @Inject
    private UnitService service;
    @Inject
    private UnitDtoTranslator translator;

    @Override
    public Response getForOwner(SecurityContext securityContext) {
        LOG.info("Get unit for current owner.");
        List<UnitDto> dtoList = translator.toDtoList(service.getUnitsForOwner());
        return new BaseResponse<>(dtoList, HttpStatus.SC_OK).createRestResponse();
    }

    @Override
    @Transactional
    public Response create(SecurityContext securityContext, UnitDto unitDto) {
        LOG.info("Create new unit.");
        Unit unit = translator.fromDto(unitDto);
        service.createUnit(unit);

        return new BaseResponse<>(unitDto, HttpStatus.SC_CREATED).createRestResponse();
    }

    @Override
    @Transactional
    public Response update(String id, SecurityContext securityContext, UnitDto unitDto) {
        LOG.info("Updating unit with id {}.", id);
        Unit unit = translator.fromDto(unitDto);

        if (id == null || !id.equals(unitDto.getId().toString())) {
            return new BaseResponse<>(HttpStatus.SC_BAD_REQUEST).createRestResponse();
        } else {
            service.updateUnit(unit);
        }

        return new BaseResponse<>(unitDto, HttpStatus.SC_OK).createRestResponse();
    }

    @Override
    @Transactional
    public Response delete(String id, SecurityContext securityContext) {
        LOG.info("Deleting unit with id {}.", id);
        if (id == null) {
            return new BaseResponse<>(HttpStatus.SC_BAD_REQUEST).createRestResponse();
        } else {
            Long idNum;
            try {
                idNum = Long.parseLong(id);
            } catch (NumberFormatException e) {
                return new BaseResponse<>(HttpStatus.SC_BAD_REQUEST).createRestResponse();
            }

            service.deleteUnit(idNum);
        }

        return new BaseResponse<>(HttpStatus.SC_OK).createRestResponse();
    }
}
