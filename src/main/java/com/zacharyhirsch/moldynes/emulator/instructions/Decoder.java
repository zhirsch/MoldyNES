package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.NesCpuMemory;
import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.memory.*;
import java.util.function.Function;

public class Decoder {

  private final NesCpuMemory memory;
  private final Registers regs;

  public Decoder(NesCpuMemory memory, Registers regs) {
    this.memory = memory;
    this.regs = regs;
  }

  public record Decoded(short pc, byte[] bytes, Instruction instruction) {

    @Override
    public String toString() {
      return String.format("%04x : %s", pc, instruction);
    }
  }

  public Decoded next() {
    byte opcode = memory.fetchByte(regs.pc);
    Instruction instruction = decode(opcode).apply((short) (regs.pc + 1));
    byte argByte1 = memory.fetchByte((short) (regs.pc + 1));
    byte argByte2 = memory.fetchByte((short) (regs.pc + 2));
    byte[] bytes =
        switch (instruction.getSize()) {
          case 1 -> new byte[] {opcode};
          case 2 -> new byte[] {opcode, argByte1};
          case 3 -> new byte[] {opcode, argByte1, argByte2};
          default -> throw new RuntimeException("impossible");
        };
    return new Decoded(regs.pc, bytes, instruction);
  }

  private Function<Short, Instruction> decode(byte opcode) {
    return switch (opcode) {
      case (byte) 0x00 -> address -> new Brk(implicit());
      case (byte) 0x01 -> address -> new Ora(indirectX(address));
      case (byte) 0x04 -> address -> new Undocumented(new Nop(zeropage(address)));
      case (byte) 0x05 -> address -> new Ora(zeropage(address));
      case (byte) 0x06 -> address -> new Asl(zeropage(address));
      case (byte) 0x08 -> address -> new Php(implicit());
      case (byte) 0x09 -> address -> new Ora(immediateByte(address));
      case (byte) 0x0a -> address -> new Asl(accumulator(address));
      case (byte) 0x0c -> address -> new Undocumented(new Nop(absolute(address)));
      case (byte) 0x0d -> address -> new Ora(absolute(address));
      case (byte) 0x0e -> address -> new Asl(absolute(address));
      case (byte) 0x10 -> address -> new Bpl(regs, immediateByte(address));
      case (byte) 0x11 -> address -> new Ora(indirectY(address));
      case (byte) 0x14 -> address -> new Undocumented(new Nop(zeropageX(address)));
      case (byte) 0x15 -> address -> new Ora(zeropageX(address));
      case (byte) 0x16 -> address -> new Asl(zeropageX(address));
      case (byte) 0x18 -> address -> new Clc(implicit());
      case (byte) 0x19 -> address -> new Ora(absoluteY(address));
      case (byte) 0x1a -> address -> new Undocumented(new Nop(implicit()));
      case (byte) 0x1c -> address -> new Undocumented(new Nop(absoluteX(address)));
      case (byte) 0x1d -> address -> new Ora(absoluteX(address));
      case (byte) 0x1e -> address -> new Asl(absoluteX(address));
      case (byte) 0x20 -> address -> new Jsr(immediateShort(address));
      case (byte) 0x21 -> address -> new And(indirectX(address));
      case (byte) 0x24 -> address -> new Bit(zeropage(address));
      case (byte) 0x25 -> address -> new And(zeropage(address));
      case (byte) 0x26 -> address -> new Rol(zeropage(address));
      case (byte) 0x28 -> address -> new Plp(implicit());
      case (byte) 0x29 -> address -> new And(immediateByte(address));
      case (byte) 0x2a -> address -> new Rol(accumulator(address));
      case (byte) 0x2c -> address -> new Bit(absolute(address));
      case (byte) 0x2d -> address -> new And(absolute(address));
      case (byte) 0x2e -> address -> new Rol(absolute(address));
      case (byte) 0x30 -> address -> new Bmi(regs, immediateByte(address));
      case (byte) 0x31 -> address -> new And(indirectY(address));
      case (byte) 0x34 -> address -> new Undocumented(new Nop(zeropageX(address)));
      case (byte) 0x35 -> address -> new And(zeropageX(address));
      case (byte) 0x36 -> address -> new Rol(zeropageX(address));
      case (byte) 0x38 -> address -> new Sec();
      case (byte) 0x39 -> address -> new And(absoluteY(address));
      case (byte) 0x3a -> address -> new Undocumented(new Nop(implicit()));
      case (byte) 0x3c -> address -> new Undocumented(new Nop(absoluteX(address)));
      case (byte) 0x3d -> address -> new And(absoluteX(address));
      case (byte) 0x3e -> address -> new Rol(absoluteX(address));
      case (byte) 0x40 -> address -> new Rti(implicit());
      case (byte) 0x41 -> address -> new Eor(indirectX(address));
      case (byte) 0x44 -> address -> new Undocumented(new Nop(zeropage(address)));
      case (byte) 0x45 -> address -> new Eor(zeropage(address));
      case (byte) 0x46 -> address -> new Lsr(zeropage(address));
      case (byte) 0x48 -> address -> new Pha(implicit());
      case (byte) 0x49 -> address -> new Eor(immediateByte(address));
      case (byte) 0x4a -> address -> new Lsr(accumulator(address));
      case (byte) 0x4c -> address -> new Jmp(immediateShort(address));
      case (byte) 0x4d -> address -> new Eor(absolute(address));
      case (byte) 0x4e -> address -> new Lsr(absolute(address));
      case (byte) 0x50 -> address -> new Bvc(regs, immediateByte(address));
      case (byte) 0x51 -> address -> new Eor(indirectY(address));
      case (byte) 0x54 -> address -> new Undocumented(new Nop(zeropageX(address)));
      case (byte) 0x55 -> address -> new Eor(zeropageX(address));
      case (byte) 0x56 -> address -> new Lsr(zeropageX(address));
      case (byte) 0x58 -> address -> new Cli(implicit());
      case (byte) 0x59 -> address -> new Eor(absoluteY(address));
      case (byte) 0x5a -> address -> new Undocumented(new Nop(implicit()));
      case (byte) 0x5c -> address -> new Undocumented(new Nop(absoluteX(address)));
      case (byte) 0x5d -> address -> new Eor(absoluteX(address));
      case (byte) 0x5e -> address -> new Lsr(absoluteX(address));
      case (byte) 0x60 -> address -> new Rts(implicit());
      case (byte) 0x61 -> address -> new Adc(indirectX(address));
      case (byte) 0x64 -> address -> new Undocumented(new Nop(zeropage(address)));
      case (byte) 0x65 -> address -> new Adc(zeropage(address));
      case (byte) 0x66 -> address -> new Ror(zeropage(address));
      case (byte) 0x68 -> address -> new Pla(implicit());
      case (byte) 0x69 -> address -> new Adc(immediateByte(address));
      case (byte) 0x6a -> address -> new Ror(accumulator(address));
      case (byte) 0x6c -> address -> new Jmp(indirect(address));
      case (byte) 0x6d -> address -> new Adc(absolute(address));
      case (byte) 0x6e -> address -> new Ror(absolute(address));
      case (byte) 0x70 -> address -> new Bvs(regs, immediateByte(address));
      case (byte) 0x71 -> address -> new Adc(indirectY(address));
      case (byte) 0x74 -> address -> new Undocumented(new Nop(zeropageX(address)));
      case (byte) 0x75 -> address -> new Adc(zeropageX(address));
      case (byte) 0x76 -> address -> new Ror(zeropageX(address));
      case (byte) 0x78 -> address -> new Sei(implicit());
      case (byte) 0x79 -> address -> new Adc(absoluteY(address));
      case (byte) 0x7a -> address -> new Undocumented(new Nop(implicit()));
      case (byte) 0x7c -> address -> new Undocumented(new Nop(absoluteX(address)));
      case (byte) 0x7d -> address -> new Adc(absoluteX(address));
      case (byte) 0x7e -> address -> new Ror(absoluteX(address));
      case (byte) 0x80 -> address -> new Undocumented(new Nop(immediateByte(address)));
      case (byte) 0x81 -> address -> new Sta(indirectX(address));
      case (byte) 0x84 -> address -> new Sty(zeropage(address));
      case (byte) 0x85 -> address -> new Sta(zeropage(address));
      case (byte) 0x86 -> address -> new Stx(zeropage(address));
      case (byte) 0x88 -> address -> new Dey(implicit());
      case (byte) 0x8d -> address -> new Sta(absolute(address));
      case (byte) 0x8a -> address -> new Txa(implicit());
      case (byte) 0x8c -> address -> new Sty(absolute(address));
      case (byte) 0x8e -> address -> new Stx(absolute(address));
      case (byte) 0x90 -> address -> new Bcc(regs, immediateByte(address));
      case (byte) 0x91 -> address -> new Sta(indirectY(address));
      case (byte) 0x94 -> address -> new Sty(zeropageX(address));
      case (byte) 0x95 -> address -> new Sta(zeropageX(address));
      case (byte) 0x96 -> address -> new Stx(zeropageY(address));
      case (byte) 0x98 -> address -> new Tya(implicit());
      case (byte) 0x99 -> address -> new Sta(absoluteY(address));
      case (byte) 0x9a -> address -> new Txs(implicit());
      case (byte) 0x9d -> address -> new Sta(absoluteX(address));
      case (byte) 0xa0 -> address -> new Ldy(immediateByte(address));
      case (byte) 0xa1 -> address -> new Lda(indirectX(address));
      case (byte) 0xa2 -> address -> new Ldx(immediateByte(address));
      case (byte) 0xa3 -> address -> new Undocumented(new Lax(indirectX(address)));
      case (byte) 0xa4 -> address -> new Ldy(zeropage(address));
      case (byte) 0xa5 -> address -> new Lda(zeropage(address));
      case (byte) 0xa6 -> address -> new Ldx(zeropage(address));
      case (byte) 0xa7 -> address -> new Undocumented(new Lax(zeropage(address)));
      case (byte) 0xa8 -> address -> new Tay(implicit());
      case (byte) 0xa9 -> address -> new Lda(immediateByte(address));
      case (byte) 0xaa -> address -> new Tax(implicit());
      case (byte) 0xac -> address -> new Ldy(absolute(address));
      case (byte) 0xad -> address -> new Lda(absolute(address));
      case (byte) 0xae -> address -> new Ldx(absolute(address));
      case (byte) 0xaf -> address -> new Undocumented(new Lax(absolute(address)));
      case (byte) 0xb0 -> address -> new Bcs(regs, immediateByte(address));
      case (byte) 0xb1 -> address -> new Lda(indirectY(address));
      case (byte) 0xb3 -> address -> new Undocumented(new Lax(indirectY(address)));
      case (byte) 0xb4 -> address -> new Ldy(zeropageX(address));
      case (byte) 0xb5 -> address -> new Lda(zeropageX(address));
      case (byte) 0xb6 -> address -> new Ldx(zeropageY(address));
      case (byte) 0xb7 -> address -> new Undocumented(new Lax(zeropageY(address)));
      case (byte) 0xb8 -> address -> new Clv(implicit());
      case (byte) 0xb9 -> address -> new Lda(absoluteY(address));
      case (byte) 0xba -> address -> new Tsx(implicit());
      case (byte) 0xbc -> address -> new Ldy(absoluteX(address));
      case (byte) 0xbd -> address -> new Lda(absoluteX(address));
      case (byte) 0xbe -> address -> new Ldx(absoluteY(address));
      case (byte) 0xbf -> address -> new Undocumented(new Lax(absoluteY(address)));
      case (byte) 0xc0 -> address -> new Cpy(immediateByte(address));
      case (byte) 0xc1 -> address -> new Cmp(indirectX(address));
      case (byte) 0xc4 -> address -> new Cpy(zeropage(address));
      case (byte) 0xc5 -> address -> new Cmp(zeropage(address));
      case (byte) 0xc6 -> address -> new Dec(zeropage(address));
      case (byte) 0xc8 -> address -> new Iny(implicit());
      case (byte) 0xca -> address -> new Dex(implicit());
      case (byte) 0xc9 -> address -> new Cmp(immediateByte(address));
      case (byte) 0xcc -> address -> new Cpy(absolute(address));
      case (byte) 0xcd -> address -> new Cmp(absolute(address));
      case (byte) 0xce -> address -> new Dec(absolute(address));
      case (byte) 0xd0 -> address -> new Bne(regs, immediateByte(address));
      case (byte) 0xd1 -> address -> new Cmp(indirectY(address));
      case (byte) 0xd4 -> address -> new Undocumented(new Nop(zeropageX(address)));
      case (byte) 0xd5 -> address -> new Cmp(zeropageX(address));
      case (byte) 0xd6 -> address -> new Dec(zeropageX(address));
      case (byte) 0xd8 -> address -> new Cld(implicit());
      case (byte) 0xd9 -> address -> new Cmp(absoluteY(address));
      case (byte) 0xda -> address -> new Undocumented(new Nop(implicit()));
      case (byte) 0xdc -> address -> new Undocumented(new Nop(absoluteX(address)));
      case (byte) 0xdd -> address -> new Cmp(absoluteX(address));
      case (byte) 0xde -> address -> new Dec(absoluteX(address));
      case (byte) 0xe0 -> address -> new Cpx(immediateByte(address));
      case (byte) 0xe1 -> address -> new Sbc(indirectX(address));
      case (byte) 0xe4 -> address -> new Cpx(zeropage(address));
      case (byte) 0xe5 -> address -> new Sbc(zeropage(address));
      case (byte) 0xe6 -> address -> new Inc(zeropage(address));
      case (byte) 0xec -> address -> new Cpx(absolute(address));
      case (byte) 0xe8 -> address -> new Inx(implicit());
      case (byte) 0xe9 -> address -> new Sbc(immediateByte(address));
      case (byte) 0xea -> address -> new Nop(implicit());
      case (byte) 0xed -> address -> new Sbc(absolute(address));
      case (byte) 0xee -> address -> new Inc(absolute(address));
      case (byte) 0xf0 -> address -> new Beq(regs, immediateByte(address));
      case (byte) 0xf1 -> address -> new Sbc(indirectY(address));
      case (byte) 0xf4 -> address -> new Undocumented(new Nop(zeropageX(address)));
      case (byte) 0xf5 -> address -> new Sbc(zeropageX(address));
      case (byte) 0xf6 -> address -> new Inc(zeropageX(address));
      case (byte) 0xf8 -> address -> new Sed(implicit());
      case (byte) 0xf9 -> address -> new Sbc(absoluteY(address));
      case (byte) 0xfa -> address -> new Undocumented(new Nop(implicit()));
      case (byte) 0xfc -> address -> new Undocumented(new Nop(absoluteX(address)));
      case (byte) 0xfd -> address -> new Sbc(absoluteX(address));
      case (byte) 0xfe -> address -> new Inc(absoluteX(address));
      default -> throw new UnknownOpcodeException(opcode);
    };
  }

