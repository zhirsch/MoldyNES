package com.zacharyhirsch.moldynes.emulator.cpu;

public class NesCpuInit implements NesCpuCycle {

  @Override
  public NesCpuCycle execute(NesCpu cpu) {
    return cycle1(cpu);
  }

  private NesCpuCycle cycle1(NesCpu cpu) {
    cpu.state.adl += 1;
    return this::cycle2;
  }

  private NesCpuCycle cycle2(NesCpu cpu) {
    cpu.fetch((byte) 0x01, cpu.state.sp--);
    return this::cycle3;
  }

  private NesCpuCycle cycle3(NesCpu cpu) {
    cpu.fetch((byte) 0x01, cpu.state.sp--);
    return this::cycle4;
  }

  private NesCpuCycle cycle4(NesCpu cpu) {
    cpu.fetch((byte) 0x01, cpu.state.sp--);
    return this::cycle5;
  }

  private NesCpuCycle cycle5(NesCpu cpu) {
    cpu.fetch((byte) 0xff, (byte) 0xfc);
    return this::cycle6;
  }

  private NesCpuCycle cycle6(NesCpu cpu) {
    cpu.fetch((byte) 0xff, (byte) 0xfd);
    cpu.state.hold = cpu.state.data;
    return this::cycle7;
  }

  private NesCpuCycle cycle7(NesCpu cpu) {
    cpu.jump(cpu.state.data, cpu.state.hold);
    cpu.fetch(cpu.state.pc++);
    return cpu::next;
  }
}
