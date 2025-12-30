package com.zacharyhirsch.moldynes.emulator.apu;

import com.zacharyhirsch.moldynes.emulator.memory.InvalidAddressWriteError;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class NesApuDmc {

  private static final Logger log = LoggerFactory.getLogger(NesApuDmc.class);

  private static final Map<Integer, Integer> RATES =
      Map.ofEntries(
          Map.entry(0x0, 428),
          Map.entry(0x1, 380),
          Map.entry(0x2, 340),
          Map.entry(0x3, 320),
          Map.entry(0x4, 286),
          Map.entry(0x5, 254),
          Map.entry(0x6, 226),
          Map.entry(0x7, 214),
          Map.entry(0x8, 190),
          Map.entry(0x9, 160),
          Map.entry(0xa, 142),
          Map.entry(0xb, 128),
          Map.entry(0xc, 106),
          Map.entry(0xd, 84),
          Map.entry(0xe, 72),
          Map.entry(0xf, 54));

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

  NesApuDmc() {
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

  NesApuIrq irq() {
    return irq;
  }

  int getBytesRemaining() {
    return bytesRemaining;
  }

  void tick() {
    if (timer.tick()) {
      if (!silence) {
        int delta = (shift & 0b0000_0001) == 0 ? -2 : 2;
        outputLevel = Math.clamp(outputLevel + delta, 0, 127);
        log.info("APU [dmc] sample output level set to {}", outputLevel);
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
      // TODO: stall CPU
      // TODO: read the byte at the current address
      buffer = 0;
      incrementCurrentAddress();
      decrementBytesRemaining();
    }
  }

  void write(int address, byte data) {
    switch (address) {
      case 0x4010 -> {
        irq.setInhibited((data & 0b1000_0000) == 0);
        loop = (data & 0b0100_0000) != 0;
        timer.setRate(RATES.get(data & 0b0000_1111));
        log.info("APU [dmc] rate set to {} cycles", timer.getRate());
      }
      case 0x4011 -> {
        outputLevel = data & 0b0111_1111;
        log.info("APU [dmc] output level directly set to {}", outputLevel);
      }
      case 0x4012 -> {
        sampleAddress = 0xc000 + (Byte.toUnsignedInt(data) << 6);
        log.info("APU [dmc] address set to {}", "%04x".formatted(address));
      }
      case 0x4013 -> {
        sampleLength = (Byte.toUnsignedInt(data) << 4) + 1;
        log.info("APU [dmc] length set to {}", sampleLength);
      }
      default -> throw new InvalidAddressWriteError(address);
    }
  }

  void restart() {
    log.info("APU [dmc] restart");
    currentAddress = sampleAddress;
    bytesRemaining = sampleLength;
  }

  void stop() {
    log.info("APU [dmc] stop");
    bytesRemaining = 0;
  }

  private void incrementCurrentAddress() {
    currentAddress++;
    if (currentAddress > 0xffff) {
      currentAddress = 0x8000;
    }
    log.info("APU [dmc] current address incremented to {}", "%04x".formatted(currentAddress));
  }

  private void decrementBytesRemaining() {
    bytesRemaining--;
    if (bytesRemaining != 0) {
      log.info("APU [dmc] bytes remaining decremented to {}", bytesRemaining);
      return;
    }
    if (loop) {
      log.info("APU [dmc] sample playback looping");
      restart();
    } else {
      log.info("APU [dmc] sample playback complete");
      irq.set(true);
    }
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
}
