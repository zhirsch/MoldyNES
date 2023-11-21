package com.zacharyhirsch.moldynes.emulator.cpu.instructions;

import com.zacharyhirsch.moldynes.emulator.cpu.NesCpu;
import com.zacharyhirsch.moldynes.emulator.cpu.NesCpuCycle;
import com.zacharyhirsch.moldynes.emulator.cpu.NesCpuDecode;
import com.zacharyhirsch.moldynes.emulator.cpu.NesCpuState;
import com.zacharyhirsch.moldynes.emulator.cpu.alu.NesAluAdd;

public final class Jmp {

  private Jmp() {}

  public static final class Absolute implements NesCpuCycle {

    @Override
    public NesCpuCycle start(NesCpu cpu, NesCpuState state) {
      cpu.fetch(state.pc++);
      return this::cycle2;
    }

    private NesCpuCycle cycle2(NesCpu cpu, NesCpuState state) {
      state.hold = state.data;
      cpu.fetch(state.pc++);
      return this::cycle3;
    }

    private NesCpuCycle cycle3(NesCpu cpu, NesCpuState state) {
      cpu.jump(state.data, state.hold);
      cpu.fetch(state.pc++);
      return NesCpuDecode::next;
    }
  }

  public static final class Indirect implements NesCpuCycle {

    @Override
    public NesCpuCycle start(NesCpu cpu, NesCpuState state) {
      cpu.fetch(state.pc++);
      return this::cycle2;
    }

    private NesCpuCycle cycle2(NesCpu cpu, NesCpuState state) {
      state.hold = state.data;
      cpu.fetch(state.pc++);
      return this::cycle3;
    }

    private NesCpuCycle cycle3(NesCpu cpu, NesCpuState state) {
      cpu.fetch(state.data, state.hold);
      state.alu = new NesAluAdd(state.hold, (byte) 1, false);
      return this::cycle4;
    }

    private NesCpuCycle cycle4(NesCpu cpu, NesCpuState state) {
      state.hold = state.data;
      cpu.fetch(state.adh, state.alu.output());
      return this::cycle5;
    }

    private NesCpuCycle cycle5(NesCpu cpu, NesCpuState state) {
      cpu.jump(state.data, state.hold);
      cpu.fetch(state.pc++);
      return NesCpuDecode::next;
    }
  }
}
