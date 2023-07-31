package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.NesAlu;
import com.zacharyhirsch.moldynes.emulator.NesCpuMemory;
import com.zacharyhirsch.moldynes.emulator.NesCpuStack;
import com.zacharyhirsch.moldynes.emulator.Registers;
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
  public Result execute2(NesCpuMemory memory, NesCpuStack stack, Registers regs) {
    // Cycle 2
    UInt8 offset = memory.fetch(regs.pc.address());
    regs.pc.inc();

    if (!predicate.test(regs.p)) {
      // Cycle 3 -- Branch Not Taken
      return new Result(
          2,
          false,
          () -> new UInt8[] {opcode, offset},
          () ->
              String.format(
                  "%s $%s",
                  name,
                  UInt16.cast(Short.toUnsignedInt(regs.pc.address().value()) + offset.value())));
    }

    // Cycle 3 -- Branch Taken
    NesAlu.Result adlResult = NesAlu.add(regs.pc.address().lsb(), offset);
    UInt8 pcl = adlResult.output();
    UInt8 ignored = memory.fetch(regs.pc.address());
    regs.pc.inc();

    if ((offset.value() > 0 && adlResult.c()) || (offset.value() < 0 && !adlResult.c())) {
      // Cycle 4 -- Page Crossed
      UInt8 pch =
          offset.value() > 0
              ? NesAlu.add(regs.pc.address().msb(), UInt8.cast(1)).output()
              : NesAlu.sub(regs.pc.address().msb(), UInt8.cast(1)).output();
      UInt8 ignored2 = memory.fetch(new UInt16(regs.pc.address().msb(), pcl));
      regs.pc.inc();

      // Cycle 5
      regs.pc.set(new UInt16(pch, pcl));

      return new Result(
          4,
          false,
          () -> new UInt8[] {opcode, offset},
          () -> String.format("%s $%s", name, regs.pc));
    }

    // Cycle 4 -- Same Page
    regs.pc.set(new UInt16(regs.pc.address().msb(), pcl));

    return new Result(
        3, false, () -> new UInt8[] {opcode, offset}, () -> String.format("%s $%s", name, regs.pc));
  }
}
