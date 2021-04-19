package com.gepardec.hogarama.reactive.processor;

import com.gepardec.hogarama.reactive.vo.Event;
import io.smallrye.reactive.messaging.annotations.Broadcast;
import io.smallrye.reactive.messaging.annotations.Merge;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.util.*;

import static java.lang.Math.round;

@ApplicationScoped
public class EventConverter {

  private HashMap<String, List<Double>> values;

  private static final Logger LOG = LoggerFactory.getLogger(EventConverter.class);

  @PostConstruct
  public void init() {
    values = new HashMap<>();
  }

  @Incoming("sensor-events")
  @Merge(Merge.Mode.MERGE)
  @Outgoing("transformed-sensor-events")
  @Broadcast
  @SuppressWarnings("unused")
  public String newValue(String valueJson) {
    LOG.info("Received event on event converter: {}", valueJson);
    Jsonb jsonb = JsonbBuilder.create();

    Event event = jsonb.fromJson(valueJson, Event.class);
    String key = event.getKey();
    double value = event.getValue();
    long id = event.getId();

    if(!values.containsKey(key)) {
      addKeyToSet(key);
    }

    fifoShiftValuesForKey(key, value);

    Event averageEvent = new Event(key, calculateAverageValueForKey(key), id);

    return jsonb.toJson(averageEvent);
  }

  private void addKeyToSet(String key) {
    values.put(key, new ArrayList<>(Arrays.asList(0.0, 0.0, 0.0, 0.0, 0.0)));
  }

  private void fifoShiftValuesForKey(String key, double value) {
    values.get(key).remove(0);
    values.get(key).add(value);
  }

  private double calculateAverageValueForKey(String key) {
    OptionalDouble optionalAverageValue = values.get(key).stream().mapToDouble(a -> a).average();
    double averageValue;

    if (optionalAverageValue.isPresent()) {
      averageValue = round(optionalAverageValue.getAsDouble() * 1000.0) / 1000.0;
    } else {
     throw new IllegalStateException(String.format("Could not calculate average of event values for key '%s'.", key));
    }
    return averageValue;
  }
}
