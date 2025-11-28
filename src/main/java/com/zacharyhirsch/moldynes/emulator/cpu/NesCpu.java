package com.zacharyhirsch.moldynes.emulator.cpu;

import com.zacharyhirsch.moldynes.emulator.NesBus;
import com.zacharyhirsch.moldynes.emulator.ppu.NesPpu;

public final class NesCpu {

  private final NesPpu ppu;
  private final NesBus bus;
  private final NesCpuDecoder decoder;

  public int cycleCount = 0;
  private NesCpuCycle cycle;
  private boolean halt;
  private boolean reset;
  private boolean nmi;

  public final NesCpuState state;
  public final NesAlu alu;

  public NesCpu(NesPpu ppu, NesBus bus) {
    this.ppu = ppu;
    this.bus = bus;
    this.decoder = new NesCpuDecoder();

    this.cycle = new NesCpuInit();
    this.halt = false;
    this.reset = false;
    this.nmi = false;

    this.state = new NesCpuState();
    this.alu = new NesAlu();

    this.ppu.setNmiHandler(() -> nmi = true);
  }

  public boolean tick() {
    if (halt) {
      return false;
    }
    try {
      cycleCount++;
      cycle = cycle.execute(this);
      if (state.write) {
        bus.write(state.adh, state.adl, state.data);
      } else {
        state.data = bus.read(state.adh, state.adl);
      }
    } catch (Exception exc) {
      throw new NesCpuCrashedException(state, exc);
    }
    return true;
  }

  public void reset() {
    reset = true;
  }

  public NesCpuCycle next(NesCpu ignored) {
    if (reset) {
      reset = false;
      state.p |= NesCpuState.STATUS_I;
      return new NesCpuInit().execute(this);
    }
    if (nmi) {
      nmi = false;
      state.pc--;
      return new NesCpuNmi().execute(this);
    }
    return decoder.decode(state.data).execute(this);
  }

  public void halt() {
    halt = true;
  }

  public void jump(byte pch, byte pcl) {
    state.pc = (short) ((pch << 8) | Byte.toUnsignedInt(pcl));
  }

  public void fetch(byte adh, byte adl) {
    state.adh = adh;
    state.adl = adl;
    state.write = false;
  }

  public void fetch(short address) {
    fetch((byte) (address >>> 8), (byte) address);
  }

  public void store(byte adh, byte adl, byte data) {
    state.adh = adh;
    state.adl = adl;
    state.data = data;
    state.write = true;
  }
}
