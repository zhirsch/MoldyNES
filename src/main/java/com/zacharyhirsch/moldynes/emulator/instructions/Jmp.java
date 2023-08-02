package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.*;

public final class Jmp extends Instruction {

  private final UInt8 opcode;

  public Jmp(UInt8 opcode) {
    this.opcode = opcode;
  }

  @Override
  public Result execute(NesCpuCycleContext context) {
    return switch (opcode.value()) {
      case (byte) 0x4c -> executeAbsolute(context);
      case (byte) 0x6c -> executeIndirect(context);
      default -> throw new UnknownOpcodeException(opcode);
    };
  }

  private Result executeAbsolute(NesCpuCycleContext context) {
    // Cycle 2
    UInt8 adl = context.fetch(context.registers().pc.getAddressAndIncrement());

    // Cycle 3
    UInt8 adh = context.fetch(context.registers().pc.getAddressAndIncrement());

    // Cycle 4
    context.registers().pc.set(new UInt16(adh, adl));

    return new Result(
        () -> new UInt8[] {opcode, adl, adh}, () -> String.format("JMP $%s", new UInt16(adh, adl)));
  }

  private Result executeIndirect(NesCpuCycleContext context) {
    // Cycle 2
    UInt8 ial = context.fetch(context.registers().pc.getAddressAndIncrement());

    // Cycle 3
    UInt8 iah = context.fetch(context.registers().pc.getAddressAndIncrement());

    // Cycle 4
    UInt8 adl = context.fetch(new UInt16(iah, ial));
    UInt8 ial1 = NesAlu.add(ial, UInt8.cast(1)).output();

    // Cycle 5
    UInt8 adh = context.fetch(new UInt16(iah, ial1));

    // Cycle 6
    context.registers().pc.set(new UInt16(adh, adl));

    return new Result(
        () -> new UInt8[] {opcode, ial, iah},
        () -> String.format("JMP ($%s) = %s", new UInt16(iah, ial), new UInt16(adh, adl)));
  }
}
