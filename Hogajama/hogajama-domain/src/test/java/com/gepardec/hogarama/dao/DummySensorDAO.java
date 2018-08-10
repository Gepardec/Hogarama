package com.gepardec.hogarama.dao;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import org.apache.commons.collections4.CollectionUtils;

import com.gepardec.hogarama.domain.sensor.SensorDAO;
import com.gepardec.hogarama.domain.sensor.SensorData;
import com.gepardec.hogarama.testdata.DummyData;

@Named("dummyHabaramaDao")
@RequestScoped
public class DummySensorDAO implements SensorDAO {

	private static final int MAX_NUMBER_OF_SENSORS = 10;
	private static final int MAX_NUMBER_OF_DATA = 10000;
	private List<SensorData> sensorDatas;
	
	public DummySensorDAO() {
		super();
		this.sensorDatas = DummyData.getDummySensorData(MAX_NUMBER_OF_SENSORS, MAX_NUMBER_OF_DATA, false);
	}
	
	public DummySensorDAO(List<SensorData> sensorDatas) {
		super();
		this.sensorDatas = sensorDatas;
	}

	@Override
	public List<String> getAllSensors() {
		return CollectionUtils.emptyIfNull(sensorDatas)
							  .stream()
							  .map(SensorData::getSensorName).distinct()
							  .collect(Collectors.toList());
	}

	@Override
	public List<SensorData> getAllData(Integer maxNumber, String sensorName, Date from, Date to) {
		Comparator<SensorData> comp = new Comparator<SensorData>() {
			
			@Override
			public int compare(SensorData o1, SensorData o2) {
				return o1.getTime().compareTo(o2.getTime());
			}
		};
		return CollectionUtils.emptyIfNull(sensorDatas)
							  .stream()
							  .filter(new SensorDataPredicate(sensorName, from, to))
							  .sorted(comp)
							  .collect(new LastNRecordCollector<>(maxNumber == null || maxNumber < 0 ? sensorDatas.size() : maxNumber));
	}

	@Override
	public String getLocationBySensorName(String sensorName) {
		return CollectionUtils.emptyIfNull(sensorDatas)
				  			  .stream()
				  			  .filter(s -> sensorName.equals(s.getSensorName()))
				  			  .map(SensorData::getLocation)
				  			  .findFirst()
				  			  .orElse(DummyData.UNKNOW_LOCATION);
	}
	
}
