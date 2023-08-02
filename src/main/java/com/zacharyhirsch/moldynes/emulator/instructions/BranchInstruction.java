package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.NesAlu;
import com.zacharyhirsch.moldynes.emulator.NesCpuCycleContext;
import com.zacharyhirsch.moldynes.emulator.StatusRegister;
import com.zacharyhirsch.moldynes.emulator.UInt16;
import com.zacharyhirsch.moldynes.emulator.UInt8;
import java.util.function.Predicate;

public abstract class BranchInstruction extends Instruction {

  private final String name;
  private final UInt8 opcode;
  private final Predicate<StatusRegister> predicate;

  public BranchInstruction(UInt8 opcode, Predicate<StatusRegister> predicate) {
    this.name = getClass().getSimpleName().toUpperCase();
    this.opcode = opcode;
    this.predicate = predicate;
  }

  @Override
  public Result execute(NesCpuCycleContext context) {
    UInt8 offset = context.fetch(context.registers().pc.getAddressAndIncrement());

    if (!predicate.test(context.registers().p)) {
      UInt16 pc = context.registers().pc.address();
      return new Result(
          () -> new UInt8[] {opcode, offset},
          () -> String.format("%s $%s", name, UInt16.cast(Short.toUnsignedInt(pc.value()) + offset.value())));
    }

    NesAlu.Result adlResult = NesAlu.add(context.registers().pc.address().lsb(), offset);
    UInt8 pcl = adlResult.output();
    UInt8 ignored = context.fetch(context.registers().pc.getAddressAndIncrement());

    UInt8 adh = context.registers().pc.getAddressAndIncrement().msb();
    UInt8 pch;
    if ((offset.value() > 0 && adlResult.c()) || (offset.value() < 0 && !adlResult.c())) {
      pch = NesAlu.add(adh, offset.signum()).output();
      UInt8 ignored2 = context.fetch(new UInt16(adh, pcl));
    } else {
      pch = adh;
    }

    context.registers().pc.set(new UInt16(pch, pcl));

    return new Result(
        () -> new UInt8[] {opcode, offset},
        () -> String.format("%s $%s", name, new UInt16(pch, pcl)));
  }
}
