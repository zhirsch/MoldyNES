package com.zacharyhirsch.moldynes.emulator.apu;

import com.zacharyhirsch.moldynes.emulator.NesClock;
import com.zacharyhirsch.moldynes.emulator.io.Display;
import java.util.BitSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public final class NesApu {

  private static final Logger log = LoggerFactory.getLogger(NesApu.class);

  private final NesClock clock;
  private final Display display;
  private final NesApuIrq irq;
  private final NesApuPulse pulse1;
  private final NesApuPulse pulse2;
  private final NesApuTriangle triangle;
  private final NesApuNoise noise;
  private final NesApuDmc dmc;
  private final NesApuMixer mixer;

  private int frameCounter;
  private long frameCounterResetDelay;

  private int mode;
  private int pendingMode;

  private boolean pendingIrqInhibited;

  private double mixBuffer;
  private long nextSampleAt;
  private long nextSampleIncrement;

  public NesApu(NesClock clock, Display display) {
    this.clock = clock;
    this.display = display;
    this.irq = new NesApuIrq();
    this.pulse1 = new NesApuPulse(clock, 1);
    this.pulse2 = new NesApuPulse(clock, 2);
    this.triangle = new NesApuTriangle(clock);
    this.noise = new NesApuNoise(clock);
    this.dmc = new NesApuDmc();
    this.mixer = new NesApuMixer(pulse1, pulse2, triangle, noise, dmc);
    this.frameCounter = 0;
    this.frameCounterResetDelay = 0;
    this.mode = 0;
    this.pendingMode = 0;
    this.pendingIrqInhibited = false;
    this.mixBuffer = 0;
    this.nextSampleAt = clock.getCycle() + 40;
    this.nextSampleIncrement = 40;
    MDC.put("frameCounter", "%5d".formatted(0));
  }

  public boolean irq() {
    return irq.get();
  }

  public void tick() {
    MDC.put("frameCounter", "%5d".formatted(frameCounter));
    if (!handleDelayedFrameCounterReset()) {
      if (mode == 0) {
        tickMode0();
      } else {
        tickMode1();
      }
      frameCounter++;
    }
    pulse1.tick();
    pulse2.tick();
    triangle.tick();
    noise.tick();
    dmc.tick();

    mixBuffer += mixer.mix();
    if (nextSampleAt == clock.getCycle()) {
      display.play(mixBuffer / nextSampleIncrement);
      mixBuffer = 0;
      nextSampleIncrement = nextSampleIncrement == 41 ? 40 : 41;
      nextSampleAt = clock.getCycle() + nextSampleIncrement;
    }
  }

  private void tickMode0() {
    switch (frameCounter) {
      case 7457 -> clockEnvelopesAndLinear();
      case 14913 -> {
        clockEnvelopesAndLinear();
        clockLengthAndSweep();
      }
      case 22371 -> clockEnvelopesAndLinear();
      case 29828 -> irq.set(true);
      case 29829 -> {
        clockEnvelopesAndLinear();
        clockLengthAndSweep();
        irq.set(true);
      }
      case 29830 -> {
        irq.set(true);
        frameCounter = 0;
      }
    }
  }

  private void tickMode1() {
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

  private void clockEnvelopesAndLinear() {
    pulse1.envelope().tick();
    pulse2.envelope().tick();
  }

  private void clockLengthAndSweep() {
    if (pulse1.length().value() > 0) {
      pulse1.length().suppressNextReset();
    }
    if (pulse2.length().value() > 0) {
      pulse2.length().suppressNextReset();
    }
    if (triangle.length().value() > 0) {
      triangle.length().suppressNextReset();
    }
    if (noise.length().value() > 0) {
      noise.length().suppressNextReset();
    }
    pulse1.length().tick();
    pulse2.length().tick();
    triangle.length().tick();
    noise.length().tick();
  }

  private boolean handleDelayedFrameCounterReset() {
    if (clock.getCycle() == frameCounterResetDelay) {
      frameCounter = 0;
      mode = pendingMode;
      irq.setInhibited(pendingIrqInhibited);
      if (mode == 1) {
        pulse1.length().tick();
        pulse2.length().tick();
        triangle.length().tick();
        noise.length().tick();
      }
      MDC.put("frameCounter", "%5d".formatted(frameCounter));
      log.trace("APU frame counter reset, mode is now {}", mode);
      return true;
    }
    return false;
  }

  public byte readStatus() {
    BitSet status = new BitSet(8);
    status.set(0, pulse1.length().value() > 0);
    status.set(1, pulse2.length().value() > 0);
    status.set(2, triangle.length().value() > 0);
    status.set(3, noise.length().value() > 0);
    status.set(4, dmc.getBytesRemaining() > 0);
    status.set(5, false); // open bus
    status.set(6, irq.get());
    status.set(7, dmc.irq().get());
    byte value = status.isEmpty() ? 0 : status.toByteArray()[0];
    log.trace("APU 4015 -> {}", "%02x".formatted(value));
    irq.set(false);
    return value;
  }

  public void writePulse1(short address, byte data) {
    log.trace("APU [pulse1] {} <- {}", "%04x".formatted(address), "%02x".formatted(data));
    pulse1.write(address, data);
  }

  public void writePulse2(short address, byte data) {
    log.trace("APU [pulse2] {} <- {}", "%04x".formatted(address), "%02x".formatted(data));
    pulse2.write(address, data);
  }

  public void writeTriangle(short address, byte data) {
    log.trace("APU [triangle] {} <- {}", "%04x".formatted(address), "%02x".formatted(data));
    triangle.write(address, data);
  }

  public void writeNoise(short address, byte data) {
    log.trace("APU [noise] {} <- {}", "%04x".formatted(address), "%02x".formatted(data));
    noise.write(address, data);
  }

  public void writeDmc(short address, byte data) {
    log.trace("APU [dmc] {} <- {}", "%04x".formatted(address), "%02x".formatted(data));
    dmc.write(address, data);
  }

  public void writeStatus(byte data) {
    log.trace("APU 4015 <- {}", "%02x".formatted(data));
    pulse1.enable((data & 0b0000_0001) != 0);
    pulse2.enable((data & 0b0000_0010) != 0);
    triangle.enable((data & 0b0000_0100) != 0);
    noise.enable((data & 0b0000_1000) != 0);
    dmc.enable((data & 0b0001_0000) != 0);
    dmc.irq().set(false);
  }

  public void writeFrameCounter(byte data) {
    log.trace("APU 4017 <- {}", "%02x".formatted(data));
    pendingMode = (data & 0b1000_0000) >>> 7;
    pendingIrqInhibited = (data & 0b0100_0000) != 0;
    frameCounterResetDelay = clock.getCycle() + ((clock.getCycle() % 2) == 1 ? 2 : 3);
    log.trace("APU scheduling frame counter reset for cycle {}", frameCounterResetDelay);
  }
}
