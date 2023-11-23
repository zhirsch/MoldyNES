package com.zacharyhirsch.moldynes.emulator.cpu.instructions;

import com.zacharyhirsch.moldynes.emulator.cpu.NesCpu;
import com.zacharyhirsch.moldynes.emulator.cpu.NesCpuCycle;

public final class Jmp {

  private Jmp() {}

  public static final class Absolute implements NesCpuCycle {

    @Override
    public NesCpuCycle execute(NesCpu cpu) {
      return cycle1(cpu);
    }

    private NesCpuCycle cycle1(NesCpu cpu) {
      cpu.fetch(cpu.state.pc++);
      return this::cycle2;
    }

    private NesCpuCycle cycle2(NesCpu cpu) {
      cpu.fetch(cpu.state.pc++);
      cpu.state.hold = cpu.state.data;
      return this::cycle3;
    }

    private NesCpuCycle cycle3(NesCpu cpu) {
      cpu.jump(cpu.state.data, cpu.state.hold);
      cpu.fetch(cpu.state.pc++);
      return cpu::done;
    }
  }

  //  public static final class Indirect implements NesCpuCycle {
  //
  //    @Override
  //    public NesCpuCycle execute(NesCpuState state, NesAlu alu, NesMmu mmu) {
  //      cpu.fetch(state.pc++);
  //      return (state1, alu, mmu) -> cycle2(cpu, state1);
  //    }
  //
  //    private NesCpuCycle cycle2(NesCpu cpu, NesCpuState state) {
  //      state.hold = state.data;
  //      cpu.fetch(state.pc++);
  //      return (state1, alu, mmu) -> cycle3(cpu1, state1);
  //    }
  //
  //    private NesCpuCycle cycle3(NesCpu cpu, NesCpuState state) {
  //      cpu.fetch(state.data, state.hold);
  //      state.alu = new NesAluAdd(state.hold, (byte) 1, false);
  //      return (state1, alu, mmu) -> cycle4(cpu1, state1);
  //    }
  //
  //    private NesCpuCycle cycle4(NesCpu cpu, NesCpuState state) {
  //      state.hold = state.data;
  //      cpu.fetch(state.adh, state.alu.output());
  //      return (state1, alu, mmu) -> cycle5(cpu1, state1);
  //    }
  //
  //    private NesCpuCycle cycle5(NesCpu cpu, NesCpuState state) {
  //      cpu.jump(state.data, state.hold);
  //      cpu.fetch(state.pc++);
  //      return (state1, alu, mmu) -> NesCpuDecode.decode(cpu1, state1);
  //    }
  //  }
}
