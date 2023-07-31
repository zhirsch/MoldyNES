package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.*;

public final class Ora extends Instruction {

  private final UInt8 opcode;
  private final InstructionHelper helper;

  public Ora(UInt8 opcode) {
    this.opcode = opcode;
    this.helper = new InstructionHelper("ORA", opcode, this::or);
  }

  @Override
  public Result execute2(NesCpuMemory memory, NesCpuStack stack, Registers regs) {
    return switch (Byte.toUnsignedInt(opcode.value())) {
      case 0x01 -> executeIndirectX(memory, stack, regs);
      case 0x05 -> helper.executeZeropage(memory, stack, regs);
      case 0x09 -> helper.executeImmediate(memory, stack, regs);
      case 0x0d -> helper.executeAbsolute(memory, stack, regs);
      case 0x11 -> executeIndirectY(memory, stack, regs);
      case 0x15 -> helper.executeZeropageX(memory, stack, regs);
      case 0x19 -> helper.executeAbsoluteY(memory, stack, regs);
      case 0x1d -> helper.executeAbsoluteX(memory, stack, regs);
      default -> throw new UnknownOpcodeException(opcode);
    };
  }

  private Result executeIndirectX(NesCpuMemory memory, NesCpuStack stack, Registers regs) {
    // Cycle 2
    UInt8 bal = memory.fetch(regs.pc.address());
    regs.pc.inc();

    // Cycle 3
    UInt8 ignored = memory.fetch(new UInt16(UInt8.cast(0), bal));
    UInt8 balX = NesAlu.add(bal, regs.x).output();

    // Cycle 4
    UInt8 adl = memory.fetch(new UInt16(UInt8.cast(0), balX));
    UInt8 balX1 = NesAlu.add(balX, UInt8.cast(1)).output();

    // Cycle 5
    UInt8 adh = memory.fetch(new UInt16(UInt8.cast(0), balX1));

    // Cycle 6
    UInt8 data = memory.fetch(new UInt16(adh, adl));

    // Cycle 7
    or(memory, stack, regs, data);

    return new Result(
        6,
        false,
        () -> new UInt8[] {opcode, bal},
        () ->
            String.format(
                "%s ($%s,X) @ %s = %s = %s", "ORA", bal, bal, new UInt16(adh, adl), data));
  }

  private Result executeIndirectY(NesCpuMemory memory, NesCpuStack stack, Registers regs) {
    // Cycle 2
    UInt8 ial = memory.fetch(regs.pc.address());
    regs.pc.inc();

    // Cycle 3
    UInt8 bal = memory.fetch(new UInt16(UInt8.cast(0), ial));
    NesAlu.Result ial1Result = NesAlu.add(ial, UInt8.cast(1));
    UInt8 ial1 = ial1Result.output();

    if (ial1Result.c()) {
      // Cycle 4 -- Page Crossed
      UInt8 bah = memory.fetch(new UInt16(UInt8.cast(0), ial1));
      UInt8 balY = NesAlu.add(bal, regs.y).output();

      // Cycle 5
      UInt8 ignored = memory.fetch(new UInt16(bah, balY));
      UInt8 bah1 = NesAlu.add(bah, UInt8.cast(1)).output();

      // Cycle 6
      UInt8 data = memory.fetch(new UInt16(bah1, balY));

      // Cycle 7
      or(memory, stack, regs, data);

      return new Result(
          6,
          false,
          () -> new UInt8[] {opcode, ial},
          () ->
              String.format(
                  "%s ($%s),Y = %s @ %s = %s",
                  "ORA", ial, new UInt16(bah1, balY), new UInt16(bah1, balY), data));
    }

    // Cycle 4 -- Same Page
    UInt8 bah = memory.fetch(new UInt16(UInt8.cast(0), ial1));
    UInt8 balY = NesAlu.add(bal, regs.y).output();

    // Cycle 5
    UInt8 data = memory.fetch(new UInt16(bah, balY));

    // Cycle 6
    or(memory, stack, regs, data);

    return new Result(
        5,
        false,
        () -> new UInt8[] {opcode, ial},
        () ->
            String.format(
                "%s ($%s),Y = %s @ %s = %s",
                "ORA", ial, new UInt16(bah, balY), new UInt16(bah, balY), data));
  }

  private void or(NesCpuMemory memory, NesCpuStack stack, Registers regs, UInt8 data) {
    NesAlu.Result result = NesAlu.or(regs.a, data);
    regs.a = result.output();
    regs.p.n = result.n();
    regs.p.z = result.z();
  }
}
