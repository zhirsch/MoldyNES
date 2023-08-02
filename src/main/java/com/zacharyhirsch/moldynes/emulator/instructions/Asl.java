package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.NesAlu;
import com.zacharyhirsch.moldynes.emulator.NesCpuCycleContext;
import com.zacharyhirsch.moldynes.emulator.UInt8;

public final class Asl extends Instruction {

  private final UInt8 opcode;
  private final ReadModifyWriteInstructionHelper helper;

  public Asl(UInt8 opcode) {
    this.opcode = opcode;
    this.helper = new ReadModifyWriteInstructionHelper("ASL", opcode, this::asl);
  }

  @Override
  public Result execute(NesCpuCycleContext context) {
    return switch (Byte.toUnsignedInt(opcode.value())) {
      case 0x06 -> helper.executeZeropage(context);
      case 0x0a -> helper.executeAccumulator(context);
      case 0x0e -> helper.executeAbsolute(context);
      case 0x16 -> helper.executeZeropageX(context);
      case 0x1e -> helper.executeAbsoluteX(context);
      default -> throw new UnknownOpcodeException(opcode);
    };
  }

  private UInt8 asl(NesCpuCycleContext context, UInt8 data) {
    NesAlu.Result result = NesAlu.asl(data);
    context.registers().p.c = result.c();
    context.registers().p.n = result.n();
    context.registers().p.z = result.z();
    return result.output();
  }
}
