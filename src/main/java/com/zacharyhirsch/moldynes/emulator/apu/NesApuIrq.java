package com.zacharyhirsch.moldynes.emulator.apu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class NesApuIrq {

  private static final Logger log = LoggerFactory.getLogger(NesApuIrq.class);

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
    log.info("APU irq <- {} [inhibited? {}]", irq, inhibited);
    this.irq = irq;
  }

  void setInhibited(boolean inhibited) {
    this.inhibited = inhibited;
    if (inhibited) {
      irq = false;
    }
  }
}
