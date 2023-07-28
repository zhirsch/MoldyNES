package com.zacharyhirsch.moldynes.emulator;

public class InfiniteLoopException extends RuntimeException {

  public InfiniteLoopException(Registers regs) {
    super("infinite loop @ pc = " + regs.pc + "\n" + regs);
  }
}
