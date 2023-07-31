package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.*;

public final class Jmp extends Instruction {

  private final UInt8 opcode;

  public Jmp(UInt8 opcode) {
    this.opcode = opcode;
  }

  @Override
  public Result execute2(NesCpuMemory memory, NesCpuStack stack, Registers regs) {
    return switch (opcode.value()) {
      case (byte) 0x4c -> executeAbsolute(memory, regs);
      case (byte) 0x6c -> executeIndirect(memory, regs);
      default -> throw new UnknownOpcodeException(opcode);
    };
  }

  private Result executeAbsolute(NesCpuMemory memory, Registers regs) {
    // Cycle 2
    UInt8 adl = memory.fetch(regs.pc.address());
    regs.pc.inc();

    // Cycle 3
    UInt8 adh = memory.fetch(regs.pc.address());
    regs.pc.inc();

    // Cycle 4
    regs.pc.set(new UInt16(adh, adl));

    return new Result(
        3,
        false,
        () -> new UInt8[] {opcode, adl, adh},
        () -> String.format("JMP $%s", new UInt16(adh, adl)));
  }

  private Result executeIndirect(NesCpuMemory memory, Registers regs) {
    // Cycle 2
    UInt8 ial = memory.fetch(regs.pc.address());
    regs.pc.inc();

    // Cycle 3
    UInt8 iah = memory.fetch(regs.pc.address());
    regs.pc.inc();

    // Cycle 4
    UInt8 adl = memory.fetch(new UInt16(iah, ial));
    UInt8 ial1 = NesAlu.add(ial, UInt8.cast(1)).output();

    // Cycle 5
    UInt8 adh = memory.fetch(new UInt16(iah, ial1));

    // Cycle 6
    regs.pc.set(new UInt16(adh, adl));

    return new Result(
        5,
        false,
        () -> new UInt8[] {opcode, ial, iah},
        () -> String.format("JMP $%s", new UInt16(adh, adl)));
  }
}
