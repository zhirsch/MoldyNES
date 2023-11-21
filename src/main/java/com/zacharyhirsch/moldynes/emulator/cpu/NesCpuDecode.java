package com.zacharyhirsch.moldynes.emulator.cpu;

import com.zacharyhirsch.moldynes.emulator.UInt8;
import com.zacharyhirsch.moldynes.emulator.cpu.addressing.FetchAbsolute;
import com.zacharyhirsch.moldynes.emulator.cpu.addressing.FetchAbsoluteX;
import com.zacharyhirsch.moldynes.emulator.cpu.addressing.FetchAbsoluteY;
import com.zacharyhirsch.moldynes.emulator.cpu.addressing.FetchImmediate;
import com.zacharyhirsch.moldynes.emulator.cpu.addressing.FetchIndirectX;
import com.zacharyhirsch.moldynes.emulator.cpu.addressing.FetchIndirectY;
import com.zacharyhirsch.moldynes.emulator.cpu.addressing.FetchZeropage;
import com.zacharyhirsch.moldynes.emulator.cpu.addressing.FetchZeropageX;
import com.zacharyhirsch.moldynes.emulator.cpu.addressing.FetchZeropageY;
import com.zacharyhirsch.moldynes.emulator.cpu.addressing.StoreAbsolute;
import com.zacharyhirsch.moldynes.emulator.cpu.addressing.StoreAbsoluteX;
import com.zacharyhirsch.moldynes.emulator.cpu.addressing.StoreAbsoluteY;
import com.zacharyhirsch.moldynes.emulator.cpu.addressing.StoreIndirectX;
import com.zacharyhirsch.moldynes.emulator.cpu.addressing.StoreIndirectY;
import com.zacharyhirsch.moldynes.emulator.cpu.addressing.StoreZeropage;
import com.zacharyhirsch.moldynes.emulator.cpu.addressing.StoreZeropageX;
import com.zacharyhirsch.moldynes.emulator.cpu.addressing.StoreZeropageY;
import com.zacharyhirsch.moldynes.emulator.cpu.instructions.Adc;
import com.zacharyhirsch.moldynes.emulator.cpu.instructions.And;
import com.zacharyhirsch.moldynes.emulator.cpu.instructions.Asl;
import com.zacharyhirsch.moldynes.emulator.cpu.instructions.Bcc;
import com.zacharyhirsch.moldynes.emulator.cpu.instructions.Bcs;
import com.zacharyhirsch.moldynes.emulator.cpu.instructions.Beq;
import com.zacharyhirsch.moldynes.emulator.cpu.instructions.Bit;
import com.zacharyhirsch.moldynes.emulator.cpu.instructions.Bmi;
import com.zacharyhirsch.moldynes.emulator.cpu.instructions.Bne;
import com.zacharyhirsch.moldynes.emulator.cpu.instructions.Bpl;
import com.zacharyhirsch.moldynes.emulator.cpu.instructions.Brk;
import com.zacharyhirsch.moldynes.emulator.cpu.instructions.Bvc;
import com.zacharyhirsch.moldynes.emulator.cpu.instructions.Bvs;
import com.zacharyhirsch.moldynes.emulator.cpu.instructions.Clc;
import com.zacharyhirsch.moldynes.emulator.cpu.instructions.Cld;
import com.zacharyhirsch.moldynes.emulator.cpu.instructions.Cli;
import com.zacharyhirsch.moldynes.emulator.cpu.instructions.Clv;
import com.zacharyhirsch.moldynes.emulator.cpu.instructions.Cmp;
import com.zacharyhirsch.moldynes.emulator.cpu.instructions.Cpx;
import com.zacharyhirsch.moldynes.emulator.cpu.instructions.Cpy;
import com.zacharyhirsch.moldynes.emulator.cpu.instructions.Dcp;
import com.zacharyhirsch.moldynes.emulator.cpu.instructions.Dec;
import com.zacharyhirsch.moldynes.emulator.cpu.instructions.Dex;
import com.zacharyhirsch.moldynes.emulator.cpu.instructions.Dey;
import com.zacharyhirsch.moldynes.emulator.cpu.instructions.Eor;
import com.zacharyhirsch.moldynes.emulator.cpu.instructions.Hlt;
import com.zacharyhirsch.moldynes.emulator.cpu.instructions.Inc;
import com.zacharyhirsch.moldynes.emulator.cpu.instructions.Inx;
import com.zacharyhirsch.moldynes.emulator.cpu.instructions.Iny;
import com.zacharyhirsch.moldynes.emulator.cpu.instructions.Isb;
import com.zacharyhirsch.moldynes.emulator.cpu.instructions.Jmp;
import com.zacharyhirsch.moldynes.emulator.cpu.instructions.Jsr;
import com.zacharyhirsch.moldynes.emulator.cpu.instructions.Lax;
import com.zacharyhirsch.moldynes.emulator.cpu.instructions.Lda;
import com.zacharyhirsch.moldynes.emulator.cpu.instructions.Ldx;
import com.zacharyhirsch.moldynes.emulator.cpu.instructions.Ldy;
import com.zacharyhirsch.moldynes.emulator.cpu.instructions.Lsr;
import com.zacharyhirsch.moldynes.emulator.cpu.instructions.Nop;
import com.zacharyhirsch.moldynes.emulator.cpu.instructions.Ora;
import com.zacharyhirsch.moldynes.emulator.cpu.instructions.Pha;
import com.zacharyhirsch.moldynes.emulator.cpu.instructions.Php;
import com.zacharyhirsch.moldynes.emulator.cpu.instructions.Pla;
import com.zacharyhirsch.moldynes.emulator.cpu.instructions.Plp;
import com.zacharyhirsch.moldynes.emulator.cpu.instructions.Rla;
import com.zacharyhirsch.moldynes.emulator.cpu.instructions.Rol;
import com.zacharyhirsch.moldynes.emulator.cpu.instructions.Ror;
import com.zacharyhirsch.moldynes.emulator.cpu.instructions.Rra;
import com.zacharyhirsch.moldynes.emulator.cpu.instructions.Rti;
import com.zacharyhirsch.moldynes.emulator.cpu.instructions.Rts;
import com.zacharyhirsch.moldynes.emulator.cpu.instructions.Sax;
import com.zacharyhirsch.moldynes.emulator.cpu.instructions.Sbc;
import com.zacharyhirsch.moldynes.emulator.cpu.instructions.Sec;
import com.zacharyhirsch.moldynes.emulator.cpu.instructions.Sed;
import com.zacharyhirsch.moldynes.emulator.cpu.instructions.Sei;
import com.zacharyhirsch.moldynes.emulator.cpu.instructions.Slo;
import com.zacharyhirsch.moldynes.emulator.cpu.instructions.Sre;
import com.zacharyhirsch.moldynes.emulator.cpu.instructions.Sta;
import com.zacharyhirsch.moldynes.emulator.cpu.instructions.Stx;
import com.zacharyhirsch.moldynes.emulator.cpu.instructions.Sty;
import com.zacharyhirsch.moldynes.emulator.cpu.instructions.Tax;
import com.zacharyhirsch.moldynes.emulator.cpu.instructions.Tay;
import com.zacharyhirsch.moldynes.emulator.cpu.instructions.Tsx;
import com.zacharyhirsch.moldynes.emulator.cpu.instructions.Txa;
import com.zacharyhirsch.moldynes.emulator.cpu.instructions.Txs;
import com.zacharyhirsch.moldynes.emulator.cpu.instructions.Tya;
import com.zacharyhirsch.moldynes.emulator.cpu.instructions.Undocumented;
import com.zacharyhirsch.moldynes.emulator.cpu.instructions.UnknownOpcodeException;

