package com.zacharyhirsch.moldynes.emulator.apu;

import java.util.BitSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class NesApu {

  private static final Logger log = LoggerFactory.getLogger(NesApu.class);

  private final NesApuPulseChannel pulse1;

  private int frameCounter;
  private byte pendingFrameCounter;
  private int frameCounterResetDelay;

  private int mode;

  private boolean irq;
  private boolean irqInhibited;

  public NesApu() {
    this.pulse1 = new NesApuPulseChannel((short) 0x4000);
    this.frameCounter = 0;
    this.pendingFrameCounter = 0;
    this.frameCounterResetDelay = 0;
    this.mode = 0;
    this.irq = false;
    this.irqInhibited = false;
  }

  public boolean irq() {
    return irq;
  }

  public void tick(boolean odd) {
    handleDelayedFrameCounterReset();
    if (odd) {
      if (mode == 0) {
        switch (frameCounter) {
          case 3728 -> {
            log.info("APU frame counter mode {} count {}", mode, frameCounter);
          }
          case 7456 -> {
            log.info("APU frame counter mode {} count {}", mode, frameCounter);
            pulse1.lengthCounter().tick();
          }
          case 11185 -> {
            log.info("APU frame counter mode {} count {}", mode, frameCounter);
          }
          case 14914 -> {
            log.info("APU frame counter mode {} count {}", mode, frameCounter);
            if (!irqInhibited) {
              irq = true;
            }
            pulse1.lengthCounter().tick();
          }
          case 14915 -> {
            log.info("APU frame counter mode {} count {}", mode, frameCounter);
            if (!irqInhibited) {
              irq = true;
            }
            frameCounter = 0;
            return;
          }
        }
      } else if (mode == 1) {
        switch (frameCounter) {
          case 3728 -> {
            log.info("APU frame counter mode {} count {}", mode, frameCounter);
          }
          case 7456 -> {
            log.info("APU frame counter mode {} count {}", mode, frameCounter);
            pulse1.lengthCounter().tick();
          }
          case 11185 -> {
            log.info("APU frame counter mode {} count {}", mode, frameCounter);
          }
          case 14914 -> {
            log.info("APU frame counter mode {} count {}", mode, frameCounter);
          }
          case 18640 -> {
            log.info("APU frame counter mode {} count {}", mode, frameCounter);
            pulse1.lengthCounter().tick();
          }
          case 18641 -> {
            log.info("APU frame counter mode {} count {}", mode, frameCounter);
            frameCounter = 0;
            return;
          }
        }
      } else {
        throw new IllegalStateException("mode = %d".formatted(mode));
      }
      frameCounter++;
    }
  }

  private void handleDelayedFrameCounterReset() {
    if (frameCounterResetDelay > 0) {
      frameCounterResetDelay--;
      if (frameCounterResetDelay == 0) {
        frameCounter = 0;
        mode = (pendingFrameCounter & 0b1000_0000) >>> 7;
        irqInhibited = (pendingFrameCounter & 0b0100_0000) != 0;
        if (irqInhibited) {
          irq = false;
        }
        if (mode == 1) {
          pulse1.lengthCounter().tick();
        }
      }
    }
  }

  public byte readStatus() {
    BitSet status = new BitSet(8);
    status.set(0, pulse1.lengthCounter().value() > 0);
    status.set(1, false); // pulse2
    status.set(2, false); // triangle
    status.set(3, false); // noise
    status.set(4, false); // dmc active
    status.set(5, false); // open bus
    status.set(6, irq);
    status.set(7, false); // dmc interrupt
    byte value = status.isEmpty() ? 0 : status.toByteArray()[0];
    log.info("APU 4015 -> {}", "%02x".formatted(value));
    irq = false;
    return value;
  }

  public void writePulse1(short address, byte data) {
    log.info("APU {} <- {}", "%04x".formatted(address), "%02x".formatted(data));
    pulse1.write(address, data);
  }

  public void writePulse2(short address, byte data) {
    throw new UnsupportedOperationException(
        "APU write pulse2 %04x <- %02x".formatted(address, data));
  }

  public void writeTriangle(short address, byte data) {
    throw new UnsupportedOperationException(
        "APU write triangle %04x <- %02x".formatted(address, data));
  }

  public void writeNoise(short address, byte data) {
    throw new UnsupportedOperationException(
        "APU write noise %04x <- %02x".formatted(address, data));
  }

  public void writeDmc(short address, byte data) {
    throw new UnsupportedOperationException("APU write DMC %04x <- %02x".formatted(address, data));
  }

  public void writeStatus(byte data) {
    log.info("APU 4015 <- {}", "%02x".formatted(data));
    pulse1.enable((data & 0b0000_0001) != 0);
  }

  public void writeFrameCounter(byte data, boolean odd) {
    log.info("APU 4017 <- {}", "%02x".formatted(data));
    pendingFrameCounter = data;
    frameCounterResetDelay = odd ? 4 : 3;
  }
}
