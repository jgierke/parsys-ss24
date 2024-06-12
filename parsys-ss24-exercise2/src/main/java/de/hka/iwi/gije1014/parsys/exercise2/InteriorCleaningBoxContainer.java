package de.hka.iwi.gije1014.parsys.exercise2;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class InteriorCleaningBoxContainer {

  private final Customizer customizer;
  private final List<InteriorCleaningBox> interiorCleaningBoxes;

  private final Lock lock = new ReentrantLock();
  private final Condition condition = lock.newCondition();

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

  private InteriorCleaningBox getInteriorCleaningBox(Car car) {
    lock.lock();
    try {
      try {
        while (!hasAvailableInteriorCleaningBox()) {
          System.err.println(getTimestamp() + ": no free interior cleaning boxes. " + car + " must wait.");
          condition.await();
        }
        return getAvailableInteriorCleaningBox();
      } catch (InterruptedException ex) {
        System.err.println("Something went wrong while waiting for an available interior cleaning box.");
        return null;
      }
    } finally {
      lock.unlock();
    }
  }

  public boolean hasAvailableInteriorCleaningBox() {
    return this.getAvailableInteriorCleaningBox() != null;
  }

  private InteriorCleaningBox getAvailableInteriorCleaningBox() {
    lock.lock();
    try {
      return interiorCleaningBoxes.stream()
                 .filter(interiorCleaningBox -> interiorCleaningBox.isAvailable())
                 .findFirst()
                 .orElse(null);
    } finally {
      lock.unlock();
    }
  }

  private void updateAvailableInteriorCleaningBoxes() {
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
