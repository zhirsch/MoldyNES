package com.zacharyhirsch.util;

/**
 * A counter that counts from 1 to N, then starts over again.
 *
 * <p>Useful for loops that need to perform an action once every N loops.
 */
public final class CircularCounter {

  private final int n;

  private int i;

  public CircularCounter(int n) {
    this.n = n;
    this.i = 1;
  }

  public boolean tick() {
    if (i == n) {
      i = 1 ;
      return true;
    }
    i++;
    return false;
  }
}
