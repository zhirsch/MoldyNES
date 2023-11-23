package com.zacharyhirsch.moldynes.emulator.cpu.addressing;

import com.zacharyhirsch.moldynes.emulator.cpu.NesCpuCycle;
import com.zacharyhirsch.moldynes.emulator.cpu.NesCpuCycleTemp;

public class FetchZeropageY implements NesCpuCycleTemp {

//  private final ModifyFunction modifyFn;
//  private final FinishFunction finishFn;
//
//  public FetchZeropageY(ModifyFunction modifyFn) {
//    this(modifyFn, state -> {});
//  }
//
//  public FetchZeropageY(ModifyFunction modifyFn, FinishFunction finishFn) {
//    this.modifyFn = modifyFn;
//    this.finishFn = finishFn;
//  }
//
//  @Override
//  public NesCpuCycle execute(NesCpuState state, NesAlu alu, NesMmu mmu) {
//    cpu.fetch(state.pc++);
//    return (state1, alu, mmu) -> cycle2(cpu1, state1);
//  }
//
//  private NesCpuCycle cycle2(NesCpu cpu, NesCpuState state) {
//    cpu.fetch((byte) 0x00, state.data);
//    state.alu = new NesAluAdd(state.data, state.y, false);
//    return (state1, alu, mmu) -> cycle3(cpu1, state1);
//  }
//
//  private NesCpuCycle cycle3(NesCpu cpu, NesCpuState state) {
//    cpu.fetch((byte) 0x00, state.alu.output());
//    return (state1, alu, mmu) -> cycle4(cpu1, state1);
//  }
//
//  private NesCpuCycle cycle4(NesCpu cpu, NesCpuState state) {
//    modifyFn.modify(state);
//    cpu.fetch(state.pc++);
//    return (state1, alu, mmu) -> cycle5(cpu1, state1);
//  }
//
//  private NesCpuCycle cycle5(NesCpu cpu, NesCpuState state) {
//    finishFn.finish(state);
//    return NesCpuDecode.decode(cpu, state);
//  }
}
