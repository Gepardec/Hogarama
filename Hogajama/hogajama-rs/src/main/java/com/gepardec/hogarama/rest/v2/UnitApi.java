package com.gepardec.hogarama.rest.v2;

import com.gepardec.hogarama.domain.entity.Unit;
import com.gepardec.hogarama.rest.interceptor.DetermineOwner;
import com.gepardec.hogarama.rest.translator.UnitDtoTranslator;
import com.gepardec.hogarama.rest.v2.dto.UnitDto;
import com.gepardec.hogarama.service.UnitService;
import org.apache.http.HttpStatus;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.List;

@DetermineOwner
@Path("v2/unit")
public class UnitApi implements ApiBase<UnitDto> {

    @Inject
    private UnitService service;
    @Inject
    private UnitDtoTranslator translator;

    @Override
    public Response getAll(SecurityContext securityContext) {
        List<UnitDto> units = translator.toDtoList(service.getAllUnits());
        return new BaseResponse<>(units, HttpStatus.SC_OK).createRestResponse();
    }

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

    @Override
    @Transactional
    public Response update(String id, SecurityContext securityContext, UnitDto unitDto) {
        Unit sensor = translator.fromDto(unitDto);
        if (id == null || !id.equals(unitDto.getId().toString())) {
            return new BaseResponse<>(HttpStatus.SC_BAD_REQUEST).createRestResponse();
        } else {
            service.updateUnit(sensor);
        }

        return new BaseResponse<>(HttpStatus.SC_OK).createRestResponse();
    }

    @Override
    @Transactional
    public Response delete(String id, SecurityContext securityContext) {
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
