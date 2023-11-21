package com.zacharyhirsch.moldynes.emulator.cpu.instructions;

import com.zacharyhirsch.moldynes.emulator.cpu.NesCpu;
import com.zacharyhirsch.moldynes.emulator.cpu.NesCpuCycle;
import com.zacharyhirsch.moldynes.emulator.cpu.NesCpuDecode;
import com.zacharyhirsch.moldynes.emulator.cpu.NesCpuState;

public class Brk implements NesCpuCycle {

  @Override
  public NesCpuCycle start(NesCpu cpu, NesCpuState state) {
    cpu.fetch(state.pc++);
    return this::cycle2;
  }

  private NesCpuCycle cycle2(NesCpu cpu, NesCpuState state) {
    cpu.store((byte) 0x01, state.sp--, state.pch());
    return this::cycle3;
  }

  private NesCpuCycle cycle3(NesCpu cpu, NesCpuState state) {
    cpu.store((byte) 0x01, state.sp--, state.pcl());
    return this::cycle4;
  }

  private NesCpuCycle cycle4(NesCpu cpu, NesCpuState state) {
    cpu.store((byte) 0x01, state.sp--, state.p);
    return this::cycle5;
  }

  private NesCpuCycle cycle5(NesCpu cpu, NesCpuState state) {
    cpu.fetch((byte) 0xff, (byte) 0xfe);
    return this::cycle6;
  }

  private NesCpuCycle cycle6(NesCpu cpu, NesCpuState state) {
    state.hold = state.data;
    cpu.fetch((byte) 0xff, (byte) 0xff);
    return this::cycle7;
  }

  private NesCpuCycle cycle7(NesCpu cpu, NesCpuState state) {
    cpu.jump(state.data, state.hold);
    cpu.fetch(state.pc++);
    return NesCpuDecode::next;
  }
}
