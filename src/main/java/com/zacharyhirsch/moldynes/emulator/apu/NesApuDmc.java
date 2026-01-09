package com.zacharyhirsch.moldynes.emulator.apu;

import com.zacharyhirsch.moldynes.emulator.cpu.NesCpu;

public final class NesApuDmc {

  private static final int[] RATES = {
    428, 380, 340, 320, 286, 254, 226, 214, 190, 160, 142, 128, 106, 84, 72, 54,
  };

  private final NesCpu cpu;
  private final NesApuTimer timer;
  private final NesApuIrq irq;

  private boolean loop;
  private int sampleAddress;
  private int sampleLength;

  private int currentAddress;
  private int bytesRemaining;
  private Byte buffer;

  private int bitsRemaining;
  private boolean silence;
  private byte shift;

  private int outputLevel;

  NesApuDmc(NesCpu cpu) {
    this.cpu = cpu;
    this.timer = new NesApuTimer();
    this.irq = new NesApuIrq();
    this.loop = false;
    this.sampleAddress = 0;
    this.sampleLength = 0;
    this.buffer = null;
    this.bitsRemaining = 8;
    this.silence = false;
    this.shift = 0;
    this.outputLevel = 0;
  }

  void enable(boolean enabled) {
    if (enabled) {
      if (getBytesRemaining() == 0) {
        restart();
      }
    } else {
      stop();
    }
  }

  NesApuIrq irq() {
    return irq;
  }

  int getBytesRemaining() {
    return bytesRemaining;
  }

  int getCurrentVolume() {
    return outputLevel;
  }

  void tick() {
    assert 0 <= outputLevel && outputLevel <= 127;
    if (timer.tick()) {
      if (!silence) {
        if ((shift & 0b0000_0001) != 0) {
          if (outputLevel <= 125) {
            outputLevel += 2;
          }
        } else {
          if (outputLevel >= 2) {
            outputLevel -= 2;
          }
        }
      }
      shift = (byte) (shift >>> 1);
      bitsRemaining--;
      if (bitsRemaining == 0) {
        bitsRemaining = 8;
        if (buffer == null) {
          silence = true;
        } else {
          silence = false;
          shift = buffer;
          buffer = null;
        }
      }
    }
    if (buffer == null && bytesRemaining > 0) {
      cpu.startDmcDma((short) sampleAddress, buffer -> this.buffer = buffer);
      buffer = 0;
      incrementCurrentAddress();
      decrementBytesRemaining();
    }
  }

  public void writeControl(byte data) {
    irq.setInhibited((data & 0b1000_0000) == 0);
    loop = (data & 0b0100_0000) != 0;
    timer.setPeriod(RATES[data & 0b0000_1111]);
  }

  public void writeDac(byte data) {
    outputLevel = data & 0b0111_1111;
  }

  public void writeAddress(byte data) {
    sampleAddress = 0xc000 + (Byte.toUnsignedInt(data) << 6);
  }

  public void writeLength(byte data) {
    sampleLength = (Byte.toUnsignedInt(data) << 4) + 1;
  }

  private void restart() {
    currentAddress = sampleAddress;
    bytesRemaining = sampleLength;
  }

  private void stop() {
    bytesRemaining = 0;
  }

  private void incrementCurrentAddress() {
    currentAddress++;
    if (currentAddress > 0xffff) {
      currentAddress = 0x8000;
    }
  }

  private void decrementBytesRemaining() {
    bytesRemaining--;
    if (bytesRemaining != 0) {
      return;
    }
    if (loop) {
      restart();
    } else {
      irq.set(true);
    }
  }
}
