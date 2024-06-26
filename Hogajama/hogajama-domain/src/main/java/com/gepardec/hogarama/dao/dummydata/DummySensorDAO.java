package com.gepardec.hogarama.dao.dummydata;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gepardec.hogarama.domain.sensor.SensorDataDAO;
import com.gepardec.hogarama.domain.watering.WateringData;
import com.gepardec.hogarama.annotations.DummyDAO;
import com.gepardec.hogarama.domain.sensor.SensorData;

@Named("dummyHabaramaDao")
@DummyDAO
@RequestScoped
public class DummySensorDAO implements SensorDataDAO {

    private static final Logger LOG = LoggerFactory.getLogger(DummySensorDAO.class);

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
    public void save(SensorData data){
        LOG.info("Don't write SensorData since DummySensorDAO. Data: {}", data);
    }

    @Override
    public void saveActorEvent(WateringData data) {
        LOG.info("Don't write WateringData  since DummySensorDAO. Data: {}", data);
    }

}
