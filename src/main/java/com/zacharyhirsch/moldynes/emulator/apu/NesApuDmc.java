package com.zacharyhirsch.moldynes.emulator.apu;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class NesApuDmc {

  private static final Logger log = LoggerFactory.getLogger(NesApuDmc.class);

  private record Rate(int lo, int hi) {
    Rate(int rate) {
      this((rate & 0x00ff) >>> 0, (rate & 0xff00) >>> 8);
    }
  }

  private static final Map<Integer, Rate> RATES =
      Map.ofEntries(
          Map.entry(0x0, new Rate(428)),
          Map.entry(0x1, new Rate(380)),
          Map.entry(0x2, new Rate(340)),
          Map.entry(0x3, new Rate(320)),
          Map.entry(0x4, new Rate(286)),
          Map.entry(0x5, new Rate(254)),
          Map.entry(0x6, new Rate(226)),
          Map.entry(0x7, new Rate(214)),
          Map.entry(0x8, new Rate(190)),
          Map.entry(0x9, new Rate(160)),
          Map.entry(0xa, new Rate(142)),
          Map.entry(0xb, new Rate(128)),
          Map.entry(0xc, new Rate(106)),
          Map.entry(0xd, new Rate(84)),
          Map.entry(0xe, new Rate(72)),
          Map.entry(0xf, new Rate(54)));

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

  int getCurrentVolume() {
    return 0;
  }

  void tick() {
    if (timer.tick()) {
      if (!silence) {
        int delta = (shift & 0b0000_0001) == 0 ? -2 : 2;
        outputLevel = Math.clamp(outputLevel + delta, 0, 127);
        log.trace("APU [dmc] sample output level set to {}", outputLevel);
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

  public void writeControl(byte data) {
    irq.setInhibited((data & 0b1000_0000) == 0);
    loop = (data & 0b0100_0000) != 0;
    Rate rate = RATES.get(data & 0b0000_1111);
    timer.setPeriodLo(rate.lo());
    timer.setPeriodHi(rate.hi(), 0);
    log.trace("APU [dmc] rate set to {} cycles", rate);
  }

  public void writeDac(byte data) {
    outputLevel = data & 0b0111_1111;
    log.trace("APU [dmc] output level directly set to {}", outputLevel);
  }

  public void writeAddress(byte data) {
    sampleAddress = 0xc000 + (Byte.toUnsignedInt(data) << 6);
    log.trace("APU [dmc] address set to {}", "%04x".formatted(sampleAddress));
  }

  public void writeLength(byte data) {
    sampleLength = (Byte.toUnsignedInt(data) << 4) + 1;
    log.trace("APU [dmc] length set to {}", sampleLength);
  }

  void restart() {
    log.trace("APU [dmc] restart");
    currentAddress = sampleAddress;
    bytesRemaining = sampleLength;
  }

  void stop() {
    log.trace("APU [dmc] stop");
    bytesRemaining = 0;
  }

  private void incrementCurrentAddress() {
    currentAddress++;
    if (currentAddress > 0xffff) {
      currentAddress = 0x8000;
    }
    log.trace("APU [dmc] current address incremented to {}", "%04x".formatted(currentAddress));
  }

  private void decrementBytesRemaining() {
    bytesRemaining--;
    if (bytesRemaining != 0) {
      log.trace("APU [dmc] bytes remaining decremented to {}", bytesRemaining);
      return;
    }
    if (loop) {
      log.trace("APU [dmc] sample playback looping");
      restart();
    } else {
      log.trace("APU [dmc] sample playback complete");
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
