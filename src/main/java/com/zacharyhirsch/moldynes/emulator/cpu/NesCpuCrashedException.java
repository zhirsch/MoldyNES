package com.zacharyhirsch.moldynes.emulator.cpu;


public class NesCpuCrashedException extends RuntimeException {

  public NesCpuCrashedException(String message, NesCpuState state, Exception cause) {
    super("%s @ pc = %04x\n%s".formatted(message, (short) (state.pc - 1), state), cause);
  }

  public NesCpuCrashedException(String message, NesCpuState state) {
    this(message, state, null);
  }

  public NesCpuCrashedException(NesCpuState state, Exception cause) {
    this("emulator crashed", state, cause);
  }
}
