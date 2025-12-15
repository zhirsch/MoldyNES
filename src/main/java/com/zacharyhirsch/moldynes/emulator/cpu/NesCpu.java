package com.zacharyhirsch.moldynes.emulator.cpu;


public final class NesCpu {

  private final NesCpuDecoder decoder;
  private final NesCpuNmiPin nmi;

  private NesCpuCycle cycle;
  private boolean halt;
  private boolean reset;
  private boolean irq = false;

  public final NesCpuState state;

  public NesCpu() {
    this.decoder = new NesCpuDecoder();
    this.nmi = new NesCpuNmiPin();

    this.cycle = new NesCpuInit();
    this.halt = false;
    this.reset = false;
    
    this.state = new NesCpuState();
  }

  public NesCpuState tick(boolean nmi, boolean irq) {
    try {
      cycle = cycle.execute(this);
      this.nmi.set(nmi);
      this.irq = irq;
    } catch (Exception exc) {
      throw new NesCpuCrashedException(state, exc);
    }
    return state;
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
      return new NesCpuInterrupt((short) 0xfffa, (short) 0xfffb, false).execute(this);
    }
    if (irq) {
      irq = false;
      if (!state.pI()) {
        state.pc--;
        return new NesCpuInterrupt((short) 0xfffe, (short) 0xffff, false).execute(this);
      }
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
