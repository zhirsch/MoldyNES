package com.zacharyhirsch.moldynes.emulator;

import static java.lang.Byte.toUnsignedInt;
import static java.lang.Short.toUnsignedInt;

import com.zacharyhirsch.moldynes.emulator.memory.Index;
import java.nio.ByteBuffer;

public class Ram {

  public final ByteBuffer ram;

  public Ram(ByteBuffer ram) {
    this.ram = ram;
  }

  public <T extends Number> T fetch(short address, Class<T> clazz) {
    return fetchInternal(toUnsignedInt(address), clazz);
  }

  public <T extends Number> T fetch(short address, Index index, Class<T> clazz) {
    return fetchInternal(toUnsignedInt(address) + toUnsignedInt(index.get()), clazz);
  }

  public <T extends Number> T fetchZeropage(byte address, Class<T> clazz) {
    return fetchInternal(toUnsignedInt(address) & 0x00ff, clazz);
  }

  public <T extends Number> T fetchZeropage(byte address, Index index, Class<T> clazz) {
    return fetchInternal((toUnsignedInt(address) + toUnsignedInt(index.get())) & 0x00ff, clazz);
  }

  public void store(short address, byte value) {
    ram.put(toUnsignedInt(address), value);
  }

  public void store(short address, Index index, byte value) {
    ram.put(toUnsignedInt(address) + toUnsignedInt(index.get()), value);
  }

  public void storeZeropage(byte address, byte value) {
    this.ram.put(toUnsignedInt(address) & 0x00ff, value);
  }

  public void storeZeropage(byte address, Index index, byte value) {
    ram.put((toUnsignedInt(address) + toUnsignedInt(index.get())) & 0x00ff, value);
  }

  private <T extends Number> T fetchInternal(int address, Class<T> clazz) {
    if (clazz == Byte.class) {
      return clazz.cast(ram.get(address));
    }
    if (clazz == Short.class) {
      return clazz.cast(ram.getShort(address));
    }
    throw new IllegalArgumentException(clazz.toString());
  }
}
