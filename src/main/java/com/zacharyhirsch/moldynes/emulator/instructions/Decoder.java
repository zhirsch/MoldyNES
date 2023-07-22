package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.Program;
import com.zacharyhirsch.moldynes.emulator.Ram;
import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.memory.*;

public class Decoder {

  private final Ram ram;
  private final Registers regs;
  private final Program program;

  public Decoder(Ram ram, Registers regs, Program program) {
    this.ram = ram;
    this.regs = regs;
    this.program = program;
  }

  public Instruction decode() {
    byte opcode = opcode();
    return switch (opcode) {
      case (byte) 0x00 -> new Brk(ram);
      case (byte) 0x08 -> new Php();
      case (byte) 0x09 -> new Ora(immediate(Byte.class));
      case (byte) 0x10 -> new Bpl(immediate(Byte.class));
      case (byte) 0x18 -> new Clc();
      case (byte) 0x20 -> new Jsr(immediate(Short.class));
      case (byte) 0x28 -> new Plp();
      case (byte) 0x30 -> new Bmi(immediate(Byte.class));
      case (byte) 0x38 -> new Sec();
      case (byte) 0x40 -> new Rti();
      case (byte) 0x48 -> new Pha();
      case (byte) 0x49 -> new Eor(immediate(Byte.class));
      case (byte) 0x4c -> new Jmp(immediate(Short.class));
      case (byte) 0x50 -> new Bvc(immediate(Byte.class));
      case (byte) 0x58 -> new Cli();
      case (byte) 0x60 -> new Rts();
      case (byte) 0x68 -> new Pla();
      case (byte) 0x69 -> new Adc(immediate(Byte.class));
      case (byte) 0x6c -> new Jmp(indirect());
      case (byte) 0x70 -> new Bvs(immediate(Byte.class));
      case (byte) 0x78 -> new Sei();
      case (byte) 0x84 -> new Sty(zeropage());
      case (byte) 0x85 -> new Sta(zeropage());
      case (byte) 0x86 -> new Stx(zeropage());
      case (byte) 0x88 -> new Dey();
      case (byte) 0x8d -> new Sta(absolute());
      case (byte) 0x8a -> new Txa();
      case (byte) 0x8c -> new Sty(absolute());
      case (byte) 0x8e -> new Stx(absolute());
      case (byte) 0x90 -> new Bcc(immediate(Byte.class));
      case (byte) 0x94 -> new Sty(zeropage(new XIndex(regs)));
      case (byte) 0x95 -> new Sta(zeropage(new XIndex(regs)));
      case (byte) 0x96 -> new Stx(zeropage(new YIndex(regs)));
      case (byte) 0x98 -> new Tya();
      case (byte) 0x99 -> new Sta(absolute(new YIndex(regs)));
      case (byte) 0x9a -> new Txs();
      case (byte) 0x9d -> new Sta(absolute(new XIndex(regs)));
      case (byte) 0xa0 -> new Ldy(immediate(Byte.class));
      case (byte) 0xa2 -> new Ldx(immediate(Byte.class));
      case (byte) 0xa4 -> new Ldy(zeropage());
      case (byte) 0xa5 -> new Lda(zeropage());
      case (byte) 0xa6 -> new Ldx(zeropage());
      case (byte) 0xa8 -> new Tay();
      case (byte) 0xa9 -> new Lda(immediate(Byte.class));
      case (byte) 0xaa -> new Tax();
      case (byte) 0xac -> new Ldy(absolute());
      case (byte) 0xad -> new Lda(absolute());
      case (byte) 0xae -> new Ldx(absolute());
      case (byte) 0xb0 -> new Bcs(immediate(Byte.class));
      case (byte) 0xb4 -> new Ldy(zeropage(new XIndex(regs)));
      case (byte) 0xb5 -> new Lda(zeropage(new XIndex(regs)));
      case (byte) 0xb6 -> new Ldx(zeropage(new YIndex(regs)));
      case (byte) 0xb8 -> new Clv();
      case (byte) 0xb9 -> new Lda(absolute(new YIndex(regs)));
      case (byte) 0xba -> new Tsx();
      case (byte) 0xbc -> new Ldy(absolute(new XIndex(regs)));
      case (byte) 0xbd -> new Lda(absolute(new XIndex(regs)));
      case (byte) 0xbe -> new Ldx(absolute(new YIndex(regs)));
      case (byte) 0xc0 -> new Cpy(immediate(Byte.class));
      case (byte) 0xc4 -> new Cpy(zeropage());
      case (byte) 0xc5 -> new Cmp(zeropage());
      case (byte) 0xc8 -> new Iny();
      case (byte) 0xca -> new Dex();
      case (byte) 0xc9 -> new Cmp(immediate(Byte.class));
      case (byte) 0xcc -> new Cpy(absolute());
      case (byte) 0xcd -> new Cmp(absolute());
      case (byte) 0xd0 -> new Bne(immediate(Byte.class));
      case (byte) 0xd5 -> new Cmp(zeropage(new XIndex(regs)));
      case (byte) 0xd8 -> new Cld();
      case (byte) 0xd9 -> new Cmp(absolute(new YIndex(regs)));
      case (byte) 0xdd -> new Cmp(absolute(new XIndex(regs)));
      case (byte) 0xe0 -> new Cpx(immediate(Byte.class));
      case (byte) 0xe4 -> new Cpx(zeropage());
      case (byte) 0xec -> new Cpx(absolute());
      case (byte) 0xe8 -> new Inx();
      case (byte) 0xea -> new Nop();
      case (byte) 0xf0 -> new Beq(immediate(Byte.class));
      case (byte) 0xf8 -> new Sed();
      default -> throw new UnknownOpcodeException(opcode);
    };
  }

  private byte opcode() {
    return program.fetchOpcode();
  }

  private AbsoluteAddress absolute() {
    return new AbsoluteAddress(ram, program.fetchArgument(Short.class));
  }

  private IndexedAbsoluteAddress absolute(Index index) {
    return new IndexedAbsoluteAddress(ram, program.fetchArgument(Short.class), index);
  }

  private ZeropageAddress zeropage() {
    return new ZeropageAddress(ram, program.fetchArgument(Byte.class));
  }

  private IndexedZeropageAddress zeropage(Index index) {
    return new IndexedZeropageAddress(ram, program.fetchArgument(Byte.class), index);
  }

  private IndirectAddress indirect() {
    return new IndirectAddress(ram, program.fetchArgument(Short.class));
  }

  private <T extends Number> Immediate<T> immediate(Class<T> clazz) {
    return new Immediate<>(program.fetchArgument(clazz));
  }
}
