package de.hka.iwi.gije1014.parsys.exercise1;

public class CarWashPark {

  private final Customizer customizer;
  private final WashingLineContainer washingLines;
  private final InteriorCleaningBoxContainer interiorCleaningBoxes;

  public CarWashPark(Customizer customizer) {
    this.customizer = customizer;
    this.washingLines = new WashingLineContainer(customizer);
    this.interiorCleaningBoxes = new InteriorCleaningBoxContainer(customizer);
  }

  public WashingLineContainer getWashingLines() {
    return this.washingLines;
  }

  public InteriorCleaningBoxContainer getInteriorCleaningBoxes() {
    return this.interiorCleaningBoxes;
  }

  // for debugging only
  public void printCounters() {
    washingLines.printCounters();
    interiorCleaningBoxes.printCounters();
  }

}
