package de.hka.iwi.gije1014.parsys.exercise2;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class InteriorCleaningBox {

  private final Customizer customizer;
  private final int id;
  private boolean isFree;

  private final Lock lock = new ReentrantLock();
  private final Condition condition = lock.newCondition();

  public InteriorCleaningBox(Customizer customizer, int id) {
    this.customizer = customizer;
    this.id = id;
    this.isFree = true;
  }

  public void enter(Car car) {
    lock.lock();
    try {
      this.isFree = false;
      System.err.println(getTimestamp() + ": " + car + " entered " + this.toString());
    } finally {
      lock.unlock();
    }
  }

  public void leave(Car car) {
    lock.lock();
    try {
      this.isFree = true;
      System.err.println(getTimestamp() + ": " + car + " left " + this.toString());
    } finally {
      lock.unlock();
    }
  }

  public void clean(Car car) {
    lock.lock();
    try {
      int cleaningTime = this.customizer.getDurationForInteriorCleaning();
      System.err.println(getTimestamp() + ": Interior cleaning of " + car + " begins.");
      try {
        Thread.sleep(cleaningTime * 1000);
      } catch (InterruptedException ex) {
        System.err.println("Something went wrong while interior cleaning " + car);
      }
      System.err.println(getTimestamp() + ": Interior cleaning of " + car + " ends.");
    } finally {
      lock.unlock();
    }
  }

  public boolean isAvailable() {
    return this.isFree;
  }

  private LocalTime getTimestamp() {
    return LocalTime.now().truncatedTo(ChronoUnit.SECONDS);
  }

  @Override
  public String toString() {
    return this.getClass().getSimpleName() + "[" + this.id + ", " + this.isFree + "]";
  }

}
