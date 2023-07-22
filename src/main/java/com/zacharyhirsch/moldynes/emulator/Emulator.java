package com.zacharyhirsch.moldynes.emulator;

import static java.nio.ByteOrder.LITTLE_ENDIAN;

import com.zacharyhirsch.moldynes.emulator.instructions.Decoder;
import com.zacharyhirsch.moldynes.emulator.instructions.Instruction;
import java.nio.ByteBuffer;

final class Emulator {

  private final Ram ram;
  private final Registers regs;
  private final Stack stack;
  private final Decoder decoder;

  public Emulator(ByteBuffer ram, short pc) {
    this.ram = new Ram(ram.order(LITTLE_ENDIAN));
    this.regs = new Registers(pc);
    this.stack = new Stack(ram, this.regs);
    this.decoder = new Decoder(this.ram, regs, new Program(this.ram, regs));
  }

  public void step() {
    System.out.printf("%04x : ", regs.pc);
    Instruction instr = decoder.decode();
    System.out.println(instr);
    instr.execute(ram, regs, stack);
  }

  public void run() {
    for (int i = 0; i < 100_000; i++) {
      short oldPc = regs.pc;
      step();
      if (regs.pc == oldPc) {
        throw new InfiniteLoopException(regs);
      }
    }
  }
}
