package com.zacharyhirsch.moldynes.emulator;

import com.zacharyhirsch.moldynes.emulator.apu.NesApu;
import com.zacharyhirsch.moldynes.emulator.cpu.NesCpu;
import com.zacharyhirsch.moldynes.emulator.ppu.NesPpu;

final class Emulator {

  private final NesCpu cpu;
  private final NesPpu ppu;
  private final NesApu apu;

  public Emulator(NesCpu cpu, NesPpu ppu, NesApu apu) {
    this.cpu = cpu;
    this.ppu = ppu;
    this.apu = apu;
  }

  public void run() {
    while (step()) {}
  }

  public boolean step() {
    ppu.tick();
    ppu.tick();
    ppu.tick();
    return cpu.tick();
  }

  public void reset() {
    System.out.println("Resetting...");
    cpu.reset();
  }
}
