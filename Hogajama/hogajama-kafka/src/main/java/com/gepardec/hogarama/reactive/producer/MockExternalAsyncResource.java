package com.gepardec.hogarama.reactive.producer;

import com.gepardec.hogarama.reactive.vo.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.util.Random;
import java.util.concurrent.*;

@ApplicationScoped
public class MockExternalAsyncResource {


  private static final int      TICK   = 1000;
  private static final Random   random = new Random();
  private static final String[] unit = new String[] {"simba", "nala", "toteGazelle"};

  private static final Logger   LOG  = LoggerFactory.getLogger(MockExternalAsyncResource.class);


  private final ScheduledExecutorService delayedExecutor = Executors.newSingleThreadScheduledExecutor(Executors.defaultThreadFactory());
  private       long                     last            = System.currentTimeMillis();
  private       long                      id             = 0L;

  @PreDestroy
  public void stop() {
    delayedExecutor.shutdown();
  }

  public CompletionStage<String> getNextValue() {
    synchronized (this) {
      CompletableFuture<String> cf = new CompletableFuture<>();
      long now = System.currentTimeMillis();
      long next = TICK + last;
      long delay = next - now;
      last = next;
      NextEvent nor = new NextEvent(cf, id++);
      delayedExecutor.schedule(nor, delay, TimeUnit.MILLISECONDS);
      return cf;
    }
  }

  private static class NextEvent implements Runnable {
    private final CompletableFuture<String> cf;
    private       long                      id;

    public NextEvent(CompletableFuture<String> cf, long id) {
      this.cf = cf;
      this.id = id;
    }

    @Override
    public void run() {
      String randomUnit = unit[random.nextInt(3)];
      double value = random.nextDouble();
      Jsonb jsonb = JsonbBuilder.create();
      Event event = new Event(randomUnit, value, id);
      String jsonString = jsonb.toJson(event);
      cf.complete(jsonString);

      LOG.info("Generated event: {}", jsonString);
    }
  }

}
