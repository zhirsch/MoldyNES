package com.zacharyhirsch.moldynes.emulator.cpu;

import com.zacharyhirsch.moldynes.emulator.NesCpuMemoryMap;
import com.zacharyhirsch.moldynes.emulator.cpu.logging.NesCpuLogger;
import java.io.OutputStream;

public final class NesCpu {

  private NesCpuCycle cycle;
  private boolean reset;
  private final NesCpuDecoder decoder;

  public int counter;
  public final NesCpuState state;
  public final NesAlu alu;
  public final NesMmu mmu;

  public NesCpu(NesCpuMemoryMap memory, OutputStream output) {
    this.cycle = new NesCpuInit();
    this.reset = false;
    this.decoder = new NesCpuDecoder(new NesCpuLogger(memory, output));

    this.counter = 0;
    this.state = new NesCpuState();
    this.alu = new NesAlu();
    this.mmu = new NesMmu(memory);
  }

  public void tick() {
    try {
      cycle = cycle.execute(this);
      mmu.execute(this);
    } catch (NesCpuHaltException exc) {
      throw exc;
    } catch (Exception exc) {
      throw new NesCpuCrashedException(state, exc);
    }
    counter++;
  }

  public void reset() {
    this.reset = true;
  }

  public NesCpuCycle done(NesCpu cpu) {
    if (this.reset) {
      this.reset = false;
      cpu.state.p |= NesCpuState.STATUS_I;
      return new NesCpuInit();
    }
    return decoder.decode(cpu).execute(cpu);
  }

  public NesCpuCycle halt() {
    throw new NesCpuHaltException();
  }

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
