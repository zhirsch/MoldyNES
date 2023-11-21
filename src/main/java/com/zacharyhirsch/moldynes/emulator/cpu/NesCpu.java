package com.zacharyhirsch.moldynes.emulator.cpu;

import com.zacharyhirsch.moldynes.emulator.EmulatorCrashedException;
import com.zacharyhirsch.moldynes.emulator.NesCpuMemoryMap;

public final class NesCpu {

  private final NesCpuState state;
  private final NesCpuMemoryMap memory;

  private NesCpuCycle cycle = new NesCpuInit();

  public NesCpu(NesCpuState state, NesCpuMemoryMap memory) {
    this.state = state;
    this.memory = memory;
  }

  public void tick() {
    try {
      cycle = cycle.start(this, state);

      if (state.alu != null) {
        state.alu.execute();
      }
      state.alu = null;

      if (state.write) {
        memory.store(state.adh, state.adl, state.data);
      } else {
        state.data = memory.fetch(state.adh, state.adl);
      }
      state.write = false;
    } catch (Exception exc) {
      throw new EmulatorCrashedException(state, exc);
    }
  }

  public NesCpuCycle halt() {
    throw new EmulatorCrashedException("halt", state);
  }

  public void jump(byte pch, byte pcl) {
    state.pc = (short) ((pch << 8) | pcl);
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

  //  private static void log(NesCpuState before, Instruction instruction, int cycle) {
  //    System.out.printf(
  //        "%02x%02x  %-8s %s%-30s  A:%02x X:%02x Y:%02x P:%02x SP:%02x PPU:  0,  0 CYC:%d\n",
  //        before.pch,
  //        before.pcl,
  //        "", // Arrays.stream(result.bytes().get()).map(UInt8::toString).collect(joining("")),
  //        instruction instanceof Undocumented ? "*" : " ",
  //        "", // result.text.get(),
  //        before.a,
  //        before.x,
  //        before.y,
  //        before.p,
  //        before.sp,
  //        cycle);
  //  }
}
