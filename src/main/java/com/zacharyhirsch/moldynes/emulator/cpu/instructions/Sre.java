package com.zacharyhirsch.moldynes.emulator.cpu.instructions;

import com.zacharyhirsch.moldynes.emulator.cpu.NesAlu;
import com.zacharyhirsch.moldynes.emulator.NesCpuCycleContext;
import com.zacharyhirsch.moldynes.emulator.UInt8;

public final class Sre extends Instruction {

  private final UInt8 opcode;
  private final ReadModifyWriteInstructionHelper helper;

  public Sre(UInt8 opcode) {
    this.opcode = opcode;
    this.helper = new ReadModifyWriteInstructionHelper("SRE", opcode, this::sre);
  }

  @Override
  public Result execute(NesCpuCycleContext context) {
    return switch (Byte.toUnsignedInt(opcode.value())) {
      case 0x43 -> helper.executeIndirectX(context);
      case 0x47 -> helper.executeZeropage(context);
      case 0x4f -> helper.executeAbsolute(context);
      case 0x53 -> helper.executeIndirectY(context);
      case 0x57 -> helper.executeZeropageX(context);
      case 0x5b -> helper.executeAbsoluteY(context);
      case 0x5f -> helper.executeAbsoluteX(context);
      default -> throw new UnknownOpcodeException(opcode);
    };
  }

  private UInt8 sre(NesCpuCycleContext context, UInt8 data) {
    NesAlu.Result lsr = NesAlu.lsr(data);
    context.registers().p.c = lsr.c();

    NesAlu.Result eor = NesAlu.xor(context.registers().a, lsr.output());
    context.registers().a = eor.output();
    context.registers().p.n = eor.n();
    context.registers().p.z = eor.z();

    return lsr.output();
  }
}