public final class NesCpuDecode {

  private NesCpuDecode() {}

  public static NesCpuCycle next(NesCpu cpu, NesCpuState state) {
    return decode(state.data).start(cpu, state);
  }

  private static NesCpuCycle decode(byte opcode) {
    return switch (Byte.toUnsignedInt(opcode)) {
      case 0x00 -> new Brk();
      case 0x01 -> new FetchIndirectX(new Ora.OnFetch(), new Ora.OnFinish());
      case 0x02 -> new Undocumented(new Hlt());
      case 0x04 -> new Undocumented(new FetchZeropage(new Nop.OnFetch()));
      case 0x05 -> new FetchZeropage(new Ora.OnFetch(), new Ora.OnFinish());
      case 0x08 -> new Php(UInt8.cast(opcode));
      case 0x09 -> new FetchImmediate(new Ora.OnFetch(), new Ora.OnFinish());
      case 0x0c -> new Undocumented(new FetchAbsolute(new Nop.OnFetch()));
      case 0x0d -> new FetchAbsolute(new Ora.OnFetch(), new Ora.OnFinish());
      case 0x10 -> new Bpl();
      case 0x11 -> new FetchIndirectY(new Ora.OnFetch(), new Ora.OnFinish());
      case 0x12 -> new Undocumented(new Hlt());
      case 0x14 -> new Undocumented(new FetchZeropageX(new Nop.OnFetch()));
      case 0x15 -> new FetchZeropageX(new Ora.OnFetch(), new Ora.OnFinish());
      case 0x18 -> new Clc();
      case 0x19 -> new FetchAbsoluteY(new Ora.OnFetch(), new Ora.OnFinish());
      case 0x1a -> new Undocumented(new Nop.Implied());
      case 0x1c -> new Undocumented(new FetchAbsoluteX(new Nop.OnFetch()));
      case 0x1d -> new FetchAbsoluteX(new Ora.OnFetch(), new Ora.OnFinish());
      case 0x20 -> new Jsr(UInt8.cast(opcode));
      case 0x21 -> new FetchIndirectX(new And.OnFetch(), new And.OnFinish());
      case 0x22 -> new Undocumented(new Hlt());
      case 0x24 -> new FetchZeropage(new Bit.OnFetch());
      case 0x25 -> new FetchZeropage(new And.OnFetch(), new And.OnFinish());
      case 0x28 -> new Plp(UInt8.cast(opcode));
      case 0x29 -> new FetchImmediate(new And.OnFetch(), new And.OnFinish());
      case 0x2c -> new FetchAbsolute(new Bit.OnFetch());
      case 0x2d -> new FetchAbsolute(new And.OnFetch(), new And.OnFinish());
      case 0x30 -> new Bmi();
      case 0x31 -> new FetchIndirectY(new And.OnFetch(), new And.OnFinish());
      case 0x32 -> new Undocumented(new Hlt());
      case 0x34 -> new Undocumented(new FetchZeropageX(new Nop.OnFetch()));
      case 0x35 -> new FetchZeropageX(new And.OnFetch(), new And.OnFinish());
      case 0x38 -> new Sec();
      case 0x39 -> new FetchAbsoluteY(new And.OnFetch(), new And.OnFinish());
      case 0x3a -> new Undocumented(new Nop.Implied());
      case 0x3c -> new Undocumented(new FetchAbsoluteX(new Nop.OnFetch()));
      case 0x3d -> new FetchAbsoluteX(new And.OnFetch(), new And.OnFinish());
      case 0x40 -> new Rti(UInt8.cast(opcode));
      case 0x41 -> new FetchIndirectX(new Eor.OnFetch(), new Eor.OnFinish());
      case 0x42 -> new Undocumented(new Hlt());
      case 0x44 -> new Undocumented(new FetchZeropage(new Nop.OnFetch()));
      case 0x45 -> new FetchZeropage(new Eor.OnFetch(), new Eor.OnFinish());
      case 0x48 -> new Pha(UInt8.cast(opcode));
      case 0x49 -> new FetchImmediate(new Eor.OnFetch(), new Eor.OnFinish());
      case 0x4c -> new Jmp.Absolute();
      case 0x4d -> new FetchAbsolute(new Eor.OnFetch(), new Eor.OnFinish());
      case 0x50 -> new Bvc();
      case 0x51 -> new FetchIndirectY(new Eor.OnFetch(), new Eor.OnFinish());
      case 0x52 -> new Undocumented(new Hlt());
      case 0x54 -> new Undocumented(new FetchZeropageX(new Nop.OnFetch()));
      case 0x55 -> new FetchZeropageX(new Eor.OnFetch(), new Eor.OnFinish());
      case 0x58 -> new Cli();
      case 0x59 -> new FetchAbsoluteY(new Eor.OnFetch(), new Eor.OnFinish());
      case 0x5a -> new Undocumented(new Nop.Implied());
      case 0x5c -> new Undocumented(new FetchAbsoluteX(new Nop.OnFetch()));
      case 0x5d -> new FetchAbsoluteX(new Eor.OnFetch(), new Eor.OnFinish());
      case 0x60 -> new Rts(UInt8.cast(opcode));
      case 0x61 -> new FetchIndirectX(new Adc.OnFetch(), new Adc.OnFinish());
      case 0x62 -> new Undocumented(new Hlt());
      case 0x64 -> new Undocumented(new FetchZeropage(new Nop.OnFetch()));
      case 0x65 -> new FetchZeropage(new Adc.OnFetch(), new Adc.OnFinish());
      case 0x68 -> new Pla(UInt8.cast(opcode));
      case 0x69 -> new FetchImmediate(new Adc.OnFetch(), new Adc.OnFinish());
      case 0x6c -> new Jmp.Indirect();
      case 0x6d -> new FetchAbsolute(new Adc.OnFetch(), new Adc.OnFinish());
      case 0x70 -> new Bvs();
      case 0x71 -> new FetchIndirectY(new Adc.OnFetch(), new Adc.OnFinish());
      case 0x72 -> new Undocumented(new Hlt());
      case 0x74 -> new Undocumented(new FetchZeropageX(new Nop.OnFetch()));
      case 0x75 -> new FetchZeropageX(new Adc.OnFetch(), new Adc.OnFinish());
      case 0x78 -> new Sei();
      case 0x79 -> new FetchAbsoluteY(new Adc.OnFetch(), new Adc.OnFinish());
      case 0x7a -> new Undocumented(new Nop.Implied());
      case 0x7c -> new Undocumented(new FetchAbsoluteX(new Nop.OnFetch()));
      case 0x7d -> new FetchAbsoluteX(new Adc.OnFetch(), new Adc.OnFinish());
      case 0x80 -> new Undocumented(new FetchImmediate(new Nop.OnFetch()));
      case 0x81 -> new StoreIndirectX(new Sta.OnStore());
      case 0x83 -> new Undocumented(new StoreIndirectX(new Sax.OnStore()));
      case 0x84 -> new StoreZeropage(new Sty.OnStore());
      case 0x85 -> new StoreZeropage(new Sta.OnStore());
      case 0x86 -> new StoreZeropage(new Stx.OnStore());
      case 0x87 -> new Undocumented(new StoreZeropage(new Sax.OnStore()));
      case 0x88 -> new Dey(UInt8.cast(opcode));
      case 0x89 -> new Undocumented(new FetchImmediate(new Nop.OnFetch()));
      case 0x8a -> new Txa(UInt8.cast(opcode));
      case 0x8c -> new StoreAbsolute(new Sty.OnStore());
      case 0x8d -> new StoreAbsolute(new Sta.OnStore());
      case 0x8e -> new StoreAbsolute(new Stx.OnStore());
      case 0x8f -> new Undocumented(new StoreAbsolute(new Sax.OnStore()));
      case 0x90 -> new Bcc();
      case 0x91 -> new StoreIndirectY(new Sta.OnStore());
      case 0x92 -> new Undocumented(new Hlt());
      case 0x94 -> new StoreZeropageX(new Sty.OnStore());
      case 0x95 -> new StoreZeropageX(new Sta.OnStore());
      case 0x96 -> new StoreZeropageY(new Stx.OnStore());
      case 0x97 -> new Undocumented(new StoreZeropageY(new Sax.OnStore()));
      case 0x98 -> new Tya(UInt8.cast(opcode));
      case 0x99 -> new StoreAbsoluteY(new Sta.OnStore());
      case 0x9a -> new Txs(UInt8.cast(opcode));
      case 0x9d -> new StoreAbsoluteX(new Sta.OnStore());
      case 0xa0 -> new FetchImmediate(new Ldy.OnFetch());
      case 0xa1 -> new FetchIndirectX(new Lda.OnFetch());
      case 0xa2 -> new FetchImmediate(new Ldx.OnFetch());
      case 0xa3 -> new Undocumented(new FetchIndirectX(new Lax.OnFetch()));
      case 0xa4 -> new FetchZeropage(new Ldy.OnFetch());
      case 0xa5 -> new FetchZeropage(new Lda.OnFetch());
      case 0xa6 -> new FetchZeropage(new Ldx.OnFetch());
      case 0xa7 -> new Undocumented(new FetchZeropage(new Lax.OnFetch()));
      case 0xa8 -> new Tay(UInt8.cast(opcode));
      case 0xa9 -> new FetchImmediate(new Lda.OnFetch());
      case 0xaa -> new Tax(UInt8.cast(opcode));
      case 0xac -> new FetchAbsolute(new Ldy.OnFetch());
      case 0xad -> new FetchAbsolute(new Lda.OnFetch());
      case 0xae -> new FetchAbsolute(new Ldx.OnFetch());
      case 0xaf -> new Undocumented(new FetchAbsolute(new Lax.OnFetch()));
      case 0xb0 -> new Bcs();
      case 0xb1 -> new FetchIndirectY(new Lda.OnFetch());
      case 0xb2 -> new Undocumented(new Hlt());
      case 0xb3 -> new Undocumented(new FetchIndirectY(new Lax.OnFetch()));
      case 0xb4 -> new FetchZeropageX(new Ldy.OnFetch());
      case 0xb5 -> new FetchZeropageX(new Lda.OnFetch());
      case 0xb6 -> new FetchZeropageY(new Ldx.OnFetch());
      case 0xb7 -> new Undocumented(new FetchZeropageY(new Lax.OnFetch()));
      case 0xb8 -> new Clv();
      case 0xb9 -> new FetchAbsoluteY(new Lda.OnFetch());
      case 0xba -> new Tsx(UInt8.cast(opcode));
      case 0xbc -> new FetchAbsoluteX(new Ldy.OnFetch());
      case 0xbd -> new FetchAbsoluteX(new Lda.OnFetch());
      case 0xbe -> new FetchAbsoluteY(new Ldx.OnFetch());
      case 0xbf -> new Undocumented(new FetchAbsoluteY(new Lax.OnFetch()));
      case 0xc0 -> new FetchImmediate(new Cpy.OnFetch(), new Cpy.OnFinish());
      case 0xc1 -> new FetchIndirectX(new Cmp.OnFetch(), new Cmp.OnFinish());
      case 0xc4 -> new FetchZeropage(new Cpy.OnFetch(), new Cpy.OnFinish());
      case 0xc5 -> new FetchZeropage(new Cmp.OnFetch(), new Cmp.OnFinish());
      case 0xc8 -> new Iny(UInt8.cast(opcode));
      case 0xc9 -> new FetchImmediate(new Cmp.OnFetch(), new Cmp.OnFinish());
      case 0xca -> new Dex(UInt8.cast(opcode));
      case 0xcc -> new FetchAbsolute(new Cpy.OnFetch(), new Cpy.OnFinish());
      case 0xcd -> new FetchAbsolute(new Cmp.OnFetch(), new Cmp.OnFinish());
      case 0xd0 -> new Bne();
      case 0xd1 -> new FetchIndirectY(new Cmp.OnFetch(), new Cmp.OnFinish());
      case 0xd2 -> new Undocumented(new Hlt());
      case 0xd4 -> new Undocumented(new FetchZeropageX(new Nop.OnFetch()));
      case 0xd5 -> new FetchZeropageX(new Cmp.OnFetch(), new Cmp.OnFinish());
      case 0xd8 -> new Cld();
      case 0xd9 -> new FetchAbsoluteY(new Cmp.OnFetch(), new Cmp.OnFinish());
      case 0xda -> new Undocumented(new Nop.Implied());
      case 0xdc -> new Undocumented(new FetchAbsoluteX(new Nop.OnFetch()));
      case 0xdd -> new FetchAbsoluteX(new Cmp.OnFetch(), new Cmp.OnFinish());
      case 0xe0 -> new FetchImmediate(new Cpx.OnFetch(), new Cpx.OnFinish());
      case 0xe1 -> new FetchIndirectX(new Sbc.OnFetch(), new Sbc.OnFinish());
      case 0xe4 -> new FetchZeropage(new Cpx.OnFetch(), new Cpx.OnFinish());
      case 0xe5 -> new FetchZeropage(new Sbc.OnFetch(), new Sbc.OnFinish());
      case 0xe8 -> new Inx(UInt8.cast(opcode));
      case 0xe9 -> new FetchImmediate(new Sbc.OnFetch(), new Sbc.OnFinish());
      case 0xea -> new Nop.Implied();
      case 0xeb -> new FetchImmediate(new Sbc.OnFetch(), new Sbc.OnFinish());
      case 0xeb -> new Undocumented(new FetchImmediate(new Sbc.OnFetch()));
      case 0xec -> new FetchAbsolute(new Cpx.OnFetch(), new Cpx.OnFinish());
      case 0xed -> new FetchAbsolute(new Sbc.OnFetch(), new Sbc.OnFinish());
      case 0xf0 -> new Beq();
      case 0xf1 -> new FetchIndirectY(new Sbc.OnFetch(), new Sbc.OnFinish());
      case 0xf2 -> new Undocumented(new Hlt());
      case 0xf4 -> new Undocumented(new FetchZeropageX(new Nop.OnFetch()));
      case 0xf5 -> new FetchZeropageX(new Sbc.OnFetch(), new Sbc.OnFinish());
      case 0xf8 -> new Sed();
      case 0xf9 -> new FetchAbsoluteY(new Sbc.OnFetch(), new Sbc.OnFinish());
      case 0xfa -> new Undocumented(new Nop.Implied());
      case 0xfc -> new Undocumented(new FetchAbsoluteX(new Nop.OnFetch()));
      case 0xfd -> new FetchAbsoluteX(new Sbc.OnFetch(), new Sbc.OnFinish());

      case 0x06, 0x0a, 0x0e, 0x16, 0x1e -> new Asl(UInt8.cast(opcode));

      case 0x26, 0x2a, 0x2e, 0x36, 0x3e -> new Rol(UInt8.cast(opcode));

      case 0x46, 0x4a, 0x4e, 0x56, 0x5e -> new Lsr(UInt8.cast(opcode));

      case 0x66, 0x6a, 0x6e, 0x76, 0x7e -> new Ror(UInt8.cast(opcode));

      case 0xc6, 0xce, 0xd6, 0xde -> new Dec(UInt8.cast(opcode));

      case 0xe6, 0xee, 0xf6, 0xfe -> new Inc(UInt8.cast(opcode));

      case 0x03, 0x07, 0x0f, 0x13, 0x17, 0x1b, 0x1f -> new Undocumented(
          new Slo(UInt8.cast(opcode)));

      case 0x23, 0x27, 0x2f, 0x33, 0x37, 0x3b, 0x3f -> new Undocumented(
          new Rla(UInt8.cast(opcode)));

      case 0x43, 0x47, 0x4f, 0x53, 0x57, 0x5b, 0x5f -> new Undocumented(
          new Sre(UInt8.cast(opcode)));

      case 0x63, 0x67, 0x6f, 0x73, 0x77, 0x7b, 0x7f -> new Undocumented(
          new Rra(UInt8.cast(opcode)));

      case 0xc3, 0xc7, 0xcf, 0xd3, 0xd7, 0xdb, 0xdf -> new Undocumented(
          new Dcp(UInt8.cast(opcode)));

      case 0xe3, 0xe7, 0xef, 0xf3, 0xf7, 0xfb, 0xff -> new Undocumented(
          new Isb(UInt8.cast(opcode)));

      default -> throw new UnknownOpcodeException(opcode);
    };
  }
}
