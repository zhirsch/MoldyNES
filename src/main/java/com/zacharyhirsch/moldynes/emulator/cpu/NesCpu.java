package com.zacharyhirsch.moldynes.emulator.cpu;

import com.zacharyhirsch.moldynes.emulator.cpu.logging.NesCpuLogger;
import com.zacharyhirsch.moldynes.emulator.memory.NesMemory;
import com.zacharyhirsch.moldynes.emulator.ppu.NesPpu;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class NesCpu {

  private final NesPpu ppu;
  private final NesMemory memory;
  private final NesCpuDecoder decoder;
  private final NesCpuLogger logger;
  private final Consumer<byte[][][]> present;

  private NesCpuCycle cycle;
  private int counter;
  private boolean halt;
  private boolean reset;

  public final NesCpuState state;
  public final NesAlu alu;

  public NesCpu(NesPpu ppu, NesMemory memory, NesCpuLogger logger) {
    this.ppu = ppu;
    this.memory = memory;
    this.decoder = new NesCpuDecoder();
    this.logger = logger;

    this.cycle = new NesCpuInit(ppu);
    this.counter = 0;
    this.halt = false;
    this.reset = false;

    this.state = new NesCpuState();
    this.alu = new NesAlu();
    present = r -> {
    };
  }

  public boolean tick() {
    if (halt) {
      return false;
    }
    try {
      cycle = cycle.execute(this);
      if (state.write) {
        memory.store(state.adh, state.adl, state.data);
      } else {
        state.data = memory.fetch(state.adh, state.adl);
      }
    } catch (Exception exc) {
      throw new NesCpuCrashedException(state, exc);
    }
    counter++;
    return true;
  }

  public void reset() {
    reset = true;
  }

  public NesCpuCycle next(NesCpu ignored) {
    if (reset) {
      reset = false;
      state.p |= NesCpuState.STATUS_I;
      return new NesCpuInit(ppu).execute(this);
    }
    if (ppu.nmi()) {
      state.pc--;
      return new NesCpuNmi().execute(this);
    }
    byte opcode = state.data;
    logger.log(opcode, counter, state, ppu, memory);
    return decoder.decode(opcode).execute(this);
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
