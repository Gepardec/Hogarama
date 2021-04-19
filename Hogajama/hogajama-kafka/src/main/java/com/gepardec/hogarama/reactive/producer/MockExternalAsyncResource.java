package com.gepardec.hogarama.reactive.producer;

import com.gepardec.hogarama.reactive.vo.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PreDestroy;
import javax.enterprise.context.Dependent;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.util.Random;
import java.util.concurrent.*;

@Dependent
public class MockExternalAsyncResource {

  private static final int      TICK   = 1000;
  private static final Random   random = new Random();
  private static final Logger   LOG  = LoggerFactory.getLogger(MockExternalAsyncResource.class);

  private final ScheduledExecutorService delayedExecutor = Executors.newSingleThreadScheduledExecutor(Executors.defaultThreadFactory());
  private       long                     last            = System.currentTimeMillis();
  private       long                     id              = 0L;


  @PreDestroy
  public void stop() {
    delayedExecutor.shutdown();
  }


  public CompletionStage<String> getNextValue(String unit) {
    synchronized (this) {
      CompletableFuture<String> cf = new CompletableFuture<>();
      long now = System.currentTimeMillis();
      long next = TICK + last;
      long delay = next - now;
      last = next;
      NextEvent nor = new NextEvent(cf, id++, unit);
      delayedExecutor.schedule(nor, delay, TimeUnit.MILLISECONDS);
      return cf;
    }
  }

  private static class NextEvent implements Runnable {
    private final CompletableFuture<String> cf;
    private final long                      id;
    private final String                    unit;

    public NextEvent(CompletableFuture<String> cf, long id, String unit) {
      this.cf = cf;
      this.id = id;
      this.unit = unit;
    }

    @Override
    public void run() {
      double value = random.nextDouble();
      Jsonb jsonb = JsonbBuilder.create();
      Event event = new Event(unit, value, id);
      String jsonString = jsonb.toJson(event);
      cf.complete(jsonString);

      LOG.info("Generated event: {}", jsonString);
    }
  }

}
