package com.zacharyhirsch.moldynes.emulator.cpu;

import com.zacharyhirsch.moldynes.emulator.NesBus;

public final class NesCpu {

  private final NesBus bus;
  private final NesCpuDecoder decoder;
  private final NesCpuNmiPin nmi;

  private NesCpuCycle cycle;
  private boolean halt;
  private boolean reset;

  public final NesCpuState state;
  public final NesAlu alu;

  public NesCpu(NesBus bus) {
    this.bus = bus;
    this.decoder = new NesCpuDecoder();
    this.nmi = new NesCpuNmiPin();

    this.cycle = new NesCpuInit();
    this.halt = false;
    this.reset = false;
    
    this.state = new NesCpuState();
    this.alu = new NesAlu();
  }

  public void tick(boolean nmi) {
    try {
      cycle = cycle.execute(this);
      if (state.write) {
        bus.write(state.adh, state.adl, state.data);
      } else {
        state.data = bus.read(state.adh, state.adl);
      }
      this.nmi.set(nmi);
    } catch (Exception exc) {
      throw new NesCpuCrashedException(state, exc);
    }
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
    if (nmi.value()) {
      nmi.reset();
      state.pc--;
      return new NesCpuNmi().execute(this);
    }
    return decoder.decode(state.data).execute(this);
  }

  public boolean isRunning() {
    return !halt;
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

  public void store(short address, byte data) {
    store((byte) (address >>> 8), (byte) address, data);
  }

  public void startOamDma(byte address) {
    cycle = new NesCpuOamDma(address, cycle);
  }
}
