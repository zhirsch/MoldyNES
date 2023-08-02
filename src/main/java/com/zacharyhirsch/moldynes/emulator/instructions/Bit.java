package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.NesAlu;
import com.zacharyhirsch.moldynes.emulator.NesCpuCycleContext;
import com.zacharyhirsch.moldynes.emulator.UInt8;

public final class Bit extends Instruction {

  private final UInt8 opcode;
  private final FetchInstructionHelper helper;

  public Bit(UInt8 opcode) {
    this.opcode = opcode;
    this.helper = new FetchInstructionHelper("BIT", opcode, this::bit);
  }

  @Override
  public Result execute(NesCpuCycleContext context) {
    return switch (Byte.toUnsignedInt(opcode.value())) {
      case 0x24 -> helper.fetchZeropage(context);
      case 0x2c -> helper.fetchAbsolute(context);
      default -> throw new UnknownOpcodeException(opcode);
    };
  }

  private void bit(NesCpuCycleContext context, UInt8 data) {
    NesAlu.Result result = NesAlu.and(context.registers().a, data);
    context.registers().p.z = result.z();
    context.registers().p.n = data.bit(7) == 1;
    context.registers().p.v = data.bit(6) == 1;
  }
}
