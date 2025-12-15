package com.zacharyhirsch.moldynes.emulator.cpu.instructions;

import com.zacharyhirsch.moldynes.emulator.cpu.NesCpuStatusRegister;
import java.util.function.Predicate;

public final class Bpl extends Bxx {

  public Bpl() {
    super(Predicate.not(NesCpuStatusRegister::n));
  }
}
