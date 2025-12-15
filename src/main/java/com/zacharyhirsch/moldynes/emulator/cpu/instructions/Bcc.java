package com.zacharyhirsch.moldynes.emulator.cpu.instructions;

import com.zacharyhirsch.moldynes.emulator.cpu.NesCpuStatusRegister;
import java.util.function.Predicate;

public class Bcc extends Bxx {

  public Bcc() {
    super(Predicate.not(NesCpuStatusRegister::c));
  }
}
