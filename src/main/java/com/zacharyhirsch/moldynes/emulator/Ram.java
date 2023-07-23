package com.zacharyhirsch.moldynes.emulator;

import static java.lang.Byte.toUnsignedInt;
import static java.lang.Short.toUnsignedInt;

import com.zacharyhirsch.moldynes.emulator.memory.Index;
import java.nio.ByteBuffer;

public final class Ram {

  private static final short STACK_BASE = 0x0100;

  private final ByteBuffer ram;
  private final Registers regs;

  public Ram(ByteBuffer ram, Registers regs) {
    this.ram = ram;
    this.regs = regs;
  }

  public <T extends Number> T fetch(short address, Class<T> clazz) {
    return fetch(toUnsignedInt(address), clazz);
  }

  public <T extends Number> T fetch(short address, Index index, Class<T> clazz) {
    return fetch(toUnsignedInt(address) + toUnsignedInt(index.get()), clazz);
  }

  public <T extends Number> T fetchZeropage(byte address, Class<T> clazz) {
    return fetch(toUnsignedInt(address) & 0x00ff, clazz);
  }

  public <T extends Number> T fetchZeropage(byte address, Index index, Class<T> clazz) {
    return fetch((toUnsignedInt(address) + toUnsignedInt(index.get())) & 0x00ff, clazz);
  }

  public void store(short address, byte value) {
    store(toUnsignedInt(address), value);
  }

  public void store(short address, Index index, byte value) {
    store(toUnsignedInt(address) + toUnsignedInt(index.get()), value);
  }

  public void storeZeropage(byte address, byte value) {
    store(toUnsignedInt(address) & 0x00ff, value);
  }

  public void storeZeropage(byte address, Index index, byte value) {
    store((toUnsignedInt(address) + toUnsignedInt(index.get())) & 0x00ff, value);
  }

  public <T extends Number> void push(T value, Class<T> clazz) {
    int offset = stackOffset(clazz);
    store(STACK_BASE + toUnsignedInt(regs.sp) - offset, value);
    regs.sp -= offset + 1;
  }

  public <T extends Number> T pull(Class<T> clazz) {
    int offset = stackOffset(clazz);
    regs.sp += offset + 1;
    return fetch(STACK_BASE + toUnsignedInt(regs.sp) - offset, clazz);
  }

  private static <T extends Number> int stackOffset(Class<T> clazz) {
    if (clazz.equals(Byte.class)) {
      return 0;
    }
    if (clazz.equals(Short.class)) {
      return 1;
    }
    throw new IllegalArgumentException(clazz.toString());
  }

  private <T extends Number> T fetch(int address, Class<T> clazz) {
    if (clazz.equals(Byte.class)) {
      return clazz.cast(ram.get(address));
    }
    if (clazz.equals(Short.class)) {
      return clazz.cast(ram.getShort(address));
    }
    throw new IllegalArgumentException(clazz.toString());
  }

  private <T extends Number> void store(int address, T value) {
    if (value.getClass().equals(Byte.class)) {
      ram.put(address, value.byteValue());
      return;
    }
    if (value.getClass().equals(Short.class)) {
      ram.putShort(address, value.shortValue());
      return;
    }
    throw new IllegalArgumentException(value.getClass().toString());
  }
}
