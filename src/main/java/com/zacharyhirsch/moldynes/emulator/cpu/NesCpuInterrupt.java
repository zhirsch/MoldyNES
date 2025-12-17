package com.zacharyhirsch.moldynes.emulator.cpu;

public class NesCpuInterrupt implements NesCpuCycle {

  private final short handlerAddressLo;
  private final short handlerAddressHi;
  private final boolean isBrk;

  public NesCpuInterrupt(short handlerAddressLo, short handlerAddressHi, boolean isBrk) {
    this.handlerAddressLo = handlerAddressLo;
    this.handlerAddressHi = handlerAddressHi;
    this.isBrk = isBrk;
  }

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
    cpu.store((byte) 0x01, cpu.state.sp--, cpu.state.p.toByte(isBrk));
    return this::cycle5;
  }

  private NesCpuCycle cycle5(NesCpu cpu) {
    cpu.fetch(handlerAddressLo);
    return this::cycle6;
  }

  private NesCpuCycle cycle6(NesCpu cpu) {
    cpu.state.hold = cpu.state.data;
    cpu.fetch(handlerAddressHi);
    cpu.state.p.i(true);
    return this::cycle7;
  }

  private NesCpuCycle cycle7(NesCpu cpu) {
    cpu.jump(cpu.state.data, cpu.state.hold);
    return cpu.next();
  }
}
