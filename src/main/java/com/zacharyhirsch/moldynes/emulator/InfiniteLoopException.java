package com.zacharyhirsch.moldynes.emulator;

public class InfiniteLoopException extends RuntimeException {

  public InfiniteLoopException(Registers regs) {
    super("infinite loop\n" + regs);
  }
}
