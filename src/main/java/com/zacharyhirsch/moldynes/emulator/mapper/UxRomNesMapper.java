package com.zacharyhirsch.moldynes.emulator.mapper;

import com.zacharyhirsch.moldynes.emulator.memory.InvalidReadError;
import com.zacharyhirsch.moldynes.emulator.memory.InvalidWriteError;
import com.zacharyhirsch.moldynes.emulator.rom.NametableLayout;
import com.zacharyhirsch.moldynes.emulator.rom.NesRom;
import java.nio.ByteBuffer;

// https://www.nesdev.org/wiki/UxROM
final class UxRomNesMapper extends AbstractNesMapper {

  private final NesRom rom;
  private final ByteBuffer vram;

  private int prgRomBankSelect;

  public UxRomNesMapper(NesRom rom) {
    this.rom = rom;
    this.vram = ByteBuffer.wrap(new byte[0x2000]);
    this.prgRomBankSelect = 0;
  }

  @Override
  protected byte readPrgRam(int address) {
    throw new InvalidReadError(address);
  }

  @Override
  protected void writePrgRam(int address, byte data) {
    throw new InvalidWriteError(address);
  }

  @Override
  protected byte readPrgRom(int address) {
    assert 0x8000 <= address && address <= 0xffff;
    if (0x8000 <= address && address <= 0xbfff) {
      return rom.prg().get(prgRomBankSelect * 0x4000 + address - 0x8000);
    }
    if (0xc000 <= address && address <= 0xffff) {
      int numBanks = rom.prg().capacity() / 0x4000;
      return rom.prg().get((numBanks - 1) * 0x4000 + address - 0xc000);
    }
    throw new IllegalStateException();
  }

  @Override
  protected byte readChrRam(int address) {
    return rom.chr().get(address);
  }

  @Override
  protected void writeChrRam(int address, byte data) {
    rom.chr().put(address, data);
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
  protected void writeRegister(int address, byte data) {
    assert 0x8000 <= address && address <= 0xffff;
    prgRomBankSelect = data & 0b0000_1111;
  }

  @Override
  protected NametableLayout getNametableLayout() {
    return rom.properties().nametableLayout();
  }
}
