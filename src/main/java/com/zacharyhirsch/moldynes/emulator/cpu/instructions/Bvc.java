package com.zacharyhirsch.moldynes.emulator.cpu.instructions;

import com.zacharyhirsch.moldynes.emulator.cpu.NesCpuStatusRegister;
import java.util.function.Predicate;

public final class Bvc extends Bxx {

  public Bvc() {
    super(Predicate.not(NesCpuStatusRegister::v));
  }
}
