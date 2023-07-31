package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.NesCpuMemory;
import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.UInt16;
import com.zacharyhirsch.moldynes.emulator.UInt8;
import com.zacharyhirsch.moldynes.emulator.memory.AbsoluteAddress;
import com.zacharyhirsch.moldynes.emulator.memory.AccumulatorRegister;
import com.zacharyhirsch.moldynes.emulator.memory.Ignore;
import com.zacharyhirsch.moldynes.emulator.memory.ImmediateByte;
import com.zacharyhirsch.moldynes.emulator.memory.ImmediateWord;
import com.zacharyhirsch.moldynes.emulator.memory.Implicit;
import com.zacharyhirsch.moldynes.emulator.memory.IndexedAbsoluteAddress;
import com.zacharyhirsch.moldynes.emulator.memory.IndexedZeropageAddress;
import com.zacharyhirsch.moldynes.emulator.memory.IndirectAddress;
import com.zacharyhirsch.moldynes.emulator.memory.IndirectXAddress;
import com.zacharyhirsch.moldynes.emulator.memory.IndirectYAddress;
import com.zacharyhirsch.moldynes.emulator.memory.XIndex;
import com.zacharyhirsch.moldynes.emulator.memory.YIndex;
import com.zacharyhirsch.moldynes.emulator.memory.ZeropageAddress;

public class Decoder {

  private final NesCpuMemory memory;
  private final Registers regs;

  public Decoder(NesCpuMemory memory, Registers regs) {
    this.memory = memory;
    this.regs = regs;
  }

  public record Decoded(UInt16 pc, UInt8 opcode, Instruction instruction) {}

  public Decoded next() {
    UInt16 origPc = regs.pc.address();
    UInt8 opcode = fetchOpcode();
    Instruction instr = decodeInstruction(opcode);
    return new Decoded(origPc, opcode, instr);
  }

  private UInt8 fetchOpcode() {
    UInt8 opcode = memory.fetch(regs.pc.address());
    regs.pc.inc();
    return opcode;
  }

