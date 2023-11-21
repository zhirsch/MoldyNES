package com.zacharyhirsch.moldynes.emulator;

import com.zacharyhirsch.moldynes.emulator.cpu.NesCpuState;

public class EmulatorCrashedException extends RuntimeException {

  public EmulatorCrashedException(String message, NesCpuState state, Exception cause) {
    super("%s @ pc = %04x\n%s".formatted(message, state.pc, state), cause);
  }

  public EmulatorCrashedException(String message, NesCpuState state) {
    this(message, state, null);
  }

  public EmulatorCrashedException(NesCpuState state, Exception cause) {
    this("emulator crashed", state, cause);
  }
}
