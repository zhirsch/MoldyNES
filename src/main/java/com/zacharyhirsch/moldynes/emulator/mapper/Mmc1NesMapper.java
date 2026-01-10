package com.zacharyhirsch.moldynes.emulator.mapper;

import com.zacharyhirsch.moldynes.emulator.memory.InvalidAddressReadError;
import com.zacharyhirsch.moldynes.emulator.memory.InvalidAddressWriteError;
import com.zacharyhirsch.moldynes.emulator.rom.NesRom;
import java.util.BitSet;
import java.util.Optional;

// https://www.nesdev.org/wiki/MMC1
final class Mmc1NesMapper implements NesMapper {

  private final NesRom rom;
  private final byte[] prgRam;
  private final ShiftRegister shiftRegister;

  private int nametableArrangement;
  private int prgRomBankMode;
  private int prgRomBankSelect;
  private int chrRomBankMode;
  private int chrRomBank0Select;
  private int chrRomBank1Select;
  private boolean prgRamEnabled;

  public Mmc1NesMapper(NesRom rom) {
    this.rom = rom;
    this.prgRam = new byte[0x2000];
    this.shiftRegister = new ShiftRegister();
    this.nametableArrangement = 0;
    this.prgRomBankMode = 0;
    this.prgRomBankSelect = 0;
    this.chrRomBankMode = 0;
    this.chrRomBank0Select = 0;
    this.chrRomBank1Select = 0;
    this.prgRamEnabled = true;
  }

  @Override
  public byte readCpu(short address, byte[] ppuRam) {
    int addr = Short.toUnsignedInt(address);
    assert 0x0000 <= addr && addr <= 0xffff;
    if (0x0000 <= addr && addr <= 0x1fff) {
      return switch (chrRomBankMode) {
        case 0 -> rom.chr().read(addr, chrRomBank0Select & 0b1111_1110);
        case 1 -> rom.chr().read(addr, chrRomBank0Select, chrRomBank1Select);
        default -> throw new IllegalStateException(String.valueOf(chrRomBankMode));
      };
    }
    if (0x2000 <= addr && addr <= 0x2fff) {
      return ppuRam[mirror(addr)];
    }
    if (0x3000 <= addr && addr <= 0x5fff) {
      throw new InvalidAddressReadError(addr);
    }
    if (0x6000 <= addr && addr <= 0x7fff) {
      if (!prgRamEnabled) {
        throw new InvalidAddressReadError(addr, "PRG-RAM");
      }
      return prgRam[addr - 0x6000];
    }
    if (0x8000 <= addr && addr <= 0xffff) {
      return switch (prgRomBankMode) {
        case 0, 1 -> rom.prg().read(addr - 0x8000, prgRomBankSelect & 0b1111_1110);
        case 2 -> rom.prg().read(addr - 0x8000, 0, prgRomBankSelect);
        case 3 -> rom.prg().read(addr - 0x8000, prgRomBankSelect, rom.prg().getNumBanks() - 1);
        default -> throw new IllegalStateException(String.valueOf(prgRomBankMode));
      };
    }
    throw new InvalidAddressReadError(addr);
  }

  @Override
  public byte readPpu(short address, byte[] ppuRam) {
    return readCpu(address, ppuRam);
  }

  @Override
  public void writeCpu(short address, byte[] ppuRam, byte data) {
    int addr = Short.toUnsignedInt(address);
    assert 0x0000 <= addr && addr <= 0xffff;
    if (0x0000 <= addr && addr <= 0x1fff) {
      rom.chr().value()[addr] = data;
      return;
    }
    if (0x2000 <= addr && addr <= 0x2fff) {
      ppuRam[mirror(addr)] = data;
      return;
    }
    if (0x3000 <= addr && addr <= 0x5fff) {
      throw new InvalidAddressWriteError(addr);
    }
    if (0x6000 <= addr && addr <= 0x7fff) {
      if (!prgRamEnabled) {
        throw new InvalidAddressWriteError(addr, "PRG-RAM");
      }
      prgRam[addr - 0x6000] = data;
      return;
    }
    if (0x8000 <= addr && addr <= 0x9fff) {
      writeControlRegister(data);
      return;
    }
    if (0xa000 <= addr && addr <= 0xbfff) {
      writeChrBank0Register(data);
      return;
    }
    if (0xc000 <= addr && addr <= 0xdfff) {
      writeChrBank1Register(data);
      return;
    }
    if (0xe000 <= addr && addr <= 0xffff) {
      writePrgBankRegister(data);
      return;
    }
    throw new InvalidAddressWriteError(addr);
  }