  private Instruction decodeInstruction(UInt8 opcode) {
    return switch (opcode.value()) {
      case (byte) 0x00 -> new Brk(immediateByte());
      case (byte) 0x01 -> new Ora(indirectX());
      case (byte) 0x03 -> new Undocumented(new Slo(indirectX()));
      case (byte) 0x04 -> new Undocumented(new Nop(ignore1()));
      case (byte) 0x05 -> new Ora(zeropage());
      case (byte) 0x06 -> new Asl(zeropage());
      case (byte) 0x07 -> new Undocumented(new Slo(zeropage()));
      case (byte) 0x08 -> new Php(implicit());
      case (byte) 0x09 -> new Ora(immediateByte());
      case (byte) 0x0a -> new Asl(accumulator());
      case (byte) 0x0c -> new Undocumented(new Nop(ignore2()));
      case (byte) 0x0d -> new Ora(absolute());
      case (byte) 0x0e -> new Asl(absolute());
      case (byte) 0x0f -> new Undocumented(new Slo(absolute()));
      case (byte) 0x10 -> new Bpl(regs, immediateByte());
      case (byte) 0x11 -> new Ora(indirectY());
      case (byte) 0x13 -> new Undocumented(new Slo(indirectY()));
      case (byte) 0x14 -> new Undocumented(new Nop(ignore1()));
      case (byte) 0x15 -> new Ora(zeropageX());
      case (byte) 0x16 -> new Asl(zeropageX());
      case (byte) 0x17 -> new Undocumented(new Slo(zeropageX()));
      case (byte) 0x18 -> new Clc(implicit());
      case (byte) 0x19 -> new Ora(absoluteY());
      case (byte) 0x1a -> new Undocumented(new Nop(implicit()));
      case (byte) 0x1b -> new Undocumented(new Slo(absoluteY()));
      case (byte) 0x1c -> new Undocumented(new Nop(ignore2()));
      case (byte) 0x1d -> new Ora(absoluteX());
      case (byte) 0x1e -> new Asl(absoluteX());
      case (byte) 0x1f -> new Undocumented(new Slo(absoluteX()));
      case (byte) 0x20 -> new Jsr(immediateWord());
      case (byte) 0x21 -> new And(indirectX());
      case (byte) 0x23 -> new Undocumented(new Rla(indirectX()));
      case (byte) 0x24 -> new Bit(zeropage());
      case (byte) 0x25 -> new And(zeropage());
      case (byte) 0x26 -> new Rol(zeropage());
      case (byte) 0x27 -> new Undocumented(new Rla(zeropage()));
      case (byte) 0x28 -> new Plp(implicit());
      case (byte) 0x29 -> new And(immediateByte());
      case (byte) 0x2a -> new Rol(accumulator());
      case (byte) 0x2c -> new Bit(absolute());
      case (byte) 0x2d -> new And(absolute());
      case (byte) 0x2e -> new Rol(absolute());
      case (byte) 0x2f -> new Undocumented(new Rla(absolute()));
      case (byte) 0x30 -> new Bmi(regs, immediateByte());
      case (byte) 0x31 -> new And(indirectY());
      case (byte) 0x32 -> new Undocumented(new Hlt(implicit()));
      case (byte) 0x33 -> new Undocumented(new Rla(indirectY()));
      case (byte) 0x34 -> new Undocumented(new Nop(ignore1()));
      case (byte) 0x35 -> new And(zeropageX());
      case (byte) 0x36 -> new Rol(zeropageX());
      case (byte) 0x37 -> new Undocumented(new Rla(zeropageX()));
      case (byte) 0x38 -> new Sec(implicit());
      case (byte) 0x39 -> new And(absoluteY());
      case (byte) 0x3a -> new Undocumented(new Nop(implicit()));
      case (byte) 0x3b -> new Undocumented(new Rla(absoluteY()));
      case (byte) 0x3c -> new Undocumented(new Nop(ignore2()));
      case (byte) 0x3d -> new And(absoluteX());
      case (byte) 0x3e -> new Rol(absoluteX());
      case (byte) 0x3f -> new Undocumented(new Rla(absoluteX()));
      case (byte) 0x40 -> new Rti(implicit());
      case (byte) 0x41 -> new Eor(indirectX());
      case (byte) 0x43 -> new Undocumented(new Sre(indirectX()));
      case (byte) 0x44 -> new Undocumented(new Nop(ignore1()));
      case (byte) 0x45 -> new Eor(zeropage());
      case (byte) 0x46 -> new Lsr(zeropage());
      case (byte) 0x47 -> new Undocumented(new Sre(zeropage()));
      case (byte) 0x48 -> new Pha(implicit());
      case (byte) 0x49 -> new Eor(immediateByte());
      case (byte) 0x4a -> new Lsr(accumulator());
      case (byte) 0x4c -> new Jmp(immediateWord());
      case (byte) 0x4d -> new Eor(absolute());
      case (byte) 0x4e -> new Lsr(absolute());
      case (byte) 0x4f -> new Undocumented(new Sre(absolute()));
      case (byte) 0x50 -> new Bvc(regs, immediateByte());
      case (byte) 0x51 -> new Eor(indirectY());
      case (byte) 0x53 -> new Undocumented(new Sre(indirectY()));
      case (byte) 0x54 -> new Undocumented(new Nop(ignore1()));
      case (byte) 0x55 -> new Eor(zeropageX());
      case (byte) 0x56 -> new Lsr(zeropageX());
      case (byte) 0x57 -> new Undocumented(new Sre(zeropageX()));
      case (byte) 0x58 -> new Cli(implicit());
      case (byte) 0x59 -> new Eor(absoluteY());
      case (byte) 0x5a -> new Undocumented(new Nop(implicit()));
      case (byte) 0x5b -> new Undocumented(new Sre(absoluteY()));
      case (byte) 0x5c -> new Undocumented(new Nop(ignore2()));
      case (byte) 0x5d -> new Eor(absoluteX());
      case (byte) 0x5e -> new Lsr(absoluteX());
      case (byte) 0x5f -> new Undocumented(new Sre(absoluteX()));
      case (byte) 0x60 -> new Rts(implicit());
      case (byte) 0x61 -> new Adc(indirectX());
      case (byte) 0x63 -> new Undocumented(new Rra(indirectX()));
      case (byte) 0x64 -> new Undocumented(new Nop(ignore1()));
      case (byte) 0x65 -> new Adc(zeropage());
      case (byte) 0x66 -> new Ror(zeropage());
      case (byte) 0x67 -> new Undocumented(new Rra(zeropage()));
      case (byte) 0x68 -> new Pla(implicit());
      case (byte) 0x69 -> new Adc(immediateByte());
      case (byte) 0x6a -> new Ror(accumulator());
      case (byte) 0x6c -> new Jmp(indirect());
      case (byte) 0x6d -> new Adc(absolute());
      case (byte) 0x6e -> new Ror(absolute());
      case (byte) 0x6f -> new Undocumented(new Rra(absolute()));
      case (byte) 0x70 -> new Bvs(regs, immediateByte());
      case (byte) 0x71 -> new Adc(indirectY());
      case (byte) 0x73 -> new Undocumented(new Rra(indirectY()));
      case (byte) 0x74 -> new Undocumented(new Nop(ignore1()));
      case (byte) 0x75 -> new Adc(zeropageX());
      case (byte) 0x76 -> new Ror(zeropageX());
      case (byte) 0x77 -> new Undocumented(new Rra(zeropageX()));
      case (byte) 0x78 -> new Sei(implicit());
      case (byte) 0x79 -> new Adc(absoluteY());
      case (byte) 0x7a -> new Undocumented(new Nop(implicit()));
      case (byte) 0x7b -> new Undocumented(new Rra(absoluteY()));
      case (byte) 0x7c -> new Undocumented(new Nop(ignore2()));
      case (byte) 0x7d -> new Adc(absoluteX());
      case (byte) 0x7e -> new Ror(absoluteX());
      case (byte) 0x7f -> new Undocumented(new Rra(absoluteX()));
      case (byte) 0x80 -> new Undocumented(new Nop(ignore1()));
      case (byte) 0x81 -> new Sta(indirectX());
      case (byte) 0x83 -> new Undocumented(new Sax(indirectX()));
      case (byte) 0x84 -> new Sty(zeropage());
      case (byte) 0x85 -> new Sta(zeropage());
      case (byte) 0x86 -> new Stx(zeropage());
      case (byte) 0x87 -> new Undocumented(new Sax(zeropage()));
      case (byte) 0x88 -> new Dey(implicit());
      case (byte) 0x89 -> new Undocumented(new Nop(ignore1()));
      case (byte) 0x8d -> new Sta(absolute());
      case (byte) 0x8a -> new Txa(implicit());
      case (byte) 0x8c -> new Sty(absolute());
      case (byte) 0x8e -> new Stx(absolute());
      case (byte) 0x8f -> new Undocumented(new Sax(absolute()));
      case (byte) 0x90 -> new Bcc(regs, immediateByte());
      case (byte) 0x91 -> new Sta(indirectY());
      case (byte) 0x94 -> new Sty(zeropageX());
      case (byte) 0x95 -> new Sta(zeropageX());
      case (byte) 0x96 -> new Stx(zeropageY());
      case (byte) 0x97 -> new Undocumented(new Sax(zeropageY()));
      case (byte) 0x98 -> new Tya(implicit());
      case (byte) 0x99 -> new Sta(absoluteY());
      case (byte) 0x9a -> new Txs(implicit());
      case (byte) 0x9d -> new Sta(absoluteX());
      case (byte) 0xa0 -> new Ldy(immediateByte());
      case (byte) 0xa1 -> new Lda(indirectX());
      case (byte) 0xa2 -> new Ldx(immediateByte());
      case (byte) 0xa3 -> new Undocumented(new Lax(indirectX()));
      case (byte) 0xa4 -> new Ldy(zeropage());
      case (byte) 0xa5 -> new Lda(zeropage());
      case (byte) 0xa6 -> new Ldx(zeropage());
      case (byte) 0xa7 -> new Undocumented(new Lax(zeropage()));
      case (byte) 0xa8 -> new Tay(implicit());
      case (byte) 0xa9 -> new Lda(immediateByte());
      case (byte) 0xaa -> new Tax(implicit());
      case (byte) 0xac -> new Ldy(absolute());
      case (byte) 0xad -> new Lda(absolute());
      case (byte) 0xae -> new Ldx(absolute());
      case (byte) 0xaf -> new Undocumented(new Lax(absolute()));
      case (byte) 0xb0 -> new Bcs(regs, immediateByte());
      case (byte) 0xb1 -> new Lda(indirectY());
      case (byte) 0xb3 -> new Undocumented(new Lax(indirectY()));
      case (byte) 0xb4 -> new Ldy(zeropageX());
      case (byte) 0xb5 -> new Lda(zeropageX());
      case (byte) 0xb6 -> new Ldx(zeropageY());
      case (byte) 0xb7 -> new Undocumented(new Lax(zeropageY()));
      case (byte) 0xb8 -> new Clv(implicit());
      case (byte) 0xb9 -> new Lda(absoluteY());
      case (byte) 0xba -> new Tsx(implicit());
      case (byte) 0xbc -> new Ldy(absoluteX());
      case (byte) 0xbd -> new Lda(absoluteX());
      case (byte) 0xbe -> new Ldx(absoluteY());
      case (byte) 0xbf -> new Undocumented(new Lax(absoluteY()));
      case (byte) 0xc0 -> new Cpy(immediateByte());
      case (byte) 0xc1 -> new Cmp(indirectX());
      case (byte) 0xc4 -> new Cpy(zeropage());
      case (byte) 0xc3 -> new Undocumented(new Dcp(indirectX()));
      case (byte) 0xc5 -> new Cmp(zeropage());
      case (byte) 0xc6 -> new Dec(zeropage());
      case (byte) 0xc7 -> new Undocumented(new Dcp(zeropage()));
      case (byte) 0xc8 -> new Iny(implicit());
      case (byte) 0xca -> new Dex(implicit());
      case (byte) 0xc9 -> new Cmp(immediateByte());
      case (byte) 0xcc -> new Cpy(absolute());
      case (byte) 0xcd -> new Cmp(absolute());
      case (byte) 0xce -> new Dec(absolute());
      case (byte) 0xcf -> new Undocumented(new Dcp(absolute()));
      case (byte) 0xd0 -> new Bne(regs, immediateByte());
      case (byte) 0xd1 -> new Cmp(indirectY());
      case (byte) 0xd3 -> new Undocumented(new Dcp(indirectY()));
      case (byte) 0xd4 -> new Undocumented(new Nop(ignore1()));
      case (byte) 0xd5 -> new Cmp(zeropageX());
      case (byte) 0xd6 -> new Dec(zeropageX());
      case (byte) 0xd7 -> new Undocumented(new Dcp(zeropageX()));
      case (byte) 0xd8 -> new Cld(implicit());
      case (byte) 0xd9 -> new Cmp(absoluteY());
      case (byte) 0xda -> new Undocumented(new Nop(implicit()));
      case (byte) 0xdb -> new Undocumented(new Dcp(absoluteY()));
      case (byte) 0xdc -> new Undocumented(new Nop(ignore2()));
      case (byte) 0xdd -> new Cmp(absoluteX());
      case (byte) 0xde -> new Dec(absoluteX());
      case (byte) 0xdf -> new Undocumented(new Dcp(absoluteX()));
      case (byte) 0xe0 -> new Cpx(immediateByte());
      case (byte) 0xe1 -> new Sbc(indirectX());
      case (byte) 0xe3 -> new Undocumented(new Isb(indirectX()));
      case (byte) 0xe4 -> new Cpx(zeropage());
      case (byte) 0xe5 -> new Sbc(zeropage());
      case (byte) 0xe6 -> new Inc(zeropage());
      case (byte) 0xe7 -> new Undocumented(new Isb(zeropage()));
      case (byte) 0xe8 -> new Inx(implicit());
      case (byte) 0xe9 -> new Sbc(immediateByte());
      case (byte) 0xea -> new Nop(implicit());
      case (byte) 0xeb -> new Undocumented(new Sbc(immediateByte()));
      case (byte) 0xec -> new Cpx(absolute());
      case (byte) 0xed -> new Sbc(absolute());
      case (byte) 0xee -> new Inc(absolute());
      case (byte) 0xef -> new Undocumented(new Isb(absolute()));
      case (byte) 0xf0 -> new Beq(regs, immediateByte());
      case (byte) 0xf1 -> new Sbc(indirectY());
      case (byte) 0xf3 -> new Undocumented(new Isb(indirectY()));
      case (byte) 0xf4 -> new Undocumented(new Nop(ignore1()));
      case (byte) 0xf5 -> new Sbc(zeropageX());
      case (byte) 0xf6 -> new Inc(zeropageX());
      case (byte) 0xf7 -> new Undocumented(new Isb(zeropageX()));
      case (byte) 0xf8 -> new Sed(implicit());
      case (byte) 0xf9 -> new Sbc(absoluteY());
      case (byte) 0xfa -> new Undocumented(new Nop(implicit()));
      case (byte) 0xfb -> new Undocumented(new Isb(absoluteY()));
      case (byte) 0xfc -> new Undocumented(new Nop(ignore2()));
      case (byte) 0xfd -> new Sbc(absoluteX());
      case (byte) 0xfe -> new Inc(absoluteX());
      case (byte) 0xff -> new Undocumented(new Isb(absoluteX()));
      default -> throw new UnknownOpcodeException(opcode);
    };
  }

