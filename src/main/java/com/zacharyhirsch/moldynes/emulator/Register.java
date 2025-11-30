package com.zacharyhirsch.moldynes.emulator;

public final class Register<T> {

  private T value;

  public Register(T value) {
    this.value = value;
  }

  public T value() {
    return value;
  }

  public void set(T value) {
    this.value = value;
  }

  public T swap(T value) {
    T temp = value();
    set(value);
    return temp;
  }
}
