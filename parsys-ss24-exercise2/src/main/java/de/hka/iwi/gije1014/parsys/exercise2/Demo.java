package de.hka.iwi.gije1014.parsys.exercise2;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Demo {

  private static final ExecutorService pool = Executors.newCachedThreadPool();
//  private static final ExecutorService pool = Executors.newFixedThreadPool(4);
//  private static final ExecutorService pool = Executors.newSingleThreadExecutor();
//  private static final ExecutorService pool = Executors.newThreadPerTaskExecutor(Executors.defaultThreadFactory());
//  private static final ExecutorService pool = Executors.newVirtualThreadPerTaskExecutor();

  private static final ScheduledExecutorService arrivalScheduler = Executors.newSingleThreadScheduledExecutor();

  public static void main(String[] args) {
    System.err.println("<<< Simulation of CarWashPark >>>");
    System.err.println();

    Customizer customizer = new Customizer();
    CarWashPark carWashPark = new CarWashPark(customizer);
    AtomicInteger totalCarCounter = new AtomicInteger(0);
    AtomicInteger hourCarCounter = new AtomicInteger(0);

    long start = System.currentTimeMillis();
    AtomicInteger currentHour = new AtomicInteger(0);

    arrivalScheduler.scheduleAtFixedRate(() -> {
      if ((System.currentTimeMillis() - start) > 240000) {
        //pool.shutdown();
        arrivalScheduler.shutdown();
        System.err.println(getTimestamp() + ": --- 4 hours are over. no new cars will arrive. ---");
        return;
      }

      int calculatedHour = getCurrentHour(start);
      if (currentHour.get() != calculatedHour) {
        currentHour.set(calculatedHour);
        System.err.println(getTimestamp() + ": --- now in " + currentHour.get() + ". hour ---");
        hourCarCounter.set(0);
      }

      int numberArrivingCars = customizer.getNumberOfNewCarsArriveAtOnce(currentHour.get());
      int everyNthCarRequiresInteriorCleaning = customizer.getEveryNthCarRequiresInteriorCleaning(currentHour.get());

      List<Car> newCars = new ArrayList<>();
      for (int i=0; i<numberArrivingCars; i++) {
        newCars.add(new Car(
            totalCarCounter.incrementAndGet(),
            (hourCarCounter.incrementAndGet() % everyNthCarRequiresInteriorCleaning) == 0,
            carWashPark
        ));
      }
      System.err.println(getTimestamp() + ": " + numberArrivingCars + " cars arrive.");

      newCars.forEach(pool::execute);
    }, 0, 5000, TimeUnit.MILLISECONDS);
  }

  private static int getCurrentHour(long start) {
    long elapsed = System.currentTimeMillis() - start;
    if (0 <= elapsed && elapsed < 60000) {
      return 1;
    } else if (60000 <= elapsed && elapsed < 120000) {
      return 2;
    } else if (120000 <= elapsed && elapsed <= 180000) {
      return 3;
    } else if (180000 <= elapsed && elapsed <= 240000) {
      return 4;
    }

    return 0;
  }

  private static LocalTime getTimestamp() {
    return LocalTime.now().truncatedTo(ChronoUnit.SECONDS);
  }

}
