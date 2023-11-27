package com.zacharyhirsch.moldynes.emulator.cpu.instructions;

import com.zacharyhirsch.moldynes.emulator.cpu.NesCpu;
import com.zacharyhirsch.moldynes.emulator.cpu.NesCpuCycle;
import java.util.function.Predicate;

public abstract class Bxx implements NesCpuCycle {

  private final Predicate<Byte> predicate;

  public Bxx(Predicate<Byte> predicate) {
    this.predicate = predicate;
  }

  @Override
  public NesCpuCycle execute(NesCpu cpu) {
    return cycle1(cpu);
  }

  private NesCpuCycle cycle1(NesCpu cpu) {
    cpu.fetch(cpu.state.pc++);
    return this::cycle2;
  }

  private NesCpuCycle cycle2(NesCpu cpu) {
    if (!predicate.test(cpu.state.p)) {
      cpu.fetch(cpu.state.pc++);
      return cpu::next;
    }
    return this::cycle3;
  }

  private NesCpuCycle cycle3(NesCpu cpu) {
    int pcl = Byte.toUnsignedInt(cpu.state.pcl()) + cpu.state.data;
    if (0x00 <= pcl && pcl <= 0xff) {
      cpu.jump(cpu.state.pch(), (byte) pcl);
      cpu.fetch(cpu.state.pc++);
      return cpu::next;
    }
    return this::cycle4;
  }

  private NesCpuCycle cycle4(NesCpu cpu) {
    int pcl = Byte.toUnsignedInt(cpu.state.pcl()) + cpu.state.data;
    byte pch = (byte) (cpu.state.pch() + (pcl < 0x00 ? -1 : 1));
    cpu.jump(pch, (byte) pcl);
    cpu.fetch(cpu.state.pc++);
    return cpu::next;
  }
}
