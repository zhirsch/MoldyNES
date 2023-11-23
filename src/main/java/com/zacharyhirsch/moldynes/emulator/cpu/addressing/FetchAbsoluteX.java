package com.zacharyhirsch.moldynes.emulator.cpu.addressing;

import com.zacharyhirsch.moldynes.emulator.cpu.NesCpuCycle;
import com.zacharyhirsch.moldynes.emulator.cpu.NesCpuCycleTemp;

public class FetchAbsoluteX implements NesCpuCycleTemp {

//  private final ModifyFunction modifyFn;
//  private final FinishFunction finishFn;
//
//  public FetchAbsoluteX(ModifyFunction modifyFn) {
//    this(modifyFn, state -> {});
//  }
//
//  public FetchAbsoluteX(ModifyFunction modifyFn, FinishFunction finishFn) {
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
//    cpu.fetch(state.pc++);
//    state.alu = new NesAluAdd(state.data, state.x, false);
//    return (state1, alu, mmu) -> cycle3(cpu1, state1);
//  }
//
//  private NesCpuCycle cycle3(NesCpu cpu, NesCpuState state) {
//    cpu.fetch(state.data, state.alu.output());
//    state.alu = new NesAluAdd(state.data, (byte) 1, false);
//    return state.alu.c() ? (state1, alu, mmu) -> cycle4(cpu1, state1)
//        : (state2, alu, mmu) -> cycle5(cpu1, state2);
//  }
//
//  private NesCpuCycle cycle4(NesCpu cpu, NesCpuState state) {
//    cpu.fetch(state.alu.output(), state.adl);
//    return (state1, alu, mmu) -> cycle5(cpu1, state1);
//  }
//
//  private NesCpuCycle cycle5(NesCpu cpu, NesCpuState state) {
//    modifyFn.modify(state);
//    cpu.fetch(state.pc++);
//    return (state1, alu, mmu) -> cycle6(cpu1, state1);
//  }
//
//  private NesCpuCycle cycle6(NesCpu cpu, NesCpuState state) {
//    finishFn.finish(state);
//    return NesCpuDecode.decode(cpu, state);
//  }
}
