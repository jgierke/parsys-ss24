package de.hka.iwi.gije1014.parsys.exercise2;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class WashingLineContainer {

  private final Customizer customizer;
  private final List<WashingLine> washingLines;

  private final Lock lock = new ReentrantLock();
  private final Condition condition = lock.newCondition();

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

  private WashingLine getWashingLine(Car car) {
    lock.lock();
    try {
      try {
        while (!hasAvailableWashingLine()) {
          System.err.println(getTimestamp() + ": no free washing lines. " + car + " must wait.");
          condition.await();
        }
        return getAvailableWashingLine();
      } catch (InterruptedException ex) {
        System.err.println("Something went wrong while waiting for an available washing line.");
        return null;
      }
    } finally {
      lock.unlock();
    }
  }

  private boolean hasAvailableWashingLine() {
    return this.getAvailableWashingLine() != null;
  }

  private WashingLine getAvailableWashingLine() {
    lock.lock();
    try {
      return washingLines.stream()
                 .filter(washingLine -> washingLine.isAvailable())
                 .findFirst()
                 .orElse(null);
    } finally {
      lock.unlock();
    }
  }

  private void updateAvailableWashingLines() {
    lock.lock();
    try {
      condition.signal();
    } finally {
      lock.unlock();
    }
  }

  private LocalTime getTimestamp() {
    return LocalTime.now().truncatedTo(ChronoUnit.SECONDS);
  }

}
