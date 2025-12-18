package com.zacharyhirsch.moldynes.emulator.cpu;

public final class NesCpuState {

  public byte a = 0;
  public byte x = 0;
  public byte y = 0;
  public final NesCpuStatusRegister p = new NesCpuStatusRegister();
  public byte sp = 0;
  public short pc;
  public byte adh;
  public byte adl;
  public byte data;
  public byte hold;
  public boolean write = false;

  public enum CycleType {
    GET,
    PUT,
    ;

    public CycleType next() {
      return switch (this) {
        case GET -> PUT;
        case PUT -> GET;
      };
    }
  }

  public CycleType cycleType = CycleType.GET;

  public byte pch() {
    return (byte) (pc >>> 8);
  }

  public byte pcl() {
    return (byte) pc;
  }
}
