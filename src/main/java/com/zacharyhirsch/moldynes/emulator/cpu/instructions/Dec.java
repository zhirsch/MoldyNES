package com.zacharyhirsch.moldynes.emulator.cpu.instructions;

import com.zacharyhirsch.moldynes.emulator.cpu.NesAlu;
import com.zacharyhirsch.moldynes.emulator.NesCpuCycleContext;
import com.zacharyhirsch.moldynes.emulator.UInt8;

public final class Dec extends Instruction {

  private final UInt8 opcode;
  private final ReadModifyWriteInstructionHelper helper;

  public Dec(UInt8 opcode) {
    this.opcode = opcode;
    this.helper = new ReadModifyWriteInstructionHelper("DEC", opcode, this::dec);
  }

  @Override
  public Result execute(NesCpuCycleContext context) {
    return switch (Byte.toUnsignedInt(opcode.value())) {
      case 0xc6 -> helper.executeZeropage(context);
      case 0xce -> helper.executeAbsolute(context);
      case 0xd6 -> helper.executeZeropageX(context);
      case 0xde -> helper.executeAbsoluteX(context);
      default -> throw new UnknownOpcodeException(opcode);
    };
  }

  private UInt8 dec(NesCpuCycleContext context, UInt8 data) {
    NesAlu.Result result = NesAlu.dec(data);
    context.registers().p.n = result.n();
    context.registers().p.z = result.z();
    return result.output();
  }
}
