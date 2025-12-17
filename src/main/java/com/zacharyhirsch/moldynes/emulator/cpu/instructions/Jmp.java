package com.zacharyhirsch.moldynes.emulator.cpu.instructions;

import com.zacharyhirsch.moldynes.emulator.cpu.NesCpu;
import com.zacharyhirsch.moldynes.emulator.cpu.NesCpuCycle;

public final class Jmp {

  private Jmp() {}

  public static final class Absolute implements NesCpuCycle {

    @Override
    public NesCpuCycle execute(NesCpu cpu) {
      return cycle1(cpu);
    }

    private NesCpuCycle cycle1(NesCpu cpu) {
      cpu.fetch(cpu.state.pc++);
      return this::cycle2;
    }

    private NesCpuCycle cycle2(NesCpu cpu) {
      cpu.fetch(cpu.state.pc++);
      cpu.state.hold = cpu.state.data;
      return this::cycle3;
    }

    private NesCpuCycle cycle3(NesCpu cpu) {
      cpu.jump(cpu.state.data, cpu.state.hold);
      return cpu.next();
    }
  }

  public static final class Indirect implements NesCpuCycle {

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
      cpu.fetch(cpu.state.data, cpu.state.hold);
      return this::cycle4;
    }

    private NesCpuCycle cycle4(NesCpu cpu) {
      cpu.state.hold = cpu.state.data;
      cpu.fetch(cpu.state.adh, (byte) (cpu.state.adl + 1));
      return this::cycle5;
    }

    private NesCpuCycle cycle5(NesCpu cpu) {
      cpu.jump(cpu.state.data, cpu.state.hold);
      return cpu.next();
    }
  }
}
