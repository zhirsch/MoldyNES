package com.zacharyhirsch.moldynes.emulator.cpu;

import com.zacharyhirsch.moldynes.emulator.apu.NesApu;
import com.zacharyhirsch.moldynes.emulator.io.NesJoypad;
import com.zacharyhirsch.moldynes.emulator.mapper.NesMapper;
import com.zacharyhirsch.moldynes.emulator.memory.InvalidAddressReadError;
import com.zacharyhirsch.moldynes.emulator.memory.InvalidAddressWriteError;
import com.zacharyhirsch.moldynes.emulator.ppu.NesPpu;
import java.util.function.Consumer;

final class NesCpuMemory {

  private final NesMapper mapper;
  private final NesPpu ppu;
  private final NesApu apu;
  private final NesJoypad joypad1;
  private final NesJoypad joypad2;
  private final Consumer<Byte> startOamDma;
  private final byte[] ram;

  NesCpuMemory(
      NesMapper mapper,
      NesPpu ppu,
      NesApu apu,
      NesJoypad joypad1,
      NesJoypad joypad2,
      Consumer<Byte> startOamDma) {
    this.mapper = mapper;
    this.ppu = ppu;
    this.apu = apu;
    this.joypad1 = joypad1;
    this.joypad2 = joypad2;
    this.startOamDma = startOamDma;
    this.ram = new byte[0x0800];
  }

  byte read(short address) {
    int addr = Short.toUnsignedInt(address);
    assert 0x0000 <= addr && addr <= 0xffff;
    if (0x0000 <= addr && addr <= 0x07ff) {
      return ram[addr];
    }
    if (0x0800 <= addr && addr <= 0x1fff) {
      return read((short) (addr & 0b0000_0111_1111_1111));
    }
    if (addr == 0x2000) {
      return 0;
    }
    if (addr == 0x2001) {
      return ppu.readMask();
    }
    if (addr == 0x2002) {
      return ppu.readStatus();
    }
    if (addr == 0x2003) {
      return 0;
    }
    if (addr == 0x2004) {
      return ppu.readOamData();
    }
    if (addr == 0x2005) {
      return 0;
    }
    if (addr == 0x2006) {
      return 0;
    }
    if (addr == 0x2007) {
      return ppu.readData();
    }
    if (0x2008 <= addr && addr <= 0x3fff) {
      return read((short) (addr & 0b0010_0000_0000_0111));
    }
    if (0x4000 <= addr && addr <= 0x4014) {
      return 0;
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
      return 0;
    }
    if (0x4020 <= addr && addr <= 0xffff) {
      return mapper.readCpu((short) addr);
    }
    throw new InvalidAddressReadError(address);
  }

  void write(short address, byte data) {
    int addr = Short.toUnsignedInt(address);
    assert 0x0000 <= addr && addr <= 0xffff;
    if (0x0000 <= addr && addr <= 0x07ff) {
      ram[addr] = data;
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
      return;
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
    if (addr == 0x4000) {
      apu.pulse1().writeControl(data);
      return;
    }
    if (addr == 0x4001) {
      apu.pulse1().writeSweep(data);
      return;
    }
    if (addr == 0x4002) {
      apu.pulse1().writeTimerLo(data);
      return;
    }
    if (addr == 0x4003) {
      apu.pulse1().writeTimerHi(data);
      return;
    }
    if (addr == 0x4004) {
      apu.pulse2().writeControl(data);
      return;
    }
    if (addr == 0x4005) {
      apu.pulse2().writeSweep(data);
      return;
    }
    if (addr == 0x4006) {
      apu.pulse2().writeTimerLo(data);
      return;
    }
    if (addr == 0x4007) {
      apu.pulse2().writeTimerHi(data);
      return;
    }
    if (addr == 0x4008) {
      apu.triangle().writeControl(data);
      return;
    }
    if (addr == 0x4009) {
      return;
    }
    if (addr == 0x400a) {
      apu.triangle().writeTimerLo(data);
      return;
    }
    if (addr == 0x400b) {
      apu.triangle().writeTimerHi(data);
      return;
    }
    if (addr == 0x400c) {
      apu.noise().writeControl(data);
      return;
    }
    if (addr == 0x400d) {
      return;
    }
    if (addr == 0x400e) {
      apu.noise().writeMode(data);
      return;
    }
    if (addr == 0x400f) {
      apu.noise().writeLength(data);
      return;
    }
    if (addr == 0x4010) {
      apu.dmc().writeControl(data);
      return;
    }
    if (addr == 0x4011) {
      apu.dmc().writeDac(data);
      return;
    }
    if (addr == 0x4012) {
      apu.dmc().writeAddress(data);
      return;
    }
    if (addr == 0x4013) {
      apu.dmc().writeLength(data);
      return;
    }
    if (addr == 0x4014) {
      startOamDma.accept(data);
      return;
    }
    if (addr == 0x4015) {
      apu.writeStatus(data);
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
      mapper.writeCpu((short) addr, data);
      return;
    }
    throw new InvalidAddressWriteError(address);
  }
}
