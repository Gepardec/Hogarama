package com.gepardec.hogarama.domain.sensor;

import static com.gepardec.hogarama.domain.DateUtils.toDate;
import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.gepardec.hogarama.testdata.TestDataProducer;

public class SensorMetricsTest {

    private List<SensorData> setupWatering() {
        TestDataProducer data = new TestDataProducer(startSensorData());
        data.addValueMinusMinutes(0.1, 10);
        data.addValueMinusMinutes(0.1, 10);
        data.addValueAt(0.6, LocalDateTime.of(2019, Month.JUNE, 20, 14, 00));
        return data.getData();
    }

    @Test
    public void testOnlyOneLatestValueInTestset() throws Exception {
        assertEquals("Only one latest sensor value", 1L, SensorMetrics.getLatestValues(setupWatering()).entrySet().size());
    }

    @Test
    public void testLatestValue() throws Exception {
        Map<String, SensorData> data = SensorMetrics.getLatestValues(setupWatering());

        assertEquals("Only one latest sensor value", "2019-06-20", LocalDate.from(data.get("My Plant").getTime().toInstant().atZone(ZoneId.systemDefault())).toString());
    }

    private SensorData startSensorData() {
        return new SensorData("1", toDate(LocalDateTime.of(2018, Month.JUNE, 20, 15, 00)), "My Plant", "noramlised",
                0.1, "Vienna", "1.0");
    }

}
