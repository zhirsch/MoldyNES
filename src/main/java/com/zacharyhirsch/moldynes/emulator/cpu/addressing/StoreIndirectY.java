package com.zacharyhirsch.moldynes.emulator.cpu.addressing;

import com.zacharyhirsch.moldynes.emulator.cpu.NesCpuCycle;
import com.zacharyhirsch.moldynes.emulator.cpu.NesCpuCycleTemp;

public class StoreIndirectY implements NesCpuCycleTemp {

//  private final StoreFunction storeFn;
//
//  public StoreIndirectY(StoreFunction storeFn) {
//    this.storeFn = storeFn;
//  }
//
//  @Override
//  public NesCpuCycle execute(NesCpuState state, NesAlu alu, NesMmu mmu) {
//    cpu.fetch(state.pc++);
//    return (state1, alu, mmu) -> cycle2(cpu, state1);
//  }
//
//  private NesCpuCycle cycle2(NesCpu cpu, NesCpuState state) {
//    cpu.fetch((byte) 0x00, state.data);
//    state.alu = new NesAluAdd(state.data, (byte) 1, false);
//    return (state1, alu, mmu) -> cycle3(cpu1, state1);
//  }
//
//  private NesCpuCycle cycle3(NesCpu cpu, NesCpuState state) {
//    cpu.fetch((byte) 0x00, state.alu.output());
//    state.alu = new NesAluAdd(state.data, state.y, false);
//    return (state1, alu, mmu) -> cycle4(cpu1, state1);
//  }
//
//  private NesCpuCycle cycle4(NesCpu cpu, NesCpuState state) {
//    cpu.fetch(state.data, state.alu.output());
//    return (state1, alu, mmu) -> cycle5(cpu1, state1);
//  }
//
//  private NesCpuCycle cycle5(NesCpu cpu, NesCpuState state) {
//    cpu.store(state.adh, state.adl, storeFn.value(state));
//    return (state1, alu, mmu) -> cycle6(cpu1, state1);
//  }
//
//  private NesCpuCycle cycle6(NesCpu cpu, NesCpuState state) {
//    cpu.fetch(state.pc++);
//    return (state1, alu, mmu) -> NesCpuDecode.decode(cpu1, state1);
//  }
}
