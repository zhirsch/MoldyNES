package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.NesAlu;
import com.zacharyhirsch.moldynes.emulator.NesCpuCycleContext;
import com.zacharyhirsch.moldynes.emulator.UInt16;
import com.zacharyhirsch.moldynes.emulator.UInt8;
import com.zacharyhirsch.moldynes.emulator.instructions.Instruction.Result;
import java.util.function.Supplier;

final class StoreInstructionHelper {

  private final String name;
  private final UInt8 opcode;

  StoreInstructionHelper(String name, UInt8 opcode) {
    this.name = name;
    this.opcode = opcode;
  }

  Result storeZeropage(NesCpuCycleContext context, Supplier<UInt8> valueSupplier) {
    // Cycle 2
    UInt8 adl = context.fetch(context.registers().pc.getAddressAndIncrement());

    // *** FOR DEBUGGING ONLY ***
    UInt8 oldValue = context.memory().fetch(new UInt16(UInt8.cast(0x00), adl));

    // Cycle 3
    UInt8 value = valueSupplier.get();
    context.store(new UInt16(UInt8.cast(0x00), adl), value);

    return new Result(
        () -> new UInt8[] {opcode, adl}, () -> String.format("%s $%s = %s", name, adl, oldValue));
  }

  Result storeAbsolute(NesCpuCycleContext context, Supplier<UInt8> valueSupplier) {
    // Cycle 2
    UInt8 adl = context.fetch(context.registers().pc.getAddressAndIncrement());

    // Cycle 3
    UInt8 adh = context.fetch(context.registers().pc.getAddressAndIncrement());

    // *** FOR DEBUGGING ONLY ***
    UInt8 oldValue = context.memory().fetch(new UInt16(adh, adl));

    // Cycle 4
    context.store(new UInt16(adh, adl), valueSupplier.get());

    return new Result(
        () -> new UInt8[] {opcode, adl, adh},
        () -> String.format("%s $%s = %s", name, new UInt16(adh, adl), oldValue));
  }

  Result storeZeropageX(NesCpuCycleContext context, Supplier<UInt8> valueSupplier) {
    return storeZeropageIndexed(context, context.registers().x, "X", valueSupplier);
  }

  Result storeZeropageY(NesCpuCycleContext context, Supplier<UInt8> valueSupplier) {
    return storeZeropageIndexed(context, context.registers().y, "Y", valueSupplier);
  }

  private Result storeZeropageIndexed(
      NesCpuCycleContext context, UInt8 index, String indexName, Supplier<UInt8> valueSupplier) {
    // Cycle 2
    UInt8 bal = context.fetch(context.registers().pc.getAddressAndIncrement());

    // Cycle 3
    UInt8 adl = NesAlu.add(bal, index).output();
    UInt8 ignored = context.fetch(new UInt16(UInt8.cast(0x00), bal));

    // *** FOR DEBUGGING ONLY ***
    UInt8 oldValue = context.memory().fetch(new UInt16(UInt8.cast(0x00), adl));

    // Cycle 4
    UInt8 value = valueSupplier.get();
    context.store(new UInt16(UInt8.cast(0x00), adl), value);

    return new Result(
        () -> new UInt8[] {opcode, bal},
        () -> String.format("%s $%s,%s @ %s = %s", name, bal, indexName, adl, oldValue));
  }

  Result storeAbsoluteX(NesCpuCycleContext context, Supplier<UInt8> valueSupplier) {
    return storeAbsoluteIndexed(context, context.registers().x, "X", valueSupplier);
  }

  Result storeAbsoluteY(NesCpuCycleContext context, Supplier<UInt8> valueSupplier) {
    return storeAbsoluteIndexed(context, context.registers().y, "Y", valueSupplier);
  }

  private Result storeAbsoluteIndexed(
      NesCpuCycleContext context, UInt8 index, String indexName, Supplier<UInt8> valueSupplier) {
    // Cycle 2
    UInt8 bal = context.fetch(context.registers().pc.getAddressAndIncrement());

    // Cycle 3
    NesAlu.Result offset = NesAlu.add(bal, index);
    UInt8 adl = offset.output();
    UInt8 bah = context.fetch(context.registers().pc.getAddressAndIncrement());

    // Cycle 4
    UInt8 adh = NesAlu.add(bah, UInt8.cast(0), offset.c()).output();
    UInt8 ignored = context.fetch(new UInt16(bah, adl));

    // *** FOR DEBUGGING ONLY ***
    UInt8 oldValue = context.memory().fetch(new UInt16(adh, adl));

    // Cycle 5
    context.store(new UInt16(adh, adl), valueSupplier.get());

    return new Result(
        () -> new UInt8[] {opcode, bal, bah},
        () ->
            String.format(
                "%s $%s,%s @ %s = %s",
                name, new UInt16(bah, bal), indexName, new UInt16(adh, adl), oldValue));
  }

  Result storeIndirectX(NesCpuCycleContext context, Supplier<UInt8> valueSupplier) {
    // Cycle 2
    UInt8 bal = context.fetch(context.registers().pc.getAddressAndIncrement());

    // Cycle 3
    UInt8 balX = NesAlu.add(bal, context.registers().x).output();
    UInt8 ignored1 = context.fetch(new UInt16(UInt8.cast(0x00), bal));

    // Cycle 4
    UInt8 balX1 = NesAlu.add(balX, UInt8.cast(1)).output();
    UInt8 adl = context.fetch(new UInt16(UInt8.cast(0x00), balX));

    // Cycle 5
    UInt8 adh = context.fetch(new UInt16(UInt8.cast(0x00), balX1));

    // *** FOR DEBUGGING ONLY ***
    UInt8 oldValue = context.memory().fetch(new UInt16(adh, adl));

    // Cycle 6
    context.store(new UInt16(adh, adl), valueSupplier.get());

    return new Result(
        () -> new UInt8[] {opcode, bal},
        () ->
            String.format(
                "%s ($%s,X) @ %s = %s = %s", name, bal, balX, new UInt16(adh, adl), oldValue));
  }

  Result storeIndirectY(NesCpuCycleContext context, Supplier<UInt8> valueSupplier) {
    // Cycle 2
    UInt8 ial = context.fetch(context.registers().pc.getAddressAndIncrement());

    // Cycle 3
    UInt8 ial1 = NesAlu.add(ial, UInt8.cast(1)).output();
    UInt8 bal = context.fetch(new UInt16(UInt8.cast(0x00), ial));

    // Cycle 4
    UInt8 adl = NesAlu.add(bal, context.registers().y).output();
    UInt8 adh = context.fetch(new UInt16(UInt8.cast(0x00), ial1));

    // Cycle 5
    UInt8 ignored = context.fetch(new UInt16(adh, adl));

    // *** FOR DEBUGGING ONLY ***
    UInt8 oldValue = context.memory().fetch(new UInt16(adh, adl));

    // Cycle 6
    context.store(new UInt16(adh, adl), valueSupplier.get());

    return new Result(
        () -> new UInt8[] {opcode, ial},
        () ->
            String.format(
                "%s ($%s),Y = %s @ %s = %s",
                name, ial, new UInt16(adh, bal), new UInt16(adh, adl), oldValue));
  }
}
