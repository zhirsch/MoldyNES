package com.zacharyhirsch.moldynes.emulator.mapper;

import com.zacharyhirsch.moldynes.emulator.memory.Address;
import com.zacharyhirsch.moldynes.emulator.rom.NametableLayout;

public abstract class AbstractNesMapper implements NesMapper {

  @Override
  public void tick(int v) {}

  @Override
  public boolean irq() {
    return false;
  }

  @Override
  public Address resolveCpu(int address) {
    assert 0x0000 <= address && address <= 0xffff;
    if (0x0000 <= address && address <= 0x5fff) {
      return Address.of(() -> (byte) 0, (_) -> {});
    }
    if (0x6000 <= address && address <= 0x7fff) {
      return Address.of(address, this::readPrgRam, this::writePrgRam);
    }
    if (0x8000 <= address && address <= 0xffff) {
      return Address.of(address, this::readPrgRom, this::writeRegister);
    }
    throw new IllegalStateException();
  }

  @Override
  public Address resolvePpu(int address) {
    assert 0x0000 <= address && address <= 0x3fff;
    if (0x0000 <= address && address <= 0x1fff) {
      return Address.of(address, this::readChrRam, this::writeChrRam);
    }
    if (0x2000 <= address && address <= 0x3eff) {
      return Address.of(address, this::readPpuRam, this::writePpuRam);
    }
    if (0x3f00 <= address && address <= 0x3fff) {
      return Address.of(address, this::readPpuRam, this::writePpuRam);
    }
    throw new IllegalStateException();
  }

  protected int mirror(int address) {
    int nametable = (address & 0b0000_1100_0000_0000) >>> 10;
    int offset = address & 0b0000_0011_1111_1111;
    return switch (getNametableLayout()) {
      case HORIZONTAL ->
          switch (nametable) {
            case 0, 1 -> offset;
            case 2, 3 -> 0x0400 | offset;
            default -> throw new IllegalStateException();
          };
      case VERTICAL ->
          switch (nametable) {
            case 0, 2 -> offset;
            case 1, 3 -> 0x0400 | offset;
            default -> throw new IllegalStateException();
          };
    };
  }

  protected abstract byte readPrgRom(int address);

  protected abstract void writeRegister(int address, byte data);

  protected abstract byte readPrgRam(int address);

  protected abstract void writePrgRam(int address, byte data);

  protected abstract byte readChrRam(int address);

  protected abstract void writeChrRam(int address, byte data);

  protected abstract byte readPpuRam(int address);

  protected abstract void writePpuRam(int address, byte data);

  protected abstract NametableLayout getNametableLayout();
}
