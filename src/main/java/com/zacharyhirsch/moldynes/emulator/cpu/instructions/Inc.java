package com.zacharyhirsch.moldynes.emulator.cpu.instructions;

import com.zacharyhirsch.moldynes.emulator.cpu.NesAlu;
import com.zacharyhirsch.moldynes.emulator.NesCpuCycleContext;
import com.zacharyhirsch.moldynes.emulator.UInt8;

public final class Inc extends Instruction {

  private final UInt8 opcode;
  private final ReadModifyWriteInstructionHelper helper;

  public Inc(UInt8 opcode) {
    this.opcode = opcode;
    this.helper = new ReadModifyWriteInstructionHelper("INC", opcode, this::inc);
  }

  @Override
  public Result execute(NesCpuCycleContext context) {
    return switch (Byte.toUnsignedInt(opcode.value())) {
      case 0xe6 -> helper.executeZeropage(context);
      case 0xee -> helper.executeAbsolute(context);
      case 0xf6 -> helper.executeZeropageX(context);
      case 0xfe -> helper.executeAbsoluteX(context);
      default -> throw new UnknownOpcodeException(opcode);
    };
  }

  private UInt8 inc(NesCpuCycleContext context, UInt8 data) {
    NesAlu.Result result = NesAlu.inc(data);
    context.registers().p.n = result.n();
    context.registers().p.z = result.z();
    return result.output();
  }
}
