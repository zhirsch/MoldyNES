package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.Registers;
import java.nio.ByteBuffer;

public interface Instruction {

  static Instruction decode(ByteBuffer ram) {
    byte opcode = ram.get();
    return switch (opcode) {
      case (byte) 0x10 -> new Bpl.Relative(ram.get());
      case (byte) 0x18 -> new Clc();
      case (byte) 0x30 -> new Bmi.Relative(ram.get());
      case (byte) 0x49 -> new Eor.Immediate(ram.get());
      case (byte) 0x4c -> new Jmp.Absolute(ram.getShort());
      case (byte) 0x69 -> new Adc.Immediate(ram.get());
      case (byte) 0x8d -> new Sta.Absolute(ram.getShort());
      case (byte) 0x88 -> new Dey();
      case (byte) 0x90 -> new Bcc.Relative(ram.get());
      case (byte) 0x98 -> new Tya();
      case (byte) 0x9a -> new Txs();
      case (byte) 0xa0 -> new Ldy.Immediate(ram.get());
      case (byte) 0xa2 -> new Ldx.Immediate(ram.get());
      case (byte) 0xa8 -> new Tay();
      case (byte) 0xa9 -> new Lda.Immediate(ram.get());
      case (byte) 0xaa -> new Tax();
      case (byte) 0xad -> new Lda.Absolute(ram.getShort());
      case (byte) 0xb0 -> new Bcs.Relative(ram.get());
      case (byte) 0xc0 -> new Cpy.Immediate(ram.get());
      case (byte) 0xca -> new Dex();
      case (byte) 0xc9 -> new Cmp.Immediate(ram.get());
      case (byte) 0xd0 -> new Bne.Relative(ram.get());
      case (byte) 0xd8 -> new Cld();
      case (byte) 0xe0 -> new Cpx.Immediate(ram.get());
      case (byte) 0xea -> new Nop();
      case (byte) 0xf0 -> new Beq.Relative(ram.get());
      default -> throw new UnknownOpcodeException(opcode);
    };
  }

  String describe();

  void execute(ByteBuffer ram, Registers regs);
}
