package com.zacharyhirsch.moldynes.emulator;

import static java.lang.Byte.toUnsignedInt;

public final class ByteUtil {

  private ByteUtil() {}

  public static short compose(byte lsb, byte msb) {
    int unsignedLsb = toUnsignedInt(lsb);
    int unsignedMsb = toUnsignedInt(msb);
    return (short) ((unsignedMsb << 8) | unsignedLsb);
  }
}
