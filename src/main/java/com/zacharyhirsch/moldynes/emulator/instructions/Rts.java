package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.*;
import com.zacharyhirsch.moldynes.emulator.memory.Implicit;

public class Rts extends Instruction {

  private final Implicit implicit;

  public Rts(Implicit implicit) {
    this.implicit = implicit;
  }

  @Override
  public void execute(NesCpuMemory memory, NesCpuStack stack, Registers regs) {
    UInt8 pcl = stack.pullByte();
    UInt8 pch = stack.pullByte();
    UInt16 pc = UInt16.cast(Short.toUnsignedInt(new UInt16(pch, pcl).value()) + 1);
    regs.pc.set(pc);
  }

  @Override
  public Argument getArgument() {
    return implicit;
  }
}
