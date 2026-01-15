package com.zacharyhirsch.moldynes.emulator.io;

import com.zacharyhirsch.jna.sdl3.SDL_KeyboardEvent;
import java.lang.foreign.MemorySegment;
import java.util.Map;

final class SdlJoypad implements Joypad {

  private final Map<Integer, Button> buttons;

  private boolean strobe = false;
  private int index = 0;
  private byte status = 0;

  SdlJoypad(Map<Integer, Button> buttons) {
    this.buttons = buttons;
  }

  @Override
  public byte readJoypad() {
    if (index > 7) {
      return 1;
    }
    byte value = (byte) bit(status, index);
    if (!strobe) {
      index++;
    }
    return value;
  }

  @Override
  public void writeJoypad(byte value) {
    strobe = bit(value, 0) == 1;
    if (strobe) {
      index = 0;
    }
  }

  @Override
  public void close() {}

  public void setButton(MemorySegment keyboardEvent, boolean pressed) {
    Button button = buttons.get(SDL_KeyboardEvent.key(keyboardEvent));
    if (button == null) {
      return;
    }
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
