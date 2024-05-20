package de.hka.iwi.gije1014.parsys.exercise1;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class Demo {

  public static void main(String[] args) {
    System.err.println("<<< Simulation of CarWashPark >>>");
    System.err.println();

    Customizer customizer = new Customizer();
    CarWashPark carWashPark = new CarWashPark(customizer);
    int totalCarCounter = 0;
    int hourCarCounter = 0;

    long start = System.currentTimeMillis();
    int currentHour = 0;

    while ((System.currentTimeMillis() - start) <= 240000) {
      int calculatedHour = getCurrentHour(start);
      if (currentHour != calculatedHour) {
        currentHour = calculatedHour;
        System.err.println(getTimestamp() + ": --- now in " + currentHour + ". hour ---");
        hourCarCounter = 0;
      }

      int numberArrivingCars = customizer.getNumberOfNewCarsArriveAtOnce(currentHour);
      int everyNthCarRequiresInteriorCleaning = customizer.getEveryNthCarRequiresInteriorCleaning(currentHour);
      List<Car> newCars = new ArrayList<>();
      for (int i=0; i<numberArrivingCars; i++) {
        newCars.add(new Car(++totalCarCounter, (++hourCarCounter % everyNthCarRequiresInteriorCleaning) == 0, carWashPark));
      }
      System.err.println(getTimestamp() + ": " + numberArrivingCars + " cars arrive.");
      newCars.forEach(car -> car.start());

      try {
        Thread.sleep(customizer.getFrequencyNewCarsArrive(currentHour) * 1000);
      } catch (InterruptedException ex) {
        System.err.println("Something went wrong while sleeping until new cars arrive.");
      }
    }
    System.err.println(getTimestamp() + ": --- 4 hours are over. no new cars will arrive. ---");

    // for debugging only
    // sleep very long until all waiting cars have been processed, so that the counters are correct
    /*
    try {
      Thread.sleep(600000);
    } catch (InterruptedException ex) {
      System.err.println("Something went wrong.");
    }
    System.err.println("totalCarCounter: " + totalCarCounter);
    carWashPark.printCounters();
    */
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
