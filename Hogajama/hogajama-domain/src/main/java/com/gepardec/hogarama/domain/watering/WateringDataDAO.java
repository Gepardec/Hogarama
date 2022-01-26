package com.gepardec.hogarama.domain.watering;

import java.util.Date;
import java.util.List;

public interface WateringDataDAO {
    List<WateringData> getWateringData(Integer maxNumber, String actorName, Date from, Date to);
    void save(WateringData wateringData);
}
