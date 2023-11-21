package com.zacharyhirsch.moldynes.emulator.cpu.addressing;

import com.zacharyhirsch.moldynes.emulator.FinishFunction;
import com.zacharyhirsch.moldynes.emulator.ModifyFunction;
import com.zacharyhirsch.moldynes.emulator.cpu.NesCpu;
import com.zacharyhirsch.moldynes.emulator.cpu.NesCpuCycle;
import com.zacharyhirsch.moldynes.emulator.cpu.NesCpuDecode;
import com.zacharyhirsch.moldynes.emulator.cpu.NesCpuState;

public class FetchImmediate implements NesCpuCycle {

  private final ModifyFunction modifyFn;
  private final FinishFunction finishFn;

  public FetchImmediate(ModifyFunction modifyFn) {
    this(modifyFn, state -> {});
  }

  public FetchImmediate(ModifyFunction modifyFn, FinishFunction finishFn) {
    this.modifyFn = modifyFn;
    this.finishFn = finishFn;
  }

  @Override
  public NesCpuCycle start(NesCpu cpu, NesCpuState state) {
    cpu.fetch(state.pc++);
    return this::cycle2;
  }

  private NesCpuCycle cycle2(NesCpu cpu, NesCpuState state) {
    modifyFn.modify(state);
    cpu.fetch(state.pc++);
    return this::cycle3;
  }

  private NesCpuCycle cycle3(NesCpu cpu, NesCpuState state) {
    finishFn.finish(state);
    return NesCpuDecode.next(cpu, state);
  }
}
