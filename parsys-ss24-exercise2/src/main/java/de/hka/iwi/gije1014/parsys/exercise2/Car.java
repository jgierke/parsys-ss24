package de.hka.iwi.gije1014.parsys.exercise2;

public class Car implements Runnable {

  private final int id;
  private final boolean requiresInteriorCleaning;
  private final CarWashPark carWashPark;

  public Car(int id, boolean requiresInteriorCleaning, CarWashPark carWashPark) {
    this.id = id;
    this.requiresInteriorCleaning = requiresInteriorCleaning;
    this.carWashPark = carWashPark;
  }

  @Override
  public void run() {
    System.err.println("I'm " + toString());
/*
    if (!this.requiresInteriorCleaning) {
      this.washing();
      return;
    // try to start with interior cleaning.
    // if you have to wait for it, switch cleaning order, maybe there is no queue for washing...
    // if washing is also blocked - that's life...
    } else if (this.carWashPark.getInteriorCleaningBoxes().hasAvailableInteriorCleaningBox()) {
      this.interiorCleaning();
      this.washing();
      return;
    }

    this.washing();
    this.interiorCleaning();
*/
  }

  private void washing() {
    carWashPark.getWashingLines().washing(this);
  }

  private void interiorCleaning() {
    carWashPark.getInteriorCleaningBoxes().interiorCleaning(this);
  }

  @Override
  public String toString() {
    return this.getClass().getSimpleName() + "[" + this.id + ", " + this.requiresInteriorCleaning + "]";
  }

}
