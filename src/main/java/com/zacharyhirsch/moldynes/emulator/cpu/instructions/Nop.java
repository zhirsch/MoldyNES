package com.zacharyhirsch.moldynes.emulator.cpu.instructions;

import com.zacharyhirsch.moldynes.emulator.ModifyFunction;
import com.zacharyhirsch.moldynes.emulator.cpu.NesCpu;
import com.zacharyhirsch.moldynes.emulator.cpu.NesCpuCycle;
import com.zacharyhirsch.moldynes.emulator.cpu.NesCpuDecode;
import com.zacharyhirsch.moldynes.emulator.cpu.NesCpuState;

public class Nop {

  private Nop() {}

  public static class Implied implements NesCpuCycle {

    @Override
    public NesCpuCycle start(NesCpu cpu, NesCpuState state) {
      cpu.fetch(state.pc++);
      return NesCpuDecode::next;
    }
  }

  public static final class OnFetch implements ModifyFunction {

    @Override
    public void modify(NesCpuState state) {}
  }
}
