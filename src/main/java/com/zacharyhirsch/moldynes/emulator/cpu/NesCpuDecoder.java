package com.zacharyhirsch.moldynes.emulator.cpu;

import com.zacharyhirsch.moldynes.emulator.cpu.addressing.FetchAbsolute;
import com.zacharyhirsch.moldynes.emulator.cpu.addressing.FetchAbsoluteX;
import com.zacharyhirsch.moldynes.emulator.cpu.addressing.FetchAbsoluteY;
import com.zacharyhirsch.moldynes.emulator.cpu.addressing.FetchImmediate;
import com.zacharyhirsch.moldynes.emulator.cpu.addressing.FetchIndirectX;
import com.zacharyhirsch.moldynes.emulator.cpu.addressing.FetchIndirectY;
import com.zacharyhirsch.moldynes.emulator.cpu.addressing.FetchZeropage;
import com.zacharyhirsch.moldynes.emulator.cpu.addressing.FetchZeropageX;
import com.zacharyhirsch.moldynes.emulator.cpu.addressing.FetchZeropageY;
import com.zacharyhirsch.moldynes.emulator.cpu.addressing.Implied;
import com.zacharyhirsch.moldynes.emulator.cpu.addressing.ReadModifyWriteAbsolute;
import com.zacharyhirsch.moldynes.emulator.cpu.addressing.ReadModifyWriteAbsoluteX;
import com.zacharyhirsch.moldynes.emulator.cpu.addressing.ReadModifyWriteAbsoluteY;
import com.zacharyhirsch.moldynes.emulator.cpu.addressing.ReadModifyWriteAccumulator;
import com.zacharyhirsch.moldynes.emulator.cpu.addressing.ReadModifyWriteIndirectX;
import com.zacharyhirsch.moldynes.emulator.cpu.addressing.ReadModifyWriteIndirectY;
import com.zacharyhirsch.moldynes.emulator.cpu.addressing.ReadModifyWriteZeropage;
import com.zacharyhirsch.moldynes.emulator.cpu.addressing.ReadModifyWriteZeropageX;
import com.zacharyhirsch.moldynes.emulator.cpu.addressing.StoreAbsolute;
import com.zacharyhirsch.moldynes.emulator.cpu.addressing.StoreAbsoluteX;
import com.zacharyhirsch.moldynes.emulator.cpu.addressing.StoreAbsoluteY;
import com.zacharyhirsch.moldynes.emulator.cpu.addressing.StoreIndirectX;
import com.zacharyhirsch.moldynes.emulator.cpu.addressing.StoreIndirectY;
import com.zacharyhirsch.moldynes.emulator.cpu.addressing.StoreZeropage;
import com.zacharyhirsch.moldynes.emulator.cpu.addressing.StoreZeropageX;
import com.zacharyhirsch.moldynes.emulator.cpu.addressing.StoreZeropageY;
import com.zacharyhirsch.moldynes.emulator.cpu.instructions.Adc;
import com.zacharyhirsch.moldynes.emulator.cpu.instructions.Alr;
import com.zacharyhirsch.moldynes.emulator.cpu.instructions.Anc;
import com.zacharyhirsch.moldynes.emulator.cpu.instructions.And;
import com.zacharyhirsch.moldynes.emulator.cpu.instructions.Arr;
import com.zacharyhirsch.moldynes.emulator.cpu.instructions.Asl;
import com.zacharyhirsch.moldynes.emulator.cpu.instructions.Atx;
import com.zacharyhirsch.moldynes.emulator.cpu.instructions.Axs;
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
import com.zacharyhirsch.moldynes.emulator.cpu.instructions.Sxa;
import com.zacharyhirsch.moldynes.emulator.cpu.instructions.Sya;
import com.zacharyhirsch.moldynes.emulator.cpu.instructions.Tax;
import com.zacharyhirsch.moldynes.emulator.cpu.instructions.Tay;
import com.zacharyhirsch.moldynes.emulator.cpu.instructions.Tsx;
import com.zacharyhirsch.moldynes.emulator.cpu.instructions.Txa;
import com.zacharyhirsch.moldynes.emulator.cpu.instructions.Txs;
import com.zacharyhirsch.moldynes.emulator.cpu.instructions.Tya;
import com.zacharyhirsch.moldynes.emulator.cpu.instructions.UnknownOpcodeException;

