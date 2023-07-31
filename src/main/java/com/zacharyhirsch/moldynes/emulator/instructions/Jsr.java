package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.*;

public final class Jsr extends Instruction {

  private final UInt8 opcode;

  public Jsr(UInt8 opcode) {
    this.opcode = opcode;
  }

  @Override
  public Result execute2(NesCpuMemory memory, NesCpuStack stack, Registers regs) {
    // Cycle 2
    UInt8 adl = memory.fetch(regs.pc.address());
    regs.pc.inc();

    // Cycle 3 -- Do Nothing

    // Cycle 4
    stack.pushByte(regs.pc.address().msb());

    // Cycle 5
    stack.pushByte(regs.pc.address().lsb());

    // Cycle 6
    UInt8 adh = memory.fetch(regs.pc.address());
    regs.pc.inc();

    // Cycle 7
    regs.pc.set(new UInt16(adh, adl));

    return new Result(
        6,
        false,
        () -> new UInt8[] {opcode, adl, adh},
        () -> String.format("JSR $%s", new UInt16(adh, adl)));
  }
}
