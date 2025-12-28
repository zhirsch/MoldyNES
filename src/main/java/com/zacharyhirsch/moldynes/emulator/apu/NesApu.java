package com.zacharyhirsch.moldynes.emulator.apu;

import java.util.BitSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class NesApu {

  private static final Logger log = LoggerFactory.getLogger(NesApu.class);

  private final NesApuIrq irq;
  private final NesApuPulseChannel pulse1;

  private long totalCycles;

  private int frameCounter;
  private long frameCounterResetDelay;

  private int mode;
  private int pendingMode;

  private boolean pendingIrqInhibited;

  public NesApu() {
    this.irq = new NesApuIrq();
    this.pulse1 = new NesApuPulseChannel((short) 0x4000);
    this.totalCycles = 0;
    this.frameCounter = 0;
    this.frameCounterResetDelay = 0;
    this.mode = 0;
  }

  public boolean irq() {
    return irq.get();
  }

  public void tick() {
    totalCycles++;
    if (handleDelayedFrameCounterReset()) {
      return;
    }
    if (mode == 0) {
      switch (frameCounter) {
        case 7457 -> clockEnvelopesAndLinear();
        case 14913 -> {
          clockEnvelopesAndLinear();
          clockLengthAndSweep();
        }
        case 22371 -> clockEnvelopesAndLinear();
        case 29828 -> irq.set(true, totalCycles, frameCounter);
        case 29829 -> {
          clockEnvelopesAndLinear();
          clockLengthAndSweep();
          irq.set(true, totalCycles, frameCounter);
        }
        case 29830 -> {
          irq.set(true, totalCycles, frameCounter);
          frameCounter = 0;
        }
      }
    } else {
      switch (frameCounter) {
        case 7457 -> clockEnvelopesAndLinear();
        case 14913 -> {
          clockEnvelopesAndLinear();
          clockLengthAndSweep();
        }
        case 22371 -> clockEnvelopesAndLinear();
        case 29829 -> {}
        case 37281 -> {
          clockEnvelopesAndLinear();
          clockLengthAndSweep();
        }
        case 37282 -> frameCounter = 0;
      }
    }
    frameCounter++;
  }

  private void clockEnvelopesAndLinear() {}

  private void clockLengthAndSweep() {
    pulse1.lengthCounter().tick();
  }

  private boolean handleDelayedFrameCounterReset() {
    if (totalCycles == frameCounterResetDelay) {
      log.info(
          "[{}] APU [{}] frame counter reset",
          "%8d".formatted(totalCycles),
          "%5d".formatted(frameCounter));
      frameCounter = 0;
      mode = pendingMode;
      irq.setInhibited(pendingIrqInhibited);
      if (mode == 1) {
        pulse1.lengthCounter().tick();
      }
      return true;
    }
    return false;
  }

  public byte readStatus() {
    BitSet status = new BitSet(8);
    status.set(0, pulse1.lengthCounter().value() > 0);
    status.set(1, false); // pulse2
    status.set(2, false); // triangle
    status.set(3, false); // noise
    status.set(4, false); // dmc active
    status.set(5, false); // open bus
    status.set(6, irq.get());
    status.set(7, false); // dmc interrupt
    byte value = status.isEmpty() ? 0 : status.toByteArray()[0];
    log.info(
        "[{}] APU [{}] 4015 -> {}",
        "%8d".formatted(totalCycles),
        "%5d".formatted(frameCounter),
        "%02x".formatted(value));
    irq.set(false, totalCycles, frameCounter);
    return value;
  }

  public void writePulse1(short address, byte data) {
    log.info(
        "[{}] APU [{}] {} <- {}",
        "%8d".formatted(totalCycles),
        "%5d".formatted(frameCounter),
        "%04x".formatted(address),
        "%02x".formatted(data));
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
    log.info(
        "[{}] APU [{}] 4015 <- {}",
        "%8d".formatted(totalCycles),
        "%5d".formatted(frameCounter),
        "%02x".formatted(data));
    pulse1.enable((data & 0b0000_0001) != 0);
  }

  public void writeFrameCounter(byte data) {
    log.info(
        "[{}] APU [{}] 4017 <- {}",
        "%8d".formatted(totalCycles),
        "%5d".formatted(frameCounter),
        "%02x".formatted(data));
    pendingMode = (data & 0b1000_0000) >>> 7;
    pendingIrqInhibited = (data & 0b0100_0000) != 0;
    frameCounterResetDelay = totalCycles + ((totalCycles % 2) == 1 ? 3 : 2);
    log.info(
        "[{}] APU [{}] scheduling frame counter reset for cycle {}",
        "%8d".formatted(totalCycles),
        "%5d".formatted(frameCounter),
        frameCounterResetDelay);
    if (pendingIrqInhibited) {
      irq.set(false, totalCycles, frameCounter);
    }
  }
}
