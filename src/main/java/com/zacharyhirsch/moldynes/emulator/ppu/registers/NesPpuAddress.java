package com.zacharyhirsch.moldynes.emulator.ppu.registers;

public final class NesPpuAddress {

  private byte hi = 0;
  private byte lo = 0;
  private boolean latch = true;

  public short get() {
    return (short) ((Byte.toUnsignedInt(hi) << 8) | Byte.toUnsignedInt(lo));
  }

  public void update(byte data) {
    if (latch) {
      hi = data;
    } else {
      lo = data;
    }
    if (get() > 0x3fff) {
        set((short) (get() & 0b0011_1111_1111_1111));
    }
    latch = !latch;
  }

  public void increment(byte inc) {
    byte lo = this.lo;
    this.lo += inc;
    if (Byte.toUnsignedInt(lo) > Byte.toUnsignedInt(this.lo)) {
      hi++;
    }
    if (get() > 0x3fff) {
      set((short) (get() & 0b0011_1111_1111_1111));
    }
  }

  public void reset() {
    latch = true;
  }

  private void set(short address) {
    hi = (byte) (address >>> 8);
    lo = (byte) (address & 0x00ff);
  }
}
