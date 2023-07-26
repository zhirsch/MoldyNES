package com.zacharyhirsch.moldynes.emulator;

import com.zacharyhirsch.moldynes.emulator.instructions.Decoder;
import com.zacharyhirsch.moldynes.emulator.instructions.Decoder.Decoded;
import com.zacharyhirsch.moldynes.emulator.instructions.Instruction;
import com.zacharyhirsch.moldynes.emulator.instructions.Undocumented;

final class Emulator {

  private static final boolean DEBUG = true;

  private final Registers regs;
  private final NesCpuMemory memory;
  private final NesCpuStack stack;

  public Emulator(NesCpuMemory memory, ProgramCounter pc) {
    this.regs = new Registers(pc);
    this.memory = memory;
    this.stack = new NesCpuStack(memory, regs);
  }

  public void run() {
    Decoder decoder = new Decoder(memory, regs);
    for (int cycle = 1; cycle < 10_000; cycle++) {
      Decoded decoded = decoder.next();
      regs.pc = regs.pc.offset(UInt8.cast(decoded.instruction().getSize()));
      if (DEBUG) {
        System.out.printf(
            "%s  %-8s %s%-30s  A:%s X:%s Y:%s P:%s SP:%s PPU:  0,  0 CYC:%d\n",
            decoded.pc(),
            formatBytes(decoded.bytes()),
            decoded.instruction() instanceof Undocumented ? "*" : " ",
            decoded.instruction(),
            regs.a,
            regs.x,
            regs.y,
            NesAlu.and(regs.sr.toByte(), UInt8.cast(0b1110_1111)).output(),
            regs.sp,
            cycle);
      }
      if (!execute(decoded.instruction())) {
        break;
      }
      if (decoded.pc() == regs.pc) {
        throw new InfiniteLoopException(regs);
      }
    }
  }

  private String formatBytes(UInt8[] bytes) {
    return switch (bytes.length) {
      case 1 -> String.format("%s", bytes[0]);
      case 2 -> String.format("%s %s", bytes[0], bytes[1]);
      case 3 -> String.format("%s %s %s", bytes[0], bytes[1], bytes[2]);
      default -> "<oops>";
    };
  }

  private boolean execute(Instruction instruction) {
    try {
      instruction.execute(memory, stack, regs);
      return true;
    } catch (HaltException ignored) {
      return false;
    } catch (Exception exc) {
      throw new EmulatorCrashedException(regs, exc);
    }
  }
}
