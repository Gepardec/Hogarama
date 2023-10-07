package com.gepardec.hogarama.rest.unitmanagement;

import com.gepardec.hogarama.domain.unitmanagement.entity.Sensor;
import com.gepardec.hogarama.domain.unitmanagement.service.SensorService;
import com.gepardec.hogarama.rest.unitmanagement.dto.SensorDto;
import com.gepardec.hogarama.rest.unitmanagement.interceptor.DetermineUser;
import com.gepardec.hogarama.rest.unitmanagement.translator.SensorDtoTranslator;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import java.util.List;

@SuppressWarnings("unused")
@Path("/unitmanagement/sensor")
@DetermineUser
public class SensorApiImpl implements SensorApi {

    private static final Logger LOG = LoggerFactory.getLogger(SensorApiImpl.class);

    @Inject
    private SensorService service;
    @Inject
    private SensorDtoTranslator translator;

    @Override
    public Response getForUser() {
        LOG.info("Get sensors for current user.");
        List<SensorDto> dtoList = translator.toDtoList(service.getAllSensorsForUser());
        return new BaseResponse<>(dtoList, HttpStatus.SC_OK).createRestResponse();
    }

    @Override
    @Transactional
    public Response create(SensorDto sensorDto) {
        LOG.info("Create sensor.");
        Sensor sensor = translator.fromDto(sensorDto);
        service.createSensor(sensor);

        return new BaseResponse<>(sensorDto, HttpStatus.SC_CREATED).createRestResponse();
    }

    @Override
    @Transactional
    public Response update(String id, SensorDto sensorDto) {
        LOG.info("Updating sensor with id {}.", id);
        Sensor sensor = translator.fromDto(sensorDto);

        if (id == null) {
            return new BaseResponse<>("Required parameter ID is not set!", HttpStatus.SC_BAD_REQUEST).createRestResponse();
        } else if (!id.equals(sensorDto.getId().toString())) {
            return new BaseResponse<>(String.format("ID %s has to match with ID %s", id, sensorDto.getId().toString()), HttpStatus.SC_BAD_REQUEST).createRestResponse();
        } else {
            service.updateSensor(sensor);
        }

        return new BaseResponse<>(sensorDto, HttpStatus.SC_OK).createRestResponse();
    }

    @Override
    @Transactional
    public Response delete(String id) {
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
