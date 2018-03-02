package com.gepardec.hogarama.rest.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.gepardec.hogarama.rest.SensorData;

@Mapper
public interface SensorMapper {

	SensorMapper INSTANCE = Mappers.getMapper(SensorMapper.class);

	SensorData mapSensor(com.gepardec.hogarama.domain.SensorData sensor);

	List<SensorData> mapSensors(List<com.gepardec.hogarama.domain.SensorData> sensors);
}
