package com.zacharyhirsch.moldynes.emulator.cpu;

public class NesCpuCrashedException extends RuntimeException {

  public NesCpuCrashedException(short pc, Exception cause) {
    super("Program Counter = %04x".formatted(pc), cause);
  }
}
