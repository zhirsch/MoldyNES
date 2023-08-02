package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.NesCpuCycleContext;
import com.zacharyhirsch.moldynes.emulator.StatusRegister;
import com.zacharyhirsch.moldynes.emulator.UInt8;
import java.util.function.Consumer;

public abstract class PToggleInstruction extends Instruction {

  private final String name;
  private final UInt8 opcode;
  private final Consumer<StatusRegister> toggle;

  protected PToggleInstruction(UInt8 opcode, Consumer<StatusRegister> toggle) {
    this.name = getClass().getSimpleName().toUpperCase();
    this.opcode = opcode;
    this.toggle = toggle;
  }

  @Override
  public Result execute(NesCpuCycleContext context) {
    // Cycle 2
    UInt8 ignored = context.fetch(context.registers().pc.address());

    // Cycle 3
    toggle.accept(context.registers().p);

    return new Result(() -> new UInt8[] {opcode}, () -> name);
  }
}
