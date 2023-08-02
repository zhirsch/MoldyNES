package com.zacharyhirsch.moldynes.emulator;

public class InfiniteLoopException extends EmulatorCrashedException {

  public InfiniteLoopException(NesCpuRegisters regs) {
    super("infinite loop", regs);
  }
}
