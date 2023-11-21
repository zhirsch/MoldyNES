package com.zacharyhirsch.moldynes.emulator.cpu;

public class NesCpuInit implements NesCpuCycle {

  @Override
  public NesCpuCycle start(NesCpu cpu, NesCpuState state) {
    return cycle1(cpu, state);
  }

  private NesCpuCycle cycle1(NesCpu cpu, NesCpuState state) {
    state.adl += 1;
    return this::cycle2;
  }

  private NesCpuCycle cycle2(NesCpu cpu, NesCpuState state) {
    cpu.fetch((byte) 0x01, state.sp--);
    return this::cycle3;
  }

  private NesCpuCycle cycle3(NesCpu cpu, NesCpuState state) {
    cpu.fetch((byte) 0x01, state.sp--);
    return this::cycle4;
  }

  private NesCpuCycle cycle4(NesCpu cpu, NesCpuState state) {
    cpu.fetch((byte) 0x01, state.sp--);
    return this::cycle5;
  }

  private NesCpuCycle cycle5(NesCpu cpu, NesCpuState state) {
    cpu.fetch((byte) 0xff, (byte) 0xfc);
    return this::cycle6;
  }

  private NesCpuCycle cycle6(NesCpu cpu, NesCpuState state) {
    state.hold = state.data;
    cpu.fetch((byte) 0xff, (byte) 0xfd);
    return this::cycle7;
  }

  private NesCpuCycle cycle7(NesCpu cpu, NesCpuState state) {
    cpu.jump(state.data, state.hold);
    cpu.fetch(state.pc++);
    return NesCpuDecode::next;
  }
}
