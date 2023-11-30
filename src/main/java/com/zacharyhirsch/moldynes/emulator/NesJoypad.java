package com.zacharyhirsch.moldynes.emulator;

public final class NesJoypad {

  public enum Button {
    RIGHT((byte) 0b1000_0000),
    LEFT((byte) 0b0100_0000),
    DOWN((byte) 0b0010_0000),
    UP((byte) 0b0001_0000),
    START((byte) 0b0000_1000),
    SELECT((byte) 0b0000_0100),
    BUTTON_B((byte) 0b0000_0010),
    BUTTON_A((byte) 0b0000_0001),
    ;

    private final byte value;

    Button(byte value) {
      this.value = value;
    }

    public byte getValue() {
      return value;
    }
  }

  private boolean strobe = false;
  private int index = 0;
  private byte status = 0;

  public byte read() {
    if (index > 7) {
      return 1;
    }
    byte value = (byte) bit(status, index);
    if (!strobe) {
      index++;
    }
    return value;
  }

  public void write(byte value) {
    strobe = bit(value, 0) == 1;
    if (strobe) {
      index = 0;
    }
  }

  public void setButton(Button button, boolean pressed) {
    if (pressed) {
      status |= button.getValue();
    } else {
      status &= (byte) ~button.getValue();
    }
  }

  private static int bit(byte value, int i) {
    return (Byte.toUnsignedInt(value) >>> i) & 1;
  }
}
