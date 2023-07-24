package com.zacharyhirsch.moldynes.emulator;

import static java.lang.Byte.toUnsignedInt;

public class NesCpuStack {

  private static final short STACK_BASE = 0x0100;

  private final NesCpuMemory memory;
  private final Registers regs;

  public NesCpuStack(NesCpuMemory memory, Registers regs) {
    this.memory = memory;
    this.regs = regs;
  }

  public <T extends Number> void push(T value, Class<T> clazz) {
    int offset = offset(clazz);
    memory.store((short) (STACK_BASE + toUnsignedInt(regs.sp) - offset), value);
    regs.sp -= offset + 1;
  }

  public byte pullByte() {
    int offset = offset(Byte.class);
    regs.sp += offset + 1;
    return memory.fetchByte((short) (STACK_BASE + toUnsignedInt(regs.sp) - offset));
  }

  public short pullShort() {
    int offset = offset(Short.class);
    regs.sp += offset + 1;
    return memory.fetchShort((short) (STACK_BASE + toUnsignedInt(regs.sp) - offset));
  }

  private static <T extends Number> int offset(Class<T> clazz) {
    if (clazz.equals(Byte.class)) {
      return 0;
    }
    if (clazz.equals(Short.class)) {
      return 1;
    }
    throw new IllegalArgumentException(clazz.toString());
  }
}
