package com.gepardec.hogarama.service.dao;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import org.apache.commons.collections4.CollectionUtils;

import com.gepardec.hogarama.domain.SensorData;
import com.gepardec.hogarama.service.collector.LastNRecordCollector;
import com.gepardec.hogarama.service.comparator.SensorDataComparator;
import com.gepardec.hogarama.service.predicate.SensorDataPredicate;
import com.gepardec.hogarama.util.DummyDataUtil;

@Named("dummyHabaramaDao")
@RequestScoped
public class DummyHabaramaDAOImpl implements HabaramaDAO {

	private static final int MAX_NUMBER_OF_SENSORS = 10;
	private static final int MAX_NUMBER_OF_DATA = 10000;
	private List<SensorData> sensorDatas = DummyDataUtil.getDummySensorData(MAX_NUMBER_OF_SENSORS, MAX_NUMBER_OF_DATA, false);

	@Override
	public List<String> getAllSensors() {
		return CollectionUtils.emptyIfNull(sensorDatas)
							  .stream()
							  .map(e -> e.getSensorName()).distinct()
							  .collect(Collectors.toList());
	}

	@Override
	public List<SensorData> getAllData(Integer maxNumber, String sensorName, Date from, Date to) {
		return CollectionUtils.emptyIfNull(sensorDatas)
							  .stream()
							  .filter(new SensorDataPredicate(sensorName, from, to))
							  .sorted(new SensorDataComparator())
							  .collect(new LastNRecordCollector<>(maxNumber == null || maxNumber < 0 ? sensorDatas.size() : maxNumber));
	}

	@Override
	public String getLocationBySensorName(String sensorName) {
		return CollectionUtils.emptyIfNull(sensorDatas)
				  			  .stream()
				  			  .filter(s -> sensorName.equals(s.getSensorName()))
				  			  .findFirst().get()
				  			  .getLocation();
	}
	
}
