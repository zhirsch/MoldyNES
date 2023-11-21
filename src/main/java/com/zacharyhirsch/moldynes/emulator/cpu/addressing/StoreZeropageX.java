package com.zacharyhirsch.moldynes.emulator.cpu.addressing;

import com.zacharyhirsch.moldynes.emulator.StoreFunction;
import com.zacharyhirsch.moldynes.emulator.cpu.NesCpu;
import com.zacharyhirsch.moldynes.emulator.cpu.NesCpuCycle;
import com.zacharyhirsch.moldynes.emulator.cpu.NesCpuDecode;
import com.zacharyhirsch.moldynes.emulator.cpu.NesCpuState;
import com.zacharyhirsch.moldynes.emulator.cpu.alu.NesAluAdd;

public class StoreZeropageX implements NesCpuCycle {

  private final StoreFunction storeFn;

  public StoreZeropageX(StoreFunction storeFn) {
    this.storeFn = storeFn;
  }

  @Override
  public NesCpuCycle start(NesCpu cpu, NesCpuState state) {
    cpu.fetch(state.pc++);
    return this::cycle2;
  }

  private NesCpuCycle cycle2(NesCpu cpu, NesCpuState state) {
    cpu.fetch((byte) 0x00, state.data);
    state.alu = new NesAluAdd(state.data, state.x, false);
    return this::cycle3;
  }

  private NesCpuCycle cycle3(NesCpu cpu, NesCpuState state) {
    cpu.store((byte) 0x00, state.alu.output(), storeFn.value(state));
    return this::cycle4;
  }

  private NesCpuCycle cycle4(NesCpu cpu, NesCpuState state) {
    cpu.fetch(state.pc++);
    return NesCpuDecode::next;
  }
}