  private Implicit implicit() {
    return new Implicit();
  }

  private Ignore ignore1() {
    UInt8 byte1 = memory.fetch(regs.pc.address());
    regs.pc.inc();
    return new Ignore(byte1);
  }

  private Ignore ignore2() {
    UInt8 byte1 = memory.fetch(regs.pc.address());
    regs.pc.inc();
    UInt8 byte2 = memory.fetch(regs.pc.address());
    regs.pc.inc();
    return new Ignore(byte1, byte2);
  }

  private AccumulatorRegister accumulator() {
    return new AccumulatorRegister(regs);
  }

  private ImmediateByte immediateByte() {
    ImmediateByte immediate = new ImmediateByte(memory.fetch(regs.pc.address()));
    regs.pc.inc();
    return immediate;
  }

  private ImmediateWord immediateWord() {
    UInt8 lsb = memory.fetch(regs.pc.address());
    regs.pc.inc();
    UInt8 msb = memory.fetch(regs.pc.address());
    regs.pc.inc();
    return new ImmediateWord(new UInt16(lsb, msb));
  }

  private ZeropageAddress zeropage() {
    UInt8 zeropage = memory.fetch(regs.pc.address());
    regs.pc.inc();
    return new ZeropageAddress(memory, zeropage);
  }

  private IndexedZeropageAddress zeropageX() {
    UInt8 zeropage = memory.fetch(regs.pc.address());
    regs.pc.inc();
    return new IndexedZeropageAddress(memory, zeropage, new XIndex(regs));
  }