  @Override
  public void writePpu(short address, byte[] ppuRam, byte data) {
    writeCpu(address, ppuRam, data);
  }

  private void writeControlRegister(byte data) {
    if ((data & 0b1000_0000) == 0b1000_0000) {
      reset();
      return;
    }
    Optional<Byte> value = shiftRegister.shift(data);
    // 4bit0
    // -----
    // CPPMM
    // |||||
    // |||++- Nametable arrangement: (0: one-screen, lower bank; 1: one-screen, upper bank;
    // |||               2: horizontal arrangement ("vertical mirroring", PPU A10);
    // |||               3: vertical arrangement ("horizontal mirroring", PPU A11) )
    // |++--- PRG-ROM bank mode (0, 1: switch 32 KB at $8000, ignoring low bit of bank number;
    // |                         2: fix first bank at $8000 and switch 16 KB bank at $C000;
    // |                         3: fix last bank at $C000 and switch 16 KB bank at $8000)
    // +----- CHR-ROM bank mode (0: switch 8 KB at a time; 1: switch two separate 4 KB banks)
    value.ifPresent(val -> nametableArrangement = val & 0b0000_0011);
    value.ifPresent(val -> prgRomBankMode = (val & 0b0000_1100) >>> 2);
    value.ifPresent(val -> chrRomBankMode = (val & 0b0001_0000) >>> 4);
  }

  private void writeChrBank0Register(byte data) {
    if ((data & 0b1000_0000) == 0b1000_0000) {
      reset();
      return;
    }
    Optional<Byte> value = shiftRegister.shift(data);
    value.ifPresent(val -> chrRomBank0Select = val);
  }

  private void writeChrBank1Register(byte data) {
    if ((data & 0b1000_0000) == 0b1000_0000) {
      reset();
      return;
    }
    Optional<Byte> value = shiftRegister.shift(data);
    value.ifPresent(val -> chrRomBank1Select = val);
  }

  private void writePrgBankRegister(byte data) {
    if ((data & 0b1000_0000) == 0b1000_0000) {
      reset();
      return;
    }
    Optional<Byte> value = shiftRegister.shift(data);
    value.ifPresent(val -> prgRomBankSelect = val & 0b0000_1111);
    value.ifPresent(val -> prgRamEnabled = (val & 0b0001_0000) == 0b0000_0000);
  }

  private void reset() {
    shiftRegister.reset();
    prgRomBankMode = 3;
  }

  private short mirror(int addr) {
    // Horizontal mirroring:
    //   [ A ] [ a ]
    //   [ B ] [ b ]
    //
    // Vertical mirroring:
    //   [ A ] [ B ]
    //   [ a ] [ b ]

    return switch (nametableArrangement) {
      case 0 -> (short) (addr & 0b0000_0011_1111_1111);
      case 1 -> throw new UnsupportedOperationException(String.valueOf(nametableArrangement));
      case 2 -> (short) ((0b0000_0000_0000_0000) | (addr & 0b0000_0011_1111_1111));
      case 3 -> (short) ((0b0000_0100_0000_0000) | (addr & 0b0000_0011_1111_1111));
      default -> throw new IllegalStateException(String.valueOf(nametableArrangement));
    };
  }

  private static final class ShiftRegister {

    private final BitSet value;

    private int count;

    ShiftRegister() {
      this.value = new BitSet(5);
      this.count = 0;
    }

    Optional<Byte> shift(byte bit) {
      value.set(count++, (bit & 0b0000_0001) == 0b0000_0001);
      if (count < 5) {
        return Optional.empty();
      }
      byte v = value.isEmpty() ? 0 : value.toByteArray()[0];
      reset();
      return Optional.of(v);
    }

    public void reset() {
      value.clear();
      count = 0;
    }
  }
}
