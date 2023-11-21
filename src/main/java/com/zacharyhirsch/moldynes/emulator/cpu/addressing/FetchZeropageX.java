package com.zacharyhirsch.moldynes.emulator.cpu.addressing;

import com.zacharyhirsch.moldynes.emulator.FinishFunction;
import com.zacharyhirsch.moldynes.emulator.ModifyFunction;
import com.zacharyhirsch.moldynes.emulator.cpu.NesCpu;
import com.zacharyhirsch.moldynes.emulator.cpu.NesCpuCycle;
import com.zacharyhirsch.moldynes.emulator.cpu.NesCpuDecode;
import com.zacharyhirsch.moldynes.emulator.cpu.NesCpuState;
import com.zacharyhirsch.moldynes.emulator.cpu.alu.NesAluAdd;

public class FetchZeropageX implements NesCpuCycle {

  private final ModifyFunction modifyFn;
  private final FinishFunction finishFn;

  public FetchZeropageX(ModifyFunction modifyFn) {
    this(modifyFn, state -> {});
  }

  public FetchZeropageX(ModifyFunction modifyFn, FinishFunction finishFn) {
    this.modifyFn = modifyFn;
    this.finishFn = finishFn;
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
    cpu.fetch((byte) 0x00, state.alu.output());
    return this::cycle4;
  }

  private NesCpuCycle cycle4(NesCpu cpu, NesCpuState state) {
    modifyFn.modify(state);
    cpu.fetch(state.pc++);
    return this::cycle5;
  }

  private NesCpuCycle cycle5(NesCpu cpu, NesCpuState state) {
    finishFn.finish(state);
    return NesCpuDecode.next(cpu, state);
  }
}
