package com.zacharyhirsch.moldynes.emulator.apu;

final class NesApuIrq {

  private boolean irq;
  private boolean inhibited;

  NesApuIrq() {
    this.irq = false;
    this.inhibited = false;
  }

  public boolean get() {
    return irq && !inhibited;
  }

  void set(boolean irq) {
    this.irq = irq;
  }

  void setInhibited(boolean inhibited) {
    this.inhibited = inhibited;
  }
}
