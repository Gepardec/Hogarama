package com.gepardec.hogarama.rest.v2;

import com.gepardec.hogarama.domain.entity.Sensor;
import com.gepardec.hogarama.rest.interceptor.DetermineOwner;
import com.gepardec.hogarama.rest.translator.SensorDtoTranslator;
import com.gepardec.hogarama.rest.v2.dto.SensorDto;
import com.gepardec.hogarama.service.SensorService;
import org.apache.http.HttpStatus;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.List;

@DetermineOwner
public class SensorApiImpl implements SensorApi {

    @Inject
    private SensorService service;
    @Inject
    private SensorDtoTranslator translator;

    @Override
    public Response getAllSensors(SecurityContext securityContext) {
        List<SensorDto> dtoList = translator.toDtoList(service.getAllSensors());
        return new BaseResponse<>(dtoList, HttpStatus.SC_OK).createRestResponse();
    }

    @Override
    public Response getSensorsForOwner(SecurityContext securityContext) {
        List<SensorDto> dtoList = translator.toDtoList(service.getAllSensorForOwner());
        return new BaseResponse<>(dtoList, HttpStatus.SC_OK).createRestResponse();
    }

    @Override
    @Transactional
    public Response createSensor(SecurityContext securityContext, SensorDto sensorDto) {
        Sensor sensor = translator.fromDto(sensorDto);
        if (sensorDto.getUnitId() == null) {
            service.createSensorForDefaultUnit(sensor);
        } else {
            service.createSensor(sensor);
        }

        return new BaseResponse<>(HttpStatus.SC_CREATED).createRestResponse();
    }
}
