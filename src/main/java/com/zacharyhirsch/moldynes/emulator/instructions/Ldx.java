package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.NesCpuMemory;
import com.zacharyhirsch.moldynes.emulator.NesCpuStack;
import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.UInt8;

public final class Ldx extends Instruction {

  private final UInt8 opcode;
  private final InstructionHelper helper;

  public Ldx(UInt8 opcode) {
    this.opcode = opcode;
    this.helper = new InstructionHelper("LDX", opcode, this::ldx);
  }

  @Override
  public Result execute2(NesCpuMemory memory, NesCpuStack stack, Registers regs) {
    return switch (Byte.toUnsignedInt(opcode.value())) {
      case 0xa2 -> helper.executeImmediate(memory, stack, regs);
      case 0xa6 -> helper.executeZeropage(memory, stack, regs);
      case 0xae -> helper.executeAbsolute(memory, stack, regs);
      case 0xb6 -> helper.executeZeropageY(memory, stack, regs);
      case 0xbe -> helper.executeAbsoluteY(memory, stack, regs);
      default -> throw new UnknownOpcodeException(opcode);
    };
  }

  private void ldx(NesCpuMemory memory, NesCpuStack stack, Registers regs, UInt8 data) {
    regs.x = data;
    regs.p.n = regs.x.bit(7) == 1;
    regs.p.z = regs.x.isZero();
  }
}
