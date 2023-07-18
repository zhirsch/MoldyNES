package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.Registers;
import java.nio.ByteBuffer;

public class Nop implements Instruction {

  @Override
  public String describe() {
    return "NOP";
  }

  @Override
  public void execute(ByteBuffer ram, Registers regs) {}
}
