package com.zacharyhirsch.moldynes.emulator;

import static java.lang.Byte.toUnsignedInt;
import static java.lang.Short.toUnsignedInt;

import java.nio.ByteBuffer;

public class Ram {

  public static final short NMI_VECTOR = (short) 0xfffe;

  private final ByteBuffer ram;

  public Ram(ByteBuffer ram) {
    this.ram = ram;
  }

  public byte getAbsoluteIndexed(short address, byte index) {
    return ram.get(toUnsignedInt(address) + toUnsignedInt(index));
  }

  public byte getAbsolute(short address) {
    return getAbsoluteIndexed(address, (byte) 0);
  }

  public byte getZeropageIndexed(byte address, byte index) {
    return ram.get((toUnsignedInt(address) + toUnsignedInt(index)) & 0x00ff);
  }

  public byte getZeropage(byte address) {
    return getZeropageIndexed(address, (byte) 0);
  }

  public short getShortAbsoluteIndexed(short address, byte index) {
    return ram.getShort(toUnsignedInt(address) + toUnsignedInt(index));
  }

  public short getShortAbsolute(short address) {
    return getShortAbsoluteIndexed(address, (byte) 0);
  }

  public void putAbsoluteIndexed(short address, byte index, byte value) {
    ram.put(toUnsignedInt(address) + toUnsignedInt(index), value);
  }

  public void putAbsolute(short address, byte value) {
    putAbsoluteIndexed(address, (byte) 0, value);
  }

  public void putZeropageIndexed(byte address, byte index, byte value) {
    ram.put((toUnsignedInt(address) + toUnsignedInt(index)) & 0x00ff, value);
  }

  public void putZeropage(byte address, byte value) {
    putZeropageIndexed(address, (byte) 0, value);
  }
}
