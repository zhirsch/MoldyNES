package com.zacharyhirsch.moldynes.emulator.cpu.instructions;

import com.zacharyhirsch.moldynes.emulator.cpu.NesCpu;
import com.zacharyhirsch.moldynes.emulator.cpu.NesCpuCycle;
import com.zacharyhirsch.moldynes.emulator.cpu.NesCpuDecode;
import com.zacharyhirsch.moldynes.emulator.cpu.NesCpuState;
import com.zacharyhirsch.moldynes.emulator.cpu.alu.NesAluAdd;
import java.util.function.Predicate;

public abstract class Bxx implements NesCpuCycle {

  private final Predicate<Byte> predicate;

  public Bxx(Predicate<Byte> predicate) {
    this.predicate = predicate;
  }

  @Override
  public NesCpuCycle start(NesCpu cpu, NesCpuState state) {
    cpu.fetch(state.pc++);
    return this::cycle2;
  }

  private NesCpuCycle cycle2(NesCpu cpu, NesCpuState state) {
    if (predicate.test(state.p)) {
      state.alu = new NesAluAdd(state.pcl(), state.data, false);
      cpu.fetch(state.pc++);
      return state.data > 0 ? this::cycle3p : this::cycle3n;
    }

    cpu.fetch(state.pc++);
    return NesCpuDecode::next;
  }

  private NesCpuCycle cycle3p(NesCpu cpu, NesCpuState state) {
    cpu.jump(state.pch(), state.alu.output());

    if (state.alu.c()) {
      state.alu = new NesAluAdd(state.pch(), (byte) 1, false);
      cpu.fetch(state.pc);
      return this::cycle4;
    }

    cpu.fetch(state.pc++);
    return NesCpuDecode::next;
  }

  private NesCpuCycle cycle3n(NesCpu cpu, NesCpuState state) {
    cpu.jump(state.pch(), state.alu.output());

    if (!state.alu.c()) {
      state.alu = new NesAluAdd(state.pch(), (byte) -1, false);
      cpu.fetch(state.pc);
      return this::cycle4;
    }

    cpu.fetch(state.pc++);
    return NesCpuDecode::next;
  }

  private NesCpuCycle cycle4(NesCpu cpu, NesCpuState state) {
    cpu.jump(state.alu.output(), state.pcl());
    cpu.fetch(state.pc++);
    return NesCpuDecode::next;
  }
}
