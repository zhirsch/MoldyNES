package com.zacharyhirsch.moldynes.emulator;

public class EmulatorCrashedException extends RuntimeException {

  public EmulatorCrashedException(Registers regs, Exception cause) {
    super("\n" + regs, cause);
  }
}
