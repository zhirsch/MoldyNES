package com.zacharyhirsch.moldynes.emulator.cpu.instructions;

import com.zacharyhirsch.moldynes.emulator.cpu.NesAlu;
import com.zacharyhirsch.moldynes.emulator.NesCpuCycleContext;
import com.zacharyhirsch.moldynes.emulator.UInt8;

public final class Lsr extends Instruction {

  private final UInt8 opcode;
  private final ReadModifyWriteInstructionHelper helper;

  public Lsr(UInt8 opcode) {
    this.opcode = opcode;
    this.helper = new ReadModifyWriteInstructionHelper("LSR", opcode, this::lsr);
  }

  @Override
  public Result execute(NesCpuCycleContext context) {
    return switch (Byte.toUnsignedInt(opcode.value())) {
      case 0x46 -> helper.executeZeropage(context);
      case 0x4a -> helper.executeAccumulator(context);
      case 0x4e -> helper.executeAbsolute(context);
      case 0x56 -> helper.executeZeropageX(context);
      case 0x5e -> helper.executeAbsoluteX(context);
      default -> throw new UnknownOpcodeException(opcode);
    };
  }

  private UInt8 lsr(NesCpuCycleContext context, UInt8 data) {
    NesAlu.Result result = NesAlu.lsr(data);
    context.registers().p.c = result.c();
    context.registers().p.n = result.n();
    context.registers().p.z = result.z();
    return result.output();
  }
}
