package com.zacharyhirsch.moldynes.emulator;

import com.zacharyhirsch.moldynes.emulator.instructions.Decoder;
import com.zacharyhirsch.moldynes.emulator.instructions.Decoder.Decoded;
import com.zacharyhirsch.moldynes.emulator.instructions.Instruction;
import com.zacharyhirsch.moldynes.emulator.instructions.Instruction.Result;
import com.zacharyhirsch.moldynes.emulator.instructions.Undocumented;
import java.util.Arrays;
import java.util.stream.Collectors;

final class Emulator {

  private static final boolean DEBUG = true;

  private final NesCpuRegisters regs;
  private final NesCpuMemory memory;

  public Emulator(NesCpuMemory memory, ProgramCounter pc) {
    this.regs = new NesCpuRegisters(pc, new StatusRegister(), new StackPointer());
    this.memory = memory;
  }

  public void run() {
    Decoder decoder = new Decoder();
    int cycle = 7; // TODO: why 7
    while (true) {
      NesCpuRegisters pre = regs.copy();
      NesCpuCycleContext context = new NesCpuCycleContext(memory, regs);
      try {
        Decoded decoded = decoder.next(context);
        Result result = decoded.instruction().execute(context);
        log(cycle, pre, decoded, result);
        if (result.halt()) {
          break;
        }
      } catch (Exception exc) {
        throw new EmulatorCrashedException(regs, exc);
      }
      cycle += context.cycles();
      if (regs.pc.address().equals(pre.pc.address())) {
        throw new InfiniteLoopException(regs);
      }
    }
  }

  private void log(int cycle, NesCpuRegisters pre, Decoded decoded, Instruction.Result result) {
    if (!DEBUG) {
      return;
    }
    System.out.printf(
        "%s  %-8s %1s%-30s  %s PPU:  0,  0 CYC:%d\n",
        pre.pc,
        Arrays.stream(result.bytes().get()).map(UInt8::toString).collect(Collectors.joining(" ")),
        decoded.instruction() instanceof Undocumented ? "*" : "",
        result.text().get(),
        pre,
        cycle);
  }
}
