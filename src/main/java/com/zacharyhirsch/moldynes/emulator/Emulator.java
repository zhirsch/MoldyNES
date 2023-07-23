package com.zacharyhirsch.moldynes.emulator;

import static java.nio.ByteOrder.LITTLE_ENDIAN;

import com.zacharyhirsch.moldynes.emulator.instructions.Decoder;
import com.zacharyhirsch.moldynes.emulator.instructions.Decoder.Decoded;
import com.zacharyhirsch.moldynes.emulator.instructions.Instruction;
import java.nio.ByteBuffer;

final class Emulator {

  private static final boolean DEBUG = false;

  private final Ram ram;
  private final Registers regs;

  public Emulator(ByteBuffer ram, short pc) {
    this.regs = new Registers(pc);
    this.ram = new Ram(ram.order(LITTLE_ENDIAN), regs);
  }

  public void run() {
    Decoder decoder = new Decoder(ram, regs);
    for (int cycle = 1; ; cycle++) {
      Decoded decoded = decoder.next();
      if (DEBUG) {
        System.out.printf("%d => %s\n", cycle, decoded);
      }
      regs.pc += decoded.instruction().getSize();
      if (!execute(decoded.instruction())) {
        break;
      }
      if (decoded.pc() == regs.pc) {
        throw new InfiniteLoopException(regs);
      }
    }
  }

  private boolean execute(Instruction instruction) {
    try {
      instruction.execute(ram, regs);
      return true;
    } catch (HaltException ignored) {
      return false;
    }
  }
}
