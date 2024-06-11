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

  // for debugging only
  private int enterCounter;
  private int leaveCounter;

  public InteriorCleaningBox(Customizer customizer, int id) {
    this.customizer = customizer;
    this.id = id;
    this.isFree = true;
    this.enterCounter = 0;
    this.leaveCounter = 0;
  }

//  public synchronized void enter(Car car) {
  public void enter(Car car) {
    this.isFree = false;
    System.err.println(getTimestamp() + ": " + car + " entered " + this.toString());
    this.enterCounter++;
  }

//  public synchronized void leave(Car car) {
  public void leave(Car car) {
    this.isFree = true;
    System.err.println(getTimestamp() + ": " + car + " left " + this.toString());
    this.leaveCounter++;
  }

//  public synchronized void clean(Car car) {
  public void clean(Car car) {
    int cleaningTime = this.customizer.getDurationForInteriorCleaning();
    System.err.println(getTimestamp() + ": Interior cleaning of " + car + " begins.");
    try {
      Thread.sleep(cleaningTime * 1000);
    } catch (InterruptedException ex) {
      System.err.println("Something went wrong while interior cleaning " + car);
    }
    System.err.println(getTimestamp() + ": Interior cleaning of " + car + " ends.");
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

  // for debugging only
  public int getEnterCounter() {
    return this.enterCounter;
  }

  // for debugging only
  public int getLeaveCounter() {
    return this.leaveCounter;
  }

}
