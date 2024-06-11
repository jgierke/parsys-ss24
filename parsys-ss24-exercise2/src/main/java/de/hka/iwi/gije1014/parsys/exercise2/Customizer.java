package de.hka.iwi.gije1014.parsys.exercise2;

import java.util.List;
import java.util.Random;

public class Customizer {

  private final int NUMBER_OF_WASHING_LINES = 3;
  private final int NUMBER_OF_INTERIOR_CLEANING_BOXES = 2;
  private final List<Integer> FREQUENCIES_NEW_CARS_ARRIVE = List.of(0, 5, 5, 5, 5);
  private final List<Integer> MIN_NEW_CARS = List.of(0, 1, 3, 1, 1);
  private final List<Integer> MAX_NEW_CARS = List.of(0, 3, 5, 2, 2);
  private final int MIN_DURATION_FOR_WASHING = 5;
  private final int MAX_DURATION_FOR_WASHING = 12;
  private final List<Integer> DURATIONS_FOR_INTERIOR_CLEANING = List.of(5, 10, 15);
  private final List<Integer> INTERIOR_CLEANING_FOR_EVERY_NTH_CAR = List.of(0, 2, 3, 1, 1);

  private final Random random;

  public Customizer() {
    this.random = new Random();
  }

  public int getNumberOfWashingLines() {
    return NUMBER_OF_WASHING_LINES;
  }

  public int getNumberOfInteriorCleaningBoxes() {
    return NUMBER_OF_INTERIOR_CLEANING_BOXES;
  }

  public int getFrequencyNewCarsArrive(int currentHour) {
    return FREQUENCIES_NEW_CARS_ARRIVE.get(currentHour);
  }

  public int getNumberOfNewCarsArriveAtOnce(int currentHour) {
    int min = MIN_NEW_CARS.get(currentHour);
    int max = MAX_NEW_CARS.get(currentHour);
    return random.nextInt(max - min + 1) + min;
  }

  public int getDurationForWashing() {
    return random.nextInt(MAX_DURATION_FOR_WASHING - MIN_DURATION_FOR_WASHING + 1) + MIN_DURATION_FOR_WASHING;
  }

  public int getDurationForInteriorCleaning() {
    return DURATIONS_FOR_INTERIOR_CLEANING.get( random.nextInt( DURATIONS_FOR_INTERIOR_CLEANING.size() ) );
  }

  public int getEveryNthCarRequiresInteriorCleaning(int currentHour) {
    return INTERIOR_CLEANING_FOR_EVERY_NTH_CAR.get(currentHour);
  }

}
