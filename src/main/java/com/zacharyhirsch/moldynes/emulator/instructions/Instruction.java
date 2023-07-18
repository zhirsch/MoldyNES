package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.Ram;
import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.Stack;

public interface Instruction {

  static Instruction decode(Ram ram, Registers regs) {
    byte opcode = next(ram, regs);
    return switch (opcode) {
      case (byte) 0x00 -> new Brk();
      case (byte) 0x08 -> new Php();
      case (byte) 0x09 -> new Ora.Immediate(next(ram, regs));
      case (byte) 0x10 -> new Bpl.Relative(next(ram, regs));
      case (byte) 0x18 -> new Clc();
      case (byte) 0x20 -> new Jsr.Absolute(nextShort(ram, regs));
      case (byte) 0x28 -> new Plp();
      case (byte) 0x30 -> new Bmi.Relative(next(ram, regs));
      case (byte) 0x38 -> new Sec();
      case (byte) 0x40 -> new Rti();
      case (byte) 0x48 -> new Pha();
      case (byte) 0x49 -> new Eor.Immediate(next(ram, regs));
      case (byte) 0x4c -> new Jmp.Absolute(nextShort(ram, regs));
      case (byte) 0x50 -> new Bvc.Relative(next(ram, regs));
      case (byte) 0x58 -> new Cli();
      case (byte) 0x60 -> new Rts();
      case (byte) 0x68 -> new Pla();
      case (byte) 0x69 -> new Adc.Immediate(next(ram, regs));
      case (byte) 0x6c -> new Jmp.Indirect(nextShort(ram, regs));
      case (byte) 0x70 -> new Bvs.Relative(next(ram, regs));
      case (byte) 0x78 -> new Sei();
      case (byte) 0x84 -> new Sty.Zeropage(next(ram, regs));
      case (byte) 0x85 -> new Sta.Zeropage(next(ram, regs));
      case (byte) 0x86 -> new Stx.Zeropage(next(ram, regs));
      case (byte) 0x88 -> new Dey();
      case (byte) 0x8d -> new Sta.Absolute(nextShort(ram, regs));
      case (byte) 0x8a -> new Txa();
      case (byte) 0x8c -> new Sty.Absolute(nextShort(ram, regs));
      case (byte) 0x8e -> new Stx.Absolute(nextShort(ram, regs));
      case (byte) 0x90 -> new Bcc.Relative(next(ram, regs));
      case (byte) 0x94 -> new Sty.ZeropageX(next(ram, regs));
      case (byte) 0x95 -> new Sta.ZeropageX(next(ram, regs));
      case (byte) 0x96 -> new Stx.ZeropageY(next(ram, regs));
      case (byte) 0x98 -> new Tya();
      case (byte) 0x99 -> new Sta.AbsoluteY(nextShort(ram, regs));
      case (byte) 0x9a -> new Txs();
      case (byte) 0x9d -> new Sta.AbsoluteX(nextShort(ram, regs));
      case (byte) 0xa0 -> new Ldy.Immediate(next(ram, regs));
      case (byte) 0xa2 -> new Ldx.Immediate(next(ram, regs));
      case (byte) 0xa4 -> new Ldy.Zeropage(next(ram, regs));
      case (byte) 0xa5 -> new Lda.Zeropage(next(ram, regs));
      case (byte) 0xa6 -> new Ldx.Zeropage(next(ram, regs));
      case (byte) 0xa8 -> new Tay();
      case (byte) 0xa9 -> new Lda.Immediate(next(ram, regs));
      case (byte) 0xaa -> new Tax();
      case (byte) 0xac -> new Ldy.Absolute(nextShort(ram, regs));
      case (byte) 0xad -> new Lda.Absolute(nextShort(ram, regs));
      case (byte) 0xae -> new Ldx.Absolute(nextShort(ram, regs));
      case (byte) 0xb0 -> new Bcs.Relative(next(ram, regs));
      case (byte) 0xb4 -> new Ldy.ZeropageX(next(ram, regs));
      case (byte) 0xb5 -> new Lda.ZeropageX(next(ram, regs));
      case (byte) 0xb6 -> new Ldx.ZeropageY(next(ram, regs));
      case (byte) 0xb8 -> new Clv();
      case (byte) 0xb9 -> new Lda.AbsoluteY(nextShort(ram, regs));
      case (byte) 0xba -> new Tsx();
      case (byte) 0xbc -> new Ldy.AbsoluteX(nextShort(ram, regs));
      case (byte) 0xbd -> new Lda.AbsoluteX(nextShort(ram, regs));
      case (byte) 0xbe -> new Ldx.AbsoluteY(nextShort(ram, regs));
      case (byte) 0xc0 -> new Cpy.Immediate(next(ram, regs));
      case (byte) 0xc4 -> new Cpy.Zeropage(next(ram, regs));
      case (byte) 0xc5 -> new Cmp.Zeropage(next(ram, regs));
      case (byte) 0xc8 -> new Iny();
      case (byte) 0xca -> new Dex();
      case (byte) 0xc9 -> new Cmp.Immediate(next(ram, regs));
      case (byte) 0xcc -> new Cpy.Absolute(nextShort(ram, regs));
      case (byte) 0xcd -> new Cmp.Absolute(nextShort(ram, regs));
      case (byte) 0xd0 -> new Bne.Relative(next(ram, regs));
      case (byte) 0xd5 -> new Cmp.ZeropageX(next(ram, regs));
      case (byte) 0xd8 -> new Cld();
      case (byte) 0xd9 -> new Cmp.AbsoluteY(nextShort(ram, regs));
      case (byte) 0xdd -> new Cmp.AbsoluteX(nextShort(ram, regs));
      case (byte) 0xe0 -> new Cpx.Immediate(next(ram, regs));
      case (byte) 0xe4 -> new Cpx.Zeropage(next(ram, regs));
      case (byte) 0xec -> new Cpx.Absolute(nextShort(ram, regs));
      case (byte) 0xe8 -> new Inx();
      case (byte) 0xea -> new Nop();
      case (byte) 0xf0 -> new Beq.Relative(next(ram, regs));
      case (byte) 0xf8 -> new Sed();
      default -> throw new UnknownOpcodeException(opcode);
    };
  }

  private static byte next(Ram ram, Registers regs) {
    byte value = ram.getAbsolute(regs.pc);
    regs.pc += 1;
    return value;
  }

  private static short nextShort(Ram ram, Registers regs) {
    short value = ram.getShortAbsolute(regs.pc);
    regs.pc += 2;
    return value;
  }

  String describe();

  void execute(Ram ram, Registers regs, Stack stack);
}
