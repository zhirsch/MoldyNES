package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.NesCpuCycleContext;
import com.zacharyhirsch.moldynes.emulator.UInt8;

public class Stx extends Instruction {

  private final UInt8 opcode;
  private final StoreInstructionHelper helper;

  public Stx(UInt8 opcode) {
    this.opcode = opcode;
    this.helper = new StoreInstructionHelper("STX", opcode);
  }

  @Override
  public Result execute(NesCpuCycleContext context) {
    return switch (Byte.toUnsignedInt(opcode.value())) {
      case 0x86 -> helper.storeZeropage(context, () -> context.registers().x);
      case 0x8e -> helper.storeAbsolute(context, () -> context.registers().x);
      case 0x96 -> helper.storeZeropageY(context, () -> context.registers().x);
      default -> throw new UnknownOpcodeException(opcode);
    };
  }
}
