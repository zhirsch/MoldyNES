package com.zacharyhirsch.moldynes.emulator.cpu;

import com.zacharyhirsch.moldynes.emulator.NesCpuMemoryMap;
import com.zacharyhirsch.moldynes.emulator.cpu.logging.NesCpuLogger;

public final class NesCpu {

  private NesCpuCycle cycle;
  private final NesCpuDecoder decoder;

  public int counter;
  public final NesCpuState state;
  public final NesAlu alu;
  public final NesMmu mmu;

  public NesCpu(NesCpuMemoryMap memory) {
    this.cycle = new NesCpuInit();
    this.decoder = new NesCpuDecoder(new NesCpuLogger(memory));

    this.counter = 0;
    this.state = new NesCpuState();
    this.alu = new NesAlu();
    this.mmu = new NesMmu(memory);
  }

  public void tick() {
    try {
      cycle = cycle.execute(this);
      mmu.execute(this);
    } catch (Exception exc) {
      throw new NesCpuCrashedException(state, exc);
    }
    counter++;
  }

  public NesCpuCycle done(NesCpu cpu) {
    return decoder.decode(this).execute(this);
  }

  //  public NesCpuCycle halt() {
  //    throw new EmulatorCrashedException("halt", state);
  //  }
  //
  public void jump(byte pch, byte pcl) {
    state.pc = (short) ((pch << 8) | Byte.toUnsignedInt(pcl));
  }

  public void fetch(byte adh, byte adl) {
    state.adh = adh;
    state.adl = adl;
    mmu.write = false;
  }

  public void fetch(short address) {
    fetch((byte) (address >>> 8), (byte) address);
  }

  public void store(byte adh, byte adl, byte data) {
    state.adh = adh;
    state.adl = adl;
    state.data = data;
    mmu.write = true;
  }
}
