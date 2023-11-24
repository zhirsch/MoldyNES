package com.zacharyhirsch.moldynes.emulator;

import com.zacharyhirsch.moldynes.emulator.cpu.NesCpu;
import com.zacharyhirsch.moldynes.emulator.cpu.NesCpuHaltException;
import java.io.OutputStream;

final class Emulator {

  private final NesCpu cpu;

  public Emulator(NesCpuMemoryMap memory, OutputStream output) {
    this.cpu = new NesCpu(memory, output);
  }

  public void run() {
    while (step()) {}
  }

  public boolean step() {
    try {
      cpu.tick();
      return true;
    } catch (NesCpuHaltException exc) {
      return false;
    }
  }

  public void reset() {
    System.out.println("Resetting...");
    cpu.reset();
  }
}
