package com.zacharyhirsch.moldynes.emulator.bus;

import com.zacharyhirsch.moldynes.emulator.NesClock;
import com.zacharyhirsch.moldynes.emulator.apu.NesApu;
import com.zacharyhirsch.moldynes.emulator.cpu.NesCpu;
import com.zacharyhirsch.moldynes.emulator.io.Display;
import com.zacharyhirsch.moldynes.emulator.io.NesJoypad;
import com.zacharyhirsch.moldynes.emulator.mapper.NesMapper;
import com.zacharyhirsch.moldynes.emulator.ppu.NesPpu;
import com.zacharyhirsch.moldynes.emulator.ppu.NesPpuPalette;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NesBus {

  private static final Logger log = LoggerFactory.getLogger(NesBus.class);

  private final NesClock clock;
  private final NesMapper mapper;
  private final NesCpu cpu;
  private final NesApu apu;
  private final NesPpu ppu;

  public NesBus(
      NesMapper mapper,
      NesPpuPalette palette,
      Display display,
      NesJoypad joypad1,
      NesJoypad joypad2) {
    this.clock = new NesClock();
    this.mapper = mapper;
    this.apu = new NesApu(clock, display, this::startDmcDma);
    this.ppu = new NesPpu(mapper, display, palette, this::onNmi);
    this.cpu = new NesCpu(mapper, ppu, apu, joypad1, joypad2, this::startOamDma);
  }

  public void tick() {
    clock.tick();
    ppu.tick();
    mapper.tick(ppu.address());
    ppu.tick();
    mapper.tick(ppu.address());
    ppu.tick();
    mapper.tick(ppu.address());
    apu.tick();
    cpu.tick();

    if (apu.irq() || mapper.irq()) {
      cpu.irq();
    }
  }

  private void startOamDma(byte address) {
    cpu.startOamDma(address);
  }

  private byte startDmcDma(short address) {
    return cpu.startDmcDma(address);
  }

  private void onNmi() {
    cpu.nmi();
  }
}