  private Implicit implicit() {
    return new Implicit();
  }

  private AccumulatorRegister accumulator(short ignored) {
    return new AccumulatorRegister(regs);
  }

  private Immediate<Byte> immediateByte(short address) {
    return new Immediate<>(memory.fetchByte(address));
  }

  private Immediate<Short> immediateShort(short address) {
    return new Immediate<>(memory.fetchShort(address));
  }

  private ZeropageAddress zeropage(short address) {
    return new ZeropageAddress(memory, memory.fetchByte(address));
  }

  private IndexedZeropageAddress zeropageX(short address) {
    return new IndexedZeropageAddress(memory, memory.fetchByte(address), new XIndex(regs));
  }

  private IndexedZeropageAddress zeropageY(short address) {
    return new IndexedZeropageAddress(memory, memory.fetchByte(address), new YIndex(regs));
  }

  private AbsoluteAddress absolute(short address) {
    return new AbsoluteAddress(memory, memory.fetchShort(address));
  }

  private IndexedAbsoluteAddress absoluteX(short address) {
    return new IndexedAbsoluteAddress(memory, memory.fetchShort(address), new XIndex(regs));
  }

  private IndexedAbsoluteAddress absoluteY(short address) {
    return new IndexedAbsoluteAddress(memory, memory.fetchShort(address), new YIndex(regs));
  }

  private IndirectAddress indirect(short address) {
    return new IndirectAddress(memory, memory.fetchShort(address));
  }

  private IndirectXAddress indirectX(short address) {
    return new IndirectXAddress(memory, memory.fetchByte(address), new XIndex(regs));
  }

  private IndirectYAddress indirectY(short address) {
    return new IndirectYAddress(memory, memory.fetchByte(address), new YIndex(regs));
  }
}
