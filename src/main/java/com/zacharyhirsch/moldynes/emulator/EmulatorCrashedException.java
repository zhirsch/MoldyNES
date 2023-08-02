package com.zacharyhirsch.moldynes.emulator;

public class EmulatorCrashedException extends RuntimeException {

  public EmulatorCrashedException(String message, NesCpuRegisters regs, Exception cause) {
    super(message + " @ pc = " + regs.pc + "\n" + regs, cause);
  }

  public EmulatorCrashedException(String message, NesCpuRegisters regs) {
    this(message, regs, null);
  }

  public EmulatorCrashedException(NesCpuRegisters regs, Exception cause) {
    this("emulator crashed", regs, cause);
  }
}
