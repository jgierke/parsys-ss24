package de.hka.iwi.gije1014.parsys.exercise2;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class WashingLineContainer {

  private final Customizer customizer;
  private final List<WashingLine> washingLines;

  public WashingLineContainer(Customizer customizer) {
    this.customizer = customizer;
    this.washingLines = new ArrayList<>();
    for (int i=0; i<this.customizer.getNumberOfWashingLines(); i++) {
      washingLines.add(new WashingLine(customizer, i+1));
    }
  }

  public void washing(Car car) {
    WashingLine washingLine = this.getWashingLine(car);

    washingLine.enter(car);
    washingLine.wash(car);
    washingLine.leave(car);

    this.updateAvailableWashingLines();
  }

//  public synchronized WashingLine getWashingLine(Car car) {
  public WashingLine getWashingLine(Car car) {
    while (!hasAvailableWashingLine()) {
      System.err.println(getTimestamp() + ": no free washing lines. " + car + " must wait.");

      try {
        wait();
      } catch (InterruptedException ex) {
        System.err.println("Something went wrong while waiting for an available washing line.");
      }
    }
    return getAvailableWashingLine();
  }

//  public synchronized boolean hasAvailableWashingLine() {
  public boolean hasAvailableWashingLine() {
    return this.getAvailableWashingLine() != null;
  }

//  private synchronized WashingLine getAvailableWashingLine() {
  private WashingLine getAvailableWashingLine() {
    return washingLines.stream()
               .filter(washingLine -> washingLine.isAvailable())
               .findFirst()
               .orElse(null);
  }

//  private synchronized void updateAvailableWashingLines() {
  private void updateAvailableWashingLines() {
    notify();
  }

  private LocalTime getTimestamp() {
    return LocalTime.now().truncatedTo(ChronoUnit.SECONDS);
  }

  // for debugging only
  public void printCounters() {
    for (WashingLine washingLine : washingLines) {
      System.err.println(washingLine + " enterCounter: " + washingLine.getEnterCounter());
      System.err.println(washingLine + " leaveCounter: " + washingLine.getLeaveCounter());
    }
  }

}
