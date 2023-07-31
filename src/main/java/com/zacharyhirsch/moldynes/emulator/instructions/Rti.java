package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.NesCpuMemory;
import com.zacharyhirsch.moldynes.emulator.NesCpuStack;
import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.UInt16;
import com.zacharyhirsch.moldynes.emulator.UInt8;
import com.zacharyhirsch.moldynes.emulator.memory.Implicit;

public class Rti extends Instruction {

  private final Implicit implicit;

  public Rti(Implicit implicit) {
    this.implicit = implicit;
  }

  @Override
  public void execute(NesCpuMemory memory, NesCpuStack stack, Registers regs) {
    UInt8 p = stack.pullByte();
    regs.p.fromByte(p);

    UInt8 pcl = stack.pullByte();
    UInt8 pch = stack.pullByte();
    regs.pc.set(new UInt16(pch, pcl));
  }

  @Override
  public Argument getArgument() {
    return implicit;
  }
}