  private IndexedZeropageAddress zeropageY() {
    UInt8 zeropage = memory.fetch(regs.pc.address());
    regs.pc.inc();
    return new IndexedZeropageAddress(memory, zeropage, new YIndex(regs));
  }

  private AbsoluteAddress absolute() {
    UInt8 lsb = memory.fetch(regs.pc.address());
    regs.pc.inc();
    UInt8 msb = memory.fetch(regs.pc.address());
    regs.pc.inc();
    return new AbsoluteAddress(memory, new UInt16(lsb, msb));
  }

  private IndexedAbsoluteAddress absoluteX() {
    UInt8 lsb = memory.fetch(regs.pc.address());
    regs.pc.inc();
    UInt8 msb = memory.fetch(regs.pc.address());
    regs.pc.inc();
    return new IndexedAbsoluteAddress(memory, new UInt16(lsb, msb), new XIndex(regs));
  }

  private IndexedAbsoluteAddress absoluteY() {
    UInt8 lsb = memory.fetch(regs.pc.address());
    regs.pc.inc();
    UInt8 msb = memory.fetch(regs.pc.address());
    regs.pc.inc();
    return new IndexedAbsoluteAddress(memory, new UInt16(lsb, msb), new YIndex(regs));
  }

  private IndirectAddress indirect() {
    UInt8 lsb = memory.fetch(regs.pc.address());
    regs.pc.inc();
    UInt8 msb = memory.fetch(regs.pc.address());
    regs.pc.inc();
    return new IndirectAddress(memory, new UInt16(lsb, msb));
  }

  private IndirectXAddress indirectX() {
    UInt8 zeropage = memory.fetch(regs.pc.address());
    regs.pc.inc();
    return new IndirectXAddress(memory, zeropage, new XIndex(regs));
  }

  private IndirectYAddress indirectY() {
    UInt8 zeropage = memory.fetch(regs.pc.address());
    regs.pc.inc();
    return new IndirectYAddress(memory, zeropage, new YIndex(regs));
  }
}
