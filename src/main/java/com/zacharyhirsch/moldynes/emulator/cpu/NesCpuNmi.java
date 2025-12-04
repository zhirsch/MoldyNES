package com.zacharyhirsch.moldynes.emulator.cpu;


public class NesCpuNmi implements NesCpuCycle {

  @Override
  public NesCpuCycle execute(NesCpu cpu) {
    return cycle1(cpu);
  }

  private NesCpuCycle cycle1(NesCpu cpu) {
    cpu.fetch(cpu.state.pc); // suppress pc increment
    return this::cycle2;
  }

  private NesCpuCycle cycle2(NesCpu cpu) {
    cpu.store((byte) 0x01, cpu.state.sp--, cpu.state.pch());
    return this::cycle3;
  }

  private NesCpuCycle cycle3(NesCpu cpu) {
    cpu.store((byte) 0x01, cpu.state.sp--, cpu.state.pcl());
    return this::cycle4;
  }

  private NesCpuCycle cycle4(NesCpu cpu) {
    byte p = (byte) ((cpu.state.p & ~NesCpuState.STATUS_B) | 0b0010_0000);
    cpu.store((byte) 0x01, cpu.state.sp--, p);
    return this::cycle5;
  }

  private NesCpuCycle cycle5(NesCpu cpu) {
    cpu.fetch((byte) 0xff, (byte) 0xfa);
    return this::cycle6;
  }

  private NesCpuCycle cycle6(NesCpu cpu) {
    cpu.state.hold = cpu.state.data;
    cpu.fetch((byte) 0xff, (byte) 0xfb);
    cpu.state.pI(true);
    return this::cycle7;
  }

  private NesCpuCycle cycle7(NesCpu cpu) {
    cpu.jump(cpu.state.data, cpu.state.hold);
    cpu.fetch(cpu.state.pc++);
    return cpu::next;
  }
}
