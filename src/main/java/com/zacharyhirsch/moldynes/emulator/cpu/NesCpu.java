package com.zacharyhirsch.moldynes.emulator.cpu;

import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class NesCpu {

  private static final Logger log = LoggerFactory.getLogger(NesCpu.class);

  private NesCpuCycle cycle;
  private boolean halt;
  private boolean reset;
  private boolean resetPending;
  private boolean nmi;
  private boolean nmiPending;
  private boolean irq;
  private boolean irqPending;

  public final NesCpuState state;
  private boolean oldI;

  public NesCpu() {
    this.cycle = new NesCpuInit();
    this.halt = false;
    this.reset = false;
    this.resetPending = false;
    this.nmi = false;
    this.nmiPending = false;
    this.irq = false;
    this.irqPending = false;

    this.state = new NesCpuState();
  }

  public void reset() {
    this.reset = true;
  }

  public void nmi() {
    this.nmi = true;
  }

  public void irq() {
    this.irq = true;
  }

  public void tick() {
    short oldPc = state.pc;
    this.oldI = state.p.i();
    try {
      cycle = cycle.execute(this);
    } catch (Exception exc) {
      throw new NesCpuCrashedException(oldPc, exc);
    }
    if (reset) {
      resetPending = true;
      reset = false;
    }
    if (nmi) {
      nmiPending = true;
      nmi = false;
    }
    if (irq) {
      irqPending = true;
      irq = false;
    }
    state.cycleType = state.cycleType.next();
  }

  public NesCpuCycle next() {
    if (resetPending) {
      resetPending = false;
      state.p.i(true);
      return new NesCpuInit().execute(this);
    }
    if (nmiPending) {
      nmiPending = false;
      return new NesCpuInterrupt((short) 0xfffa, (short) 0xfffb, false).execute(this);
    }
    if (irqPending) {
      irqPending = false;
      if (!oldI) {
        log.info("CPU entering IRQ handler");
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

  public void startDmcDma(short address, Consumer<Byte> callback) {
    cycle = new NesCpuDmcDma(address, callback, cycle);
  }
}
