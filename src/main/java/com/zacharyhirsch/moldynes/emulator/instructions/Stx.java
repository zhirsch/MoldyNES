package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.NesCpuMemory;
import com.zacharyhirsch.moldynes.emulator.NesCpuStack;
import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.UInt8;

public class Stx extends Instruction {

  private final UInt8 opcode;
  private final InstructionHelper helper;

  public Stx(UInt8 opcode) {
    this.opcode = opcode;
    this.helper = new InstructionHelper("STX", opcode, this::stx);
  }

  @Override
  public Result execute2(NesCpuMemory memory, NesCpuStack stack, Registers regs) {
    return switch (Byte.toUnsignedInt(opcode.value())) {
      case 0x86 -> helper.executeZeropage(memory, stack, regs);
      case 0x8e -> helper.executeAbsolute(memory, stack, regs);
      case 0x96 -> helper.executeZeropageY(memory, stack, regs);
      default -> throw new UnknownOpcodeException(opcode);
    };
  }

  private void stx(NesCpuMemory memory, NesCpuStack stack, Registers regs, UInt8 data) {
    memory.store(data, regs.x);
  }
}
