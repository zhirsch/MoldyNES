package com.zacharyhirsch.moldynes.emulator.cpu;

public final class NesCpu {

  private NesCpuCycle cycle;
  private boolean halt;
  private boolean reset;
  private boolean irq;
  private boolean nmi;
  private boolean nmiPending;

  public final NesCpuState state;

  public NesCpu() {
    this.cycle = new NesCpuInit();
    this.halt = false;
    this.reset = false;
    this.irq = false;
    this.nmi = false;
    this.nmiPending = false;

    this.state = new NesCpuState();
  }

  public void nmi() {
    this.nmi = true;
  }

  public NesCpuState tick(boolean irq) {
    try {
      cycle = cycle.execute(this);
    } catch (Exception exc) {
      throw new NesCpuCrashedException(state, exc);
    }
    if (nmi) {
      nmiPending = true;
      nmi = false;
    }
    this.irq = irq;
    state.cycleType = state.cycleType.next();
    return state;
  }

  public void reset() {
    reset = true;
  }

  public NesCpuCycle next() {
    if (reset) {
      reset = false;
      state.p.i(true);
      return new NesCpuInit().execute(this);
    }
    if (nmiPending) {
      nmiPending = false;
      return new NesCpuInterrupt((short) 0xfffa, (short) 0xfffb, false).execute(this);
    }
    if (irq) {
      irq = false;
      if (!state.p.i()) {
        return new NesCpuInterrupt((short) 0xfffe, (short) 0xffff, false).execute(this);
      }
    }
    fetch(state.pc++);
    return cpu -> NesCpuDecoder.decode(state.data).execute(cpu);
  }

  public boolean isRunning() {
    return !halt;
  }

  public NesCpuCycle halt() {
    halt = true;
    return next();
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
