package com.zacharyhirsch.moldynes.emulator.apu;

final class NesApuEnvelope {

  private boolean start;
  private int divider;
  private int decayLevelCounter;

  private boolean loop;
  private boolean constant;
  private int envelope;

  NesApuEnvelope() {
    this.start = false;
    this.divider = 0;
    this.decayLevelCounter = 0;
    this.loop = false;
    this.constant = false;
    this.envelope = 0;
  }

  int getVolume() {
    return constant ? envelope : decayLevelCounter;
  }

  void start() {
    this.start = true;
  }

  void tick() {
    if (start) {
      start = false;
      decayLevelCounter = 15;
      divider = envelope;
      return;
    }
    if (divider > 0) {
      divider--;
      return;
    }
    divider = envelope;
    if (decayLevelCounter > 0) {
      decayLevelCounter--;
      return;
    }
    if (loop) {
      decayLevelCounter = 15;
    }
  }

  void setLoop(boolean loop) {
    this.loop = loop;
  }

  void setConstant(boolean constant) {
    this.constant = constant;
  }

  void setEnvelope(int envelope) {
    this.envelope = envelope;
  }
}
