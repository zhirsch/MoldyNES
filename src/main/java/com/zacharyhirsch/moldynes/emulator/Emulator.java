package com.zacharyhirsch.moldynes.emulator;

import static java.nio.ByteOrder.LITTLE_ENDIAN;

import com.zacharyhirsch.moldynes.emulator.instructions.Instruction;
import java.nio.ByteBuffer;

final class Emulator {

  private final ByteBuffer ram;
  private final Registers regs;

  public Emulator(byte[] ram, short pc) {
    this.ram = ByteBuffer.wrap(ram).order(LITTLE_ENDIAN);
    this.regs = new Registers(pc);
  }

  public void step() {
    var instr = Instruction.decode(ram.position(regs.pc));
//    System.out.printf("%04x : %s\n", regs.pc, instr.describe());
    regs.pc = (short) ram.position();
    instr.execute(ram, regs);
  }

  public void run() {
    for (int i = 0; i < 100_000; i++) {
      short oldPc = regs.pc;
      step();
      if (regs.pc == oldPc) {
        throw new InfiniteLoopException(regs.pc);
      }
    }
  }
}
