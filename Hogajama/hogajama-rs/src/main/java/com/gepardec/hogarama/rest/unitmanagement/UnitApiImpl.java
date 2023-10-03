package com.gepardec.hogarama.rest.unitmanagement;

import com.gepardec.hogarama.domain.unitmanagement.entity.Unit;
import com.gepardec.hogarama.domain.unitmanagement.service.UnitService;
import com.gepardec.hogarama.rest.unitmanagement.dto.UnitDto;
import com.gepardec.hogarama.rest.unitmanagement.interceptor.DetermineUser;
import com.gepardec.hogarama.rest.unitmanagement.translator.UnitDtoTranslator;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import java.util.List;

@DetermineUser
public class UnitApiImpl implements UnitApi {

    private static final Logger LOG = LoggerFactory.getLogger(UnitApiImpl.class);

    @Inject
    private UnitService service;
    @Inject
    private UnitDtoTranslator translator;

    @Override
    public Response getForUser() {
        LOG.info("Get unit for current user.");
        List<UnitDto> dtoList = translator.toDtoList(service.getUnitsForUser());
        return new BaseResponse<>(dtoList, HttpStatus.SC_OK).createRestResponse();
    }

    @Override
    @Transactional
    public Response create(UnitDto unitDto) {
        LOG.info("Create new unit {}", unitDto);
        Unit unit = translator.fromDto(unitDto);
        service.createUnit(unit);

        return new BaseResponse<>(unitDto, HttpStatus.SC_CREATED).createRestResponse();
    }

    @Override
    @Transactional
    public Response update(String id, UnitDto unitDto) {
        LOG.info("Updating unit {}.", unitDto);
        Unit unit = translator.fromDto(unitDto);

        if (id == null) {
            return new BaseResponse<>("Required parameter ID is not set!", HttpStatus.SC_BAD_REQUEST).createRestResponse();
        } else if (!id.equals(unitDto.getId().toString())) {
            return new BaseResponse<>(String.format("ID %s has to match with ID %s", id, unitDto.getId().toString()), HttpStatus.SC_BAD_REQUEST).createRestResponse();
        } else {
            service.updateUnit(unit);
        }

        return new BaseResponse<>(unitDto, HttpStatus.SC_OK).createRestResponse();
    }

    @Override
    @Transactional
    public Response delete(String id) {
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
