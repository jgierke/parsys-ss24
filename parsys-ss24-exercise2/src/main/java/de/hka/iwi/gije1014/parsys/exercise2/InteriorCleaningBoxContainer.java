package de.hka.iwi.gije1014.parsys.exercise2;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class InteriorCleaningBoxContainer {

  private final Customizer customizer;
  private final List<InteriorCleaningBox> interiorCleaningBoxes;

  public InteriorCleaningBoxContainer(Customizer customizer) {
    this.customizer = customizer;
    interiorCleaningBoxes = new ArrayList<>();
    for (int i=0; i<this.customizer.getNumberOfInteriorCleaningBoxes(); i++) {
      interiorCleaningBoxes.add(new InteriorCleaningBox(customizer, i+1));
    }
  }

  public void interiorCleaning(Car car) {
    InteriorCleaningBox interiorCleaningBox = this.getInteriorCleaningBox(car);

    interiorCleaningBox.enter(car);
    interiorCleaningBox.clean(car);
    interiorCleaningBox.leave(car);

    this.updateAvailableInteriorCleaningBoxes();
  }

//  public synchronized InteriorCleaningBox getInteriorCleaningBox(Car car) {
  public InteriorCleaningBox getInteriorCleaningBox(Car car) {
    while (!hasAvailableInteriorCleaningBox()) {
      System.err.println(getTimestamp() + ": no free interior cleaning boxes. " + car + " must wait.");
      try {
        wait();
      } catch (InterruptedException ex) {
        System.err.println("Something went wrong while waiting for an available interior cleaning box.");
      }
    }
    return getAvailableInteriorCleaningBox();
  }

//  public synchronized boolean hasAvailableInteriorCleaningBox() {
  public boolean hasAvailableInteriorCleaningBox() {
    return this.getAvailableInteriorCleaningBox() != null;
  }

//  private synchronized InteriorCleaningBox getAvailableInteriorCleaningBox() {
  private InteriorCleaningBox getAvailableInteriorCleaningBox() {
    return interiorCleaningBoxes.stream()
               .filter(interiorCleaningBox -> interiorCleaningBox.isAvailable())
               .findFirst()
               .orElse(null);
  }

//  private synchronized void updateAvailableInteriorCleaningBoxes() {
  private void updateAvailableInteriorCleaningBoxes() {
    notify();
  }

  private LocalTime getTimestamp() {
    return LocalTime.now().truncatedTo(ChronoUnit.SECONDS);
  }

  // for debugging only
  public void printCounters() {
    for (InteriorCleaningBox interiorCleaningBox : interiorCleaningBoxes) {
      System.err.println(interiorCleaningBox + " enterCounter: " + interiorCleaningBox.getEnterCounter());
      System.err.println(interiorCleaningBox + " leaveCounter: " + interiorCleaningBox.getLeaveCounter());
    }
  }

}
