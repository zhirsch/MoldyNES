package com.zacharyhirsch.moldynes.emulator.mapper;

import com.zacharyhirsch.moldynes.emulator.memory.InvalidReadError;
import com.zacharyhirsch.moldynes.emulator.memory.InvalidWriteError;
import com.zacharyhirsch.moldynes.emulator.rom.NametableLayout;
import com.zacharyhirsch.moldynes.emulator.rom.NesRom;
import java.nio.ByteBuffer;

// https://www.nesdev.org/wiki/CNROM
final class CnromNesMapper extends AbstractNesMapper {

  private final NesRom rom;
  private final ByteBuffer vram;

  private int chrRomBankSelect;

  public CnromNesMapper(NesRom rom) {
    this.rom = rom;
    this.vram = ByteBuffer.wrap(new byte[0x2000]);
    this.chrRomBankSelect = 0;
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
    return rom.prg().get(address - 0x8000);
  }

  @Override
  protected byte readChrRam(int address) {
    return rom.prg().get(chrRomBankSelect * 0x2000 + address);
  }

  @Override
  protected void writeChrRam(int address, byte data) {
    throw new InvalidWriteError(address);
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
    chrRomBankSelect = data & 0b0000_0011;
  }

  @Override
  protected NametableLayout getNametableLayout() {
    return rom.properties().nametableLayout();
  }
}
