package com.zacharyhirsch.moldynes.emulator.cpu.addressing;

import com.zacharyhirsch.moldynes.emulator.StoreFunction;
import com.zacharyhirsch.moldynes.emulator.cpu.NesCpu;
import com.zacharyhirsch.moldynes.emulator.cpu.NesCpuCycle;
import com.zacharyhirsch.moldynes.emulator.cpu.NesCpuDecode;
import com.zacharyhirsch.moldynes.emulator.cpu.NesCpuState;
import com.zacharyhirsch.moldynes.emulator.cpu.alu.NesAluAdd;
import java.util.function.Supplier;

public class StoreAbsoluteX implements NesCpuCycle {

  private final StoreFunction storeFn;

  public StoreAbsoluteX(StoreFunction storeFn) {
    this.storeFn = storeFn;
  }

  @Override
  public NesCpuCycle start(NesCpu cpu, NesCpuState state) {
    cpu.fetch(state.pc++);
    return this::cycle2;
  }

  private NesCpuCycle cycle2(NesCpu cpu, NesCpuState state) {
    cpu.fetch(state.pc++);
    state.alu = new NesAluAdd(state.data, state.x, false);
    return this::cycle3;
  }

  private NesCpuCycle cycle3(NesCpu cpu, NesCpuState state) {
    cpu.fetch(state.data, state.alu.output());
    state.alu = new NesAluAdd(state.data, (byte) 0, state.alu.c());
    return this::cycle4;
  }

  private NesCpuCycle cycle4(NesCpu cpu, NesCpuState state) {
    cpu.store(state.alu.output(), state.adl, storeFn.value(state));
    return this::cycle5;
  }

  private NesCpuCycle cycle5(NesCpu cpu, NesCpuState state) {
    cpu.fetch(state.pc++);
    return NesCpuDecode::next;
  }
}
