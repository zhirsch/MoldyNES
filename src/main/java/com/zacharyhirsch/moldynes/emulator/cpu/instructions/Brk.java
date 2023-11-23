package com.zacharyhirsch.moldynes.emulator.cpu.instructions;

import com.zacharyhirsch.moldynes.emulator.cpu.NesCpuCycle;
import com.zacharyhirsch.moldynes.emulator.cpu.NesCpuCycleTemp;

public class Brk implements NesCpuCycleTemp {
//
//  @Override
//  public NesCpuCycle execute(NesCpu cpu) {
//    cpu.fetch(state.pc++);
//    return (state1, alu, mmu) -> cycle2(cpu, state1);
//  }
//
//  private NesCpuCycle cycle2(NesCpu cpu, NesCpuState state) {
//    cpu.store((byte) 0x01, state.sp--, state.pch());
//    return (state1, alu, mmu) -> cycle3(cpu1, state1);
//  }
//
//  private NesCpuCycle cycle3(NesCpu cpu, NesCpuState state) {
//    cpu.store((byte) 0x01, state.sp--, state.pcl());
//    return (state1, alu, mmu) -> cycle4(cpu1, state1);
//  }
//
//  private NesCpuCycle cycle4(NesCpu cpu, NesCpuState state) {
//    cpu.store((byte) 0x01, state.sp--, state.p);
//    return (state1, alu, mmu) -> cycle5(cpu1, state1);
//  }
//
//  private NesCpuCycle cycle5(NesCpu cpu, NesCpuState state) {
//    cpu.fetch((byte) 0xff, (byte) 0xfe);
//    return (state1, alu, mmu) -> cycle6(cpu1, state1);
//  }
//
//  private NesCpuCycle cycle6(NesCpu cpu, NesCpuState state) {
//    state.hold = state.data;
//    cpu.fetch((byte) 0xff, (byte) 0xff);
//    return (state1, alu, mmu) -> cycle7(cpu1, state1);
//  }
//
//  private NesCpuCycle cycle7(NesCpu cpu, NesCpuState state) {
//    cpu.jump(state.data, state.hold);
//    cpu.fetch(state.pc++);
//    return (state1, alu, mmu) -> NesCpuDecode.decode(cpu1, state1);
//  }
}
