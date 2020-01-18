package com.gepardec.hogarama.testdata;

import static com.gepardec.hogarama.domain.DateUtils.toDate;
import static com.gepardec.hogarama.domain.DateUtils.toLocalDateTime;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.gepardec.hogarama.domain.sensor.SensorData;
import com.gepardec.hogarama.domain.watering.WateringService;

public class TestDataProducer {

	private ArrayList<SensorData> data;
	private SensorData akt;
	private LocalDateTime lastTime;
	private int index = -1;
	
	public TestDataProducer(SensorData sensorData) {
		data = new ArrayList<SensorData>();
		akt = sensorData;
		data.add(akt);
		lastTime = toLocalDateTime(akt.getTime());
	}

	public void addValueMinusMinutes(double value, int minutes) {
		addValueAt( value, lastTime.minus(Duration.ofMinutes(minutes)));
	}

	public void addValueAt(double value, LocalDateTime time) {
		akt = copy(akt);
		akt.setId(Integer.toString(Integer.parseInt(akt.getId()) + 1 ));
		lastTime = time;
		akt.setTime(toDate(lastTime));
		akt.setValue(value);
		data.add(akt);
	}

	
	private SensorData copy(SensorData from) {
		SensorData to = new SensorData();
		to.setId(from.getId());
		to.setLocation(from.getLocation());
		to.setSensorName(from.getSensorName());
		to.setTime(from.getTime());
		to.setType(from.getType());
		to.setValue(from.getValue());
		to.setVersion(from.getVersion());
		return to;
	}

	public List<SensorData> getData() {
		return data;
	}

    public SensorData getNext() {
        return data.get(++index);
    }

    public void waterAll(WateringService watering) {
        for (SensorData sensorData : data) {
            watering.water(sensorData);
        }  
    }	

}