public final class NesCpuDecoder {

  public NesCpuDecoder() {}

  public NesCpuCycle decode(byte opcode) {
    return switch (Byte.toUnsignedInt(opcode)) {
      case 0x00 -> new Brk();
      case 0x01 -> new FetchIndirectX(new Ora());
      case 0x03 -> new ReadModifyWriteIndirectX(new Slo());
      case 0x04 -> new FetchZeropage(new Nop());
      case 0x05 -> new FetchZeropage(new Ora());
      case 0x06 -> new ReadModifyWriteZeropage(new Asl());
      case 0x07 -> new ReadModifyWriteZeropage(new Slo());
      case 0x08 -> new Php();
      case 0x09 -> new FetchImmediate(new Ora());
      case 0x0a -> new ReadModifyWriteAccumulator(new Asl());
      case 0x0b -> new FetchImmediate(new Anc());
      case 0x0c -> new FetchAbsolute(new Nop());
      case 0x0d -> new FetchAbsolute(new Ora());
      case 0x0e -> new ReadModifyWriteAbsolute(new Asl());
      case 0x0f -> new ReadModifyWriteAbsolute(new Slo());
      case 0x10 -> new Bpl();
      case 0x11 -> new FetchIndirectY(new Ora());
      case 0x13 -> new ReadModifyWriteIndirectY(new Slo());
      case 0x14 -> new FetchZeropageX(new Nop());
      case 0x15 -> new FetchZeropageX(new Ora());
      case 0x16 -> new ReadModifyWriteZeropageX(new Asl());
      case 0x17 -> new ReadModifyWriteZeropageX(new Slo());
      case 0x18 -> new Implied(new Clc());
      case 0x19 -> new FetchAbsoluteY(new Ora());
      case 0x1a -> new Implied(new Nop());
      case 0x1b -> new ReadModifyWriteAbsoluteY(new Slo());
      case 0x1c -> new FetchAbsoluteX(new Nop());
      case 0x1d -> new FetchAbsoluteX(new Ora());
      case 0x1e -> new ReadModifyWriteAbsoluteX(new Asl());
      case 0x1f -> new ReadModifyWriteAbsoluteX(new Slo());
      case 0x20 -> new Jsr();
      case 0x21 -> new FetchIndirectX(new And());
      case 0x23 -> new ReadModifyWriteIndirectX(new Rla());
      case 0x24 -> new FetchZeropage(new Bit());
      case 0x25 -> new FetchZeropage(new And());
      case 0x26 -> new ReadModifyWriteZeropage(new Rol());
      case 0x27 -> new ReadModifyWriteZeropage(new Rla());
      case 0x28 -> new Plp();
      case 0x29 -> new FetchImmediate(new And());
      case 0x2a -> new ReadModifyWriteAccumulator(new Rol());
      case 0x2b -> new FetchImmediate(new Anc());
      case 0x2c -> new FetchAbsolute(new Bit());
      case 0x2d -> new FetchAbsolute(new And());
      case 0x2e -> new ReadModifyWriteAbsolute(new Rol());
      case 0x2f -> new ReadModifyWriteAbsolute(new Rla());
      case 0x30 -> new Bmi();
      case 0x31 -> new FetchIndirectY(new And());
      case 0x32 -> new Hlt();
      case 0x33 -> new ReadModifyWriteIndirectY(new Rla());
      case 0x34 -> new FetchZeropageX(new Nop());
      case 0x35 -> new FetchZeropageX(new And());
      case 0x36 -> new ReadModifyWriteZeropageX(new Rol());
      case 0x37 -> new ReadModifyWriteZeropageX(new Rla());
      case 0x38 -> new Implied(new Sec());
      case 0x39 -> new FetchAbsoluteY(new And());
      case 0x3a -> new Implied(new Nop());
      case 0x3b -> new ReadModifyWriteAbsoluteY(new Rla());
      case 0x3c -> new FetchAbsoluteX(new Nop());
      case 0x3d -> new FetchAbsoluteX(new And());
      case 0x3e -> new ReadModifyWriteAbsoluteX(new Rol());
      case 0x3f -> new ReadModifyWriteAbsoluteX(new Rla());
      case 0x40 -> new Rti();
      case 0x41 -> new FetchIndirectX(new Eor());
      case 0x43 -> new ReadModifyWriteIndirectX(new Sre());
      case 0x44 -> new FetchZeropage(new Nop());
      case 0x45 -> new FetchZeropage(new Eor());
      case 0x46 -> new ReadModifyWriteZeropage(new Lsr());
      case 0x47 -> new ReadModifyWriteZeropage(new Sre());
      case 0x48 -> new Pha();
      case 0x49 -> new FetchImmediate(new Eor());
      case 0x4a -> new ReadModifyWriteAccumulator(new Lsr());
      case 0x4b -> new FetchImmediate(new Alr());
      case 0x4c -> new Jmp.Absolute();
      case 0x4d -> new FetchAbsolute(new Eor());
      case 0x4e -> new ReadModifyWriteAbsolute(new Lsr());
      case 0x4f -> new ReadModifyWriteAbsolute(new Sre());
      case 0x50 -> new Bvc();
      case 0x51 -> new FetchIndirectY(new Eor());
      case 0x53 -> new ReadModifyWriteIndirectY(new Sre());
      case 0x54 -> new FetchZeropageX(new Nop());
      case 0x55 -> new FetchZeropageX(new Eor());
      case 0x56 -> new ReadModifyWriteZeropageX(new Lsr());
      case 0x57 -> new ReadModifyWriteZeropageX(new Sre());
      case 0x58 -> new Implied(new Cli());
      case 0x59 -> new FetchAbsoluteY(new Eor());
      case 0x5a -> new Implied(new Nop());
      case 0x5b -> new ReadModifyWriteAbsoluteY(new Sre());
      case 0x5c -> new FetchAbsoluteX(new Nop());
      case 0x5d -> new FetchAbsoluteX(new Eor());
      case 0x5e -> new ReadModifyWriteAbsoluteX(new Lsr());
      case 0x5f -> new ReadModifyWriteAbsoluteX(new Sre());
      case 0x60 -> new Rts();
      case 0x61 -> new FetchIndirectX(new Adc());
      case 0x63 -> new ReadModifyWriteIndirectX(new Rra());
      case 0x64 -> new FetchZeropage(new Nop());
      case 0x65 -> new FetchZeropage(new Adc());
      case 0x66 -> new ReadModifyWriteZeropage(new Ror());
      case 0x67 -> new ReadModifyWriteZeropage(new Rra());
      case 0x68 -> new Pla();
      case 0x69 -> new FetchImmediate(new Adc());
      case 0x6a -> new ReadModifyWriteAccumulator(new Ror());
      case 0x6b -> new FetchImmediate(new Arr());
      case 0x6c -> new Jmp.Indirect();
      case 0x6d -> new FetchAbsolute(new Adc());
      case 0x6e -> new ReadModifyWriteAbsolute(new Ror());
      case 0x6f -> new ReadModifyWriteAbsolute(new Rra());
      case 0x70 -> new Bvs();
      case 0x71 -> new FetchIndirectY(new Adc());
      case 0x73 -> new ReadModifyWriteIndirectY(new Rra());
      case 0x74 -> new FetchZeropageX(new Nop());
      case 0x75 -> new FetchZeropageX(new Adc());
      case 0x76 -> new ReadModifyWriteZeropageX(new Ror());
      case 0x77 -> new ReadModifyWriteZeropageX(new Rra());
      case 0x78 -> new Implied(new Sei());
      case 0x79 -> new FetchAbsoluteY(new Adc());
      case 0x7a -> new Implied(new Nop());
      case 0x7b -> new ReadModifyWriteAbsoluteY(new Rra());
      case 0x7c -> new FetchAbsoluteX(new Nop());
      case 0x7d -> new FetchAbsoluteX(new Adc());
      case 0x7e -> new ReadModifyWriteAbsoluteX(new Ror());
      case 0x7f -> new ReadModifyWriteAbsoluteX(new Rra());
      case 0x80 -> new FetchImmediate(new Nop());
      case 0x81 -> new StoreIndirectX(Sta.VALUE);
      case 0x82 -> new FetchImmediate(new Nop());
      case 0x83 -> new StoreIndirectX(Sax.VALUE);
      case 0x84 -> new StoreZeropage(Sty.VALUE);
      case 0x85 -> new StoreZeropage(Sta.VALUE);
      case 0x86 -> new StoreZeropage(Stx.VALUE);
      case 0x87 -> new StoreZeropage(Sax.VALUE);
      case 0x88 -> new Implied(new Dey());
      case 0x89 -> new FetchImmediate(new Nop());
      case 0x8a -> new Implied(new Txa());
      case 0x8c -> new StoreAbsolute(Sty.VALUE);
      case 0x8d -> new StoreAbsolute(Sta.VALUE);
      case 0x8e -> new StoreAbsolute(Stx.VALUE);
      case 0x8f -> new StoreAbsolute(Sax.VALUE);
      case 0x90 -> new Bcc();
      case 0x91 -> new StoreIndirectY(Sta.VALUE);
      case 0x94 -> new StoreZeropageX(Sty.VALUE);
      case 0x95 -> new StoreZeropageX(Sta.VALUE);
      case 0x96 -> new StoreZeropageY(Stx.VALUE);
      case 0x97 -> new StoreZeropageY(Sax.VALUE);
      case 0x98 -> new Implied(new Tya());
      case 0x99 -> new StoreAbsoluteY(Sta.VALUE);
      case 0x9a -> new Implied(new Txs());
      case 0x9c -> new Sya();
      case 0x9d -> new StoreAbsoluteX(Sta.VALUE);
      case 0x9e -> new Sxa();
      case 0xa0 -> new FetchImmediate(new Ldy());
      case 0xa1 -> new FetchIndirectX(new Lda());
      case 0xa2 -> new FetchImmediate(new Ldx());
      case 0xa3 -> new FetchIndirectX(new Lax());
      case 0xa4 -> new FetchZeropage(new Ldy());
      case 0xa5 -> new FetchZeropage(new Lda());
      case 0xa6 -> new FetchZeropage(new Ldx());
      case 0xa7 -> new FetchZeropage(new Lax());
      case 0xa8 -> new Implied(new Tay());
      case 0xa9 -> new FetchImmediate(new Lda());
      case 0xaa -> new Implied(new Tax());
      case 0xab -> new FetchImmediate(new Atx());
      case 0xac -> new FetchAbsolute(new Ldy());
      case 0xad -> new FetchAbsolute(new Lda());
      case 0xae -> new FetchAbsolute(new Ldx());
      case 0xaf -> new FetchAbsolute(new Lax());
      case 0xb0 -> new Bcs();
      case 0xb1 -> new FetchIndirectY(new Lda());
      case 0xb3 -> new FetchIndirectY(new Lax());
      case 0xb4 -> new FetchZeropageX(new Ldy());
      case 0xb5 -> new FetchZeropageX(new Lda());
      case 0xb6 -> new FetchZeropageY(new Ldx());
      case 0xb7 -> new FetchZeropageY(new Lax());
      case 0xb8 -> new Implied(new Clv());
      case 0xb9 -> new FetchAbsoluteY(new Lda());
      case 0xba -> new Implied(new Tsx());
      case 0xbc -> new FetchAbsoluteX(new Ldy());
      case 0xbd -> new FetchAbsoluteX(new Lda());
      case 0xbe -> new FetchAbsoluteY(new Ldx());
      case 0xbf -> new FetchAbsoluteY(new Lax());
      case 0xc0 -> new FetchImmediate(new Cpy());
      case 0xc1 -> new FetchIndirectX(new Cmp());
      case 0xc2 -> new FetchImmediate(new Nop());
      case 0xc3 -> new ReadModifyWriteIndirectX(new Dcp());
      case 0xc4 -> new FetchZeropage(new Cpy());
      case 0xc5 -> new FetchZeropage(new Cmp());
      case 0xc6 -> new ReadModifyWriteZeropage(new Dec());
      case 0xc7 -> new ReadModifyWriteZeropage(new Dcp());
      case 0xc8 -> new Implied(new Iny());
      case 0xc9 -> new FetchImmediate(new Cmp());
      case 0xca -> new Implied(new Dex());
      case 0xcb -> new FetchImmediate(new Axs());
      case 0xcc -> new FetchAbsolute(new Cpy());
      case 0xcd -> new FetchAbsolute(new Cmp());
      case 0xce -> new ReadModifyWriteAbsolute(new Dec());
      case 0xcf -> new ReadModifyWriteAbsolute(new Dcp());
      case 0xd0 -> new Bne();
      case 0xd1 -> new FetchIndirectY(new Cmp());
      case 0xd3 -> new ReadModifyWriteIndirectY(new Dcp());
      case 0xd4 -> new FetchZeropageX(new Nop());
      case 0xd5 -> new FetchZeropageX(new Cmp());
      case 0xd6 -> new ReadModifyWriteZeropageX(new Dec());
      case 0xd7 -> new ReadModifyWriteZeropageX(new Dcp());
      case 0xd8 -> new Implied(new Cld());
      case 0xd9 -> new FetchAbsoluteY(new Cmp());
      case 0xda -> new Implied(new Nop());
      case 0xdb -> new ReadModifyWriteAbsoluteY(new Dcp());
      case 0xdc -> new FetchAbsoluteX(new Nop());
      case 0xdd -> new FetchAbsoluteX(new Cmp());
      case 0xde -> new ReadModifyWriteAbsoluteX(new Dec());
      case 0xdf -> new ReadModifyWriteAbsoluteX(new Dcp());
      case 0xe0 -> new FetchImmediate(new Cpx());
      case 0xe1 -> new FetchIndirectX(new Sbc());
      case 0xe2 -> new FetchImmediate(new Nop());
      case 0xe3 -> new ReadModifyWriteIndirectX(new Isb());
      case 0xe4 -> new FetchZeropage(new Cpx());
      case 0xe5 -> new FetchZeropage(new Sbc());
      case 0xe6 -> new ReadModifyWriteZeropage(new Inc());
      case 0xe7 -> new ReadModifyWriteZeropage(new Isb());
      case 0xe8 -> new Implied(new Inx());
      case 0xe9 -> new FetchImmediate(new Sbc());
      case 0xea -> new Implied(new Nop());
      case 0xeb -> new FetchImmediate(new Sbc());
      case 0xec -> new FetchAbsolute(new Cpx());
      case 0xed -> new FetchAbsolute(new Sbc());
      case 0xee -> new ReadModifyWriteAbsolute(new Inc());
      case 0xef -> new ReadModifyWriteAbsolute(new Isb());
      case 0xf0 -> new Beq();
      case 0xf1 -> new FetchIndirectY(new Sbc());
      case 0xf3 -> new ReadModifyWriteIndirectY(new Isb());
      case 0xf4 -> new FetchZeropageX(new Nop());
      case 0xf5 -> new FetchZeropageX(new Sbc());
      case 0xf6 -> new ReadModifyWriteZeropageX(new Inc());
      case 0xf7 -> new ReadModifyWriteZeropageX(new Isb());
      case 0xf8 -> new Implied(new Sed());
      case 0xf9 -> new FetchAbsoluteY(new Sbc());
      case 0xfa -> new Implied(new Nop());
      case 0xfb -> new ReadModifyWriteAbsoluteY(new Isb());
      case 0xfc -> new FetchAbsoluteX(new Nop());
      case 0xfd -> new FetchAbsoluteX(new Sbc());
      case 0xfe -> new ReadModifyWriteAbsoluteX(new Inc());
      case 0xff -> new ReadModifyWriteAbsoluteX(new Isb());
      default -> throw new UnknownOpcodeException(opcode);
    };
  }
}
