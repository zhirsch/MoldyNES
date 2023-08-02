package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.NesAlu;
import com.zacharyhirsch.moldynes.emulator.NesCpuCycleContext;
import com.zacharyhirsch.moldynes.emulator.UInt16;
import com.zacharyhirsch.moldynes.emulator.UInt8;
import com.zacharyhirsch.moldynes.emulator.instructions.Instruction.Result;
import java.util.function.BiConsumer;

final class FetchInstructionHelper {

  private final String name;
  private final UInt8 opcode;
  private final BiConsumer<NesCpuCycleContext, UInt8> impl;

  FetchInstructionHelper(String name, UInt8 opcode, BiConsumer<NesCpuCycleContext, UInt8> impl) {
    this.name = name;
    this.opcode = opcode;
    this.impl = impl;
  }

  Result fetchImmediate(NesCpuCycleContext context) {
    UInt8 data = context.fetch(context.registers().pc.getAddressAndIncrement());

    impl.accept(context, data);

    return new Result(() -> new UInt8[] {opcode, data}, () -> String.format("%s #$%s", name, data));
  }

  Result fetchZeropage(NesCpuCycleContext context) {
    UInt8 adl = context.fetch(context.registers().pc.getAddressAndIncrement());

    UInt8 data = context.fetch(adl);

    impl.accept(context, data);

    return new Result(
        () -> new UInt8[] {opcode, adl}, () -> String.format("%s $%s = %s", name, adl, data));
  }

  Result fetchAbsolute(NesCpuCycleContext context) {
    UInt8 adl = context.fetch(context.registers().pc.getAddressAndIncrement());

    UInt8 adh = context.fetch(context.registers().pc.getAddressAndIncrement());

    UInt8 data = context.fetch(new UInt16(adh, adl));

    impl.accept(context, data);

    return new Result(
        () -> new UInt8[] {opcode, adl, adh},
        () -> String.format("%s $%s = %s", name, new UInt16(adh, adl), data));
  }

  Result fetchZeropageX(NesCpuCycleContext context) {
    return fetchZeropageIndexed(context, context.registers().x, "X");
  }

  Result fetchZeropageY(NesCpuCycleContext context) {
    return fetchZeropageIndexed(context, context.registers().y, "Y");
  }

  private Result fetchZeropageIndexed(NesCpuCycleContext context, UInt8 index, String indexName) {
    UInt8 bal = context.fetch(context.registers().pc.getAddressAndIncrement());

    UInt8 adl = NesAlu.add(bal, index).output();
    UInt8 ignored = context.fetch(bal);

    UInt8 data = context.fetch(adl);

    impl.accept(context, data);

    return new Result(
        () -> new UInt8[] {opcode, bal},
        () -> String.format("%s $%s,%s @ %s = %s", name, bal, indexName, adl, data));
  }

  Result fetchAbsoluteX(NesCpuCycleContext context) {
    return fetchAbsoluteIndexed(context, context.registers().x, "X");
  }

  Result fetchAbsoluteY(NesCpuCycleContext context) {
    return fetchAbsoluteIndexed(context, context.registers().y, "Y");
  }

  private Result fetchAbsoluteIndexed(NesCpuCycleContext context, UInt8 index, String indexName) {
    UInt8 bal = context.fetch(context.registers().pc.getAddressAndIncrement());

    NesAlu.Result offset = NesAlu.add(bal, index);
    UInt8 adl = offset.output();
    boolean pageCrossed = offset.c();
    UInt8 bah = context.fetch(context.registers().pc.getAddressAndIncrement());

    UInt8 adh;
    if (pageCrossed) {
      adh = NesAlu.add(bah, UInt8.cast(1)).output();
      UInt8 ignored = context.fetch(new UInt16(bah, adl));
    } else {
      adh = bah;
    }

    UInt8 data = context.fetch(new UInt16(adh, adl));

    impl.accept(context, data);

    return new Result(
        () -> new UInt8[] {opcode, bal, bah},
        () ->
            String.format(
                "%s $%s,%s @ %s = %s",
                name, new UInt16(bah, bal), indexName, new UInt16(adh, adl), data));
  }

  Result fetchIndirectX(NesCpuCycleContext context) {
    UInt8 bal = context.fetch(context.registers().pc.getAddressAndIncrement());

    UInt8 balX = NesAlu.add(bal, context.registers().x).output();
    UInt8 ignored = context.fetch(bal);

    UInt8 balX1 = NesAlu.add(balX, UInt8.cast(1)).output();
    UInt8 adl = context.fetch(balX);

    UInt8 adh = context.fetch(balX1);

    UInt8 data = context.fetch(new UInt16(adh, adl));

    impl.accept(context, data);

    return new Result(
        () -> new UInt8[] {opcode, bal},
        () ->
            String.format(
                "%s ($%s,X) @ %s = %s = %s", name, bal, balX, new UInt16(adh, adl), data));
  }

  Result fetchIndirectY(NesCpuCycleContext context) {
    // Cycle 2
    UInt8 ial = context.fetch(context.registers().pc.getAddressAndIncrement());

    // Cycle 3
    UInt8 ial1 = NesAlu.add(ial, UInt8.cast(1)).output();
    UInt8 bal = context.fetch(ial);

    // Cycle 4
    NesAlu.Result offset = NesAlu.add(bal, context.registers().y);
    UInt8 adl = offset.output();
    boolean pageCrossed = offset.c();
    UInt8 bah = context.fetch(ial1);

    UInt8 adh;
    if (pageCrossed) {
      // Cycle 5
      adh = NesAlu.add(bah, UInt8.cast(1)).output();
      UInt8 ignored = context.fetch(new UInt16(bah, adl));
    } else {
      adh = bah;
    }

    // Cycle 5 or 6
    UInt8 data = context.fetch(new UInt16(adh, adl));
    impl.accept(context, data);

    return new Result(
        () -> new UInt8[] {opcode, ial},
        () ->
            String.format(
                "%s ($%s),Y = %s @ %s = %s",
                name, ial, new UInt16(bah, bal), new UInt16(adh, adl), data));
  }
}
