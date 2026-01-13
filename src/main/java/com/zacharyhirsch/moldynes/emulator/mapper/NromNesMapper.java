package com.zacharyhirsch.moldynes.emulator.mapper;

import com.zacharyhirsch.moldynes.emulator.rom.NametableLayout;
import com.zacharyhirsch.moldynes.emulator.rom.NesRom;
import java.nio.ByteBuffer;

// https://www.nesdev.org/wiki/NROM
final class NromNesMapper extends AbstractNesMapper {

  private final NesRom rom;
  private final ByteBuffer wram;
  private final ByteBuffer vram;

  NromNesMapper(NesRom rom) {
    this.rom = rom;
    this.wram = ByteBuffer.wrap(new byte[0x2000]);
    this.vram = ByteBuffer.wrap(new byte[0x2000]);
  }

  @Override
  protected byte readPpuRam(int address) {
    return vram.get(mirror(address));
  }

  @Override
  protected void writePpuRam(int address, byte data) {
    vram.put(mirror(address), data);
  }

  @Override
  protected byte readPrgRam(int address) {
    return wram.get(address - 0x6000);
  }

  @Override
  protected void writePrgRam(int address, byte data) {
    wram.put(address - 0x6000, data);
  }

  @Override
  protected byte readChrRam(int address) {
    assert 0x0000 <= address && address <= 0x1fff;
    return rom.chr().get(address & (rom.chr().capacity() - 1));
  }

  @Override
  protected void writeChrRam(int address, byte data) {
    assert 0x0000 <= address && address <= 0x1fff;
    rom.chr().put(address & (rom.chr().capacity() - 1), data);
  }

  @Override
  protected byte readPrgRom(int address) {
    assert 0x8000 <= address && address <= 0xffff;
    return rom.prg().get(address & (rom.prg().capacity() - 1));
  }

  @Override
  protected void writeRegister(int address, byte data) {}

  @Override
  protected NametableLayout getNametableLayout() {
    return rom.properties().nametableLayout();
  }
}
