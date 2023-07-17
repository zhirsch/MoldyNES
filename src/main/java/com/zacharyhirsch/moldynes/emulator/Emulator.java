package com.zacharyhirsch.moldynes.emulator;

import com.zacharyhirsch.moldynes.emulator.instructions.Instruction;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

final class Emulator {

  private final ByteBuffer ram;

  private int pc;

  public Emulator(byte[] ram, int pc) {
    this.ram = ByteBuffer.wrap(ram).order(ByteOrder.LITTLE_ENDIAN);
    this.pc = pc;
  }

  public void step() {
    Instruction instruction = Instruction.decode(ram.position(pc));
    System.out.printf("%04x: %s\n", pc, instruction.describe());
    pc = ram.position();
  }
}
