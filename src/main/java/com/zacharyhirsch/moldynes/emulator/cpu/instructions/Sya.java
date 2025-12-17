package com.zacharyhirsch.moldynes.emulator.cpu.instructions;

import com.zacharyhirsch.moldynes.emulator.cpu.NesCpu;
import com.zacharyhirsch.moldynes.emulator.cpu.NesCpuCycle;

public class Sya implements NesCpuCycle {

  public Sya() {}

  @Override
  public NesCpuCycle execute(NesCpu cpu) {
    return cycle1(cpu);
  }

  private NesCpuCycle cycle1(NesCpu cpu) {
    cpu.fetch(cpu.state.pc++);
    return this::cycle2;
  }

  private NesCpuCycle cycle2(NesCpu cpu) {
    cpu.state.hold = cpu.state.data;
    cpu.fetch(cpu.state.pc++);
    return this::cycle3;
  }

  private NesCpuCycle cycle3(NesCpu cpu) {
    cpu.fetch(cpu.state.data, (byte) (cpu.state.hold + cpu.state.x));
    return Byte.toUnsignedInt(cpu.state.hold) + Byte.toUnsignedInt(cpu.state.x) > 255
        ? this::cycle4cross
        : this::cycle4;
  }

  private NesCpuCycle cycle4cross(NesCpu cpu) {
    byte value = (byte) (cpu.state.y & (cpu.state.adh + 1));
    cpu.store((byte) (value >>> 8), cpu.state.adl, value);
    return this::cycle5;
  }

  private NesCpuCycle cycle4(NesCpu cpu) {
    byte value = (byte) (cpu.state.y & (cpu.state.adh + 1));
    cpu.store(cpu.state.adh, cpu.state.adl, value);
    return this::cycle5;
  }

  private NesCpuCycle cycle5(NesCpu cpu) {
    return cpu.next();
  }
}
