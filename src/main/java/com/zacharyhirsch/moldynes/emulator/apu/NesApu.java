package com.zacharyhirsch.moldynes.emulator.apu;

public final class NesApu {

  private boolean irq;
  private byte frameCounter;
  private int cycle;

  public NesApu() {
    this.irq = false;
    this.frameCounter = 0;
    this.cycle = 0;
  }

  public boolean irq() {
    return this.irq;
  }

  public void tick() {
    cycle++;
    // mode 0:    mode 1:       function
    // ---------  -----------  -----------------------------
    // - - - f    - - - - -    IRQ (if bit 6 is clear)
    // - l - l    - l - - l    Length counter and sweep
    // e e e e    e e e - e    Envelope and linear counter
    int mode = (frameCounter & 0b1000_0000) >>> 7;
    if (mode == 0) {
      switch (cycle) {
        case 3728 -> {}
        case 7456 -> {}
        case 11185 -> {}
        case 14914 -> {
          irq = (frameCounter & 0b0100_0000) == 0;
          cycle = 0;
        }
      }
      return;
    }
    if (mode == 1) {
      switch (cycle) {
        case 3728 -> {}
        case 7456 -> {}
        case 11185 -> {}
        case 14914 -> {}
        case 18640 -> cycle = 0;
      }
      return;
    }
  }

  public byte readStatus() {
    irq = false;
    return 0;
  }

  public void writePulse1(short address, byte data) {}

  public void writePulse2(short address, byte data) {}

  public void writeTriangle(short address, byte data) {}

  public void writeNoise(short address, byte data) {}

  public void writeDmc(short address, byte data) {}

  public void writeControl(byte data) {}

  public void writeFrameCounter(byte data) {
    frameCounter = data;
    irq = ((frameCounter & 0b0100_0000) != 0) && irq;
  }
}
