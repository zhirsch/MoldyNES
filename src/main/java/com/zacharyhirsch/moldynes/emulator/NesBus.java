package com.zacharyhirsch.moldynes.emulator;

import com.zacharyhirsch.moldynes.emulator.apu.NesApu;
import com.zacharyhirsch.moldynes.emulator.cpu.NesCpu;
import com.zacharyhirsch.moldynes.emulator.cpu.NesCpuState;
import com.zacharyhirsch.moldynes.emulator.io.Display;
import com.zacharyhirsch.moldynes.emulator.io.NesJoypad;
import com.zacharyhirsch.moldynes.emulator.mapper.NesMapper;
import com.zacharyhirsch.moldynes.emulator.memory.InvalidAddressReadError;
import com.zacharyhirsch.moldynes.emulator.memory.InvalidAddressWriteError;
import com.zacharyhirsch.moldynes.emulator.ppu.NesPpu;
import com.zacharyhirsch.moldynes.emulator.ppu.NesPpuPalette;

public class NesBus {

  private final NesClock clock;
  private final NesMapper mapper;
  private final NesApu apu;
  private final NesCpu cpu;
  private final NesPpu ppu;
  private final NesJoypad joypad1;
  private final NesJoypad joypad2;
  private final byte[] cpuRam;

  public NesBus(
      NesMapper mapper,
      NesPpuPalette palette,
      Display display,
      NesJoypad joypad1,
      NesJoypad joypad2) {
    this.clock = new NesClock();
    this.mapper = mapper;
    this.apu = new NesApu();
    this.cpu = new NesCpu();
    this.ppu = new NesPpu(mapper, display, palette, cpu::nmi);
    this.joypad1 = joypad1;
    this.joypad2 = joypad2;
    this.cpuRam = new byte[0x0800];
  }

  public boolean isRunning() {
    return cpu.isRunning();
  }

  @SuppressWarnings("DuplicateBranchesInSwitch")
  public void tick() {
    switch (clock.tick() % 24) {
      case 0 -> ppu.tick();
      case 1 -> cpuTick();
      case 2 -> apu.tick();
      case 3 -> {}
      case 4 -> ppu.tick();
      case 5 -> {}
      case 6 -> {}
      case 7 -> {}
      case 8 -> ppu.tick();
      case 9 -> {}
      case 10 -> {}
      case 11 -> {}
      case 12 -> ppu.tick();
      case 13 -> cpuTick();
      case 14 -> {}
      case 15 -> {}
      case 16 -> ppu.tick();
      case 17 -> {}
      case 18 -> {}
      case 19 -> {}
      case 20 -> ppu.tick();
      case 21 -> {}
      case 22 -> {}
      case 23 -> {}
      default -> throw new IllegalStateException();
    }
  }

  private void cpuTick() {
    NesCpuState state = cpu.tick(apu.irq());
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
      throw new InvalidAddressReadError(address, "PPUCTRL");
    }
    if (addr == 0x2001) {
      return ppu.readMask();
    }
    if (addr == 0x2002) {
      return ppu.readStatus();
    }
    if (addr == 0x2003) {
      throw new InvalidAddressReadError(address, "OAMADDR");
    }
    if (addr == 0x2004) {
      return ppu.readOamData();
    }
    if (addr == 0x2005) {
      throw new InvalidAddressReadError(address, "PPUSCROLL");
    }
    if (addr == 0x2006) {
      throw new InvalidAddressReadError(address, "PPUADDR");
    }
    if (addr == 0x2007) {
      return ppu.readData();
    }
    if (0x2008 <= addr && addr <= 0x3fff) {
      return read((short) (addr & 0b0010_0000_0000_0111));
    }
    if (0x4000 <= addr && addr <= 0x4003) {
      // APU Pulse 1
      // throw new IllegalArgumentException(String.format("unable to read address %04x", addr));
      return 0;
    }
    if (0x4004 <= addr && addr <= 0x4007) {
      // APU Pulse 2
      // throw new IllegalArgumentException(String.format("unable to read address %04x", addr));
      return 0;
    }
    if (0x4008 <= addr && addr <= 0x400b) {
      // APU Triangle
      // throw new IllegalArgumentException(String.format("unable to read address %04x", addr));
      return 0;
    }
    if (0x400c <= addr && addr <= 0x400f) {
      // APU Noise
      // throw new IllegalArgumentException(String.format("unable to read address %04x", addr));
      return 0;
    }
    if (0x4010 <= addr && addr <= 0x4013) {
      // APU DMC
      // throw new IllegalArgumentException(String.format("unable to read address %04x", addr));
      return 0;
    }
    if (addr == 0x4014) {
      throw new InvalidAddressReadError(address);
    }
    if (addr == 0x4015) {
      return apu.readStatus();
    }
    if (addr == 0x4016) {
      return joypad1.read();
    }
    if (addr == 0x4017) {
      return joypad2.read();
    }
    if (0x4018 <= addr && addr <= 0x401f) {
      throw new InvalidAddressReadError(address);
    }
    if (0x4020 <= addr && addr <= 0xffff) {
      return mapper.read((short) addr, null);
    }
    throw new InvalidAddressReadError(address);
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
      throw new InvalidAddressWriteError(address, "PPUSTATUS");
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
      apu.writePulse1((short) addr, data);
      return;
    }
    if (0x4004 <= addr && addr <= 0x4007) {
      apu.writePulse2((short) addr, data);
      return;
    }
    if (0x4008 <= addr && addr <= 0x400b) {
      apu.writeTriangle((short) addr, data);
      return;
    }
    if (0x400c <= addr && addr <= 0x400f) {
      apu.writeNoise((short) addr, data);
      return;
    }
    if (0x4010 <= addr && addr <= 0x4013) {
      apu.writeDmc((short) addr, data);
      return;
    }
    if (addr == 0x4014) {
      cpu.startOamDma(data);
      return;
    }
    if (addr == 0x4015) {
      apu.writeControl(data);
      return;
    }
    if (addr == 0x4016) {
      joypad1.write(data);
      joypad2.write(data);
      return;
    }
    if (addr == 0x4017) {
      apu.writeFrameCounter(data);
      return;
    }
    if (0x4018 <= addr && addr <= 0x401f) {
      throw new InvalidAddressWriteError(address);
    }
    if (0x4020 <= addr && addr <= 0xffff) {
      mapper.write((short) addr, null, data);
      return;
    }
    throw new InvalidAddressWriteError(address);
  }
}
