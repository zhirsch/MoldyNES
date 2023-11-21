package com.zacharyhirsch.moldynes.emulator.cpu.instructions;

import com.zacharyhirsch.moldynes.emulator.cpu.NesAlu;
import com.zacharyhirsch.moldynes.emulator.NesCpuCycleContext;
import com.zacharyhirsch.moldynes.emulator.UInt8;

public final class Ror extends Instruction {

  private final UInt8 opcode;
  private final ReadModifyWriteInstructionHelper helper;

  public Ror(UInt8 opcode) {
    this.opcode = opcode;
    this.helper = new ReadModifyWriteInstructionHelper("ROR", opcode, this::ror);
  }

  @Override
  public Result execute(NesCpuCycleContext context) {
    return switch (Byte.toUnsignedInt(opcode.value())) {
      case 0x66 -> helper.executeZeropage(context);
      case 0x6a -> helper.executeAccumulator(context);
      case 0x6e -> helper.executeAbsolute(context);
      case 0x76 -> helper.executeZeropageX(context);
      case 0x7e -> helper.executeAbsoluteX(context);
      default -> throw new UnknownOpcodeException(opcode);
    };
  }

  private UInt8 ror(NesCpuCycleContext context, UInt8 data) {
    NesAlu.Result result = NesAlu.ror(data, context.registers().p.c);
    context.registers().p.c = result.c();
    context.registers().p.n = result.n();
    context.registers().p.z = result.z();
    return result.output();
  }
}
