package com.zacharyhirsch.moldynes.emulator.cpu.logging;

import com.google.common.collect.ImmutableMap;
import com.zacharyhirsch.moldynes.emulator.NesCpuMemoryMap;
import com.zacharyhirsch.moldynes.emulator.cpu.NesCpu;
import java.util.Map;

public final class NesCpuLogger {

  private static final Map<Byte, Decompiler> DECOMPILERS =
      ImmutableMap.<Byte, Decompiler>builder()
          .put((byte) 0x01, new IndirectXDecompiler("ORA"))
          .put((byte) 0x05, new ZeropageDecompiler("ORA"))
          .put((byte) 0x06, new ZeropageDecompiler("ASL"))
          .put((byte) 0x08, new ImpliedDecompiler("PHP"))
          .put((byte) 0x09, new ImmediateDecompiler("ORA"))
          .put((byte) 0x0a, new AccumulatorDecompiler("ASL"))
          .put((byte) 0x0d, new AbsoluteDecompiler("ORA"))
          .put((byte) 0x0e, new AbsoluteDecompiler("ASL"))
          .put((byte) 0x10, new BranchDecompiler("BPL"))
          .put((byte) 0x18, new ImpliedDecompiler("CLC"))
          .put((byte) 0x20, new JumpAbsoluteDecompiler("JSR"))
          .put((byte) 0x21, new IndirectXDecompiler("AND"))
          .put((byte) 0x24, new ZeropageDecompiler("BIT"))
          .put((byte) 0x25, new ZeropageDecompiler("AND"))
          .put((byte) 0x26, new ZeropageDecompiler("ROL"))
          .put((byte) 0x28, new ImpliedDecompiler("PLP"))
          .put((byte) 0x29, new ImmediateDecompiler("AND"))
          .put((byte) 0x2a, new AccumulatorDecompiler("ROL"))
          .put((byte) 0x2c, new AbsoluteDecompiler("BIT"))
          .put((byte) 0x2d, new AbsoluteDecompiler("AND"))
          .put((byte) 0x2e, new AbsoluteDecompiler("ROL"))
          .put((byte) 0x30, new BranchDecompiler("BMI"))
          .put((byte) 0x38, new ImpliedDecompiler("SEC"))
          .put((byte) 0x40, new ImpliedDecompiler("RTI"))
          .put((byte) 0x41, new IndirectXDecompiler("EOR"))
          .put((byte) 0x45, new ZeropageDecompiler("EOR"))
          .put((byte) 0x46, new ZeropageDecompiler("LSR"))
          .put((byte) 0x48, new ImpliedDecompiler("PHA"))
          .put((byte) 0x49, new ImmediateDecompiler("EOR"))
          .put((byte) 0x4c, new JumpAbsoluteDecompiler("JMP"))
          .put((byte) 0x4d, new AbsoluteDecompiler("EOR"))
          .put((byte) 0x4e, new AbsoluteDecompiler("LSR"))
          .put((byte) 0x4a, new AccumulatorDecompiler("LSR"))
          .put((byte) 0x50, new BranchDecompiler("BVC"))
          .put((byte) 0x60, new ImpliedDecompiler("RTS"))
          .put((byte) 0x61, new IndirectXDecompiler("ADC"))
          .put((byte) 0x65, new ZeropageDecompiler("ADC"))
          .put((byte) 0x66, new ZeropageDecompiler("ROR"))
          .put((byte) 0x68, new ImpliedDecompiler("PLA"))
          .put((byte) 0x69, new ImmediateDecompiler("ADC"))
          .put((byte) 0x6a, new AccumulatorDecompiler("ROR"))
          .put((byte) 0x6d, new AbsoluteDecompiler("ADC"))
          .put((byte) 0x6e, new AbsoluteDecompiler("ROR"))
          .put((byte) 0x70, new BranchDecompiler("BVS"))
          .put((byte) 0x78, new ImpliedDecompiler("SEI"))
          .put((byte) 0x81, new IndirectXDecompiler("STA"))
          .put((byte) 0x84, new ZeropageDecompiler("STY"))
          .put((byte) 0x85, new ZeropageDecompiler("STA"))
          .put((byte) 0x86, new ZeropageDecompiler("STX"))
          .put((byte) 0x88, new ImpliedDecompiler("DEY"))
          .put((byte) 0x8a, new ImpliedDecompiler("TXA"))
          .put((byte) 0x8c, new AbsoluteDecompiler("STY"))
          .put((byte) 0x8d, new AbsoluteDecompiler("STA"))
          .put((byte) 0x8e, new AbsoluteDecompiler("STX"))
          .put((byte) 0x90, new BranchDecompiler("BCC"))
          .put((byte) 0x98, new ImpliedDecompiler("TYA"))
          .put((byte) 0x9a, new ImpliedDecompiler("TXS"))
          .put((byte) 0xa0, new ImmediateDecompiler("LDY"))
          .put((byte) 0xa1, new IndirectXDecompiler("LDA"))
          .put((byte) 0xa2, new ImmediateDecompiler("LDX"))
          .put((byte) 0xa4, new ZeropageDecompiler("LDY"))
          .put((byte) 0xa5, new ZeropageDecompiler("LDA"))
          .put((byte) 0xa6, new ZeropageDecompiler("LDX"))
          .put((byte) 0xa8, new ImpliedDecompiler("TAY"))
          .put((byte) 0xa9, new ImmediateDecompiler("LDA"))
          .put((byte) 0xaa, new ImpliedDecompiler("TAX"))
          .put((byte) 0xac, new AbsoluteDecompiler("LDY"))
          .put((byte) 0xad, new AbsoluteDecompiler("LDA"))
          .put((byte) 0xae, new AbsoluteDecompiler("LDX"))
          .put((byte) 0xb0, new BranchDecompiler("BCS"))
          .put((byte) 0xb8, new ImpliedDecompiler("CLV"))
          .put((byte) 0xba, new ImpliedDecompiler("TSX"))
          .put((byte) 0xc0, new ImmediateDecompiler("CPY"))
          .put((byte) 0xc1, new IndirectXDecompiler("CMP"))
          .put((byte) 0xc4, new ZeropageDecompiler("CPY"))
          .put((byte) 0xc5, new ZeropageDecompiler("CMP"))
          .put((byte) 0xc6, new ZeropageDecompiler("DEC"))
          .put((byte) 0xc8, new ImpliedDecompiler("INY"))
          .put((byte) 0xc9, new ImmediateDecompiler("CMP"))
          .put((byte) 0xca, new ImpliedDecompiler("DEX"))
          .put((byte) 0xcc, new AbsoluteDecompiler("CPY"))
          .put((byte) 0xcd, new AbsoluteDecompiler("CMP"))
          .put((byte) 0xce, new AbsoluteDecompiler("DEC"))
          .put((byte) 0xd0, new BranchDecompiler("BNE"))
          .put((byte) 0xd8, new ImpliedDecompiler("CLD"))
          .put((byte) 0xe0, new ImmediateDecompiler("CPX"))
          .put((byte) 0xe1, new IndirectXDecompiler("SBC"))
          .put((byte) 0xe4, new ZeropageDecompiler("CPX"))
          .put((byte) 0xe5, new ZeropageDecompiler("SBC"))
          .put((byte) 0xe6, new ZeropageDecompiler("INC"))
          .put((byte) 0xe8, new ImpliedDecompiler("INX"))
          .put((byte) 0xe9, new ImmediateDecompiler("SBC"))
          .put((byte) 0xea, new ImpliedDecompiler("NOP"))
          .put((byte) 0xec, new AbsoluteDecompiler("CPX"))
          .put((byte) 0xed, new AbsoluteDecompiler("SBC"))
          .put((byte) 0xee, new AbsoluteDecompiler("INC"))
          .put((byte) 0xf0, new BranchDecompiler("BEQ"))
          .put((byte) 0xf8, new ImpliedDecompiler("SED"))
          .build();

  private final NesCpuMemoryMap memory;

  public NesCpuLogger(NesCpuMemoryMap memory) {
    this.memory = memory;
  }

  public void log(byte opcode, NesCpu cpu) {
    Decompiler decompiler = DECOMPILERS.getOrDefault(opcode, new TodoDecompiler());
    System.out.printf(
        "%02X%02X  %-40s  A:%02X X:%02X Y:%02X P:%02X SP:%02X PPU:  0,  0 CYC:%d\n",
        cpu.state.adh,
        cpu.state.adl,
        decompiler.decompile(opcode, cpu.state.pc, cpu, memory),
        cpu.state.a,
        cpu.state.x,
        cpu.state.y,
        cpu.state.p,
        cpu.state.sp,
        cpu.counter);
  }

  private static final class TodoDecompiler implements Decompiler {

    @Override
    public String decompile(byte opcode, short pc, NesCpu cpu, NesCpuMemoryMap memory) {
      return String.format("%02X TODO", opcode);
    }
  }
}
