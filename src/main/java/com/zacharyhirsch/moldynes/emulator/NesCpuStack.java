package com.zacharyhirsch.moldynes.emulator;

public class NesCpuStack {

  private static final int STACK_BASE = 0x0100;

  private final NesCpuMemory memory;
  private final Registers regs;

  public NesCpuStack(NesCpuMemory memory, Registers regs) {
    this.memory = memory;
    this.regs = regs;
  }

  public void pushByte(UInt8 value) {
    memory.storeByte(UInt16.cast(STACK_BASE + Byte.toUnsignedInt(regs.sp.value())), value);
    regs.sp = UInt8.cast(regs.sp.value() - 1);
  }

  public void pushWord(UInt16 value) {
    memory.storeWord(UInt16.cast(STACK_BASE + Byte.toUnsignedInt(regs.sp.value()) - 1), value);
    regs.sp = UInt8.cast(Byte.toUnsignedInt(regs.sp.value()) - 2);
  }

  public UInt8 pullByte() {
    regs.sp = UInt8.cast(Byte.toUnsignedInt(regs.sp.value()) + 1);
    return memory.fetchByte(UInt16.cast(STACK_BASE + Byte.toUnsignedInt(regs.sp.value())));
  }

  public UInt16 pullWord() {
    regs.sp = UInt8.cast(Byte.toUnsignedInt(regs.sp.value()) + 2);
    return memory.fetchWord(UInt16.cast(STACK_BASE + Byte.toUnsignedInt(regs.sp.value()) - 1));
  }
}
