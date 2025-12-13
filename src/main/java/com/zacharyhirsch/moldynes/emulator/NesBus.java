package com.zacharyhirsch.moldynes.emulator;

import com.zacharyhirsch.moldynes.emulator.apu.NesApu;
import com.zacharyhirsch.moldynes.emulator.cpu.NesCpu;
import com.zacharyhirsch.moldynes.emulator.cpu.NesCpuState;
import com.zacharyhirsch.moldynes.emulator.io.Display;
import com.zacharyhirsch.moldynes.emulator.io.NesJoypad;
import com.zacharyhirsch.moldynes.emulator.mappers.NesMapper;
import com.zacharyhirsch.moldynes.emulator.ppu.NesPpu;
import com.zacharyhirsch.moldynes.emulator.ppu.NesPpuPalette;

public class NesBus {

  private final byte[] cpuRam = new byte[0x0800];
  private final NesMapper mapper;
  private @SuppressWarnings("unused") final NesApu apu;
  private final NesCpu cpu;
  private final NesPpu ppu;
  private final NesJoypad joypad1;
  private final NesJoypad joypad2;

  public NesBus(
      NesMapper mapper,
      NesPpuPalette palette,
      Display display,
      NesJoypad joypad1,
      NesJoypad joypad2) {
    this.mapper = mapper;
    this.apu = new NesApu();
    this.cpu = new NesCpu();
    this.ppu = new NesPpu(mapper, display, palette);
    this.joypad1 = joypad1;
    this.joypad2 = joypad2;
  }

  public boolean isRunning() {
    return cpu.isRunning();
  }

  public void tick() {
    boolean nmi = false;
    for (int i = 0; i < 3; i++) {
      nmi = ppu.tick() || nmi;
    }
    boolean irq = false;
    NesCpuState state = cpu.tick(nmi, irq);
    if (state.write) {
      write(state.adh, state.adl, state.data);
    } else {
      state.data = read(state.adh, state.adl);
    }
  }

  public void reset() {
    cpu.reset();
  }

  public byte read(byte adh, byte adl) {
    return read((short) ((adh << 8) | Byte.toUnsignedInt(adl)));
  }

  private byte read(short address) {
    int addr = Short.toUnsignedInt(address);
    assert 0x0000 <= addr && addr <= 0xffff;
    if (0x0000 <= addr && addr <= 0x07ff) {
      return cpuRam[addr];
    }
    if (0x0800 <= addr && addr <= 0x1fff) {
      return read((short) (addr & 0b0000_0111_1111_1111));
    }
    if (addr == 0x2000) {
      throw new IllegalArgumentException("reading PPUCTRL is not allowed");
    }
    if (addr == 0x2001) {
      return ppu.readMask();
    }
    if (addr == 0x2002) {
      return ppu.readStatus();
    }
    if (addr == 0x2003) {
      throw new IllegalArgumentException("reading OAMADDR is not allowed");
    }
    if (addr == 0x2004) {
      return ppu.readOamData();
    }
    if (addr == 0x2005) {
      throw new IllegalArgumentException("reading PPUSCROLL is not allowed");
    }
    if (addr == 0x2006) {
      throw new IllegalArgumentException("reading PPUADDR is not allowed");
    }
    if (addr == 0x2007) {
      return ppu.readData();
    }
    if (0x2008 <= addr && addr <= 0x3fff) {
      return read((short) (addr & 0b0010_0000_0000_0111));
    }
    if (0x4000 <= addr && addr <= 0x4003) {
      // APU Pulse 1
      return (byte) 0x00;
    }
    if (0x4004 <= addr && addr <= 0x4007) {
      // APU Pulse 2
      return (byte) 0x00;
    }
    if (0x4008 <= addr && addr <= 0x400b) {
      // APU Triangle
      return (byte) 0x00;
    }
    if (0x400c <= addr && addr <= 0x400f) {
      // APU Noise
      return (byte) 0x00;
    }
    if (0x4010 <= addr && addr <= 0x4013) {
      // APU DMC
      return (byte) 0x00;
    }
    if (addr == 0x4014) {
      throw new IllegalArgumentException(String.format("unable to read address %04x", addr));
    }
    if (addr == 0x4015) {
      // APU Status
      return (byte) 0x00;
    }
    if (addr == 0x4016) {
      return joypad1.read();
    }
    if (addr == 0x4017) {
      return joypad2.read();
    }
    if (0x4018 <= addr && addr <= 0x401f) {
      throw new IllegalArgumentException(String.format("unable to read address %04x", addr));
    }
    if (0x4020 <= addr && addr <= 0xffff) {
      return mapper.read((short) addr, null);
    }
    throw new IllegalArgumentException(String.format("unable to read address %04x", addr));
  }

  public void write(byte adh, byte adl, byte data) {
    write((short) ((adh << 8) | Byte.toUnsignedInt(adl)), data);
  }

  private void write(short address, byte data) {
    int addr = Short.toUnsignedInt(address);
    assert 0x0000 <= addr && addr <= 0xffff;
    if (0x0000 <= addr && addr <= 0x07ff) {
      cpuRam[addr] = data;
      return;
    }
    if (0x0800 <= addr && addr <= 0x1fff) {
      write((short) (addr & 0b0000_0111_1111_1111), data);
      return;
    }
    if (addr == 0x2000) {
      ppu.writeControl(data);
      return;
    }
    if (addr == 0x2001) {
      ppu.writeMask(data);
      return;
    }
    if (addr == 0x2002) {
      throw new IllegalArgumentException("writing PPUSTATUS is not allowed");
    }
    if (addr == 0x2003) {
      ppu.writeOamAddr(data);
      return;
    }
    if (addr == 0x2004) {
      ppu.writeOamData(data);
      return;
    }
    if (addr == 0x2005) {
      ppu.writeScroll(data);
      return;
    }
    if (addr == 0x2006) {
      ppu.writeAddress(data);
      return;
    }
    if (addr == 0x2007) {
      ppu.writeData(data);
      return;
    }
    if (0x2008 <= addr && addr <= 0x3fff) {
      write((short) (addr & 0b0010_0000_0000_0111), data);
      return;
    }
    if (0x4000 <= addr && addr <= 0x4003) {
      // APU Pulse 1
      return;
    }
    if (0x4004 <= addr && addr <= 0x4007) {
      // APU Pulse 2
      return;
    }
    if (0x4008 <= addr && addr <= 0x400b) {
      // APU Triangle
      return;
    }
    if (0x400c <= addr && addr <= 0x400f) {
      // APU Noise
      return;
    }
    if (0x4010 <= addr && addr <= 0x4013) {
      // APU DMC
      return;
    }
    if (addr == 0x4014) {
      cpu.startOamDma(data);
      return;
    }
    if (addr == 0x4015) {
      // APU Status
      return;
    }
    if (addr == 0x4016) {
      joypad1.write(data);
      joypad2.write(data);
      return;
    }
    if (addr == 0x4017) {
      // APU Frame Counter
      return;
    }
    if (0x4018 <= addr && addr <= 0x401f) {
      throw new IllegalArgumentException(String.format("unable to write address %04x", addr));
    }
    if (0x4020 <= addr && addr <= 0xffff) {
      mapper.write((short) addr, null, data);
      return;
    }
    throw new IllegalArgumentException(String.format("unable to write address %04x", addr));
  }
}
