package com.zacharyhirsch.moldynes.emulator;

public final class Program {

  private final Ram ram;
  private final Registers regs;

  public Program(Ram ram, Registers regs) {
    this.ram = ram;
    this.regs = regs;
  }

  public byte fetchOpcode() {
    return next(Byte.class);
  }

  public <T extends Number> T fetchArgument(Class<T> clazz) {
    return next(clazz);
  }

  private <T extends Number> T next(Class<T> clazz) {
    T value = ram.fetch(regs.pc, clazz);
    regs.pc += clazz.equals(Byte.class) ? 1 : 2;
    return value;
  }
}
