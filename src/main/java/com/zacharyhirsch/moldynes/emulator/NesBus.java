package com.zacharyhirsch.moldynes.emulator;

import com.zacharyhirsch.moldynes.emulator.apu.NesApu;
import com.zacharyhirsch.moldynes.emulator.cpu.NesCpu;
import com.zacharyhirsch.moldynes.emulator.mappers.NesMapper;
import com.zacharyhirsch.moldynes.emulator.ppu.NesPpu;
import com.zacharyhirsch.moldynes.emulator.ppu.NesPpuPalette;

public class NesBus {

  private final byte[] cpuRam = new byte[0x0800];
  private final NesMapper mapper;
  private final NesApu apu;
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
    this.cpu = new NesCpu(this);
    this.ppu = new NesPpu(mapper, display, palette);
    this.joypad1 = joypad1;
    this.joypad2 = joypad2;
  }

  public boolean isRunning() {
    return cpu.isRunning();
  }

  public void tick() {
    boolean nmi = false;
    nmi = nmi | ppu.tick();
    nmi = nmi | ppu.tick();
    nmi = nmi | ppu.tick();
    cpu.tick();
    if (nmi) {
      cpu.nmi.set(true);
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
    if (0x0000 <= addr && addr < 0x0800) {
      return cpuRam[addr];
    }
    if (0x0800 <= addr && addr < 0x2000) {
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
    if (0x2008 <= addr && addr < 0x4000) {
      return read((short) (addr & 0b0010_0000_0000_0111));
    }
    if (0x4000 <= addr && addr < 0x4014) {
      // APU
      return (byte) 0x00;
    }
    if (addr == 0x4014) {
      throw new IllegalArgumentException(String.format("unable to read address %04x", addr));
    }
    if (addr == 0x4015) {
      // APU
      return (byte) 0x00;
    }
    if (addr == 0x4016) {
      return joypad1.read();
    }
    if (addr == 0x4017) {
      return joypad2.read();
    }
    if (0x4018 <= addr && addr < 0x4020) {
      throw new IllegalArgumentException(String.format("unable to read address %04x", addr));
    }
    if (0x4020 <= addr && addr < 0x10000) {
      return mapper.read((short) addr);
    }
    throw new IllegalArgumentException(String.format("unable to read address %04x", addr));
  }

  public void write(byte adh, byte adl, byte data) {
    write((short) ((adh << 8) | Byte.toUnsignedInt(adl)), data);
  }

  private void write(short address, byte data) {
    int addr = Short.toUnsignedInt(address);
    if (0x0000 <= addr && addr < 0x0800) {
      cpuRam[addr] = data;
      return;
    }
    if (0x0800 <= addr && addr < 0x2000) {
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
    if (0x2008 <= addr && addr < 0x4000) {
      write((short) (addr & 0b0010_0000_0000_0111), data);
      return;
    }
    if (0x4000 <= addr && addr < 0x4014) {
      // APU
      return;
    }
    if (addr == 0x4014) {
      byte[] buffer = new byte[0x100];
      for (int i = 0; i < 256; i++) {
        buffer[i] = read(data, (byte) i);
      }
      ppu.writeOamDma(buffer);
      // TODO: account for cycles during DMA
      return;
    }
    if (addr == 0x4015) {
      // APU
      return;
    }
    if (addr == 0x4016) {
      joypad1.write(data);
      joypad2.write(data);
      return;
    }
    if (addr == 0x4017) {
      return;
    }
    if (0x4018 <= addr && addr < 0x4020) {
      throw new IllegalArgumentException(String.format("unable to write address %04x", addr));
    }
    if (0x4020 <= addr && addr < 0x10000) {
      mapper.write((short) addr, data);
      return;
    }
    throw new IllegalArgumentException(String.format("unable to write address %04x", addr));
  }
}
