package com.gepardec.hogarama.rest.unitmanagement;

import com.gepardec.hogarama.domain.unitmanagement.entity.Sensor;
import com.gepardec.hogarama.domain.unitmanagement.service.SensorService;
import com.gepardec.hogarama.rest.unitmanagement.dto.SensorDto;
import com.gepardec.hogarama.rest.unitmanagement.interceptor.DetermineOwner;
import com.gepardec.hogarama.rest.unitmanagement.translator.SensorDtoTranslator;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.List;

@SuppressWarnings("unused")
@DetermineOwner
public class SensorApiImpl implements SensorApi {

    private static final Logger LOG = LoggerFactory.getLogger(SensorApiImpl.class);

    @Inject
    private SensorService service;
    @Inject
    private SensorDtoTranslator translator;

    @Override
    public Response getForOwner(SecurityContext securityContext) {
        LOG.info("Get sensors for current owner.");
        List<SensorDto> dtoList = translator.toDtoList(service.getAllSensorsForOwner());
        return new BaseResponse<>(dtoList, HttpStatus.SC_OK).createRestResponse();
    }

    @Override
    @Transactional
    public Response create(SecurityContext securityContext, SensorDto sensorDto) {
        LOG.info("Create sensor.");
        Sensor sensor = translator.fromDto(sensorDto);
        service.createSensor(sensor);

        return new BaseResponse<>(HttpStatus.SC_CREATED).createRestResponse();
    }

    @Override
    @Transactional
    public Response update(String id, SecurityContext securityContext, SensorDto sensorDto) {
        LOG.info("Updating sensor with id {}.", id);
        Sensor sensor = translator.fromDto(sensorDto);
        if (id == null || !id.equals(sensorDto.getId().toString()) || sensorDto.getUnitId() == null) {
            return new BaseResponse<>(HttpStatus.SC_BAD_REQUEST).createRestResponse();
        } else {
            service.updateSensor(sensor);
        }

        return new BaseResponse<>(HttpStatus.SC_OK).createRestResponse();
    }

    @Override
    @Transactional
    public Response delete(String id, SecurityContext securityContext) {
        LOG.info("Deleting sensor with id {}.", id);
        if (id == null) {
            return new BaseResponse<>(HttpStatus.SC_BAD_REQUEST).createRestResponse();
        } else {
            Long idNum;
            try {
                idNum = Long.parseLong(id);
            } catch (NumberFormatException e) {
                return new BaseResponse<>(HttpStatus.SC_BAD_REQUEST).createRestResponse();
            }

            service.deleteSensor(idNum);
        }

        return new BaseResponse<>(HttpStatus.SC_OK).createRestResponse();
    }
}
