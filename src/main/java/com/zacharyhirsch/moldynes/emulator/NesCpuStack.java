package com.zacharyhirsch.moldynes.emulator;

public class NesCpuStack {

  private static final UInt16 STACK_BASE = UInt16.cast(0x0100);

  private final NesCpuMemory memory;
  private final Registers regs;

  public NesCpuStack(NesCpuMemory memory, Registers regs) {
    this.memory = memory;
    this.regs = regs;
  }

  public void pushByte(UInt8 value) {
    memory.storeByte(STACK_BASE.add(regs.sp), value);
    UInt8 rhs = UInt8.cast(1);
    regs.sp = NesAlu.sub(regs.sp, rhs, false).output();
  }

  public void pushWord(UInt16 value) {
    UInt8 offset = UInt8.cast(1);
    memory.storeWord(STACK_BASE.add(regs.sp).sub(offset), value);
    UInt8 rhs = UInt8.cast(1);
    regs.sp = NesAlu.sub(NesAlu.sub(regs.sp, offset, false).output(), rhs, false).output();
  }

  public UInt8 pullByte() {
    UInt8 rhs = UInt8.cast(1);
    regs.sp = NesAlu.add(regs.sp, rhs, false).output();
    return memory.fetchByte(STACK_BASE.add(regs.sp));
  }

  public UInt16 pullWord() {
    UInt8 offset = UInt8.cast(1);
    UInt8 rhs = UInt8.cast(1);
    regs.sp = NesAlu.add(NesAlu.add(regs.sp, offset, false).output(), rhs, false).output();
    return memory.fetchWord(STACK_BASE.add(regs.sp).sub(offset));
  }
}
