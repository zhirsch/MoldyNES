package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.NesCpuCycleContext;
import com.zacharyhirsch.moldynes.emulator.UInt8;

public class Sty extends Instruction {

  private final UInt8 opcode;
  private final StoreInstructionHelper helper;

  public Sty(UInt8 opcode) {
    this.opcode = opcode;
    this.helper = new StoreInstructionHelper("STY", opcode);
  }

  @Override
  public Result execute(NesCpuCycleContext context) {
    return switch (Byte.toUnsignedInt(opcode.value())) {
      case 0x84 -> helper.storeZeropage(context, () -> context.registers().y);
      case 0x8c -> helper.storeAbsolute(context, () -> context.registers().y);
      case 0x94 -> helper.storeZeropageX(context, () -> context.registers().y);
      default -> throw new UnknownOpcodeException(opcode);
    };
  }
}
